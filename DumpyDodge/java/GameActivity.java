package com.application.nick.dumpydodge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.application.nick.dumpydodge.IAB.util.IabHelper;
import com.application.nick.dumpydodge.IAB.util.IabResult;
import com.application.nick.dumpydodge.IAB.util.Inventory;
import com.application.nick.dumpydodge.IAB.util.Purchase;
import com.application.nick.dumpydodge.scene.GameScene;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.CroppedResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.LayoutGameActivity;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LevelEndEvent;
import com.crashlytics.android.answers.LevelStartEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.crashlytics.android.answers.RatingEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.kobakei.ratethisapp.RateThisApp;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;


public class GameActivity extends LayoutGameActivity implements GameValues, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String SKU_REMOVE_ADS = "ad_free";

    public static final int CAMERA_WIDTH = 640;
    public static final int CAMERA_HEIGHT = 360;

    private Camera mCamera;
    private ResourceManager mResourceManager;
    private SceneManager mSceneManager;

    //private GameActivity mActivity = this;

    private boolean soundOn, mAdFree;

    private GoogleApiClient mGoogleApiClient;

    private static int RC_SIGN_IN = 9001, REQUEST_LEADERBOARD = 9002, REQUEST_ACHIEVEMENTS = 9003, PLAY_SERVICES_ERROR_REQUEST_CODE = 9004, IAB_REQUEST_CODE = 9005;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;
    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false; // set to true when you're in the middle of the sign in flow, to know you should not attempt to connect in onStart()
    boolean rateDialogOpenedThisSession = false;
    boolean inAppBillingSetup = false;

    private IabHelper mHelper;

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


        Chartboost.startWithAppId(this, getString(R.string.chartboost_app_id), getString(com.application.nick.dumpydodge.R.string.chartboost_signature));
        /* Optional: If you want to program responses to Chartboost events, supply a delegate object here and see step (10) for more information */
        //Chartboost.setDelegate(delegate);
        Chartboost.onCreate(this);
        Chartboost.setDelegate(chartboostDelegate);
        Chartboost.setAutoCacheAds(true);
        if(DEBUGGING)
            Log.i(TAG, "onCreate()");

        soundOn = true;

        initializeRateDialog();

    }

    private void setupInAppBilling() {
        String base64EncodedPublicKey = "OMITTED";

        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    if(DEBUGGING)
                        Log.d(TAG, "Problem setting up In-app Billing: " + result);
                } else {
                    if(DEBUGGING)
                        Log.i(TAG, "In app billing Successfully set up");

                    queryInventory();

                    inAppBillingSetup = true;
                }
                // Hooray, IAB is fully set up!
            }
        });

    }

    private void queryInventory() {
        List<String> additionalSkuList = new ArrayList<String>();
        additionalSkuList.add(SKU_REMOVE_ADS);
        mHelper.queryInventoryAsync(true, additionalSkuList,
                mQueryFinishedListener);
    }

    public boolean getInAppBillingSetup() {
        return inAppBillingSetup;
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
                if(DEBUGGING)
                    Log.e(TAG, "QueryInventory Error: " + result.toString());
                return;
            }
            /*if (inventory.hasPurchase(SKU_REMOVE_ADS)) {
                mHelper.consumeAsync(inventory.getPurchase(SKU_REMOVE_ADS), null);
            }*/
            //get prices of available purchases
            //String priceRemoveAds = inventory.getSkuDetails(getString(R.string.sku_remove_ads)).getPrice();

            mAdFree = inventory.hasPurchase(SKU_REMOVE_ADS);

        }
    };

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
                alert(getString(com.application.nick.dumpydodge.R.string.transaction_in_progress));
            }
        } else {
            alert(getString(com.application.nick.dumpydodge.R.string.connection_error));
        }
    }

    public void purchaseNoAds() {

        if(!mAdFree) {
            purchase(SKU_REMOVE_ADS);
        } else {
            alert(getString(com.application.nick.dumpydodge.R.string.already_purchased));
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
                alert(getString(com.application.nick.dumpydodge.R.string.remove_ads_thank_you_message));
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


    /*IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isSuccess()) {
                        if (purchase.getSku().equals(getString(R.string.sku_remove_ads))) {

                            alert(getString(R.string.remove_ads_thank_you_message));

                        }

                        mSceneManager.setScene(SceneManager.SceneType.SCENE_MENU);
                    }
                    else {
                        alert(getString(R.string.something_went_wrong));
                    }
                }
            };*/



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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if(DEBUGGING)
            Log.i(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        if(DEBUGGING)
            Log.i(TAG, "onCreateEngineOptions()");

        mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new CroppedResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
        engineOptions.getAudioOptions().setNeedsSound(true).setNeedsMusic(true);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)
            throws Exception {
        if(DEBUGGING)
            Log.i(TAG, "onCreateResources()");

        mResourceManager = ResourceManager.getInstance();
        mResourceManager.prepare(this);
        mResourceManager.loadSplashResources();

        mSceneManager = SceneManager.getInstance();

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        Log.i(TAG, "onCreateScene()");
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
        return com.application.nick.dumpydodge.R.layout.activity_main;
    }

    @Override
    protected int getRenderSurfaceViewID() {
        return com.application.nick.dumpydodge.R.id.SurfaceViewId;
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
     * this runs post-Game and handles whether to display an ad or the rate dialog
     */
    public void handleGameFinished() {
        setNumGamesPlayedSinceAdShown(getNumGamesPlayedSinceAdShown() + 1);

            if (!mAdFree && getNumGamesPlayedSinceAdShown() >= GAMES_TO_PLAY_BEFORE_SHOWING_AD) {
                showAd();
            } else {
                if(isNetworkAvailable() && !getRatedAlready())
                    openRateDialog();
            }

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
            mResourceManager.mMusic.resume();
            getEngine().getSoundManager().setMasterVolume(1);
        } else {
            soundOn = false;
            mResourceManager.mMusic.pause();
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


    public void alert(String string) {
        final String message = string;
        final Context context = this;
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder bld = new AlertDialog.Builder(context);
                bld.setMessage(message);
                bld.setNeutralButton("OK", null);
                if(DEBUGGING)
                    Log.d(TAG, "Showing alert dialog: " + message);
                bld.create().show();
            }
        });


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
        return getPreferences(Context.MODE_PRIVATE).getInt("maxScore", 0);
    }

    public void setMaxScore(int maxScore) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("maxScore", maxScore).commit();
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

    /**
     * used for achievements that require total time played. increments by minutes
     * @return number of seconds left over from previous games (less than 60)
     */
    public int getLeftOverSeconds() {
        return getPreferences(Context.MODE_PRIVATE).getInt("leftOverSeconds", 0);
    }

    public void setLeftOverSeconds(int seconds) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("leftOverSeconds", seconds).commit();
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

    public boolean getShowingTutorial() {
        return getPreferences(Context.MODE_PRIVATE).getBoolean("showingTutorial", true);
    }

    public void setShowingTutorial(boolean showingTutorial) {
        getPreferences(Context.MODE_PRIVATE).edit().putBoolean("showingTutorial", showingTutorial).commit();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onDestroy() {
        if(DEBUGGING)
            Log.i(TAG, "onDestroy()");
        super.onDestroy();
        if (mHelper != null) {
            mHelper.dispose();
        }
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
        if(DEBUGGING)
            Log.i(TAG, "onResume()");
        super.onResume();
        Chartboost.onResume(this);

        if(getResourcesLoaded() && !(mSceneManager.getCurrentSceneType() == SceneManager.SceneType.SCENE_GAME)) {
            if (mResourceManager.mMusic != null && !mResourceManager.mMusic.isPlaying()) {
                mResourceManager.mMusic.play();
            }
        }
        //check if user has no ads
        if(!mHelper.getAsyncInProgress() && getInAppBillingSetup() && isNetworkAvailable()) {
            queryInventory();
        }

    }

    @Override
    protected void onPause() {
        if(DEBUGGING)
            Log.i(TAG, "onPause()");
        if(getResourcesLoaded()) {
            if (mResourceManager.mMusic != null && mResourceManager.mMusic.isPlaying()) {
                mResourceManager.mMusic.pause();
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
        // TODO The player is signed in. Hide the sign-in button and allow the player to proceed.
        if(getMaxScoreSinceLastConnection() > 0) {
            submitScoreToLeaderboard(getMaxScoreSinceLastConnection());
            setMaxScoreSinceLastConnection(0);
        }

        handleAchievementsSinceLastConnection();
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
                    RC_SIGN_IN, getString(com.application.nick.dumpydodge.R.string.signin_other_error))) {
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

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

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
                            requestCode, resultCode, com.application.nick.dumpydodge.R.string.signin_failure);
                }
            } else if (requestCode == PLAY_SERVICES_ERROR_REQUEST_CODE) {
                Intent refreshIntent = new Intent(this, GameActivity.class);
                startActivity(refreshIntent);
                finish();
            }
            super.onActivityResult(requestCode, resultCode, intent);
        } else {
            if(DEBUGGING)
                Log.d(TAG, "onActivityResult handled by IABUtil.");
        }

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
    }

    public boolean isConnectedToGooglePlay() {
        return mGoogleApiClient.isConnected();
    }

    public boolean isGooglePlayServicesAvailable() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS;
    }

    public void openGooglePlayServicesErrorDialog() {
        final GameActivity mActivity = this;
        runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity), mActivity, PLAY_SERVICES_ERROR_REQUEST_CODE);
                dialog.show();
            }
        });
    }

    public void submitScoreToLeaderboard(int score) {
        if(mGoogleApiClient.isConnected()) {
            if(DEBUGGING)
                Log.i(TAG, "Submitting:" + score);
            Games.Leaderboards.submitScore(mGoogleApiClient, getString(com.application.nick.dumpydodge.R.string.leaderboard_id), score);
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
                    getString(com.application.nick.dumpydodge.R.string.leaderboard_id)), REQUEST_LEADERBOARD);
        } else {
            alert(getString(com.application.nick.dumpydodge.R.string.google_play_connection_error));
        }

    }

    public void openAchievements() {
        if(mGoogleApiClient.isConnected()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                    REQUEST_ACHIEVEMENTS);
        } else {
            alert(getString(com.application.nick.dumpydodge.R.string.google_play_connection_error));
        }
    }

    public void unlockAchievement(String achievementID) {
        if(mGoogleApiClient.isConnected()) {
            Games.Achievements.unlock(mGoogleApiClient, achievementID);
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

    public void incrementAchievement(String achievementID, int num) {
        if(mGoogleApiClient.isConnected()) {
            Games.Achievements.increment(mGoogleApiClient, achievementID, num);
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
                getString(com.application.nick.dumpydodge.R.string.ach_time_single_game_1),
                getString(com.application.nick.dumpydodge.R.string.ach_time_single_game_2),
                getString(com.application.nick.dumpydodge.R.string.ach_time_single_game_3),
                getString(com.application.nick.dumpydodge.R.string.ach_time_single_game_4),
                getString(com.application.nick.dumpydodge.R.string.ach_time_single_game_5),
                getString(com.application.nick.dumpydodge.R.string.ach_in_a_row_1),
                getString(com.application.nick.dumpydodge.R.string.ach_in_a_row_2),
                getString(com.application.nick.dumpydodge.R.string.ach_first_bird_dead),
                getString(com.application.nick.dumpydodge.R.string.ach_no_moving),
                getString(com.application.nick.dumpydodge.R.string.ach_killed_in_air),
                getString(com.application.nick.dumpydodge.R.string.ach_wall_to_wall_1),
                getString(com.application.nick.dumpydodge.R.string.ach_wall_to_wall_2),
                getString(com.application.nick.dumpydodge.R.string.ach_tree_touch_1),
                getString(com.application.nick.dumpydodge.R.string.ach_tree_touch_2),
                getString(com.application.nick.dumpydodge.R.string.ach_tree_touch_3),
                getString(com.application.nick.dumpydodge.R.string.ach_mothership_first_time),
                getString(com.application.nick.dumpydodge.R.string.ach_mothership),
                getString(com.application.nick.dumpydodge.R.string.ach_slow_motion),
                getString(com.application.nick.dumpydodge.R.string.ach_machine_gun),
                getString(com.application.nick.dumpydodge.R.string.ach_umbrella_bounces)

        };
        String[] incrementAchievementIDs = {
                getString(com.application.nick.dumpydodge.R.string.ach_time_total_1),
                getString(com.application.nick.dumpydodge.R.string.ach_time_total_2),
                getString(com.application.nick.dumpydodge.R.string.ach_time_total_3),
                getString(com.application.nick.dumpydodge.R.string.ach_jumping),
                getString(com.application.nick.dumpydodge.R.string.ach_ghost_birds),
                getString(com.application.nick.dumpydodge.R.string.ach_pirate_booty),
                getString(com.application.nick.dumpydodge.R.string.ach_ninja_dodge),
                getString(com.application.nick.dumpydodge.R.string.ach_deaths),
                getString(com.application.nick.dumpydodge.R.string.ach_deaths_by_mega_crap),
                getString(com.application.nick.dumpydodge.R.string.ach_total_powerups)
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


    private void initializeRateDialog() {
        // Custom criteria: 3 days and 5 launches
        RateThisApp.Config config = new RateThisApp.Config(3, 5);
        // Custom title and message
        config.setTitle(com.application.nick.dumpydodge.R.string.rate_title);
        config.setMessage(com.application.nick.dumpydodge.R.string.rate_message);
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

    @Override
    public void onRestart() {
        super.onRestart();
        if(DEBUGGING)
            Log.i(TAG, "onRestart()");
    }




}
