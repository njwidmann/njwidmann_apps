package com.application.nick.pizzaplanet.scene;

import com.application.nick.pizzaplanet.SceneManager;
import com.application.nick.pizzaplanet.entity.Purchases.EmployeePurchase;
import com.application.nick.pizzaplanet.entity.Purchases.Location;
import com.application.nick.pizzaplanet.entity.Purchases.PizzaStorage;
import com.application.nick.pizzaplanet.GameValues;
import com.application.nick.pizzaplanet.util.ParallaxLayer;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.util.HorizontalAlign;

/**
 * Created by Nick on 4/5/2015.
 */
public abstract class GameScene extends BaseScene implements IOnSceneTouchListener, ScrollDetector.IScrollDetectorListener, PinchZoomDetector.IPinchZoomDetectorListener, GameValues {

    private Sprite gameHUDBackground, tutorial;
    private TiledSprite scene;
    private TiledSprite sellButton, marketButton, closeButton;

    private Text pizzaText, moneyText, rateText;

    private float pizzaTimeCounter = 0;

    private int pizzaNotPickedUp;

    public GameScene self = this;

    private ZoomCamera mZoomCamera;
    private SurfaceScrollDetector mScrollDetector;
    private PinchZoomDetector mPinchZoomDetector;
    private float mPinchZoomStartedCameraZoomFactor;

    public Location location;
    public PizzaStorage pizzaStorage;

    private HUD gameHUD;
    private CameraScene mTutorialScene;

    private boolean showingTutorial = false;

    @Override
    public void createScene() {
        mEngine.registerUpdateHandler(new FPSLogger());

        location = GameScene.locations[mActivity.getLocationNumber()];
        pizzaStorage = location.getStorages()[mActivity.getStorageNumber()];



        mZoomCamera = (ZoomCamera) mCamera;

        setOnAreaTouchTraversalFrontToBack();

        this.mScrollDetector = new SurfaceScrollDetector(this);
        if(MultiTouch.isSupported(mActivity)) {
            try {
                this.mPinchZoomDetector = new PinchZoomDetector(this);
            } catch (Exception e) {
                this.mPinchZoomDetector = null;
            }
        } else {
            this.mPinchZoomDetector = null;
        }

        setOnSceneTouchListener(this);
        setTouchAreaBindingOnActionDownEnabled(true);

        if (mResourceManager.mMusic != null && !mResourceManager.mMusic.isPlaying()) {
            mResourceManager.mMusic.resume();
        }

        if(mActivity.getLocationNumber() < 4) {
            if (mActivity.getLocationNumber() < 2) {
                scene = new TiledSprite(0, SCENE_Y, mResourceManager.mScenesTextureRegion, mVertexBufferObjectManager);
            } else if (mActivity.getLocationNumber() < 4) {
                scene = new TiledSprite(0, SCENE_Y, mResourceManager.mScenes2TextureRegion, mVertexBufferObjectManager);
            }

            scene.setCurrentTileIndex(pizzaStorage.getSceneTileIndex());
            attachChild(scene);
        } else if(mActivity.getLocationNumber() < 6) {
            scene = new TiledSprite(0, SCENE_Y, mResourceManager.mScenes3TextureRegion, mVertexBufferObjectManager);

            ParallaxLayer parallaxBackground = new ParallaxLayer(mZoomCamera, false);
            parallaxBackground.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-4, scene, false));
            parallaxBackground.setParallaxChangePerSecond(10);
            attachChild(parallaxBackground);

        }



        setPizzaNotPickedUp((int) mActivity.getPizza() % SLICES_PER_PIZZA);



         /* Handle moving entities */
        registerUpdateHandler(new IUpdateHandler() {

            @Override
            public void reset() {
            }

            @Override
            public void onUpdate(float pSecondsElapsed) {

                pizzaTimeCounter += pSecondsElapsed;
                if (pizzaTimeCounter >= 1.0f) {
                    long previousPizza = mActivity.getPizza();
                    mActivity.addPizza(employeePurchases[mActivity.getEmployeeNumber()].getNumEmployees());
                    if (mActivity.getPizza() > pizzaStorage.getPizzaCapacity() * SLICES_PER_PIZZA) {
                        mActivity.setPizza(pizzaStorage.getPizzaCapacity() * SLICES_PER_PIZZA);
                        pizzaNotPickedUp += (pizzaStorage.getPizzaCapacity() * SLICES_PER_PIZZA - previousPizza);
                    } else {
                        pizzaNotPickedUp += employeePurchases[mActivity.getEmployeeNumber()].getNumEmployees();
                    }
                    pizzaTimeCounter = 0;
                }

                updateText();


            }
        });


        //CREATE HUD

        gameHUD = new HUD();

        gameHUDBackground = new Sprite(0, 0, mResourceManager.mHUDTextureRegion, mVertexBufferObjectManager);
        gameHUD.attachChild(gameHUDBackground);


        final float moneyTextX = 175;
        final float moneyTextY = 44;
        final float pizzaTextX = 175;
        final float pizzaTextY = 140;
        final float rateTextX = 318;
        final float rateTextY = 241;


        moneyText = new Text(moneyTextX, moneyTextY, mResourceManager.mFontBlack45, "0123456789kmbtq", new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
        gameHUD.attachChild(moneyText);

        pizzaText = new Text(pizzaTextX, pizzaTextY, mResourceManager.mFontBlack45, "0123456789 /0123456789kmbtq", new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
        gameHUD.attachChild(pizzaText);

        rateText = new Text(rateTextX, rateTextY, mResourceManager.mFontBlack45, "0123456789kmbtq", new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
        gameHUD.attachChild(rateText);

        final float sellButtonY = 315;
        final float sellButtonX = (SCREEN_WIDTH / 2) - mResourceManager.mSellButtonTextureRegion.getWidth() * 1.5f;
        final float marketButtonY = sellButtonY;
        final float marketButtonX = (SCREEN_WIDTH / 2) + mResourceManager.mMarketButtonTextureRegion.getWidth() * 0.5f;

        sellButton = new TiledSprite(sellButtonX, sellButtonY, mResourceManager.mSellButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(mActivity.getPizza() >= SLICES_PER_PIZZA) {
                    if (pSceneTouchEvent.isActionDown() && getCurrentTileIndex() == 0) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();
                    }
                    if (pSceneTouchEvent.isActionUp()) {
                        setCurrentTileIndex(0);
                        sellPizza();
                    }
                } else if (mActivity.getPizza() < SLICES_PER_PIZZA && pSceneTouchEvent.isActionDown() && getCurrentTileIndex() == 0){
                    mActivity.alert("You need at least 1 full pizza (10 slices) to sell.");
                }

                return true;
            }

        };
        sellButton.setCurrentTileIndex(0);
        gameHUD.registerTouchArea(sellButton);
        gameHUD.attachChild(sellButton);

        marketButton = new TiledSprite(marketButtonX, marketButtonY, mResourceManager.mMarketButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown() && getCurrentTileIndex() == 0) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();
                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    gameHUD.clearTouchAreas();
                    gameHUD.detachChildren();
                    mZoomCamera.setZoomFactor(1);
                    mSceneManager.setScene(SceneManager.SceneType.SCENE_MARKET);
                }

                return true;
            }

        };
        marketButton.setCurrentTileIndex(0);
        gameHUD.registerTouchArea(marketButton);
        gameHUD.attachChild(marketButton);

        final float volumeButtonX = 595;
        final float volumeButtonY = 35;

        final TiledSprite volumeButton = new TiledSprite(volumeButtonX, volumeButtonY, mResourceManager.mVolumeButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (isVisible() && pSceneTouchEvent.isActionUp()) {
                    if(getCurrentTileIndex() == 0) {
                        setCurrentTileIndex(1);
                    } else {
                        setCurrentTileIndex(0);
                    }
                    mResourceManager.mButtonSound.play();
                    mActivity.toggleSound();
                }
                return true;
            }
        };
        if(mActivity.getSoundOn()) {
            volumeButton.setCurrentTileIndex(0);
        } else {
            volumeButton.setCurrentTileIndex(1);
        }
        volumeButton.setScale(0.75f);
        gameHUD.registerTouchArea(volumeButton);
        gameHUD.attachChild(volumeButton);

        final float helpX = volumeButtonX;
        final float helpY = 110;

        final TiledSprite helpButton = new TiledSprite(helpX, helpY, mResourceManager.mHelpButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(!showingTutorial) {
                    if (pSceneTouchEvent.isActionDown()) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();
                    }

                    if (pSceneTouchEvent.isActionUp()) {
                        setCurrentTileIndex(0);
                        showTutorial();
                    }
                } else {
                    if (pSceneTouchEvent.isActionDown()) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();
                    }

                    if (pSceneTouchEvent.isActionUp()) {
                        setCurrentTileIndex(0);
                        hideTutorial();
                    }

                }

                return true;
            }
        };


        helpButton.setCurrentTileIndex(0);
        helpButton.setScale(0.75f);
        gameHUD.registerTouchArea(helpButton);
        gameHUD.attachChild(helpButton);


        mZoomCamera.setHUD(gameHUD);


        tutorial = new Sprite(0, 0, mResourceManager.mTutorialSceneTextureRegion, mVertexBufferObjectManager);
        gameHUD.attachChild(tutorial);
        tutorial.setVisible(false);


        closeButton = new TiledSprite(helpX, helpY, mResourceManager.mCloseButtonTextureRegion, mVertexBufferObjectManager);
        closeButton.setCurrentTileIndex(0);
        closeButton.setScale(0.75f);
        gameHUD.registerTouchArea(closeButton);
        gameHUD.attachChild(closeButton);
        closeButton.setVisible(false);
    }

    @Override
    public void onScrollStarted(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
        final float zoomFactor = mZoomCamera.getZoomFactor();
        mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);}

    @Override
    public void onScroll(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
        final float zoomFactor = mZoomCamera.getZoomFactor();
        mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);}

    @Override
    public void onScrollFinished(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
        final float zoomFactor = mZoomCamera.getZoomFactor();
        mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);}


    @Override
    public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
        this.mPinchZoomStartedCameraZoomFactor = this.mZoomCamera.getZoomFactor();
    }

    @Override
    public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
        float newZoomFactor = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
        if (newZoomFactor < 1f) {
            newZoomFactor = 1f;
        }
        if (newZoomFactor > 2f) {
            newZoomFactor = 2f;
        }

        this.mZoomCamera.setZoomFactor(newZoomFactor);
    }

    @Override
    public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
        float newZoomFactor = mPinchZoomStartedCameraZoomFactor * pZoomFactor;
        if (newZoomFactor < 1f) {
            newZoomFactor = 1f;
        }
        if (newZoomFactor > 2f) {
            newZoomFactor = 2f;
        }

        this.mZoomCamera.setZoomFactor(newZoomFactor);
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

        if (this.mPinchZoomDetector.isZooming()) {
            this.mScrollDetector.setEnabled(false);
        } else {
            if (pSceneTouchEvent.isActionDown()) {
                this.mScrollDetector.setEnabled(true);
            }
            this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
        }

        return true;

    }

    @Override
    public void onBackKeyPressed() {
        if(showingTutorial) {
            hideTutorial();
        } else {
            mResourceManager.unloadGameResources();
            mActivity.finish();
        }
    }


    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_GAME;
    }

    public abstract SceneManager.GameSceneType getGameSceneType();

    @Override
    public void disposeScene() {
        //TODO
    }

    public void showTutorial() {
        tutorial.setVisible(true);
        closeButton.setVisible(true);
        showingTutorial = true;
    }

    public void hideTutorial() {
        tutorial.setVisible(false);
        closeButton.setVisible(false);
        showingTutorial = false;
    }

    private void sellPizza() {
        long unsoldPizza = mActivity.getPizza() % SLICES_PER_PIZZA;
        mActivity.addMoney(mActivity.getPizza() - unsoldPizza);
        mActivity.addNetValue(mActivity.getPizza() - unsoldPizza);
        mActivity.setPizza(unsoldPizza);
        mSceneManager.setGameScene(getGameSceneType());
    }

    private void updateText() {

        moneyText.setText(String.valueOf(mActivity.convertNumberToFriendlyString(mActivity.getMoney())));
        pizzaText.setText(String.valueOf(mActivity.convertNumberToFriendlyString(mActivity.getPizza())) + " / " + pizzaStorage.getCapacityString());
        rateText.setText(String.valueOf(mActivity.convertNumberToFriendlyString(employeePurchases[mActivity.getEmployeeNumber()].getNumEmployees())));

    }

    public int getPizzaNotPickedUp() {
        return pizzaNotPickedUp;
    }

    public void setPizzaNotPickedUp(int pizzaNotPickedUp) {
        this.pizzaNotPickedUp = pizzaNotPickedUp;
    }

    public void addPizzaNotPickedUp(int pizza) {
        setPizzaNotPickedUp(getPizzaNotPickedUp() + pizza);
    }
}
