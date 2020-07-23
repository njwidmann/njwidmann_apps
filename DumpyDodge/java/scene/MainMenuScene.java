package com.application.nick.dumpydodge.scene;

import android.util.Log;

import com.application.nick.dumpydodge.GameValues;
import com.application.nick.dumpydodge.R;
import com.application.nick.dumpydodge.SceneManager;
import com.application.nick.dumpydodge.entity.Person;
import com.application.nick.dumpydodge.util.ParallaxLayer;

import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;

/**
 * Created by Nick on 4/5/2015.
 */
public class MainMenuScene extends BaseScene implements GameValues{


    private TiledSprite googlePlayButton, moreOptionsButton, playButton;
    private Sprite title;

    private boolean googlePlaySceneOpen, moreOptionsSceneOpen;

    private CameraScene mGooglePlayScene, mMoreOptionsScene;


    @Override
    public void createScene() {
        if(DEBUGGING)
            Log.i(TAG, "MainMenuScene.createScene()");
        mEngine.registerUpdateHandler(new FPSLogger());


        ParallaxLayer parallaxBackground = new ParallaxLayer(mCamera, false);
        parallaxBackground.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-5, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerBack.getHeight(), mResourceManager.mParallaxLayerBack, mVertexBufferObjectManager), false));
        parallaxBackground.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-10, new Sprite(0, GROUND, mResourceManager.mParallaxLayerGround, mVertexBufferObjectManager), false));
        parallaxBackground.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-10, new Sprite(0, GROUND - mResourceManager.mParallaxLayerGrass.getHeight(), mResourceManager.mParallaxLayerGrass, mVertexBufferObjectManager), false));
        parallaxBackground.setParallaxChangePerSecond(10);
        attachChild(parallaxBackground);


        title = new Sprite(0, 0, mResourceManager.mTitleTextureRegion, mVertexBufferObjectManager);
        title.setPosition((SCREEN_WIDTH - title.getWidth()) / 2f, 50);
        attachChild(title);


        if (!mResourceManager.mMusic.isPlaying()) {
            mResourceManager.mMusic.resume();
        }

        Person person = new Person((SCREEN_WIDTH - mResourceManager.mPersonTextureRegion.getWidth()) / 2, GROUND - mResourceManager.mPersonTextureRegion.getHeight(), mResourceManager.mPersonTextureRegion, mVertexBufferObjectManager);
        person.move(Direction.RIGHT);
        attachChild(person);


        final float googlePlayX = SCREEN_WIDTH / 2;
        final float googlePlayY = SCREEN_HEIGHT / 2 - mResourceManager.mPlayButtonTextureRegion.getHeight() / 4;

        googlePlayButton = new TiledSprite(googlePlayX, googlePlayY, mResourceManager.mGooglePlayButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (googlePlayButton.isVisible()) {
                    if (pSceneTouchEvent.isActionDown()) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();
                    }
                    if (pSceneTouchEvent.isActionUp()) {
                        setCurrentTileIndex(0);
                        if(!mActivity.isGooglePlayServicesAvailable()) {
                            mActivity.openGooglePlayServicesErrorDialog();
                        } else if(!mActivity.isConnectedToGooglePlay()) {
                            mActivity.signInClicked();
                        } else {
                            openGooglePlayScene(true);
                        }


                    }
                }
                return true;
            }
        };


        googlePlayButton.setCurrentTileIndex(0);
        googlePlayButton.setScale(0.75f);
        registerTouchArea(googlePlayButton);
        attachChild(googlePlayButton);

        final float moreOptionsX = googlePlayX;
        final float moreOptionsY = SCREEN_HEIGHT / 2 + mResourceManager.mHelpButtonTextureRegion.getHeight() / 2;

        moreOptionsButton = new TiledSprite(moreOptionsX, moreOptionsY, mResourceManager.mMoreOptionsButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (moreOptionsButton.isVisible()) {
                    if (pSceneTouchEvent.isActionDown()) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();
                    }
                    if (pSceneTouchEvent.isActionUp()) {
                        setCurrentTileIndex(0);

                        openMoreOptionsScene(true);


                    }
                }
                return true;
            }
        };


        moreOptionsButton.setCurrentTileIndex(0);
        moreOptionsButton.setScale(0.75f);
        registerTouchArea(moreOptionsButton);
        attachChild(moreOptionsButton);

        playButton = new TiledSprite(0, 0, mResourceManager.mPlayButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (playButton.isVisible()) {
                    if (pSceneTouchEvent.isActionDown()) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();
                    }
                    if (pSceneTouchEvent.isActionUp()) {
                        setCurrentTileIndex(0);
                        mSceneManager.setScene(SceneManager.SceneType.SCENE_GAME);

                    }
                }
                return true;
            }
        };


        playButton.setCurrentTileIndex(0);
        playButton.setX(RectangularShape.Side.RIGHT, SCREEN_WIDTH / 2);
        playButton.setY(RectangularShape.Side.CENTER, (googlePlayButton.getY(RectangularShape.Side.BOTTOM) + moreOptionsButton.getY(RectangularShape.Side.TOP)) / 2);
        registerTouchArea(playButton);
        attachChild(playButton);


        mGooglePlayScene = new CameraScene(mCamera);
        mGooglePlayScene.setBackgroundEnabled(false);

        final TiledSprite leaderboardButton = new TiledSprite(0, 0, mResourceManager.mLeaderboardButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();

                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    mActivity.openLeaderboard();
                }
                return true;
            }
        };
        leaderboardButton.setCurrentTileIndex(0);
        leaderboardButton.setScale(0.75f);
        leaderboardButton.setX(RectangularShape.Side.RIGHT, SCREEN_WIDTH / 2);
        leaderboardButton.setY(googlePlayButton.getY());
        mGooglePlayScene.registerTouchArea(leaderboardButton);
        mGooglePlayScene.attachChild(leaderboardButton);

        final TiledSprite achievementsButton = new TiledSprite(0, 0, mResourceManager.mAchievementsButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();

                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    mActivity.openAchievements();
                }
                return true;
            }
        };
        achievementsButton.setCurrentTileIndex(0);
        achievementsButton.setScale(0.75f);
        achievementsButton.setX(RectangularShape.Side.RIGHT, SCREEN_WIDTH / 2);
        achievementsButton.setY(moreOptionsButton.getY());
        mGooglePlayScene.registerTouchArea(achievementsButton);
        mGooglePlayScene.attachChild(achievementsButton);


        final TiledSprite backButton = new TiledSprite(0, 0, mResourceManager.mBackButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();

                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    openGooglePlayScene(false);
                }
                return true;
            }
        };
        backButton.setCurrentTileIndex(0);
        backButton.setScale(0.75f);
        backButton.setX(googlePlayButton.getX());
        backButton.setY(googlePlayButton.getY());
        mGooglePlayScene.registerTouchArea(backButton);
        mGooglePlayScene.attachChild(backButton);


        final TiledSprite logoutButton = new TiledSprite(0, 0, mResourceManager.mLogoutButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();

                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    mActivity.signOutClicked();
                    openGooglePlayScene(false);
                    mActivity.displayShortToast(mActivity.getString(R.string.signed_out));
                }
                return true;
            }
        };
        logoutButton.setCurrentTileIndex(0);
        logoutButton.setScale(0.75f);
        logoutButton.setX(moreOptionsButton.getX());
        logoutButton.setY(moreOptionsButton.getY());
        mGooglePlayScene.registerTouchArea(logoutButton);
        mGooglePlayScene.attachChild(logoutButton);


        mMoreOptionsScene = new CameraScene(mCamera);
        mMoreOptionsScene.setBackgroundEnabled(false);

        final TiledSprite helpButton = new TiledSprite(0, 0, mResourceManager.mHelpButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();

                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    mActivity.setShowingTutorial(true);
                    mSceneManager.setScene(SceneManager.SceneType.SCENE_GAME);
                }
                return true;
            }
        };
        helpButton.setCurrentTileIndex(0);
        helpButton.setScale(0.75f);
        helpButton.setX(RectangularShape.Side.RIGHT, SCREEN_WIDTH / 2);
        helpButton.setY(googlePlayButton.getY());
        mMoreOptionsScene.registerTouchArea(helpButton);
        mMoreOptionsScene.attachChild(helpButton);

        final TiledSprite rateButton = new TiledSprite(0, 0, mResourceManager.mRateButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();

                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    mActivity.openRate();
                }
                return true;
            }
        };
        rateButton.setCurrentTileIndex(0);
        rateButton.setScale(0.75f);
        rateButton.setX(RectangularShape.Side.RIGHT, SCREEN_WIDTH / 2);
        rateButton.setY(moreOptionsButton.getY());
        mMoreOptionsScene.registerTouchArea(rateButton);
        mMoreOptionsScene.attachChild(rateButton);


        final TiledSprite adFreeButton = new TiledSprite(0, 0, mResourceManager.mAdFreeButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();

                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    mActivity.purchaseNoAds();
                }
                return true;
            }
        };
        adFreeButton.setCurrentTileIndex(0);
        adFreeButton.setScale(0.75f);
        adFreeButton.setX(googlePlayButton.getX());
        adFreeButton.setY(googlePlayButton.getY());
        mMoreOptionsScene.registerTouchArea(adFreeButton);
        mMoreOptionsScene.attachChild(adFreeButton);


        final TiledSprite backButton2 = new TiledSprite(0, 0, mResourceManager.mBackButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();

                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    openMoreOptionsScene(false);
                }
                return true;
            }
        };
        backButton2.setCurrentTileIndex(0);
        backButton2.setScale(0.75f);
        backButton2.setX(moreOptionsButton.getX());
        backButton2.setY(moreOptionsButton.getY());
        mMoreOptionsScene.registerTouchArea(backButton2);
        mMoreOptionsScene.attachChild(backButton2);

        final TiledSprite aboutButton = new TiledSprite(0, 0, mResourceManager.mAboutButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();

                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    mActivity.alert(mActivity.getString(R.string.about));
                }
                return true;
            }
        };
        aboutButton.setCurrentTileIndex(0);
        aboutButton.setScale(0.75f);
        aboutButton.setX(RectangularShape.Side.CENTER, SCREEN_WIDTH / 2);
        aboutButton.setY(moreOptionsButton.getY() + mResourceManager.mAboutButtonTextureRegion.getHeight() * 3 / 4);
        mMoreOptionsScene.registerTouchArea(aboutButton);
        mMoreOptionsScene.attachChild(aboutButton);





    }


    @Override
    public void onBackKeyPressed() {
            if(googlePlaySceneOpen) {
                openGooglePlayScene(false);
            } else if(moreOptionsSceneOpen) {
                openMoreOptionsScene(false);
            } else {
                    mResourceManager.unloadGameResources();
                    mActivity.finish();
            }


    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_MENU;
    }

    @Override
    public void disposeScene() {
        if(DEBUGGING)
            Log.i(TAG, "MainMenuScene.disposeScene()");
        //TODO
    }

    private void openGooglePlayScene(boolean bool) {
        if(bool) {
            googlePlaySceneOpen = true;
            playButton.setVisible(false);
            googlePlayButton.setVisible(false);
            moreOptionsButton.setVisible(false);
            setChildScene(mGooglePlayScene, false, false, true);
        } else {
            googlePlaySceneOpen = false;
            playButton.setVisible(true);
            googlePlayButton.setVisible(true);
            moreOptionsButton.setVisible(true);
            clearChildScene();
        }
    }

    private void openMoreOptionsScene(boolean bool) {
        if(bool) {
            moreOptionsSceneOpen = true;
            playButton.setVisible(false);
            googlePlayButton.setVisible(false);
            moreOptionsButton.setVisible(false);
            setChildScene(mMoreOptionsScene, false, false, true);
        } else {
            moreOptionsSceneOpen = false;
            playButton.setVisible(true);
            googlePlayButton.setVisible(true);
            moreOptionsButton.setVisible(true);
            clearChildScene();
        }
    }

}
