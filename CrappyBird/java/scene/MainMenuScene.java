package com.application.nick.crappybird.scene;

import com.application.nick.crappybird.R;
import com.application.nick.crappybird.SceneManager;
import com.application.nick.crappybird.entity.Bird;
import com.application.nick.crappybird.entity.crap.Crap;
import com.application.nick.crappybird.entity.crap.CrapPool;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 4/5/2015.
 */
public class MainMenuScene extends BaseScene {


    private Bird mBird;
    private CrapPool mCrapPool;
    private List<Crap> mCraps = new ArrayList<Crap>();

    private TiledSprite playButton, myBirdsButton, moreOptionsButton;
    private Sprite title;

    private boolean playTransition = false, tutorialSceneOpen = false, googlePlaySceneOpen = false, moreOptionsSceneOpen = false;

    private final int MAX_CRAPS = 10;

    private AutoParallaxBackground autoParallaxBackground;
    private ParallaxBackground.ParallaxEntity parallaxLayerBack;
    private ParallaxBackground.ParallaxEntity parallaxLayerMiddle;
    private ParallaxBackground.ParallaxEntity parallaxLayerFront;

    private CameraScene mTutorialScene, mMoreOptionsScene, mGooglePlayScene;


    @Override
    public void createScene() {

        mEngine.registerUpdateHandler(new FPSLogger());

        AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 10);
        parallaxLayerBack = new ParallaxBackground.ParallaxEntity(0, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerBack.getHeight(), mResourceManager.mParallaxLayerBack, mVertexBufferObjectManager));
        parallaxLayerMiddle = new ParallaxBackground.ParallaxEntity(0, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerFront.getHeight() - mResourceManager.mParallaxLayerMiddle.getHeight(), mResourceManager.mParallaxLayerMiddle, mVertexBufferObjectManager));
        parallaxLayerFront = new ParallaxBackground.ParallaxEntity(0, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerFront.getHeight(), mResourceManager.mParallaxLayerFront, mVertexBufferObjectManager));

        autoParallaxBackground.attachParallaxEntity(parallaxLayerBack);
        autoParallaxBackground.attachParallaxEntity(parallaxLayerFront);
        autoParallaxBackground.attachParallaxEntity(parallaxLayerMiddle);

        setBackground(autoParallaxBackground);

        title = new Sprite(0, 0, mResourceManager.mTitleTextureRegion, mVertexBufferObjectManager);
        title.setPosition((SCREEN_WIDTH - title.getWidth()) / 2f, 50);
        attachChild(title);


        final float birdX = (SCREEN_WIDTH - mResourceManager.mBirdTextureRegion.getWidth()) / 2;
        final float birdY = title.getY() + title.getHeight() + 25;
        mBird = new Bird(birdX, birdY, mResourceManager.mBirdsTextureRegion, mVertexBufferObjectManager);
        mBird.setRotation(-15);
        mBird.animate(mActivity.getSelectedBird());
        attachChild(mBird);

        mCrapPool = new CrapPool(mResourceManager.mCrapTextureRegion, mVertexBufferObjectManager);
        mCrapPool.batchAllocatePoolItems(MAX_CRAPS);


        if (!mResourceManager.mMusic.isPlaying()) {
            mResourceManager.mMusic.resume();
        }

        final float birdsButtonX = SCREEN_WIDTH / 2;
        final float birdsButtonY = SCREEN_HEIGHT / 2 - mResourceManager.mPlayButtonTextureRegion.getHeight() / 4;

        myBirdsButton = new TiledSprite(birdsButtonX, birdsButtonY, mResourceManager.mBirdsButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (myBirdsButton.isVisible()) {
                    if (pSceneTouchEvent.isActionDown()) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();
                    }
                    if (pSceneTouchEvent.isActionUp()) {
                        setCurrentTileIndex(0);

                        mSceneManager.setScene(SceneManager.SceneType.SCENE_MARKET);

                    }
                }
                return true;
            }
        };


        myBirdsButton.setCurrentTileIndex(0);
        myBirdsButton.setScale(0.75f);
        registerTouchArea(myBirdsButton);
        attachChild(myBirdsButton);

        final float moreOptionsX = birdsButtonX;
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
        playButton.setY(RectangularShape.Side.CENTER, (myBirdsButton.getY(RectangularShape.Side.BOTTOM) + moreOptionsButton.getY(RectangularShape.Side.TOP)) / 2);
        playButton.setZIndex(100);
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
        leaderboardButton.setY(myBirdsButton.getY());
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
                    openMoreOptionsScene(true);
                }
                return true;
            }
        };
        backButton.setCurrentTileIndex(0);
        backButton.setScale(0.75f);
        backButton.setX(myBirdsButton.getX());
        backButton.setY(myBirdsButton.getY());
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
                    mActivity.displayShortToast(mActivity.getString(R.string.signed_out));
                    mActivity.signOutClicked();
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
                    openTutorialScene(true);
                }
                return true;
            }
        };
        helpButton.setCurrentTileIndex(0);
        helpButton.setScale(0.75f);
        helpButton.setX(RectangularShape.Side.RIGHT, SCREEN_WIDTH / 2);
        helpButton.setY(myBirdsButton.getY());
        mMoreOptionsScene.registerTouchArea(helpButton);
        mMoreOptionsScene.attachChild(helpButton);


        final TiledSprite googlePlayButton = new TiledSprite(0, 0, mResourceManager.mGooglePlayButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
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
                            openMoreOptionsScene(false);
                            openGooglePlayScene(true);
                        }
                }
                return true;
            }
        };
        googlePlayButton.setCurrentTileIndex(0);
        googlePlayButton.setScale(0.75f);
        googlePlayButton.setX(RectangularShape.Side.LEFT, SCREEN_WIDTH / 2);
        googlePlayButton.setY(myBirdsButton.getY());
        mMoreOptionsScene.registerTouchArea(googlePlayButton);
        mMoreOptionsScene.attachChild(googlePlayButton);


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
        adFreeButton.setX(RectangularShape.Side.RIGHT, SCREEN_WIDTH / 2);
        adFreeButton.setY(moreOptionsButton.getY());
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

        final Rectangle ground = new Rectangle(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerFront.getHeight(), SCREEN_WIDTH, mResourceManager.mParallaxLayerFront.getHeight(), mVertexBufferObjectManager);
        ground.setColor(Color.TRANSPARENT);

        /* The actual collision-checking. */
        registerUpdateHandler(new IUpdateHandler() {

            @Override
            public void reset() {
            }

            @Override
            public void onUpdate(float pSecondsElapsed) {

                checkBirdPosition(SCREEN_HEIGHT / 2 - mBird.getHeight() * 2);

                if (mCraps.size() > 0) {
                    checkForCrapGroundContact(ground);
                }

                //rotate bird with changing velocity
                if (mBird.getY() + mBird.getHeight() < ground.getY()) {
                    mBird.setRotation((mBird.getVelocityY() / 30) * 2 - 10);
                }

                if (mBird.getX() > SCREEN_WIDTH && playTransition) {
                    playTransition = false;
                    mSceneManager.setScene(SceneManager.SceneType.SCENE_GAME);
                }


            }
        });


        mTutorialScene = new CameraScene(mCamera);
        mTutorialScene.setBackgroundEnabled(false);

        final float boardX = (SCREEN_WIDTH - mResourceManager.mTutorialBoardTextureRegion.getWidth()) / 2;
        final float boardY = boardX;

        final float tutorialX = boardX;
        final float tutorialY = boardY;

        final float nextX = boardX + mResourceManager.mTutorialBoardTextureRegion.getWidth() - mResourceManager.mNextButtonTextureRegion.getWidth();
        final float nextY = boardY + mResourceManager.mTutorialBoardTextureRegion.getHeight() - mResourceManager.mNextButtonTextureRegion.getHeight();

        final float closeX = boardX;
        final float closeY = nextY;

        final Sprite tutorialBoard = new Sprite(boardX, boardY, mResourceManager.mTutorialBoardTextureRegion, mVertexBufferObjectManager);
        mTutorialScene.attachChild(tutorialBoard);

        final TiledSprite tutorial = new TiledSprite(tutorialX, tutorialY, mResourceManager.mTutorialTextureRegion, mVertexBufferObjectManager);
        mTutorialScene.attachChild(tutorial);

        final TiledSprite nextButton = new TiledSprite(nextX, nextY, mResourceManager.mNextButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();
                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    int tutorialTileIndex = tutorial.getCurrentTileIndex();
                    if (tutorialTileIndex == MAX_TUTORIAL_TILE_INDEX) {
                        tutorial.setCurrentTileIndex(0);
                        openTutorialScene(false);
                    } else {
                        tutorial.setCurrentTileIndex(tutorialTileIndex + 1);
                    }
                }
                return true;
            }
        };
        nextButton.setCurrentTileIndex(0);
        nextButton.setScale(0.75f);
        mTutorialScene.registerTouchArea(nextButton);
        mTutorialScene.attachChild(nextButton);


        final TiledSprite closeButton = new TiledSprite(closeX, closeY, mResourceManager.mCloseButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();
                }
                if (pSceneTouchEvent.isActionUp()) {
                    tutorial.setCurrentTileIndex(0);
                    openTutorialScene(false);
                }
                return true;
            }
        };
        closeButton.setCurrentTileIndex(0);
        closeButton.setScale(0.75f);
        mTutorialScene.registerTouchArea(closeButton);
        mTutorialScene.attachChild(closeButton);


        mActivity.setInitialSceneLoaded(true);

        if(mActivity.getFirstTimePlaying()) {
            openTutorialScene(true);
            mActivity.setFirstTimePlaying(false);
        }
    }

    /**
     * checks if bird is below y. if so, jumps and drops crap
     * @param y y value
     */
    private void checkBirdPosition(float y) {
        if(mBird.getY() > y) {
            mBird.setY(y - 1);
            jumpBird();
        }
    }


    private void jumpBird() {

        mBird.jump();

        dropCrap(mBird.getX(), mBird.getY());
        if(mResourceManager.mJumpSound != null) {
            mResourceManager.mJumpSound.play();
            mResourceManager.mJumpSound.setVolume(0.75f);
        }

    }



    private void dropCrap(float currentXPosition, float currentYPosition) {
        if(mCraps.size() == MAX_CRAPS) {
            detachChild(mCraps.get(0));
            mCrapPool.recyclePoolItem(mCraps.get(0));
            mCraps.remove(0);
        }

        mCraps.add(mCrapPool.obtainPoolItem());

        Crap crap = mCraps.get(mCraps.size() - 1);

        crap.setCurrentTileIndex(mActivity.getSelectedBird() * 2);

        crap.setPosition(currentXPosition, currentYPosition + (mBird.getHeight()));

        crap.setVelocityX(randomizeCrapVelocityX());

        //handle ninja bird
        if(mActivity.getSelectedBird() == 6) {
            crap.setAngularVelocity(600);
        }

        attachChild(crap);

        sortChildren();

    }

    private float randomizeCrapVelocityX() {
        int rand = (int)(Math.random()*10) - 5;
        return (float) rand;
    }

    private void checkForCrapGroundContact(IShape ground) {
        for (int i = mCraps.size() - 1; i >= 0; i--) {
            if (mCraps.get(i).getY() + mCraps.get(i).getHeight() > ground.getY()) {
                mCraps.get(i).hitsGround(true, mActivity.getSelectedBird());
            }
        }
    }


    @Override
    public void onBackKeyPressed() {
        if(tutorialSceneOpen) {
            openTutorialScene(false);
        } else if (googlePlaySceneOpen) {
            openGooglePlayScene(false);
            openMoreOptionsScene(true);
        } else if(moreOptionsSceneOpen) {
            openMoreOptionsScene(false);
        } else if (playTransition) {
            mSceneManager.setScene(SceneManager.SceneType.SCENE_MENU);
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
        //TODO
    }

    private void openGooglePlayScene(boolean bool) {
        if(bool) {
            googlePlaySceneOpen = true;
            playButton.setVisible(false);
            myBirdsButton.setVisible(false);
            moreOptionsButton.setVisible(false);
            setChildScene(mGooglePlayScene, false, false, true);
        } else {
            googlePlaySceneOpen = false;
            playButton.setVisible(true);
            myBirdsButton.setVisible(true);
            moreOptionsButton.setVisible(true);
            clearChildScene();
        }
    }

    private void openMoreOptionsScene(boolean bool) {
        if(bool) {
            moreOptionsSceneOpen = true;
            playButton.setVisible(false);
            myBirdsButton.setVisible(false);
            moreOptionsButton.setVisible(false);
            setChildScene(mMoreOptionsScene, false, false, true);
        } else {
            moreOptionsSceneOpen = false;
            playButton.setVisible(true);
            myBirdsButton.setVisible(true);
            moreOptionsButton.setVisible(true);
            clearChildScene();
        }
    }

    private void openTutorialScene(boolean bool) {
        if(bool) {
            tutorialSceneOpen = true;
            playButton.setVisible(false);
            myBirdsButton.setVisible(false);
            moreOptionsButton.setVisible(false);
            setChildScene(mTutorialScene, false, true, true);
        } else {
            tutorialSceneOpen = false;
            playButton.setVisible(true);
            myBirdsButton.setVisible(true);
            moreOptionsButton.setVisible(true);
            clearChildScene();
        }
    }

}
