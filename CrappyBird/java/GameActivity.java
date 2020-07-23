package com.application.nick.crappybird;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.application.nick.crappybird.entity.MarketBird;
import com.application.nick.crappybird.scene.GameScene;
import com.application.nick.crappybird.scene.MarketScene;
import com.application.nick.crappybird.iabutil.IabHelper;
import com.application.nick.crappybird.iabutil.IabResult;
import com.application.nick.crappybird.iabutil.Inventory;
import com.application.nick.crappybird.iabutil.Purchase;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LevelEndEvent;
import com.crashlytics.android.answers.LevelStartEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.crashlytics.android.answers.RatingEvent;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements;
import com.kobakei.ratethisapp.RateThisApp;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;

import io.fabric.sdk.android.Fabric;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.CroppedResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.LayoutGameActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;


public class GameActivity extends LayoutGameActivity implements
        GameValues,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final String SKU_REMOVE_ADS = "no_ads";

    public static final int CAMERA_WIDTH = 320;
    public static final int CAMERA_HEIGHT = 533;

    public MarketBird[] marketBirds = new MarketBird[18];

    private Camera mCamera;
    private ResourceManager mResourceManager;
    private SceneManager mSceneManager;

    private IabHelper mHelper;

    private boolean inAppBillingSetup = false, soundOn = true, mAdFree, rateDialogOpenedThisSession, initialSceneLoaded = false;

    private GameActivity mActivity = this;

    private GoogleApiClient mGoogleApiClient;

    private static int RC_SIGN_IN = 9001, REQUEST_LEADERBOARD = 9002, REQUEST_ACHIEVEMENTS = 9003, PLAY_SERVICES_ERROR_REQUEST_CODE = 9004, IAB_REQUEST_CODE = 9005;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;
    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false; // set to true when you're in the middle of the sign in flow, to know you should not attempt to connect in onStart()

    //handles chartboost callbacks. Declare delegate methods here, see CBSample project for examples
    // https://answers.chartboost.com/hc/en-us/articles/201219505#inplay
    private ChartboostDelegate chartboostDelegate = new ChartboostDelegate() {

        // Called after an interstitial has been displayed on the screen.
        public void didDisplayInterstitial(String location) {
            if(getSoundOn()) {
                mResourceManager.mMusic.pause();
                getEngine().getSoundManager().setMasterVolume(0);
            }
        }

        // Called after an interstitial has been closed.
        public void didCloseInterstitial(String location) {
            if(getSoundOn()) {
                mResourceManager.mMusic.resume();
                getEngine().getSoundManager().setMasterVolume(1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        MarketBird[] marketBirds = {
                new MarketBird("Classic Bird", null, 0, true),
                new MarketBird("Red Bird", getString(R.string.achievement_red), 1, false),
                new MarketBird("Blue Bird", getString(R.string.achievement_blue), 2, false),
                new MarketBird("Brown Bird", getString(R.string.achievement_brown), 3, false),
                new MarketBird("Black Bird", getString(R.string.achievement_black), 4, false),
                new MarketBird("White Bird", getString(R.string.achievement_white), 5, false),
                new MarketBird("Pirate Bird", getString(R.string.achievement_pirate), 14, false),
                new MarketBird("Ninja Bird", getString(R.string.achievement_ninja), 6, false),
                new MarketBird("Rainbow Bird", getString(R.string.achievement_rainbow), 7, false),
                new MarketBird("Backward Bird", getString(R.string.achievement_backwards), 12, false),
                new MarketBird("Flashy Bird", getString(R.string.achievement_flashy), 15, false),
                new MarketBird("Super Bird", getString(R.string.achievement_super), 17, false),
                new MarketBird("Hungry Bird", getString(R.string.achievement_hungry), 8, false),
                new MarketBird("Blocky Bird", getString(R.string.achievement_blocky), 9, false),
                new MarketBird("#FFB Bird", getString(R.string.achievement_ffb), 13, false),
                new MarketBird("Ghost Bird", getString(R.string.achievement_ghost), 10, false),
                new MarketBird("Mystery Bird", getString(R.string.achievement_mystery), 16, false),
                new MarketBird("Golden Bird", getString(R.string.achievement_gold), 11, false)
        };

        for(int i = 0; i < marketBirds.length; i++) {
            this.marketBirds[i] = marketBirds[i];
        }



        int screenWidth;
        int screenHeight;

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = getWindowManager().getDefaultDisplay();
            screenWidth = display.getWidth();  // deprecated
            screenHeight = display.getHeight();  // deprecated
        } else {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        }

        double dimensionRatio = (double)screenHeight / screenWidth;


        Log.i("Screen Width", String.valueOf(screenWidth));
        Log.i("Screen Height", String.valueOf(screenHeight));
        Log.i("Dimension Ratio", String.valueOf(dimensionRatio));


        if(isGooglePlayServicesAvailable()) {

            // Create the Google Api Client with access to the Play Games services
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                    .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                            // add other APIs and scopes here as needed
                    .build();

            setupInAppBilling();

        }

        Chartboost.startWithAppId(this, getString(R.string.chartboost_id), getString(R.string.chartboost_signature));
        /* Optional: If you want to program responses to Chartboost events, supply a delegate object here and see step (10) for more information */
        Chartboost.setDelegate(chartboostDelegate);
        Chartboost.onCreate(this);


        initializeRateDialog();
    }

    public boolean isGooglePlayServicesAvailable() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS;
    }

    public void openGooglePlayServicesErrorDialog() {

        runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity), mActivity, PLAY_SERVICES_ERROR_REQUEST_CODE);
                dialog.show();
            }
        });
    }


    private void setupInAppBilling() {
        String base64EncodedPublicKey = "OMITTED";

        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                } else {
                    Log.i(TAG, "IAB Successfully set up");

                    queryInventory();

                    inAppBillingSetup = true;

                }
                // Hooray, IAB is fully set up!

            }
        });

    }

    public boolean getInAppBillingSetup() {
        return inAppBillingSetup;
    }

    private void queryInventory() {
        List<String> additionalSkuList = new ArrayList<String>();
        additionalSkuList.add(SKU_REMOVE_ADS);
        mHelper.queryInventoryAsync(true, additionalSkuList,
                mQueryFinishedListener);
    }

    /**
     * This is a callback for after querying IAPs from Google Play Dev Console
     */
    IabHelper.QueryInventoryFinishedListener
            mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory)
        {
            if (result.isFailure()) {
                // handle error
                Log.e(TAG, "QueryInventory Error: " + result.toString());
                return;
            }
            mAdFree = inventory.hasPurchase(SKU_REMOVE_ADS);

        }
    };

    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new CroppedResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
        engineOptions.getAudioOptions().setNeedsSound(true).setNeedsMusic(true);
        engineOptions.getAudioOptions().getSoundOptions().setMaxSimultaneousStreams(100);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)
            throws Exception {
        mResourceManager = ResourceManager.getInstance();
        mResourceManager.prepare(this);
        mResourceManager.loadSplashResources();

        mSceneManager = SceneManager.getInstance();

        pOnCreateResourcesCallback.onCreateResourcesFinished();

    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
            throws Exception {

        mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                mResourceManager.loadGameResources();
                mSceneManager.setScene(SceneManager.SceneType.SCENE_MENU);
                mResourceManager.unloadSplashResources();
                requestNewInterstitial();

            }
        }));
        pOnCreateSceneCallback.onCreateSceneFinished(mSceneManager.createSplashScene());
    }

    @Override
    public void onPopulateScene(Scene pScene,
                                OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected int getRenderSurfaceViewID() {
        return R.id.SurfaceViewId;
    }


    public void openTwitterShare(int score) {

        String application = "com.twitter.android";

        String text = getString(R.string.i_just_scored) + " " + score + " " + getString(R.string.points_in_crappy_bird_twitter_share);


        Intent intent = this.getPackageManager().getLaunchIntentForPackage(application);
        if (intent != null) {
            // The application exists
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage(application);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            // Start the specific social application
            startActivity(shareIntent);
        } else {
            // The application does not exist
            String sharerUrl = "http://twitter.com/share?text=" + text;
            Intent shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            startActivity(shareIntent);

        }


    }

    public void openFacebookShare(int score) {

        String application = "com.facebook.katana";

        String url = "https://goo.gl/eDWvTO";


        Intent intent = this.getPackageManager().getLaunchIntentForPackage(application);
        if (intent != null) {
            // The application exists
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage(application);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, url);
            // Start the specific social application
            startActivity(shareIntent);
        } else {
            // The application does not exist

            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + url;
            Intent shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            startActivity(shareIntent);

        }

    }

    public void openOtherShare(int score) {
        String message = getString(R.string.i_just_scored) + " " + score + " " + getString(R.string.points_in_crappy_bird);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(Intent.createChooser(share, getString(R.string.share)));
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
            if(intent == null) {
                intent = new Intent();
            }
            // Pass on the activity result to the helper for handling
            if (!isGooglePlayServicesAvailable() || !mHelper.handleActivityResult(requestCode, resultCode, intent)) {
                // not handled, so handle it ourselves (here's where you'd
                // perform any handling of activity results not related to in-app
                // billing...
                if (requestCode == RC_SIGN_IN) {
                    mSignInClicked = false;
                    mResolvingConnectionFailure = false;
                    if (resultCode == RESULT_OK) {
                        mGoogleApiClient.connect();
                    } else {
                        // Bring up an error dialog to alert the user that sign-in
                        // failed. The R.string.signin_failure should reference an error
                        // string in your strings.xml file that tells the user they
                        // could not be signed in, such as "Unable to sign in."
                        BaseGameUtils.showActivityResultError(this,
                                requestCode, resultCode, R.string.unable_to_sign_in);
                    }
                }else if (requestCode == PLAY_SERVICES_ERROR_REQUEST_CODE) {
                    Intent refreshIntent = new Intent(this, GameActivity.class);
                    startActivity(refreshIntent);
                    finish();
                }
                super.onActivityResult(requestCode, resultCode, intent);
            } else {
                Log.d(TAG, "onActivityResult handled by IABUtil.");
            }

    }

    public void displayConnectionError() {
        alert(getString(R.string.connection_error));
    }

    public void displayLongToast(String string) {
        final CharSequence text = string;

        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void displayShortToast(String string) {
        final CharSequence text = string;

        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setSoundOn(boolean bool) {
        if(bool) {
            soundOn = true;
            getEngine().getMusicManager().setMasterVolume(1);
            getEngine().getSoundManager().setMasterVolume(1);
        } else {
            soundOn = false;
            getEngine().getMusicManager().setMasterVolume(0);
            getEngine().getSoundManager().setMasterVolume(0);
        }
    }

    public void toggleSound() {
        if(soundOn) {
            setSoundOn(false);
        } else {
            setSoundOn(true);
        }
    }

    public boolean getSoundOn() {
        return soundOn;
    }

    /**
     * for launching the IAB form.
     * @param sku the sku of the purchase
     */
    public void purchase(String sku) {
        if(isNetworkAvailable()) {
            if(!mHelper.getAsyncInProgress()) {
                mHelper.launchPurchaseFlow(this, sku, IAB_REQUEST_CODE,
                        mPurchaseFinishedListener, "");

            } else {
                alert(getString(R.string.transaction_in_progress));
            }
        } else {
            displayConnectionError();
        }
    }

    public void purchaseNoAds() {

        if(!mAdFree) {
            purchase(SKU_REMOVE_ADS);
        } else {
            alert(getString(R.string.already_purchased));
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase)
        {
            //if we were disposed of in the meantime, quit
            if (mHelper == null) return;

            if (result.isFailure()) {
                if(DEBUGGING)
                    Log.d(TAG, "Error purchasing: " + result);
                Answers.getInstance().logPurchase(new PurchaseEvent()
                        .putItemPrice(BigDecimal.valueOf(0.99))
                        .putCurrency(Currency.getInstance("USD"))
                        .putItemName("Remove Ads")
                        .putItemId("ad_free")
                        .putSuccess(false));
                return;
            }
            else {
                //purchase successful
                alert(getString(R.string.remove_ads_thank_you_message));
                mAdFree = true;

                mSceneManager.setScene(SceneManager.SceneType.SCENE_MENU);

                Answers.getInstance().logPurchase(new PurchaseEvent()
                        .putItemPrice(BigDecimal.valueOf(0.99))
                        .putCurrency(Currency.getInstance("USD"))
                        .putItemName("Remove Ads")
                        .putItemId("ad_free")
                        .putSuccess(true));
            }
        }
    };



    public void alert(String string) {
        final String message = string;
        final Context context = this;
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder bld = new AlertDialog.Builder(context);
                bld.setMessage(message);
                bld.setNeutralButton("OK", null);
                Log.d(TAG, "Showing alert dialog: " + message);
                bld.create().show();
            }
        });


    }

    public void logGameStart() {
        Answers.getInstance().logLevelStart(new LevelStartEvent());
        if(DEBUGGING)
            Log.i(TAG, "sending level start event to answers");
    }

    public void logGameFinished(int score) {
        Answers.getInstance().logLevelEnd(new LevelEndEvent()
                .putScore(score));
        if(DEBUGGING)
            Log.i(TAG, "sending level end event to answers with score " + score);

    }

    private void requestNewInterstitial() {
        Chartboost.cacheInterstitial(CBLocation.LOCATION_GAMEOVER);
    }


    public void showAd() {
        if (Chartboost.hasInterstitial(CBLocation.LOCATION_GAMEOVER)) {
            setNumGamesPlayedSinceAdShown(0);
            Chartboost.showInterstitial(CBLocation.LOCATION_GAMEOVER);
        }
        else {
            requestNewInterstitial();
        }

    }

    /**
     * used to show tutorial on first launch
     * @return whether it is the first time the user is playing
     */
    public boolean getFirstTimePlaying() {
        return getPreferences(Context.MODE_PRIVATE).getBoolean("firstTimePlaying", true);
    }

    public void setFirstTimePlaying(boolean bool) {
        getPreferences(Context.MODE_PRIVATE).edit().putBoolean("firstTimePlaying", bool).commit();
    }


    /**
     * this runs post-Game and handles whether to display an ad or the rate dialog
     */
    public void handleGameFinished(int score) {
        setNumGamesPlayedSinceAdShown(getNumGamesPlayedSinceAdShown() + 1);

        if (!mAdFree && getNumGamesPlayedSinceAdShown() >= GAMES_TO_PLAY_BEFORE_SHOWING_AD) {
            showAd();
        } else {
            if(isNetworkAvailable() && !getRatedAlready())
                openRateDialog();
        }

        submitScoreToLeaderboard(score);
        logGameFinished(score);
    }


    private void initializeRateDialog() {
        // Custom criteria: 3 days and 5 launches
        RateThisApp.Config config = new RateThisApp.Config(3, 10);
        // Custom title and message
        config.setTitle(R.string.rate_title);
        config.setMessage(R.string.rate_message);
        RateThisApp.init(config);

        // Monitor launch times and interval from installation
        RateThisApp.onStart(this);
    }

    public void openRateDialog() {
        final GameActivity mActivity = this;

        if(!rateDialogOpenedThisSession) {
            runOnUiThread(new Runnable() {
                public void run() {
                    RateThisApp.showRateDialogIfNeeded(mActivity);
                    rateDialogOpenedThisSession = true;
                }
            });

        }
    }

    public void openRate() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        if(DEBUGGING)
            Log.i(TAG, "package name: " + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
            setRatedAlready(true);
            Answers.getInstance().logRating(new RatingEvent()
                    .putCustomAttribute("Location", "Main menu"));
        } catch (ActivityNotFoundException e) {
            alert(getString(R.string.could_not_launch_play_store));
        }
    }

    public boolean getRatedAlready() {
        return getPreferences(Context.MODE_PRIVATE).getBoolean("ratedAlready", false);
    }

    public void setRatedAlready(boolean bool) {
        getPreferences(Context.MODE_PRIVATE).edit().putBoolean("ratedAlready", bool).commit();
    }


    public int getMaxScore() {
        return getPreferences(Context.MODE_PRIVATE).getInt("maxScore2", 0);
    }

    public void setMaxScore(int maxScore) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("maxScore2", maxScore).commit();
    }


    public int getMaxScoreSinceLastConnection() {
        return getPreferences(Context.MODE_PRIVATE).getInt("maxScoreSinceLastConnection", 0);
    }

    public void setMaxScoreSinceLastConnection(int maxScore) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("maxScoreSinceLastConnection", maxScore).commit();
    }

    public int getNumGamesPlayedSinceAdShown() {
        return getPreferences(Context.MODE_PRIVATE).getInt("numGamesPlayed", -3); //start at -3 so person gets a few games in before first ad
    }

    public void setNumGamesPlayedSinceAdShown(int num) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("numGamesPlayed", num).commit();
    }

    public int getSelectedBird() {
        return getPreferences(Context.MODE_PRIVATE).getInt("selectedBird2", 0);
    }

    public void setSelectedBird(int birdNum) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("selectedBird2", birdNum).commit();
    }

    /**
     * used for achievements that require getting a certain score multiple times in a row
     * @return the number of times already (without break) that they got that score
     */
    public int getAchievementInARow1Count() {
        return getPreferences(Context.MODE_PRIVATE).getInt("achievementInARow1Count", 0);
    }

    public void setAchievementInARow1Count(int num) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("achievementInARow1Count", num).commit();
    }

    public int getAchievementInARow2Count() {
        return getPreferences(Context.MODE_PRIVATE).getInt("achievementInARow2Count", 0);
    }

    public void setAchievementInARow2Count(int num) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("achievementInARow2Count", num).commit();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);

        if (mHelper != null) mHelper.dispose();
        mHelper = null;
        Chartboost.onDestroy(this);
        System.exit(0);

    }

    @Override
    public void onBackPressed() {
        // If an interstitial is on screen, close it.
        if (Chartboost.onBackPressed())
            return;
        if (mSceneManager.getCurrentScene() != null) {
            mSceneManager.getCurrentScene().onBackKeyPressed();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Chartboost.onResume(this);


        if(getResourcesLoaded() && !(mSceneManager.getCurrentSceneType() == SceneManager.SceneType.SCENE_GAME)) {
            if (mResourceManager.mMusic != null && !mResourceManager.mMusic.isPlaying()) {
                mResourceManager.mMusic.play();
            }
        }

    }

    @Override
    protected void onPause() {
        if(getResourcesLoaded()) {
            if (mResourceManager.mMusic != null && mResourceManager.mMusic.isPlaying()) {
                mResourceManager.mMusic.pause();
            }
            if (mResourceManager.mMariachiFast != null && mResourceManager.mMariachiFast.isPlaying()) {
                mResourceManager.mMariachiFast.pause();
            }
            if (mResourceManager.mMariachiSlow != null && mResourceManager.mMariachiSlow.isPlaying()) {
                mResourceManager.mMariachiSlow.pause();
            }

            if (mSceneManager.getCurrentSceneType() == SceneManager.SceneType.SCENE_GAME) {
                ((GameScene) mSceneManager.getCurrentScene()).setPause(true);
            }
        }

        super.onPause();
        Chartboost.onPause(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Chartboost.onStart(this);
        if(DEBUGGING)
            Log.d(TAG, "onStart(): connecting to Google Play Games");
        if (isGooglePlayServicesAvailable() && !mInSignInFlow && !mExplicitSignOut) {
            // auto sign in
            mGoogleApiClient.connect();
            mInSignInFlow = true;
        }
    }

    @Override
    protected void onStop() {
        Chartboost.onStop(this);
        super.onStop();
        if(DEBUGGING)
            Log.d(TAG, "onStop(): disconnecting from Google Play Games");
        if(isGooglePlayServicesAvailable()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mInSignInFlow = false;

        if(getMaxScoreSinceLastConnection() > 0) {
            submitScoreToLeaderboard(getMaxScoreSinceLastConnection());
            setMaxScoreSinceLastConnection(0);
        }

        handleAchievementsSinceLastConnection();

        checkOwnedBirds();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(DEBUGGING)
            Log.d(TAG, "onConnectionFailed(): attempting to resolve");

        mInSignInFlow = false;

        if (mResolvingConnectionFailure) {
            // already resolving
            if(DEBUGGING)
                Log.d(TAG, "onConnectionFailed(): already resolving");
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }

        // Put code here to display the sign-in button
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        if(DEBUGGING)
            Log.d(TAG, "onConnectionSuspended(): attempting to connect");
        mGoogleApiClient.connect();
    }

    // Call when the sign-in button is clicked
    public void signInClicked() {
        mSignInClicked = true;
        mInSignInFlow = true;
        mGoogleApiClient.connect();
    }

    // Call when the sign-out button is clicked
    public void signOutClicked() {
        mSignInClicked = false;
        // user explicitly signed out, so turn off auto sign in
        mExplicitSignOut = true;
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Games.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
        setSelectedBird(0);
        resetUnlockedBirds();
        mSceneManager.setScene(SceneManager.SceneType.SCENE_MENU);
    }

    public boolean isConnectedToGooglePlay() {
        return mGoogleApiClient.isConnected();
    }


    public void submitScoreToLeaderboard(int score) {
        if(mGoogleApiClient.isConnected()) {
            if(DEBUGGING)
                Log.i(TAG, "Submitting:" + score);
            Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_id), score);
        } else {
            if(score > getMaxScoreSinceLastConnection()) {
                if(DEBUGGING)
                    Log.i(TAG, "Saving locally:" + score);
                setMaxScoreSinceLastConnection(score);
            }
        }
    }

    public void openLeaderboard() {
        if(mGoogleApiClient.isConnected()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                    getString(R.string.leaderboard_id)), REQUEST_LEADERBOARD);
        } else {
            alert(getString(R.string.google_play_connection_error));
        }

    }

    public void openAchievements() {
        if(mGoogleApiClient.isConnected()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                    REQUEST_ACHIEVEMENTS);
        } else {
            alert(getString(R.string.google_play_connection_error));
        }
    }

    public void unlockAchievement(String achievementID) {
        if(mGoogleApiClient.isConnected()) {
            Games.Achievements.unlock(mGoogleApiClient, achievementID);
            unlockBird(achievementID);
        } else {
            setAchievementUnlocked(achievementID, true);
        }

    }

    public boolean getAchievementUnlocked(String achievementID) {
        return getPreferences(Context.MODE_PRIVATE).getBoolean("unlocked" + achievementID, false);
    }


    public void setAchievementUnlocked(String achievementID, boolean unlocked) {
        getPreferences(Context.MODE_PRIVATE).edit().putBoolean("unlocked" + achievementID, unlocked).commit();
    }

    public void incrementAchievement(final String achievementID, int num) {
        if(mGoogleApiClient.isConnected()) {
            Games.Achievements.incrementImmediate(mGoogleApiClient, achievementID, num); /*.setResultCallback(new ResultCallback<Achievements.UpdateAchievementResult>() {

                @Override
                public void onResult(Achievements.UpdateAchievementResult arg0) {
                    if (arg0.getStatus().getStatusCode() == GamesStatusCodes.STATUS_ACHIEVEMENT_UNLOCKED) {
                        unlockBird(achievementID);
                    }
                }
            });*/
        } else {
            setAchievementIncrementSinceLastConnection(achievementID, getAchievementIncrementSinceLastConnection(achievementID) + num);
        }
    }

    public int getAchievementIncrementSinceLastConnection(String achievementID) {
        return getPreferences(Context.MODE_PRIVATE).getInt("incremented" + achievementID, 0);
    }

    public void setAchievementIncrementSinceLastConnection(String achievementID, int num) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("incremented" + achievementID, num).commit();
    }

    private void handleAchievementsSinceLastConnection() {
        String[] unlockAchievementIDs = {
                getString(R.string.achievement_red),
                getString(R.string.achievement_rainbow),
                getString(R.string.achievement_ghost),
                getString(R.string.achievement_super),
                getString(R.string.achievement_gold),
                getString(R.string.achievement_blue),
                getString(R.string.achievement_brown),
                getString(R.string.achievement_pirate),
                getString(R.string.achievement_hungry),
                getString(R.string.achievement_ffb),
                getString(R.string.achievement_mystery),
                getString(R.string.achievement_backwards),
                getString(R.string.achievement_flashy),
                getString(R.string.achievement_ninja)

        };
        String[] incrementAchievementIDs = {
                getString(R.string.achievement_black),
                getString(R.string.achievement_white),
                getString(R.string.achievement_blocky)

        };

        for(String ID : unlockAchievementIDs) {
            if(getAchievementUnlocked(ID)) {
                if(DEBUGGING)
                    Log.i(TAG, "Unlocking " + ID);
                unlockAchievement(ID);
                setAchievementUnlocked(ID, false);
            }
        }

        for(String ID : incrementAchievementIDs) {
            if(getAchievementIncrementSinceLastConnection(ID) > 0) {
                if(DEBUGGING)
                    Log.i(TAG, "Incrementing " + ID + " by " + getAchievementIncrementSinceLastConnection(ID));
                incrementAchievement(ID, getAchievementIncrementSinceLastConnection(ID));
                setAchievementIncrementSinceLastConnection(ID, 0);
            }
        }
    }

    public void checkOwnedBirds() {
        Games.Achievements.load(mGoogleApiClient, false).setResultCallback(new AchievementsLoadedCallback());
    }

    public void unlockBird(String achievementID) {
        for(MarketBird bird : marketBirds) {
            if(!bird.isUnlocked() && bird.getAchievementID().equals(achievementID)) {
                bird.setUnlocked(true);
            }
        }
    }

    /**
     * resets unlocked birds to just the "Classic Bird". Used for logout.
     */
    public void resetUnlockedBirds() {
        for(int i = 1; i < marketBirds.length; i++) {
            marketBirds[i].setUnlocked(false);
        }
    }

    class AchievementsLoadedCallback implements ResultCallback<Achievements.LoadAchievementsResult> {

        @Override
        public void onResult(Achievements.LoadAchievementsResult arg0) {
            com.google.android.gms.games.achievement.Achievement ach;
            AchievementBuffer aBuffer = arg0.getAchievements();
            Iterator<com.google.android.gms.games.achievement.Achievement> aIterator = aBuffer.iterator();

            while (aIterator.hasNext()) {
                ach = aIterator.next();
                for(MarketBird bird : marketBirds) {
                    if (!bird.isUnlocked() && bird.getAchievementID().equals(ach.getAchievementId())) {
                        if (ach.getState() == com.google.android.gms.games.achievement.Achievement.STATE_UNLOCKED) {
                            bird.setUnlocked(true);
                        }
                        //break;
                    }
                }
            }
            aBuffer.close();

            if(initialSceneLoaded && mSceneManager.getCurrentSceneType().equals(SceneManager.SceneType.SCENE_MARKET)) {
                ((MarketScene)(mSceneManager.getCurrentScene())).syncNewBirds();
            }
        }
    }

    public void setInitialSceneLoaded(boolean initialSceneLoaded) {
        this.initialSceneLoaded = initialSceneLoaded;
    }
}
