package com.application.nick.pizzaplanet;

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

import com.application.nick.pizzaplanet.scene.GameScene;
import com.application.nick.pizzaplanet.util.NumberFormatter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.CroppedResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.LayoutGameActivity;


public class GameActivity extends LayoutGameActivity implements GameValues{

    private final int PLAY_SERVICES_ERROR_REQUEST_CODE = 10002;

    private ZoomCamera mCamera;
    private ResourceManager mResourceManager;
    private SceneManager mSceneManager;

    private AdView mAdView;

    private boolean soundOn = true;

    private GameActivity mActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int screenWidth;
        int screenHeight;

        /**************************
         * TESTING
         *

        setMoney(24589230);
        setLocationNumber(4);
        setStorageNumber(5);
        setEmployeeNumber(15);

        /**************************
         * END TESTING
         */

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

            if (isNetworkAvailable()) {

                if(dimensionRatio > 1.6) { //don't create banner ad if it will cover up buttons.
                    //create banner ad from admob
                    createBannerAd();
                }

            }

        }


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


    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        mCamera.setBounds(0f, 0f, CAMERA_WIDTH, CAMERA_HEIGHT);
        mCamera.setBoundsEnabled(true);

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
                setGameScene();
                mResourceManager.unloadSplashResources();
            }
        }));
        pOnCreateSceneCallback.onCreateSceneFinished(mSceneManager.createSplashScene());
    }

    public void setGameScene() {
        switch (getLocationNumber()) {
            case 0:
                mSceneManager.setGameScene(SceneManager.GameSceneType.HOME);
                break;
            case 1:
                mSceneManager.setGameScene(SceneManager.GameSceneType.WAREHOUSE);
                break;
            case 2:
                mSceneManager.setGameScene(SceneManager.GameSceneType.COAST);
                break;
            case 3:
                mSceneManager.setGameScene(SceneManager.GameSceneType.ISLAND);
                break;
            case 4:
                mSceneManager.setGameScene(SceneManager.GameSceneType.PLANET);
                break;
        }
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        if (mSceneManager.getCurrentScene() != null) {
            mSceneManager.getCurrentScene().onBackKeyPressed();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getResourcesLoaded()) {
            if (mResourceManager.mMusic != null && !mResourceManager.mMusic.isPlaying()) {
                mResourceManager.mMusic.play();
            }
        }

        Log.i("Paused Time", String.valueOf(getPausedTime()));
        Log.i("Current Time", String.valueOf(System.nanoTime()));
        if(mActivity.getPizza() != 0) {

            Long difference = System.nanoTime() - getPausedTime();
            if (difference < 0) {
                difference = System.nanoTime();
            }
            Long secondsElapsed = difference / (long) (Math.pow(10, 9)); //convert to seconds
            Log.i("Time Elapsed", String.valueOf(secondsElapsed));

            addPizza(secondsElapsed * employeePurchases[getEmployeeNumber()].getNumEmployees());

            if (getResourcesLoaded() && mSceneManager.getCurrentSceneType() == SceneManager.SceneType.SCENE_GAME) {
                ((GameScene) mSceneManager.getCurrentScene()).addPizzaNotPickedUp((int) (secondsElapsed * employeePurchases[getEmployeeNumber()].getNumEmployees()));
            }
        }

    }

    @Override
    protected void onPause() {
        if(getResourcesLoaded()) {
            if (mResourceManager.mMusic != null && mResourceManager.mMusic.isPlaying()) {
                mResourceManager.mMusic.pause();
            }
        }

        setPausedTime(System.nanoTime());

        super.onPause();

    }


    public void createBannerAd() {

        mAdView = (AdView) findViewById(R.id.adViewId);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

    }


    public void openRate() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            alert("Could not launch Play Store.");
        }
    }


    public void displayConnectionError() {
        alert("Please check your connection and try again.");
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

    public void alert(String string) {
        final String message = string;
        final Context context = this;
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder bld = new AlertDialog.Builder(context);
                bld.setMessage(message);
                bld.setNeutralButton("OK", null);
                Log.d("Pizza Planet", "Showing alert dialog: " + message);
                bld.create().show();
            }
        });


    }

    /**
     * for getting the time in nanoseconds when the game was paused
     * @return
     */
    public long getPausedTime() {
        return getPreferences(Context.MODE_PRIVATE).getLong("pausedTime", 0);
    }


    /**
     * For setting the time in nanoseconds when the game was paused
     * @param time
     */
    public void setPausedTime(long time) {
        getPreferences(Context.MODE_PRIVATE).edit().putLong("pausedTime", time).commit();
    }


    public long getPizza() {
        return getPreferences(Context.MODE_PRIVATE).getLong("pizza", 0);
    }

    public void setPizza(long pizza) {
        getPreferences(Context.MODE_PRIVATE).edit().putLong("pizza", pizza).commit();
    }

    public void addPizza(long pizza) {
        long currentPizza = getPizza();
        currentPizza += pizza;
        setPizza(currentPizza);
    }

    public void subtractPizza(long pizza) {
        long currentPizza = getPizza();
        if(currentPizza > pizza) {
            currentPizza -= pizza;
            setPizza(currentPizza);
        } else {
            setPizza(0);
        }
    }

    public int getLocationNumber() {
        return getPreferences(Context.MODE_PRIVATE).getInt("locationNumber", 0);
    }

    public void setLocationNumber(int locationNumber) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("locationNumber", locationNumber).commit();
    }

    public int getStorageNumber() {
        return getPreferences(Context.MODE_PRIVATE).getInt("storageNumber", 0);
    }

    public void setStorageNumber(int storageNumber) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("storageNumber", storageNumber).commit();
    }

    public int getEmployeeNumber() {
        return getPreferences(Context.MODE_PRIVATE).getInt("employeeNumber", 1);
    }

    public void setEmployeeNumber(int num) {
        getPreferences(Context.MODE_PRIVATE).edit().putInt("employeeNumber", num).commit();
    }

    public long getNetValue() {
        return getPreferences(Context.MODE_PRIVATE).getLong("netValue", 0);
    }

    public void setNetValue(long netValue) {
        getPreferences(Context.MODE_PRIVATE).edit().putLong("netValue", netValue).commit();
    }

    public void addNetValue(long money) {
        long currentValue = getNetValue();
        currentValue += money;
        setNetValue(currentValue);
    }

    public void subtractMoney(long money) {
        long currentMoney = getMoney();
        if(currentMoney > money) {
            currentMoney -= money;
            setMoney(currentMoney);
        } else {
            setMoney(0);
        }
    }

    public long getMoney() {
        return getPreferences(Context.MODE_PRIVATE).getLong("money", 0);
    }

    public void setMoney(long money) {
        getPreferences(Context.MODE_PRIVATE).edit().putLong("money", money).commit();
    }

    public void addMoney(long money) {
        long currentMoney = getMoney();
        currentMoney += money;
        setMoney(currentMoney);
    }



    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String convertNumberToFriendlyString(long number) {
        return NumberFormatter.convertNumberToFriendlyString(number);
    }


}
