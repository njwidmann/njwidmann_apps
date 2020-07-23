package com.application.nick.crappybird.scene;

import com.application.nick.crappybird.achievement.IncrementalAchievement;
import com.application.nick.crappybird.R;
import com.application.nick.crappybird.SceneManager;
import com.application.nick.crappybird.achievement.Achievement;
import com.application.nick.crappybird.achievement.CountAchievement;
import com.application.nick.crappybird.entity.Bird;
import com.application.nick.crappybird.entity.collectable.Collectable;
import com.application.nick.crappybird.entity.collectable.CollectableBurger;
import com.application.nick.crappybird.entity.collectable.CollectableHam;
import com.application.nick.crappybird.entity.collectable.CollectableMelon;
import com.application.nick.crappybird.entity.collectable.CollectableMuffin;
import com.application.nick.crappybird.entity.collectable.CollectablePool;
import com.application.nick.crappybird.entity.collectable.CollectableTaco;
import com.application.nick.crappybird.entity.crap.Crap;
import com.application.nick.crappybird.entity.crap.CrapPool;
import com.application.nick.crappybird.entity.crap.MegaCrap;
import com.application.nick.crappybird.entity.crap.MegaCrapPool;
import com.application.nick.crappybird.entity.obstacle.MotherShip;
import com.application.nick.crappybird.entity.obstacle.Obstacle;
import com.application.nick.crappybird.entity.obstacle.ObstacleBalloon;
import com.application.nick.crappybird.entity.obstacle.ObstaclePlane;
import com.application.nick.crappybird.entity.obstacle.ObstaclePoolAir;
import com.application.nick.crappybird.entity.obstacle.ObstaclePoolGround;
import com.application.nick.crappybird.entity.obstacle.ObstacleTrain;
import com.application.nick.crappybird.entity.obstacle.ObstacleTrainCar;
import com.application.nick.crappybird.entity.target.Target;
import com.application.nick.crappybird.entity.target.TargetPerson1;
import com.application.nick.crappybird.entity.target.TargetPool;
import com.application.nick.crappybird.util.NumberFormatter;
import com.application.nick.crappybird.util.ParallaxLayer;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 4/5/2015.
 */
public class GameScene extends BaseScene implements IOnSceneTouchListener{



    //private AutoParallaxBackground mAutoParallaxBackground;

    private ParallaxLayer mParallaxBackground;

    private Text mHudTextScore;
    private int score;
    private int most;


    private Bird mBird;

    private AnimatedSprite mAnimatedCrapMeter;

    private TiledSprite mCrapMeter, mAlertSign;

    private MotherShip mMotherShip;
    private ObstacleTrain mTrain;

    private List<Obstacle> mObstacles;
    private ObstaclePoolAir mObstaclePoolAir;
    private ObstaclePoolGround mObstaclePoolGround;

    private List<Collectable> mCollectables;
    private CollectablePool mCollectablePool;

    private List<Target> mTargets;
    private TargetPool mTargetPool;

    private CrapPool mCrapPool;
    private MegaCrapPool mMegaCrapPool;
    private List<Crap> mCraps = new ArrayList<Crap>();


    private boolean mGameOver, mOutOfCrap, mMachineCrapActivated = false, mMachineCrapping = false, mAnimatedCrapMeterBlinkingSlow = false, mAnimatedCrapMeterBlinkingFast = false,
            mDoublePointsActivated = false, mMotherShipIncoming = false, mMotherShipOnScreen = false, mMotherShipPassed = false, mSharingVisible = false,
            mMegaCrapActivated = false, mSlowMotionActivated = false, mHyperSpeedActivated = false,
            mInvincibilityActivated = false, mGooglePlaySceneOpen = false;

    private float obstacleGroundSpawnCounter = 0, obstacleAirSpawnCounter = 0, machineCrappingTime = 0, machineCrappingLastCrapTime, doublePointsTime = 0, doublePointsLastPointTime, megaCrappingTime = 0, slowMotionTime = 0, hyperSpeedTime = 0,
            lastHyperSpeedCrapTime = 0, invincibilityTime = 0, trainIncomingTime = 0;

    private CameraScene mGameReadyScene, mGameOverScene, mPauseScene;
    private Text scoreText, mostText;
    private TiledSprite mPlusTwo, playButton, backButton, achievementsButton, googlePlayButton,
            leaderboardButton, pauseButton, volumeButton, birdsButton;

    private Rectangle mGround;

    private float MAX_CRAP_METER_SIZE, mostX;

    private int selectedBird, collectableAllocationIndex, numPlanesOnScreen = 0;

    private GameScene mGameScene = this;


    //achievement stuff
    Achievement achTrainEncounter, achMotherShipEncounter, achTrainBreak;
    CountAchievement achScore1,achScore2, achScore3, achScore4, achScore5, achFood, achBalloonKills, achBurgers, achKills;
    IncrementalAchievement achMegaCrap, achPeopleCrappedOn, achMuffins;

    List<Achievement> unlockableAchievements;
    List<IncrementalAchievement> incrementalAchievements;



    @Override
    public void createScene() {
        mEngine.registerUpdateHandler(new FPSLogger());

        setOnSceneTouchListener(this);

        mActivity.logGameStart();

        if (mResourceManager.mMusic != null && !mResourceManager.mMusic.isPlaying()) {
            mResourceManager.mMusic.resume();
        }
        if (mResourceManager.mMariachiFast.isPlaying() || mResourceManager.mMariachiSlow.isPlaying()) {
            mResourceManager.mMariachiFast.pause();
            mResourceManager.mMariachiSlow.pause();
        }


        /*mAutoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 10);
        mAutoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-5.0f, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerBack.getHeight(), mResourceManager.mParallaxLayerBack, mVertexBufferObjectManager)));
        mAutoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-10.0f, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerFront.getHeight(), mResourceManager.mParallaxLayerFront, mVertexBufferObjectManager)));
        mAutoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-10.0f, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerFront.getHeight() - mResourceManager.mParallaxLayerMiddle.getHeight(), mResourceManager.mParallaxLayerMiddle, mVertexBufferObjectManager)));
        setBackground(mAutoParallaxBackground);
*/
        mParallaxBackground = new ParallaxLayer(mCamera, false);
        mParallaxBackground.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-5, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerBack.getHeight(), mResourceManager.mParallaxLayerBack, mVertexBufferObjectManager), false));
        mParallaxBackground.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-10, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerFront.getHeight(), mResourceManager.mParallaxLayerFront, mVertexBufferObjectManager), false));
        mParallaxBackground.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-10, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerFront.getHeight() - mResourceManager.mParallaxLayerMiddle.getHeight(), mResourceManager.mParallaxLayerMiddle, mVertexBufferObjectManager), false));
        attachChild(mParallaxBackground);

        mParallaxBackground.setParallaxChangePerSecond(10);

        //create entities
        final float birdX = (SCREEN_WIDTH - mResourceManager.mBirdTextureRegion.getWidth()) / 2 - 50;
        final float birdY = (SCREEN_HEIGHT - mResourceManager.mBirdTextureRegion.getHeight()) / 2 - 30;


        selectedBird = mActivity.getSelectedBird();


        mBird = new Bird(birdX, birdY, mResourceManager.mBirdsTextureRegion, mVertexBufferObjectManager);
        mBird.setZIndex(10);
        mBird.animate(selectedBird);
        mBird.setCurrentTileIndex(selectedBird * 3);
        attachChild(mBird);


        //create entities
        mGround = new Rectangle(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerFront.getHeight() + 10, SCREEN_WIDTH, mResourceManager.mParallaxLayerFront.getHeight(), mVertexBufferObjectManager);
        mGround.setColor(Color.TRANSPARENT);
        final Rectangle roof = new Rectangle(0, 0, SCREEN_WIDTH, 1, mVertexBufferObjectManager);
        roof.setColor(Color.TRANSPARENT);

        mObstaclePoolAir = new ObstaclePoolAir(mResourceManager, mVertexBufferObjectManager, mGround.getY());
        mObstaclePoolAir.batchAllocatePoolItems(NUM_OBSTACLES / 2);

        mObstaclePoolGround = new ObstaclePoolGround(mResourceManager, mVertexBufferObjectManager, mGround.getY());
        mObstaclePoolGround.batchAllocatePoolItems(NUM_OBSTACLES / 2);

        mObstacles = new ArrayList<Obstacle>();
        addObstacleGround();

        mCollectablePool = new CollectablePool(mResourceManager, mVertexBufferObjectManager, mGround.getY());
        mCollectablePool.batchAllocatePoolItems(NUM_COLLECTABLES[collectableAllocationIndex]);

        //add special items to the pool
        //taco is for machine crap
        CollectableTaco taco = new CollectableTaco(mResourceManager.mCollectableTacoTextureRegion, mVertexBufferObjectManager, mGround.getY(), mResourceManager.mCollectableTacoTextureRegion.getHeight());
        mCollectablePool.addAdditionalPoolItem(taco);
        //ham is double points
        CollectableHam ham = new CollectableHam(mResourceManager.mCollectableHamTextureRegion, mVertexBufferObjectManager, mGround.getY(), mResourceManager.mCollectableHamTextureRegion.getHeight());
        mCollectablePool.addAdditionalPoolItem(ham);

        //Mega melon gives mega massive craps
        CollectableMelon melon = new CollectableMelon(mResourceManager.mCollectableMelonTextureRegion, mVertexBufferObjectManager, mGround.getY(), mResourceManager.mCollectableMelonTextureRegion.getHeight());
        mCollectablePool.addAdditionalPoolItem(melon);

        //Slo-mo muffin slows down obstacles
        CollectableMuffin muffin = new CollectableMuffin(mResourceManager.mCollectableMuffinTextureRegion, mVertexBufferObjectManager, mGround.getY(), mResourceManager.mCollectableMuffinTextureRegion.getHeight());
        mCollectablePool.addAdditionalPoolItem(muffin);

        //burger is for hyper speed
        CollectableBurger burger = new CollectableBurger(mResourceManager.mCollectableBurgerTextureRegion, mVertexBufferObjectManager, mGround.getY(), mResourceManager.mCollectableBurgerTextureRegion.getHeight());
        mCollectablePool.addAdditionalPoolItem(burger);


        mCollectablePool.shufflePoolItems();

        mMotherShip = new MotherShip(mResourceManager.mObstacleMotherShipTextureRegion, mVertexBufferObjectManager);

        mTrain = new ObstacleTrain(10, mResourceManager, mVertexBufferObjectManager, mGround.getY());

        float alertX = (SCREEN_WIDTH - mResourceManager.mAlertTextureRegion.getWidth()) / 2;
        float alertY = (mMotherShip.getHeight() / 2) + 20;

        mAlertSign = new TiledSprite(alertX, alertY, mResourceManager.mAlertTextureRegion, mVertexBufferObjectManager);
        mAlertSign.setScale(2);
        mAlertSign.setVisible(false);

        mCollectables = new ArrayList<Collectable>();
        mCollectables.add(mCollectablePool.obtainPoolItem());

        mTargetPool = new TargetPool(mResourceManager, mVertexBufferObjectManager, mGround.getY());
        mTargetPool.batchAllocatePoolItems(NUM_TARGETS);

        mTargets = new ArrayList<Target>();
        mTargets.add(mTargetPool.obtainPoolItem());

        mCrapPool = new CrapPool(mResourceManager.mCrapTextureRegion, mVertexBufferObjectManager);
        mCrapPool.batchAllocatePoolItems(NUM_CRAPS);
        mMegaCrapPool = new MegaCrapPool(mResourceManager.mMegaCrapTextureRegion, mVertexBufferObjectManager);
        mMegaCrapPool.batchAllocatePoolItems(NUM_CRAPS);
        mOutOfCrap = false;

        attachChild(mGround);
        attachChild(roof);
        attachChild(mCollectables.get(mCollectables.size() - 1));
        attachChild(mTargets.get(mTargets.size() - 1));
        attachChild(mAlertSign);

        initializeAchievments();


        /* The actual collision-checking. */
        registerUpdateHandler(new IUpdateHandler() {

            @Override
            public void reset() {
            }

            @Override
            public void onUpdate(float pSecondsElapsed) {

                checkForBirdGroundContact();

                checkObstaclePosition(); //handle obstacles leaving screen and update score if bird passes them

                checkCollectablePosition();

                checkTargetPosition(); //check targets leaving screen and also handle falling people from balloons

                if (mCraps.size() > 0) {
                    checkCrapPosition(mGround);
                }

                addNewTarget(SCREEN_WIDTH / 2); //add a new target if the earliest added on the screen passes x

                if (mCollectables.size() <= MAX_COLLECTABLES_ON_SCREEN) {
                    addNewCollectable(300); //add a new collectable if the earliest added on the screen passes x
                }

                if (!mGameOver) {
                    if (mSlowMotionActivated) {
                        obstacleAirSpawnCounter += pSecondsElapsed / 2;
                        obstacleGroundSpawnCounter += pSecondsElapsed / 2;
                    } else if (mHyperSpeedActivated) {
                        obstacleAirSpawnCounter += pSecondsElapsed * 3;
                        obstacleGroundSpawnCounter += pSecondsElapsed * 3;
                    } else {
                        obstacleAirSpawnCounter += pSecondsElapsed;
                        obstacleGroundSpawnCounter += pSecondsElapsed;
                    }

                    double groundDistance = (SCREEN_WIDTH - mResourceManager.mObstacleHouseTextureRegion.getWidth()) / Math.pow(getObstacleIndex(), 1.0 / 6) + mResourceManager.mObstacleHouseTextureRegion.getWidth();
                    float groundTime = (float) groundDistance / SCROLL_SPEED;

                    double airDistance = (SCREEN_WIDTH - mResourceManager.mObstaclePlanesTextureRegion.getWidth()) / Math.pow(getObstacleIndex(), 1.0 / 6) + mResourceManager.mObstaclePlanesTextureRegion.getWidth();
                    float airTime = (float) airDistance / getPlaneSpeed();

                    if (!mMotherShipOnScreen && !mMotherShipIncoming) {
                        for (int i = GameScene.MAX_OBSTACLES_ON_SCREEN.length - 1; i >= 0; i--) {
                            if (getObstacleIndex() >= GameScene.MAX_OBSTACLES_ON_SCREEN_OBSTACLE_INDEX[i]) {
                                if (mObstacles.size() < MAX_OBSTACLES_ON_SCREEN[i]) {
                                    int rand = (int) (Math.random() * 2); //here random allows air and ground obstacles to have an equal chance of spawning
                                    if (rand == 0) {
                                        if (obstacleAirSpawnCounter > airTime) {
                                            addObstacleAir();
                                        } else if (obstacleGroundSpawnCounter > groundTime) {
                                            addObstacleGround();
                                        }
                                    } else {
                                        if (obstacleGroundSpawnCounter > groundTime) {
                                            addObstacleGround();
                                        } else if (obstacleAirSpawnCounter > airTime) {
                                            addObstacleAir();
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }


                //Handle mother ship////////////////////////////////
                if (getObstacleIndex() >= MOTHERSHIP_OBSTACLE_INDEX && !mMotherShipIncoming && !mMotherShipOnScreen && !mMotherShipPassed) {
                    setMotherShipIncoming(true);
                }
                if (mMotherShipIncoming && mObstacles.size() == 0) {
                    setMotherShipOnScreen(true);
                }
                if (mMotherShipOnScreen) {
                    checkMotherShipPosition();
                    checkForMotherShipContact();
                }

                //handle train////////////////////////////////
                if (getObstacleIndex() >= TRAIN_OBSTACLE_INDEX && !mTrain.getOnScreen() && !mTrain.getIncoming()) {
                    setTrainIncoming(true);

                }
                if (mTrain.getIncoming() && !mGameOver) {
                    if (mSlowMotionActivated) {
                        trainIncomingTime += pSecondsElapsed / 2;
                        mResourceManager.mTrainSound.setRate(1 / 2f);
                        mResourceManager.mTrainSound.resume();

                    } else if (mHyperSpeedActivated) {
                        trainIncomingTime += pSecondsElapsed * 3;
                        mResourceManager.mTrainSound.setRate(1 / 2f);
                        mResourceManager.mTrainSound.resume();
                    } else {
                        trainIncomingTime += pSecondsElapsed;
                        mResourceManager.mTrainSound.setRate(1f);
                        mResourceManager.mTrainSound.resume();
                    }
                    if (trainIncomingTime > TRAIN_INCOMING_TIME) {
                        setTrainIncoming(false);
                        mTrain.attachToScene(mGameScene);
                        if (mSlowMotionActivated) {
                            mTrain.setSlowMotionActivated(true);
                        } else if (mHyperSpeedActivated) {
                            mTrain.setHyperSpeedActivated(true);
                        }

                        //handle Achievement
                        achTrainEncounter.accomplish();
                    }
                }

                if (mTrain.getOnScreen()) {
                    mTrain.updateTrain();
                    checkForTrainCarBirdContact();

                    if (mSlowMotionActivated) {
                        mResourceManager.mTrainSound.setRate(1 / 2f);
                        mResourceManager.mTrainSound.resume();
                    } else if (mHyperSpeedActivated) {
                        mResourceManager.mTrainSound.setRate(1 / 2f);
                        mResourceManager.mTrainSound.resume();
                    } else {
                        mResourceManager.mTrainSound.setRate(1f);
                        mResourceManager.mTrainSound.resume();
                    }
                }


                if (!mGameOver) { //only advance powerup times if gameover is true. This way, you will still have them after a respawn

                    //Handle machine crap powerup/////////////////////////////////
                    if (mMachineCrapActivated) {
                        if (mMachineCrapping) {
                            if (machineCrappingTime > machineCrappingLastCrapTime + MACHINE_CRAPPING_TIME_BETWEEN_CRAPS) {
                                jumpBird();
                                machineCrappingLastCrapTime = machineCrappingTime;
                            }
                        }

                        machineCrappingTime += pSecondsElapsed;
                        if (machineCrappingTime > MAX_POWER_UP_TIME) {
                            setMachineCrap(false);
                            machineCrappingTime = 0;
                        }

                    }

                    //Handle hyper speed powerup/////////////////////////////////
                    if (mHyperSpeedActivated) {
                        if (hyperSpeedTime > lastHyperSpeedCrapTime + HYPER_SPEED_TIME_BETWEEN_CRAPS) {
                            dropHyperSpeedCrap(mBird.getX(), mBird.getY());
                            lastHyperSpeedCrapTime = hyperSpeedTime;
                        }

                        hyperSpeedTime += pSecondsElapsed;
                        if (hyperSpeedTime > MAX_POWER_UP_TIME) {
                            setHyperSpeed(false);
                            hyperSpeedTime = 0;
                        }

                    }

                    if (mInvincibilityActivated) {
                        invincibilityTime += pSecondsElapsed;
                        if (invincibilityTime > MAX_INVINCIBILITY_TIME) {
                            setInvincibility(false);
                        } else if (invincibilityTime > MAX_INVINCIBILITY_TIME - 0.5f && !mAnimatedCrapMeterBlinkingFast) {
                            mAnimatedCrapMeter.animate(new long[]{100, 100}, 5, 6, true);
                            mAnimatedCrapMeterBlinkingFast = true;
                            mAnimatedCrapMeterBlinkingSlow = false;
                        }
                    }

                    //Handle mega crap powerup/////////////////////////////////
                    if (mMegaCrapActivated) {
                        megaCrappingTime += pSecondsElapsed;
                        if (megaCrappingTime > MAX_POWER_UP_TIME) {
                            setMegaCrap(false);
                            megaCrappingTime = 0;
                        }
                    }

                    //Handle slow motion powerup/////////////////////////////////
                    if (mSlowMotionActivated) {
                        slowMotionTime += pSecondsElapsed;
                        if (slowMotionTime > MAX_POWER_UP_TIME) {
                            setSlowMotion(false);
                            slowMotionTime = 0;
                        }
                    }

                    //Handle double points powerup//////////////////////////////////
                    if (mDoublePointsActivated) {
                        if (mPlusTwo.isVisible()) { //flash "+2" next to score for every point scored while double points is activated
                            if (doublePointsTime > doublePointsLastPointTime + DOUBLE_POINTS_TIME_TO_SHOW_PLUS_TWO) {
                                mPlusTwo.setVisible(false);
                            }
                        }

                        doublePointsTime += pSecondsElapsed;
                        if (doublePointsTime > MAX_POWER_UP_TIME) {
                            setDoublePoints(false);
                            mPlusTwo.setVisible(false);
                        }

                    }
                }

                //don't let bird leave top of screen
                if (mBird.getY() < 0) {
                    mBird.setVelocityY(0);
                }


                //rotate bird with changing velocity
                if (mBird.getY() + mBird.getHeight() < mGround.getY()) {
                    mBird.setRotation(mBird.getVelocityY() / 15 - 10);
                }

                handleInGameAchievements();

            }
        });


        final float labelX = (SCREEN_WIDTH - mResourceManager.mHelpTextureRegion.getWidth()) / 2;
        final float labelY = 100;

        //create CameraScene for get ready
        final float readyX = (SCREEN_WIDTH - mResourceManager.mStateTextureRegion.getWidth()) / 2;
        final float readyY = labelY - mResourceManager.mStateTextureRegion.getHeight();

        mGameReadyScene = new CameraScene(mCamera);
        //"Get Ready" picture
        final TiledSprite getReadySprite = new TiledSprite(readyX, readyY, mResourceManager.mStateTextureRegion, mVertexBufferObjectManager);
        getReadySprite.setCurrentTileIndex(0);
        mGameReadyScene.attachChild(getReadySprite);
        //how to picture
        final Sprite tapHelpSprite = new Sprite(labelX, labelY, mResourceManager.mHelpTextureRegion, mVertexBufferObjectManager);
        mGameReadyScene.attachChild(tapHelpSprite);

        mGameReadyScene.setBackgroundEnabled(false);

        mGameReadyScene.setOnSceneTouchListener(new IOnSceneTouchListener() {

            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                if (pSceneTouchEvent.isActionUp()) {
                    clearChildScene();
                    mHudTextScore.setVisible(true);
                    mCrapMeter.setVisible(true);
                    pauseButton.setVisible(true);

                    if (numPlanesOnScreen > 0) {
                        mResourceManager.mPropellerSound.play();
                    }

                }
                return true;
            }
        });

        setChildScene(mGameReadyScene, false, true, true);
        if (numPlanesOnScreen > 0) {
            mResourceManager.mPropellerSound.stop();
        }


        //create CameraScene for game over
        final float overX = (SCREEN_WIDTH - mResourceManager.mScoreBoardTextureRegion.getWidth()) / 2;
        final float overY = labelY + mResourceManager.mStateTextureRegion.getHeight();

        final float scoreX = overX + 55;
        final float scoreY = overY + 40;

        mostX = overX + 165;
        final float mostY = scoreY;

        mGameOverScene = new CameraScene(mCamera);

        final TiledSprite gameOverTitle = new TiledSprite(0,0, mResourceManager.mStateTextureRegion, mVertexBufferObjectManager);
        gameOverTitle.setCurrentTileIndex(1);
        gameOverTitle.setX(RectangularShape.Side.CENTER, SCREEN_WIDTH / 2);
        gameOverTitle.setY(RectangularShape.Side.BOTTOM, overY);
        mGameOverScene.attachChild(gameOverTitle);

        final Sprite boardSprite = new Sprite(overX, overY, mResourceManager.mScoreBoardTextureRegion, mVertexBufferObjectManager);
        mGameOverScene.attachChild(boardSprite);

        scoreText = new Text(scoreX, scoreY, mResourceManager.mFont4, "0123456789", new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
        scoreText.setText("0");
        mGameOverScene.attachChild(scoreText);

        mostText = new Text(mostX, mostY, mResourceManager.mFont4, "0123456789", new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
        mostText.setText(String.valueOf(most));
        mGameOverScene.attachChild(mostText);


        birdsButton = new TiledSprite(0, 0, mResourceManager.mBirdsButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();
                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    mSceneManager.setScene(SceneManager.SceneType.SCENE_MARKET);
                    if (mResourceManager.mMariachiFast.isPlaying()) {
                        mResourceManager.mMariachiFast.pause();
                    }
                    if (mResourceManager.mMariachiSlow.isPlaying()) {
                        mResourceManager.mMariachiSlow.pause();
                    }
                }
                return true;
            }
        };
        birdsButton.setCurrentTileIndex(0);
        birdsButton.setScale(0.75f);
        birdsButton.setX(RectangularShape.Side.CENTER, SCREEN_WIDTH / 2);
        birdsButton.setY(RectangularShape.Side.TOP, boardSprite.getY(RectangularShape.Side.BOTTOM));
        mGameOverScene.registerTouchArea(birdsButton);
        mGameOverScene.attachChild(birdsButton);


        playButton = new TiledSprite(0, 0, mResourceManager.mPlayButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();

                }
                if (pSceneTouchEvent.isActionUp()) {
                    clearChildScene(); //start new game
                    mSceneManager.setScene(SceneManager.SceneType.SCENE_GAME);
                }
                return true;
            }
        };
        playButton.setCurrentTileIndex(0);
        playButton.setX(RectangularShape.Side.CENTER, SCREEN_WIDTH / 2);
        playButton.setY(RectangularShape.Side.TOP, birdsButton.getY(RectangularShape.Side.BOTTOM));
        mGameOverScene.registerTouchArea(playButton);
        mGameOverScene.attachChild(playButton);


        googlePlayButton = new TiledSprite(0, 0, mResourceManager.mGooglePlayButtonTextureRegion, mVertexBufferObjectManager) {

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
                        openGooglePlayButtons(true);
                    }
                }
                return true;
            }
        };
        googlePlayButton.setCurrentTileIndex(0);
        googlePlayButton.setScale(0.5f);
        googlePlayButton.setX(RectangularShape.Side.CENTER, SCREEN_WIDTH / 2);
        googlePlayButton.setY(RectangularShape.Side.TOP, boardSprite.getY(RectangularShape.Side.CENTER));
        mGameOverScene.registerTouchArea(googlePlayButton);
        mGameOverScene.attachChild(googlePlayButton);


        backButton = new TiledSprite(googlePlayButton.getX(), googlePlayButton.getY(), mResourceManager.mBackButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(isVisible()) {
                    if (pSceneTouchEvent.isActionDown()) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();

                    }
                    if (pSceneTouchEvent.isActionUp()) {
                        setCurrentTileIndex(0);
                        openGooglePlayButtons(false);
                    }
                }
                return true;
            }
        };
        backButton.setCurrentTileIndex(0);
        backButton.setScale(0.5f);
        backButton.setVisible(false);
        mGameOverScene.attachChild(backButton);


        leaderboardButton = new TiledSprite(0, 0, mResourceManager.mLeaderboardButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(isVisible()) {
                    if (pSceneTouchEvent.isActionDown()) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();

                    }
                    if (pSceneTouchEvent.isActionUp()) {
                        setCurrentTileIndex(0);
                        mActivity.openLeaderboard();
                    }
                }
                return true;
            }
        };
        leaderboardButton.setCurrentTileIndex(0);
        leaderboardButton.setScale(0.75f);
        leaderboardButton.setX(RectangularShape.Side.LEFT, SCREEN_WIDTH / 2);
        leaderboardButton.setY(playButton.getY());
        leaderboardButton.setVisible(false);
        mGameOverScene.attachChild(leaderboardButton);

        achievementsButton = new TiledSprite(0, 0, mResourceManager.mAchievementsButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(isVisible()) {
                    if (pSceneTouchEvent.isActionDown()) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();

                    }
                    if (pSceneTouchEvent.isActionUp()) {
                        setCurrentTileIndex(0);
                        mActivity.openAchievements();
                    }
                }
                return true;
            }
        };
        achievementsButton.setCurrentTileIndex(0);
        achievementsButton.setScale(0.75f);
        achievementsButton.setX(RectangularShape.Side.RIGHT, SCREEN_WIDTH / 2);
        achievementsButton.setY(playButton.getY());
        achievementsButton.setVisible(false);
        mGameOverScene.attachChild(achievementsButton);



        mGameOverScene.setBackgroundEnabled(false);

        most = mActivity.getMaxScore();

        //create HUD for score
        HUD gameHUD = new HUD();
        // CREATE SCORE TEXT
        mHudTextScore = new Text(SCREEN_WIDTH / 2, 50, mResourceManager.mFont5, "0123456789", new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
        mHudTextScore.setText("0");
        mHudTextScore.setX((SCREEN_WIDTH - mHudTextScore.getWidth()) / 2);
        mHudTextScore.setVisible(false);
        gameHUD.attachChild(mHudTextScore);

        //"+2" popup for double points
        mPlusTwo = new TiledSprite((SCREEN_WIDTH / 2 + mHudTextScore.getWidth() * 2), 50, mResourceManager.mPlusTwoTextureRegion, mVertexBufferObjectManager);
        mPlusTwo.setVisible(false);
        gameHUD.attachChild(mPlusTwo);

        mCrapMeter = new TiledSprite((SCREEN_WIDTH - mResourceManager.mMeter2TextureRegion.getWidth()) / 2, mResourceManager.mMeter2TextureRegion.getHeight(), mResourceManager.mMeter2TextureRegion, mVertexBufferObjectManager);
        mCrapMeter.setCurrentTileIndex(6);
        mCrapMeter.setScale(0.75f);
        mCrapMeter.setX((SCREEN_WIDTH - mCrapMeter.getWidth()) / 2);
        MAX_CRAP_METER_SIZE = mCrapMeter.getWidth();
        mCrapMeter.setVisible(false);
        gameHUD.attachChild(mCrapMeter);


        //for machine crap power up (thunder taco)
        mAnimatedCrapMeter = new AnimatedSprite((SCREEN_WIDTH - mResourceManager.mMeter2TextureRegion.getWidth()) / 2, mResourceManager.mMeter2TextureRegion.getHeight(), mResourceManager.mMeter2TextureRegion, mVertexBufferObjectManager);
        mAnimatedCrapMeter.setScale(0.75f);
        mAnimatedCrapMeter.setX((SCREEN_WIDTH - mAnimatedCrapMeter.getWidth()) / 2);
        mAnimatedCrapMeter.setVisible(false);
        gameHUD.attachChild(mAnimatedCrapMeter);

        mCamera.setHUD(gameHUD);


        mPauseScene = new CameraScene(mCamera);

        final float playOnPauseX = (SCREEN_WIDTH - mResourceManager.mPlayButtonTextureRegion.getWidth()) / 2;
        final float playOnPauseY = SCREEN_HEIGHT / 2 - mResourceManager.mPlayButtonTextureRegion.getHeight();

        final float restartOnPauseX = playOnPauseX;
        final float restartOnPauseY = SCREEN_HEIGHT / 2;

        final TiledSprite playButtonOnPause = new TiledSprite(playOnPauseX, playOnPauseY, mResourceManager.mPlayButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();
                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    setPause(false);
                }
                return true;
            }
        };
        playButtonOnPause.setCurrentTileIndex(0);
        playButtonOnPause.setScale(0.75f);
        mPauseScene.registerTouchArea(playButtonOnPause);
        mPauseScene.attachChild(playButtonOnPause);

        final TiledSprite restartButtonOnPause = new TiledSprite(restartOnPauseX, restartOnPauseY, mResourceManager.mRestartButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();
                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);
                    mSceneManager.setScene(SceneManager.SceneType.SCENE_GAME);

                }
                return true;
            }
        };
        restartButtonOnPause.setCurrentTileIndex(0);
        restartButtonOnPause.setScale(0.75f);
        mPauseScene.registerTouchArea(restartButtonOnPause);
        mPauseScene.attachChild(restartButtonOnPause);


        mPauseScene.setBackgroundEnabled(false);


        pauseButton = new TiledSprite(0, 0, mResourceManager.mPauseButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    if (isVisible()) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();
                    }
                }
                if (pSceneTouchEvent.isActionUp()) {
                    if (isVisible()) {
                        setCurrentTileIndex(0);
                        setPause(true);
                    }
                }
                return true;
            }
        };
        pauseButton.setCurrentTileIndex(0);
        pauseButton.setScale(0.25f);
        pauseButton.setX(mCrapMeter.getX() + (mCrapMeter.getWidth() - mCrapMeter.getWidthScaled()) / 2 + mCrapMeter.getWidthScaled() - (pauseButton.getWidth() - pauseButton.getWidthScaled()) / 2);
        pauseButton.setY(mCrapMeter.getY() + (mCrapMeter.getHeight() - mCrapMeter.getHeightScaled()) / 2 - (pauseButton.getHeight() - pauseButton.getHeightScaled()) / 2 - (pauseButton.getHeightScaled() - mCrapMeter.getHeightScaled()) / 2);
        registerTouchArea(pauseButton);
        attachChild(pauseButton);
        pauseButton.setVisible(false);

        volumeButton = new TiledSprite(0, 0, mResourceManager.mVolumeButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (isVisible() && pSceneTouchEvent.isActionUp()) {
                    if (getCurrentTileIndex() == 0) {
                        setCurrentTileIndex(1);
                    } else {
                        setCurrentTileIndex(0);
                    }
                    mActivity.toggleSound();
                }
                return true;
            }
        };
        if (mActivity.getSoundOn()) {
            volumeButton.setCurrentTileIndex(0);
        } else {
            volumeButton.setCurrentTileIndex(1);
        }
        volumeButton.setScale(0.25f);
        volumeButton.setX(mCrapMeter.getX() + (mCrapMeter.getWidth() - mCrapMeter.getWidthScaled()) / 2 - (volumeButton.getWidth() - volumeButton.getWidthScaled()) / 2 - volumeButton.getWidthScaled());
        volumeButton.setY(mCrapMeter.getY() + (mCrapMeter.getHeight() - mCrapMeter.getHeightScaled()) / 2 - (volumeButton.getHeight() - volumeButton.getHeightScaled()) / 2 - (volumeButton.getHeightScaled() - mCrapMeter.getHeightScaled()) / 2);
        registerTouchArea(volumeButton);
        mPauseScene.registerTouchArea(volumeButton);
        mGameOverScene.registerTouchArea(volumeButton);
        mGameReadyScene.registerTouchArea(volumeButton);
        attachChild(volumeButton);


    }






    public void setPause(boolean bool) {
        if (bool) {
            if(pauseButton.isVisible()) {
                pauseButton.setVisible(false);
                setIgnoreUpdate(true);
                setChildScene(mPauseScene, false, true, true);

                mMachineCrapping = false;

                mResourceManager.mMusic.pause();
                mResourceManager.mMariachiFast.pause();
                mResourceManager.mMariachiSlow.pause();

                if(numPlanesOnScreen > 0) {
                    mResourceManager.mPropellerSound.pause();
                }
                if(mMotherShipOnScreen) {
                    mResourceManager.mMotherShipSound.pause();
                }
                if(mTrain.getIncoming() || mTrain.getOnScreen()) {
                    mResourceManager.mTrainSound.pause();
                }

            }
        } else {
            clearChildScene();
            setIgnoreUpdate(false);
            pauseButton.setVisible(true);

            if (mMachineCrapActivated) {
                if(mSlowMotionActivated) {
                    mResourceManager.mMariachiSlow.resume();
                } else {
                    mResourceManager.mMariachiFast.resume();
                }
            } else {
                mResourceManager.mMusic.resume();
            }

            if(numPlanesOnScreen > 0) {
                mResourceManager.mPropellerSound.resume();
            }
            if(mMotherShipOnScreen) {
                mResourceManager.mMotherShipSound.resume();
            }
            if(mTrain.getIncoming() || mTrain.getOnScreen()) {
                mResourceManager.mTrainSound.resume();
            }
        }
    }

    /**
     * This method checks for targets leaving the screen... recycles if so
     * Also checks if a person who was falling has hit the ground
     */
    private void checkTargetPosition() {
        for(int i = mTargets.size() - 1; i >= 0; i--) {
            Target target = mTargets.get(i);
            if (target.getX() <  -target.getWidth() || (target.getX() > SCREEN_WIDTH && mGameOver)) {
                detachChild(target);
                mTargetPool.recyclePoolItem(target);
                mTargetPool.shufflePoolItems();
                mTargets.remove(i);
            } else if (target.getTargetType() == Target.targetType.PERSON1) {
                if(((TargetPerson1)target).getFalling() && (target.getY() + target.getHeight()) >= mGround.getY()) {
                    ((TargetPerson1)target).hitsGround(mGameOver);
                }
            }
        }
    }

    /**
     * used to check if the mothership has left the screen wMotherShipOnScreen = true
     * Also adds score if leaves screen and not game over
     */
    private void checkMotherShipPosition() {
            if(mMotherShip.getX() < -mMotherShip.getWidth()) {
                setMotherShipOnScreen(false);
                if(!mGameOver) {
                    addPoint();
                }

            }

    }

    /**
     * this method checks for mothership contact with the bird and targets.
     * if contact with bird, gameover (unless machine crapping)
     */
    private void checkForMotherShipContact() {
        if (!mGameOver && mMotherShip.collidesWith(mBird) && !mMotherShip.getCollidedWith()) {
            if (mMachineCrapActivated || mHyperSpeedActivated || mInvincibilityActivated) {

                float birdYVelocity = mBird.getVelocityY();
                float velocityY = birdYVelocity * 2;
                if (velocityY != 0 && Math.abs(velocityY) < 500) {
                    velocityY = (velocityY / Math.abs(velocityY)) * 500;
                }
                mMotherShip.blastOff(velocityY);
                mMotherShip.setCollidedWith();
            } else {
                setGameOver();
            }
        }
    }

    private void setGameOver() {

            setGameOver(true);

    }

    private void setGameOver(boolean bool) {
        if(bool) {
            mGameOver = true;
            mResourceManager.mHitSound.play();
            mBird.stopAnimation(selectedBird * 3);
            mMachineCrapping = false; //stop current hold session
            stopCrap();

            if(mInvincibilityActivated) {
                setInvincibility(false);
            }

            killObstacles();
            killCollectables();
            killTargets();

            if(mTrain.getIncoming()) {
                mResourceManager.mTrainSound.pause();
            }
            if(mTrain.getOnScreen()) {
                mTrain.setGameOver(true);
                mResourceManager.mTrainSound.setRate(1);
            }

            mParallaxBackground.setParallaxChangePerSecond(0);

        } else { //for respawn (deprecated)
            mGameOver = false;

            if(mTrain.getIncoming()) {
                mResourceManager.mTrainSound.resume();
            }
            if(mTrain.getOnScreen()) {
                mTrain.setGameOver(false);
            }

            mBird.animate(selectedBird);

            addObstacleGround();
            mTargets.add(mTargetPool.obtainPoolItem());
            attachChild(mTargets.get(0));
            mCollectables.add(mCollectablePool.obtainPoolItem());
            attachChild(mCollectables.get(0));



            if(mSlowMotionActivated) {
                mParallaxBackground.setParallaxChangePerSecond(PARALLAX_CHANGE_PER_SECOND / 2);
                mTargets.get(0).setSlowMotionActivated(true);
            } else {
                mParallaxBackground.setParallaxChangePerSecond(PARALLAX_CHANGE_PER_SECOND);
            }
        }
    }


    /**
     * used to set mother ship incoming. makes alert sign pop up
     * @param bool
     */
    private void setMotherShipIncoming(boolean bool) {
        if(bool) {
            mMotherShipIncoming = true;
            mAlertSign.setVisible(true);
            mResourceManager.mAlertSound.play();

        } else {
            mMotherShipIncoming = false;
            mAlertSign.setVisible(false);
        }
    }

    private void setTrainIncoming(boolean bool) {
        if(bool) {
            mTrain.setIncoming(true);
            mAlertSign.setVisible(true);
            mAlertSign.setY(mGround.getY() - mAlertSign.getY());
            mResourceManager.mAlertSound.play();
            mResourceManager.mTrainSound.play();
        } else {
            mTrain.setIncoming(false);
            mAlertSign.setVisible(false);
        }
    }

    /**
     * This method is used to make the mother ship fly across the screen
     * @param bool if true: starts flight. if false: ends flight
     */
    private void setMotherShipOnScreen(boolean bool) {
        if (bool) {
            setMotherShipIncoming(false);
            mMotherShipOnScreen = true;
            mMotherShip.flyingPastScreen(true);
            attachChild(mMotherShip);
            mResourceManager.mMotherShipSound.play();
            if(mSlowMotionActivated) {
                mMotherShip.setSlowMotionActivated(true);
                mResourceManager.mMotherShipSound.setRate(0.5f);
            }
            if (mHyperSpeedActivated) {
                mMotherShip.setHyperSpeedActivated(true);
            }

            //handle achievement
            achMotherShipEncounter.accomplish();

        } else {
            mMotherShipOnScreen = false;
            mMotherShipPassed = true;
            mMotherShip.flyingPastScreen(false);
            detachChild(mMotherShip);
        }
    }


    private int getObstacleIndex() {
        return mObstaclePoolAir.getObstacleIndex() + mObstaclePoolGround.getObstacleIndex();
    }


    private void addObstacleAir() {

        obstacleAirSpawnCounter = 0;

        mObstacles.add(mObstaclePoolAir.obtainPoolItem());
        Obstacle newObstacle = mObstacles.get(mObstacles.size() - 1);
        newObstacle.setX(SCREEN_WIDTH);
        //scale plane velocity according to number of passed obstacles
        if (newObstacle.getObstacleType() == Obstacle.obstacleType.PLANE) {
            mResourceManager.mPropellerSound.play();
            mResourceManager.mPropellerSound.setVolume(1f);
            newObstacle.setVelocityX(-getPlaneSpeed());

            //handle sound effect
            if (getObstacleIndex() < 50) {
                mResourceManager.mPropellerSound.setRate((float) (1 + getObstacleIndex() / 100.0));
            } else {
                mResourceManager.mPropellerSound.setRate(1.5f);
            }

            numPlanesOnScreen++;

            //if passed obstacle number x, have plane fly at random angles across the screen
            if (getObstacleIndex() > 50) {
                ((ObstaclePlane) newObstacle).randomizeYVelocity();
            }
        } else if (newObstacle.getObstacleType() == Obstacle.obstacleType.BALLOON) {
            if (getObstacleIndex() > 0) {
                ((ObstacleBalloon) newObstacle).randomizeYVelocity();
            }
        }
        if(mSlowMotionActivated) {
            newObstacle.setSlowMotionActivated(true);
        }
        if(mHyperSpeedActivated) {
            newObstacle.setHyperSpeedActivated(true);
        }
        attachChild(newObstacle);
        sortChildren();
    }

    private float getPlaneSpeed() {
        if (getObstacleIndex() < 50) {
            return 200f + getObstacleIndex();
        } else {
            return 250;
        }
    }

    private void addObstacleGround() {

        obstacleGroundSpawnCounter = 0;

        mObstacles.add(mObstaclePoolGround.obtainPoolItem());
        Obstacle newObstacle = mObstacles.get(mObstacles.size() - 1);
        newObstacle.setX(SCREEN_WIDTH);

        if(mSlowMotionActivated) {
            newObstacle.setSlowMotionActivated(true);
        }
        if(mHyperSpeedActivated) {
            newObstacle.setHyperSpeedActivated(true);
        }
        attachChild(newObstacle);
        sortChildren();
    }

    /**
     * adds a new collectable when the earliest added collectable still on the screen reaches a certain x value
     * @param x the x value to check
     */
    private void addNewCollectable(float x) {
        if(!mGameOver) {
            for(int i = mCollectables.size() - 1; i >= 0; i--) {
                Collectable collectable = mCollectables.get(i);
                    if (collectable.getX() < x && !collectable.getPassedAddXValue()) {
                        collectable.passedAddXValue();
                        mCollectables.add(mCollectablePool.obtainPoolItem());
                        attachChild(mCollectables.get(mCollectables.size() - 1));
                        sortChildren();
                    }

            }
        }
    }

    /**
     * adds a new target when the earliest added target still on the screen reaches a certain x value
     * @param x the x value to check
     */
    private void addNewTarget(float x) {
        if(!mGameOver) {
            int numTargets = mTargets.size();
            for(int i = mTargets.size() - 1; i >= 0; i--) {
                Target target = mTargets.get(i);
                if (target.getX() < x && !target.getPassedAddXValue()) {
                    target.passedAddXValue();
                    if(numTargets <= MAX_TARGETS_ON_SCREEN) {
                        mTargets.add(mTargetPool.obtainPoolItem());
                        Target newTarget = mTargets.get(mTargets.size() - 1);
                        if(newTarget.getHitValue()) {
                            newTarget.reset();
                        }
                        if(mSlowMotionActivated) {
                            newTarget.setSlowMotionActivated(true);
                        }
                        if(mHyperSpeedActivated) {
                            newTarget.setHyperSpeedActivated(true);
                        }
                        attachChild(newTarget);
                        sortChildren();
                    }
                }

            }
        }
    }

    /**
     * used when bird dies
     */
    private void killTargets() {
        for(int i = mTargets.size() - 1; i >= 0; i--) {
            Target target = mTargets.get(i);
            if(!target.getHitValue()) {
                float xVelocity = target.getVelocityX();
                if(mSlowMotionActivated) {
                    target.setVelocity(xVelocity + SCROLL_SPEED / 2, 0);
                } else {
                    target.setVelocity(xVelocity + SCROLL_SPEED, 0);
                }
            } else {
                target.setVelocity(0, 0);
            }
        }
    }

    /**
     * used when bird dies
     */
    private void killObstacles() {

        for(int i = mObstacles.size() - 1; i >= 0; i--) {
            Obstacle obstacle = mObstacles.get(i);
            if(obstacle.getObstacleType() == Obstacle.obstacleType.PLANE) {
                if(obstacle.getVelocityX() < -(SCROLL_SPEED + MIN_PLANE_SPEED_ON_GAMEOVER)) {
                    obstacle.setVelocityX(obstacle.getVelocityX() + SCROLL_SPEED); //keep vertical velocity //plane keeps flying just at a lower velocity because scrolling stops
                } else {
                    obstacle.setVelocityX(-MIN_PLANE_SPEED_ON_GAMEOVER);
                }
                if(!mSlowMotionActivated) {
                    mResourceManager.mPropellerSound.setRate(mResourceManager.mPropellerSound.getRate() / 2);
                }
            } else if (obstacle.getObstacleType() == Obstacle.obstacleType.BALLOON) {
                obstacle.setVelocityX(0);
            } else {
                //obstacle.die();
                obstacle.setVelocityX(0);
            }

        }
    }

    /**
     * used when bird dies
     */
    private void killCollectables() {

        for(int i = mCollectables.size() - 1; i >= 0; i--) {
            Collectable collectable = mCollectables.get(i);
                collectable.setVisible(false);
                //collectable.die();

        }
    }

    private void checkForTrainCarBirdContact() {
        for(int i = 0; i < mTrain.size(); i++) {
            ObstacleTrainCar trainCar = mTrain.get(i);
            if (!mGameOver && trainCar.collidesWith(mBird) && !trainCar.getCollidedWith() && !trainCar.getHitByMegaCrap()) {
                //if machine crap is activated, blast obstacle off screen
                if (mMachineCrapActivated || mHyperSpeedActivated || mInvincibilityActivated) {

                    float birdYVelocity = mBird.getVelocityY(); //convert from m/s to px/sec
                    float velocityY = birdYVelocity * 2;
                    if (velocityY != 0 && Math.abs(velocityY) < 500) {
                        velocityY = (velocityY / Math.abs(velocityY)) * 500;
                    }
                    mTrain.breakTrain(i);

                    trainCar.blastOff(velocityY);
                    trainCar.setCollidedWith();

                    //achievement
                    achTrainBreak.accomplish();

                } else {
                    //bird dies
                    setGameOver();
                }
            }
        }
    }


    /**
     * This method check all of the obstacles for contact with bird. If machine crap, blasts off. If balloon basket, drops target.
     * Also checks for obstacles leaving screen.
     * Also checks for passing obstacles to give points.
     */
    private void checkObstaclePosition() {
        for(int i = mObstacles.size() - 1; i >= 0; i--) {
            Obstacle obstacle = mObstacles.get(i);
            if (!mGameOver && obstacle.collidesWith(mBird) && !obstacle.getCollidedWith() && !obstacle.getHitByMegaCrap()) {
                //if machine crap is activated, blast obstacle off screen
                if(mMachineCrapActivated || mHyperSpeedActivated || mInvincibilityActivated) {

                    float birdYVelocity = mBird.getVelocityY(); //convert from m/s to px/sec
                    float velocityY = birdYVelocity * 2;
                    if (velocityY != 0 && Math.abs(velocityY) < 500) {
                        velocityY = (velocityY / Math.abs(velocityY)) * 500;
                    }
                    obstacle.blastOff(velocityY);
                    obstacle.setCollidedWith();

                    addPoint();


                } else {
                    //bird dies
                    setGameOver();
                }
                return;
            } else if //if this is a hot air balloon and the bird hits the basket... knock the person out
                    (!mGameOver &&  obstacle.getObstacleType() == Obstacle.obstacleType.BALLOON &&
                            ((ObstacleBalloon)obstacle).collidesWithBasket(mBird) && !((ObstacleBalloon) obstacle).getBasketHit()) {
                ((ObstacleBalloon) obstacle).birdHitsBasket();

                mTargets.add(mTargetPool.obtainPoolItem());

                ((TargetPerson1)(mTargets.get(mTargets.size() - 1))).setFalling(obstacle.getVelocityX(), obstacle.getX() + 28, obstacle.getY() + 110);
                attachChild(mTargets.get(mTargets.size() - 1));

                mResourceManager.mWilhelmScreamSound.play();

                if(mSlowMotionActivated) {
                    mTargets.get(mTargets.size() - 1).setSlowMotionActivated(true);
                    mResourceManager.mWilhelmScreamSound.setRate(0.5f);
                } else {
                    mResourceManager.mWilhelmScreamSound.setRate(1);
                }

                if(mHyperSpeedActivated) {
                    mTargets.get(mTargets.size() - 1).setHyperSpeedActivated(true);
                }
                sortChildren();

                addPoint();

                //handle achievement
                achBalloonKills.increment();
                achKills.increment();

                //if bird passes obstacle without hitting it
            } else if (!mGameOver && !obstacle.getScoreAdded() && mBird.getX() > (obstacle.getX() + obstacle.getWidth())) {
                obstacle.setScoreAdded();

                addPoint();

                //if obstacle leaves screen
            } else if (obstacle.getX() < -obstacle.getWidth() || obstacle.getY() < -obstacle.getHeight() || obstacle.getY() > SCREEN_HEIGHT) {
                detachChild(obstacle);
                if (obstacle.getObstacleType() == Obstacle.obstacleType.PLANE) {
                    numPlanesOnScreen--;
                }
                if(obstacle.getObstacleType() == Obstacle.obstacleType.PLANE || obstacle.getObstacleType() == Obstacle.obstacleType.BALLOON) {
                    mObstaclePoolAir.recyclePoolItem(obstacle);
                    mObstaclePoolAir.shufflePoolItems();
                } else {
                    mObstaclePoolGround.recyclePoolItem(obstacle);
                    mObstaclePoolGround.shufflePoolItems();
                }
                mObstacles.remove(i);
            }


        }
    }

    private void checkCollectablePosition() {
        for(int i = mCollectables.size() - 1; i >= 0; i--) {
            Collectable collectable = mCollectables.get(i);
            if (collectable.getX() <  -collectable.getWidth()) {
                detachChild(collectable);
                mCollectablePool.recyclePoolItem(collectable);
                mCollectablePool.shufflePoolItems();
                mCollectables.remove(i);
            } else if (!mGameOver && collectable.collidesWith(mBird) && !collectable.getCollected()) {
                collectable.setVisible(false);
                collectable.collect();
                if(collectable.getCollectableType() == Collectable.collectableType.TACO && !mHyperSpeedActivated) {
                    setMachineCrap(true);
                } else if(collectable.getCollectableType() == Collectable.collectableType.HAM) {
                    setDoublePoints(true);
                } else if(collectable.getCollectableType() == Collectable.collectableType.MELON && !mHyperSpeedActivated) {
                    setMegaCrap(true);
                } else if(collectable.getCollectableType() == Collectable.collectableType.MUFFIN && !mHyperSpeedActivated) {
                    setSlowMotion(true);
                    achMuffins.increment();
                } else if(collectable.getCollectableType() == Collectable.collectableType.BURGER) {
                    setHyperSpeed(true);
                    achBurgers.increment();
                }
                growCrapMeter(MAX_CRAP_METER_SIZE); //fill crap meter

                mResourceManager.mCollectionSound.play();

                //handle achievement
                achFood.increment();
            }


        }
    }


    /**
     * Shrinks crap meter on jump/crap
     * @param amountToShrink
     */
    private void shrinkCrapMeter(float amountToShrink) {
        if(!mMachineCrapActivated) {
            float currentWidth = mCrapMeter.getWidth();
            if (currentWidth <= amountToShrink) {
                mOutOfCrap = true;
                mCrapMeter.setWidth(0);
            } else {
                mCrapMeter.setWidth(currentWidth - amountToShrink);
            }
        }
    }

    private void growCrapMeter(float amountToGrow) {
        float currentWidth = mCrapMeter.getWidth();
        if(currentWidth + amountToGrow > MAX_CRAP_METER_SIZE) {
            mCrapMeter.setWidth(MAX_CRAP_METER_SIZE);
        } else {
            mCrapMeter.setWidth(currentWidth + amountToGrow);
        }
        mOutOfCrap = false;
    }

    /**
     * Check if crap has left screen. If so, recycle.
     * Also check if crap hits ground.
     * @param ground ground object to check for contact with
     */
    private void checkCrapPosition(IShape ground) {
        for (int i = mCraps.size() - 1; i >= 0; i--) {
            Crap crap = mCraps.get(i);
            if(crap.getX() < -crap.getWidth()) {
                detachChild(crap);
                if(crap.getCrapType() == Crap.crapType.MEGA) {
                    mMegaCrapPool.recyclePoolItem(crap);
                } else {
                    mCrapPool.recyclePoolItem(crap);
                }
                mCraps.remove(i);
                continue;
            } else if (crap.getFalling() && crap.getY() + crap.getHeight() > ground.getY()) {
                crap.hitsGround(mGameOver, selectedBird);
            }
            if(crap.getFalling() && !mGameOver) {
                boolean crapContactWithTarget = false;
                for(int j = mTargets.size() - 1; j >= 0; j--) {
                    Target target = mTargets.get(j);

                    //if target hits crap, target is not hit yet, and the target isn't a person FALLING from a balloon...
                    if(target.collidesWith(crap) && !target.getHitValue() && !(target.getTargetType() == Target.targetType.PERSON1 && ((TargetPerson1)target).getFalling())) {
                        target.hitWithCrap(mGameOver); //turn target to gravestone. sets velocity to scroll (unless gameover, then 0)

                        if(mHyperSpeedActivated) {
                            target.setHyperSpeedActivated(true);
                        }

                        crapContactWithTarget = true; //boolean allows one crap to take out multiple targets

                        addPoint();

                        //achievement
                        achKills.increment();
                        achPeopleCrappedOn.increment();

                    }
                }
                if(crapContactWithTarget && crap.getCrapType() == Crap.crapType.NORMAL) {
                    detachChild(mCraps.get(i)); //now we remove the crap
                    mCrapPool.recyclePoolItem(mCraps.get(i));
                    mCraps.remove(i);
                }
                if(crap.getCrapType() == Crap.crapType.MEGA) {
                    for(int j = mObstacles.size() - 1; j >= 0; j--) {
                        Obstacle obstacle = mObstacles.get(j);
                        if(obstacle.collidesWith(crap) && !obstacle.getHitByMegaCrap()) {
                            obstacle.hitWithMegaCrap((MegaCrap) crap);
                            obstacle.setScoreAdded();

                            addPoint(); //add one point for "passing" the obstacle
                            addPoint(); //and another for knocking it off the screen with mega crap

                            //handle achievement
                            achMegaCrap.increment();
                        }
                    }
                    if(mTrain.getOnScreen()) {
                        for(int j = 0; j < mTrain.size(); j++) {
                            ObstacleTrainCar trainCar = mTrain.get(j);
                            if(trainCar.collidesWith(crap) && !trainCar.getHitByMegaCrap()) {
                                mTrain.breakTrain(j);
                                trainCar.hitWithMegaCrap((MegaCrap) crap);
                                trainCar.setScoreAdded();

                                addPoint(); //add one point for "passing" the obstacle
                                addPoint(); //and another for knocking it off the screen with mega crap

                                //handle achievements
                                achTrainBreak.accomplish();
                                achMegaCrap.increment();
                            }
                        }
                    }
                }
            }

        }
    }


    private void addPoint() {
        mResourceManager.mCoinSound.play();
        mResourceManager.mCoinSound.setVolume(0.1f);
        if(mSlowMotionActivated) {
            mResourceManager.mCoinSound.setRate(0.5f);
        } else {
            mResourceManager.mCoinSound.setRate(1);
        }

        if(mDoublePointsActivated) {
            addDoublePoints();
        } else {
            score++;
            //handle achievements
            achScore1.increment();
            achScore2.increment();
            achScore3.increment();
            achScore4.increment();
            achScore5.increment();
        }

        mHudTextScore.setText(NumberFormatter.commaize(score));
    }

    /**
     * for adding points to the score when double points is activated
     */
    private void addDoublePoints() {
        mPlusTwo.setVisible(true);
        score += 2;
        doublePointsLastPointTime = doublePointsTime;
        //handle achievements
        achScore1.increment(2);
        achScore2.increment(2);
        achScore3.increment(2);
        achScore4.increment(2);
        achScore5.increment(2);
    }

    /**
     * Stops crap when gameover
     */
    private void stopCrap() {
        if(mCraps.size() > 0) {
                for (int i = mCraps.size() - 1; i >= 0; i--) {
                    mCraps.get(i).setVelocityX(0);
                }

        }
    }

    /**
     * for turning on and off the machine crap power up
     * @param bool
     */
    private void setMachineCrap(boolean bool) {
        if(bool) {

            setInvincibility(false);

            mMachineCrapActivated = true;
            mAnimatedCrapMeter.setVisible(true);
            mAnimatedCrapMeter.animate(new long[]{100, 100, 100, 100, 100, 100}, 0, 5, true);
            mCrapMeter.setVisible(false);
            machineCrappingLastCrapTime = 0;
            machineCrappingTime = 0;

            mAnimatedCrapMeterBlinkingFast = false;
            mAnimatedCrapMeterBlinkingSlow = false;

            mResourceManager.mMusic.pause();
            if(mSlowMotionActivated) {
                mResourceManager.mMariachiSlow.resume();
            } else {
                mResourceManager.mMariachiFast.resume();
            }

        } else {
            mMachineCrapActivated = false;

            setInvincibility(true);

            mResourceManager.mMariachiFast.pause();
            mResourceManager.mMariachiSlow.pause();
            mResourceManager.mMusic.resume();

        }
    }

    /**
     * for turning on and off the hyper speed power up
     * @param bool
     */
    private void setHyperSpeed(boolean bool) {
        if(bool) {

            if(mSlowMotionActivated) {
                setSlowMotion(false);
            }
            if(mMachineCrapActivated) {
                setMachineCrap(false);
            }
            if(mMegaCrapActivated) {
                setMegaCrap(false);
            }
            if(mInvincibilityActivated) {
                setInvincibility(false);
            }

            mHyperSpeedActivated = true;
            mAnimatedCrapMeter.setVisible(true);
            mAnimatedCrapMeter.animate(new long[]{100, 100, 100, 100, 100, 100}, 0, 5, true);
            mCrapMeter.setVisible(false);
            lastHyperSpeedCrapTime = 0;
            hyperSpeedTime = 0;

            mAnimatedCrapMeterBlinkingFast = false;
            mAnimatedCrapMeterBlinkingSlow = false;

            mBird.setVelocityY(0);
            mBird.setAccelerationY(HYPER_SPEED_Y_ACCELERATION);


            for (int i = 0; i < mObstacles.size(); i++) {
                mObstacles.get(i).setHyperSpeedActivated(true);
            }
            for(int i = 0; i < mTargets.size(); i++) {
                mTargets.get(i).setHyperSpeedActivated(true);
            }
            if(mMotherShipOnScreen) {
                mMotherShip.setHyperSpeedActivated(true);
            }
            if(mTrain.getOnScreen()) {
                mTrain.setHyperSpeedActivated(true);
            }

        } else {

            mHyperSpeedActivated = false;

            setInvincibility(true);

            mBird.setAccelerationY(GRAVITY);

            for (int i = 0; i < mObstacles.size(); i++) {
                mObstacles.get(i).setHyperSpeedActivated(false);
            }
            for(int i = 0; i < mTargets.size(); i++) {
                mTargets.get(i).setHyperSpeedActivated(false);
            }
            if(mMotherShipOnScreen) {
                mMotherShip.setHyperSpeedActivated(false);
            }
            if(mTrain.getOnScreen()) {
                mTrain.setHyperSpeedActivated(false);
            }
        }
    }

    private void setInvincibility(boolean bool) {
        if(bool) {
            mInvincibilityActivated = true;
            invincibilityTime = 0;
            mAnimatedCrapMeter.setVisible(true);
            mCrapMeter.setVisible(false);

            mAnimatedCrapMeter.animate(new long[]{400, 400}, 5, 6, true);
            mAnimatedCrapMeterBlinkingSlow = true;

        } else {
            mInvincibilityActivated = false;

            mAnimatedCrapMeter.setVisible(false);
            mCrapMeter.setVisible(true);
            growCrapMeter(MAX_CRAP_METER_SIZE);

            mAnimatedCrapMeterBlinkingSlow = false;
            mAnimatedCrapMeterBlinkingFast = false;
        }
    }


    /**
     * for turning on and off the machine crap power up
     * @param bool
     */
    private void setMegaCrap(boolean bool) {
        if (bool) {
            mMegaCrapActivated = true;
            megaCrappingTime = 0;
        } else {
            mMegaCrapActivated = false;
        }
    }

    private void setSlowMotion(boolean bool) {
        if(bool) {
            slowMotionTime = 0;
            if(!mSlowMotionActivated) {
                mSlowMotionActivated = true;
                for (int i = 0; i < mObstacles.size(); i++) {
                    if (!mObstacles.get(i).getHitByMegaCrap()) {
                        mObstacles.get(i).setSlowMotionActivated(true);
                    }
                }
                for(int i = 0; i < mTargets.size(); i++) {
                    mTargets.get(i).setSlowMotionActivated(true);
                    if(((TargetPerson1)mTargets.get(i)).getFalling()) {
                        mResourceManager.mWilhelmScreamSound.setRate(0.5f);
                    }
                }
                for(int i = 0; i < mCraps.size(); i++) {
                    mCraps.get(i).setSlowMotionActivated(true);
                }
                if(mMotherShipOnScreen) {
                    mMotherShip.setSlowMotionActivated(true);
                    mResourceManager.mMotherShipSound.setRate(0.5f);
                }
                if(mTrain.getOnScreen()) {
                    mTrain.setSlowMotionActivated(true);
                }
                mParallaxBackground.setParallaxChangePerSecond(PARALLAX_CHANGE_PER_SECOND / 2);

                if(mMachineCrapActivated) {
                    mResourceManager.mMariachiFast.pause();
                    mResourceManager.mMariachiSlow.resume();
                }
                if (numPlanesOnScreen > 0) { //handle propeller sound slow down
                    mResourceManager.mPropellerSound.setRate(mResourceManager.mPropellerSound.getRate() / 2);
                }
            }

        } else {
            mSlowMotionActivated = false;
            for (int i = 0; i < mObstacles.size(); i++) {
                if (!mObstacles.get(i).getHitByMegaCrap()) {
                    mObstacles.get(i).setSlowMotionActivated(false);
                }
            }
            for(int i = 0; i < mTargets.size(); i++) {
                mTargets.get(i).setSlowMotionActivated(false);
                if(((TargetPerson1)mTargets.get(i)).getFalling()) {
                    mResourceManager.mWilhelmScreamSound.setRate(1);
                }
            }
            for(int i = 0; i < mCraps.size(); i++) {
                mCraps.get(i).setSlowMotionActivated(false);
            }
            if(mMotherShipOnScreen) {
                mMotherShip.setSlowMotionActivated(false);
                mResourceManager.mMotherShipSound.setRate(1f);

            }
            if(mTrain.getOnScreen()) {
                mTrain.setSlowMotionActivated(false);
            }
            mParallaxBackground.setParallaxChangePerSecond(PARALLAX_CHANGE_PER_SECOND);
            if(mMachineCrapActivated) {
                mResourceManager.mMariachiSlow.pause();
                mResourceManager.mMariachiFast.resume();
            }
            if (numPlanesOnScreen > 0) { //handle propeller sound speed up back to normal
                mResourceManager.mPropellerSound.setRate(mResourceManager.mPropellerSound.getRate() * 2);
            }
        }
    }


    /**
     * for turning on and off the double points power up
     * @param bool
     */
    private void setDoublePoints(boolean bool) {
        if(bool) {
            mDoublePointsActivated = true;
            doublePointsTime = 0;
            doublePointsLastPointTime = 0;
        } else {
            mDoublePointsActivated = false;
        }
    }



    private void dropCrap(float currentXPosition, float currentYPosition) {
        if(mMegaCrapActivated) {
            mCraps.add(mMegaCrapPool.obtainPoolItem());
        } else {
            mCraps.add(mCrapPool.obtainPoolItem());
        }

        Crap crap = mCraps.get(mCraps.size() - 1);

        crap.setPosition(currentXPosition, currentYPosition + (mBird.getHeight()));

        attachChild(crap);

        crap.setCurrentTileIndex(selectedBird * 2);

        if(getObstacleIndex() < CRAP_METER_SHRINK_STOP_OBSTACLE_INDEX) {
            shrinkCrapMeter((float) Math.sqrt((double) getObstacleIndex() * 2));
        } else {
            shrinkCrapMeter((float) Math.sqrt((double) CRAP_METER_SHRINK_STOP_OBSTACLE_INDEX * 2));
        }

        if(selectedBird == 6) { //if ninja bird, spin throwing star crap
            crap.setAngularVelocity(600);
        }

        if(mSlowMotionActivated) {
            crap.setSlowMotionActivated(true);
        }

        if(mMegaCrapActivated) {
            mResourceManager.mMegaCrapSound.play();
            mResourceManager.mMegaCrapSound.setVolume(2);
            if(mSlowMotionActivated) {
                mResourceManager.mMegaCrapSound.setRate(0.5f);
            } else {
                mResourceManager.mMegaCrapSound.setRate(1);
            }
        } else {
            mResourceManager.mJumpSound.play();
            mResourceManager.mJumpSound.setVolume(0.75f);
            if (mSlowMotionActivated) {
                mResourceManager.mJumpSound.setRate(0.5f);
            } else {
                mResourceManager.mJumpSound.setRate(1);
            }
        }

    }

    private void dropHyperSpeedCrap(float currentXPosition, float currentYPosition) {

        if(mMegaCrapActivated) {
            mCraps.add(mMegaCrapPool.obtainPoolItem());
        } else {
            mCraps.add(mCrapPool.obtainPoolItem());
        }

        Crap crap = mCraps.get(mCraps.size() - 1);

        crap.setPosition(currentXPosition, currentYPosition + (mBird.getHeight() - crap.getHeight()) / 2);

        attachChild(crap);

        crap.setHyperSpeedActivated(mBird.getVelocityY());

        crap.setCurrentTileIndex(selectedBird * 2);

        if(selectedBird == 6) { //if ninja bird, spin throwing star crap
            crap.setAngularVelocity(600);
        }

        if(mMegaCrapActivated) {
            mResourceManager.mMegaCrapSound.play();
            mResourceManager.mMegaCrapSound.setVolume(2);
            mResourceManager.mMegaCrapSound.setRate(1);

        } else {
            mResourceManager.mJumpSound.play();
            mResourceManager.mJumpSound.setVolume(0.75f);
            mResourceManager.mJumpSound.setRate(1);

        }

    }


    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

        if(pSceneTouchEvent.isActionDown()) {
            if(!mGameOver) {
                if (!mOutOfCrap && !mMachineCrapActivated && !mHyperSpeedActivated) {
                    jumpBird();
                    return true;
                } else if (mMachineCrapActivated) {
                    mMachineCrapping = true;
                    machineCrappingLastCrapTime = 0;
                } else if (mHyperSpeedActivated) {
                    mBird.setAccelerationY(-HYPER_SPEED_Y_ACCELERATION);
                }
            }
        }
        if(pSceneTouchEvent.isActionUp()) {
            if(mMachineCrapping) {
                mMachineCrapping = false;
            } else if (mHyperSpeedActivated) {
                mBird.setAccelerationY(HYPER_SPEED_Y_ACCELERATION);
            }
        }


        if(!mGameOver && !mOutOfCrap && pSceneTouchEvent.isActionDown() && !mMachineCrapActivated && !mHyperSpeedActivated) {
                jumpBird();
                return true;
            } else if(!mGameOver && mMachineCrapActivated && pSceneTouchEvent.isActionDown()) {
                mMachineCrapping = true;
                machineCrappingLastCrapTime = 0;
            } else if(mMachineCrapping && pSceneTouchEvent.isActionUp()) {
                mMachineCrapping = false;
            }

        return false;
    }


    private void jumpBird() {
        mBird.setRotation(-15);
        float currentYPosition = mBird.getY();
        float currentXPosition = mBird.getX();

        mBird.jump(mMegaCrapActivated);

        dropCrap(currentXPosition, currentYPosition);

    }


    private void displayScore() {

        if (score > most ) {
            most = score;
            mActivity.setMaxScore(most);
        }

        scoreText.setText(NumberFormatter.commaize(score)); //update values
        mostText.setText(NumberFormatter.commaize(most));

        scoreText.setX(scoreText.getX() - (scoreText.getWidth() / 2)); //adjust margins
        mostText.setX(mostX - (mostText.getWidth() / 2));


        //hide score and crapMeter
        mHudTextScore.setVisible(false);
        mCrapMeter.setVisible(false);
        mAnimatedCrapMeter.setVisible(false);
        mPlusTwo.setVisible(false);
        mAlertSign.setVisible(false);
        pauseButton.setVisible(false);

        setChildScene(mGameOverScene, false, true, true);

        mActivity.handleGameFinished(score);

        handlePostGameAchievements();

    }


    private void checkForBirdGroundContact() {
        if(mBird.getY() + mBird.getHeight() > mGround.getY()) {

            mBird.setVelocityY(0);
            mBird.setAcceleration(0, 0);
            mBird.setY(mGround.getY() - mBird.getHeight() - 5);

            if(!mGameOver) { //if the bird hasn't already hit an obstacle
                setGameOver();
            }

            //display game over with score
            displayScore();

        }
    }

    @Override
    public void onBackKeyPressed() {
        if(mGooglePlaySceneOpen) {
            openGooglePlayButtons(false);
        } else {

            mHudTextScore.setVisible(false);
            mAnimatedCrapMeter.setVisible(false);
            mCrapMeter.setVisible(false);
            mPlusTwo.setVisible(false);
            mAlertSign.setVisible(false);
            mResourceManager.mMariachiFast.pause();
            mResourceManager.mMariachiSlow.pause();

            mSceneManager.setScene(SceneManager.SceneType.SCENE_MENU);

        }
    }

    private void openGooglePlayButtons(boolean bool) {
        if(bool) {
            mGooglePlaySceneOpen = true;
            birdsButton.setVisible(false);
            googlePlayButton.setVisible(false);
            leaderboardButton.setVisible(true);
            achievementsButton.setVisible(true);
            backButton.setVisible(true);
            mGameOverScene.unregisterTouchArea(birdsButton);
            mGameOverScene.unregisterTouchArea(googlePlayButton);
            mGameOverScene.registerTouchArea(leaderboardButton);
            mGameOverScene.registerTouchArea(achievementsButton);
            mGameOverScene.registerTouchArea(backButton);
        } else {
            mGooglePlaySceneOpen = false;
            birdsButton.setVisible(true);
            googlePlayButton.setVisible(true);
            leaderboardButton.setVisible(false);
            achievementsButton.setVisible(false);
            backButton.setVisible(false);
            mGameOverScene.unregisterTouchArea(leaderboardButton);
            mGameOverScene.unregisterTouchArea(achievementsButton);
            mGameOverScene.unregisterTouchArea(backButton);
            mGameOverScene.registerTouchArea(birdsButton);
            mGameOverScene.registerTouchArea(googlePlayButton);

        }
    }

    private void initializeAchievments() {
        achScore1 = new CountAchievement(mActivity.getString(R.string.achievement_red), ACH_SCORE_1);
        achScore2 = new CountAchievement(mActivity.getString(R.string.achievement_rainbow), ACH_SCORE_2);
        achScore3 = new CountAchievement(mActivity.getString(R.string.achievement_ghost), ACH_SCORE_3);
        achScore4 = new CountAchievement(mActivity.getString(R.string.achievement_super), ACH_SCORE_4);
        achScore5 = new CountAchievement(mActivity.getString(R.string.achievement_gold), ACH_SCORE_5);
        achTrainEncounter = new Achievement(mActivity.getString(R.string.achievement_brown));
        achMotherShipEncounter = new Achievement(mActivity.getString(R.string.achievement_blue));
        achTrainBreak = new Achievement(mActivity.getString(R.string.achievement_pirate));
        achFood = new CountAchievement(mActivity.getString(R.string.achievement_hungry), ACH_FOOD_IN_ONE_GAME);
        achBalloonKills = new CountAchievement(mActivity.getString(R.string.achievement_backwards), ACH_BALLOON_KILLS);
        achBurgers = new CountAchievement(mActivity.getString(R.string.achievement_flashy), ACH_BURGERS);
        achKills = new CountAchievement(mActivity.getString(R.string.achievement_ninja), ACH_KILLS_IN_GAME);

        unlockableAchievements = new ArrayList<Achievement>();

        unlockableAchievements.add(achScore1);
        unlockableAchievements.add(achScore2);
        unlockableAchievements.add(achScore3);
        unlockableAchievements.add(achScore4);
        unlockableAchievements.add(achScore5);
        unlockableAchievements.add(achTrainEncounter);
        unlockableAchievements.add(achTrainBreak);
        unlockableAchievements.add(achMotherShipEncounter);
        unlockableAchievements.add(achFood);
        unlockableAchievements.add(achBalloonKills);
        unlockableAchievements.add(achBurgers);
        unlockableAchievements.add(achKills);

        achMegaCrap = new IncrementalAchievement(mActivity.getString(R.string.achievement_black));
        achPeopleCrappedOn = new IncrementalAchievement(mActivity.getString(R.string.achievement_white));
        achMuffins = new IncrementalAchievement(mActivity.getString(R.string.achievement_blocky));

        incrementalAchievements = new ArrayList<IncrementalAchievement>();

        incrementalAchievements.add(achMegaCrap);
        incrementalAchievements.add(achPeopleCrappedOn);
        incrementalAchievements.add(achMuffins);


    }

    private void handleInGameAchievements() {

        for(Achievement achievement : unlockableAchievements) {
            if(achievement.isReadyToSubmit()) {
                mActivity.unlockAchievement(achievement.getAchievementID());
            }
        }

    }

    private void handlePostGameAchievements() {

        //check "in a row" achievements
        if(score >= ACH_IN_A_ROW_1) {
            mActivity.setAchievementInARow1Count(mActivity.getAchievementInARow1Count() + 1);
        } else {
            mActivity.setAchievementInARow1Count(0);
        }
        if(score >= ACH_IN_A_ROW_2) {
            mActivity.setAchievementInARow2Count(mActivity.getAchievementInARow2Count() + 1);
        } else {
            mActivity.setAchievementInARow2Count(0);
        }
        if(mActivity.getAchievementInARow1Count() >= ACH_IN_A_ROW_NUM) {
            mActivity.unlockAchievement(mActivity.getString(R.string.achievement_ffb));
        }
        if(mActivity.getAchievementInARow2Count() >= ACH_IN_A_ROW_NUM) {
            mActivity.unlockAchievement(mActivity.getString(R.string.achievement_mystery));
        }

        //handle incremental achievements
        for(IncrementalAchievement achievement : incrementalAchievements) {
            if(achievement.getCount() > 0) {
                mActivity.incrementAchievement(achievement.getAchievementID(), achievement.getCount());
            }
        }



    }



    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {
        //TODO
    }



}
