package com.application.nick.dumpydodge.scene;

import com.application.nick.dumpydodge.R;
import com.application.nick.dumpydodge.SceneManager;
import com.application.nick.dumpydodge.entity.Bird;
import com.application.nick.dumpydodge.entity.BirdPool;
import com.application.nick.dumpydodge.entity.powerup.Bullet;
import com.application.nick.dumpydodge.entity.powerup.BulletPool;
import com.application.nick.dumpydodge.entity.powerup.MachineGun;
import com.application.nick.dumpydodge.entity.powerup.MotherShip;
import com.application.nick.dumpydodge.entity.Person;
import com.application.nick.dumpydodge.entity.powerup.Umbrella;
import com.application.nick.dumpydodge.entity.Wall;
import com.application.nick.dumpydodge.entity.collectable.Collectable;
import com.application.nick.dumpydodge.entity.collectable.CollectableMachineGun;
import com.application.nick.dumpydodge.entity.collectable.CollectableMothership;
import com.application.nick.dumpydodge.entity.collectable.CollectablePool;
import com.application.nick.dumpydodge.entity.collectable.CollectableSlowMotion;
import com.application.nick.dumpydodge.entity.collectable.CollectableUmbrella;
import com.application.nick.dumpydodge.entity.crap.Crap;
import com.application.nick.dumpydodge.entity.crap.CrapPool;
import com.application.nick.dumpydodge.entity.crap.MegaCrapPool;
import com.application.nick.dumpydodge.entity.scenery.Scenery;
import com.application.nick.dumpydodge.entity.scenery.SceneryHouse;
import com.application.nick.dumpydodge.entity.scenery.SceneryTree;
import com.application.nick.dumpydodge.util.ParallaxLayer;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import java.util.ArrayList;

/**
 * Created by Nick on 4/5/2015.
 */
public class GameScene extends BaseScene implements IOnSceneTouchListener {


    float mParallaxChangePerSecond = 0;
    ParallaxLayer mParallaxBackground;
    Person mPerson;

    BirdPool birdPool;
    CrapPool crapPool;
    MegaCrapPool megaCrapPool;
    CollectablePool collectablePool;
    BulletPool bulletPool;

    MotherShip motherShip;
    MachineGun machineGun;
    Umbrella umbrella;

    ArrayList<Bird> birds;
    ArrayList<Crap> craps;
    ArrayList<Collectable> collectables;
    ArrayList<Scenery> sceneries;
    ArrayList<Wall> walls;
    ArrayList<Bullet> bullets;

    Wall leftWall, rightWall;

    float birdSpawnCounter, timeAlive, collectableOnScreenCounter, collectableOffScreenCounter, slowMotionCounter, machineGunCounter, bulletCounter, umbrellaCounter, machineGunSoundCounter;
    int score, most;

    Text mHudTextScore, mostText, scoreText;

    CameraScene mGameOverScene, mPauseScene, mTutorialScene;
    TiledSprite volumeButton, pauseButton, playButton, leaderboardButton, achievementsButton, backButton, googlePlayButton;

    boolean mGameOver, mGameStarted, jumpPressed = false, leftPressed = false, rightPressed = false, mGooglePlaySceneOpen = false, slowMotionActivated = false;

    //achievement stuff
    Direction lastWallTouched;
    int achNumWallsTouched = 0, achNumNinjaCrapsDodged = 0, achNumGhostBirdsEncountered = 0, achNumJumps = 0, achBirdsKilledWithMothership = 0, achNumSlowMotions = 0, achNumPowerUps = 0,
            achBirdsShotWithMachineGun = 0;
    float treeTouchTime = 0;
    boolean achHitInAir = false, achKilledByFirstBird = false, achNoMovement = true, achKilledByMegaCrap = false, achKilledByPirateBird = false, achMotherShipFirstTime = false,
            achUmbrellaBounces = false, gotAchTreeTouch1 = false, gotAchTreeTouch2 = false, gotAchTreeTouch3 = false, gotAchWallToWall1 = false, gotAchWallToWall2 = false,
            gotAchMotherShip = false, gotAchSlowMotion = false, gotAchMotherShipFirstTime = false, gotAchMachineGun = false, gotAchUmbrellaBounces = false;

    int numFingersOnScreen = 0;

    @Override
    public void createScene() {
        mEngine.registerUpdateHandler(new FPSLogger());

        setOnAreaTouchTraversalFrontToBack();
        setOnSceneTouchListener(this);

        mActivity.logGameStart();

        if (mResourceManager.mMusic != null && !mResourceManager.mMusic.isPlaying()) {
            mResourceManager.mMusic.resume();
        }

        mParallaxBackground = new ParallaxLayer(mCamera, false);
        mParallaxBackground.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-5, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerBack.getHeight(), mResourceManager.mParallaxLayerBack, mVertexBufferObjectManager), false));
        mParallaxBackground.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-10, new Sprite(0, GROUND, mResourceManager.mParallaxLayerGround, mVertexBufferObjectManager), false));
        mParallaxBackground.attachParallaxEntity(new ParallaxLayer.ParallaxEntity(-10, new Sprite(0, GROUND - mResourceManager.mParallaxLayerGrass.getHeight(), mResourceManager.mParallaxLayerGrass, mVertexBufferObjectManager), false));
        attachChild(mParallaxBackground);


        mPerson = new Person((SCREEN_WIDTH - mResourceManager.mPersonTextureRegion.getWidth()) / 2, GROUND - mResourceManager.mPersonTextureRegion.getHeight(), mResourceManager.mPersonTextureRegion, mVertexBufferObjectManager);
        attachChild(mPerson);
        mPerson.setZIndex(PERSON_Z_INDEX);

        birdPool = new BirdPool(mResourceManager.mBirdsTextureRegion, mVertexBufferObjectManager);
        birdPool.batchAllocatePoolItems(BIRDS_TO_ALLOCATE);
        crapPool = new CrapPool(mResourceManager.mCrapTextureRegion, mVertexBufferObjectManager);
        crapPool.batchAllocatePoolItems(CRAPS_TO_ALLOCATE);
        megaCrapPool = new MegaCrapPool(mResourceManager.mMegaCrapTextureRegion, mVertexBufferObjectManager);
        megaCrapPool.batchAllocatePoolItems(CRAPS_TO_ALLOCATE);
        bulletPool = new BulletPool(mResourceManager.mBulletTextureRegion, mVertexBufferObjectManager);
        bulletPool.batchAllocatePoolItems(BULLETS_TO_ALLOCATE);
        collectablePool = new CollectablePool(mResourceManager, mVertexBufferObjectManager);

        collectablePool.addSinglePoolItem(new CollectableMothership(0, 0, mResourceManager.mCollectableMelonTextureRegion, mVertexBufferObjectManager));

        collectablePool.addSinglePoolItem(new CollectableSlowMotion(0, 0, mResourceManager.mCollectableMuffinTextureRegion, mVertexBufferObjectManager));

        collectablePool.addSinglePoolItem(new CollectableMachineGun(0, 0, mResourceManager.mCollectableTacoTextureRegion, mVertexBufferObjectManager));

        collectablePool.addSinglePoolItem(new CollectableUmbrella(0,0, mResourceManager.mCollectableMushroomTextureRegion, mVertexBufferObjectManager));

        collectablePool.shufflePoolItems();

        birds = new ArrayList<Bird>();
        craps = new ArrayList<Crap>();
        collectables = new ArrayList<Collectable>();
        sceneries = new ArrayList<Scenery>();
        walls = new ArrayList<Wall>();
        bullets = new ArrayList<Bullet>();

        motherShip = new MotherShip(SCREEN_WIDTH * 2, 0, mResourceManager.mObstacleMotherShipTextureRegion, mVertexBufferObjectManager);
        machineGun = new MachineGun(0, 0, mResourceManager.mGunTextureRegion, mVertexBufferObjectManager);
        umbrella = new Umbrella(0,0, mResourceManager.mUmbrellaTextureRegion, mVertexBufferObjectManager);

        final Scenery tree1 = new SceneryTree(SCREEN_WIDTH, GROUND - mResourceManager.mObstacleTreesTextureRegion.getHeight(), mResourceManager.mObstacleTreesTextureRegion, mVertexBufferObjectManager);
        attachChild(tree1);
        final Scenery house1 = new SceneryHouse(0, GROUND - mResourceManager.mObstacleHouseTextureRegion.getHeight(), mResourceManager.mObstacleHouseTextureRegion, mVertexBufferObjectManager);
        attachChild(house1);
        final Scenery tree2 = new SceneryTree(-mResourceManager.mObstacleTreesTextureRegion.getWidth() * 1.5f, GROUND - mResourceManager.mObstacleTreesTextureRegion.getHeight(), mResourceManager.mObstacleTreesTextureRegion, mVertexBufferObjectManager);
        attachChild(tree2);
        final Scenery tree3 = new SceneryTree(SCREEN_WIDTH / 2 + mResourceManager.mObstacleHouseTextureRegion.getWidth(), GROUND - mResourceManager.mObstacleTreesTextureRegion.getHeight(), mResourceManager.mObstacleTreesTextureRegion, mVertexBufferObjectManager);
        attachChild(tree3);
        final Scenery house2 = new SceneryHouse(SCREEN_WIDTH * 1.25f, GROUND - mResourceManager.mObstacleHouseTextureRegion.getHeight(), mResourceManager.mObstacleHouseTextureRegion, mVertexBufferObjectManager);
        attachChild(house2);

        sceneries.add(tree1);
        sceneries.add(tree2);
        sceneries.add(tree3);
        sceneries.add(house1);
        sceneries.add(house2);

        leftWall = new Wall(-(SCREEN_WIDTH - mResourceManager.mWallTextureRegion.getWidth()) / 2, GROUND - mResourceManager.mWallTextureRegion.getHeight(), mResourceManager.mWallTextureRegion, mVertexBufferObjectManager);
        rightWall = new Wall(SCREEN_WIDTH * 1.5f, GROUND - mResourceManager.mWallTextureRegion.getHeight(), mResourceManager.mWallTextureRegion, mVertexBufferObjectManager);
        attachChild(leftWall);
        attachChild(rightWall);

        walls.add(leftWall);
        walls.add(rightWall);


        birdSpawnCounter = 0;
        timeAlive = 0;
        collectableOnScreenCounter = 0;
        collectableOffScreenCounter = 0;
        machineGunCounter = 0;
        mGameStarted = false;
        mGameOver = false;

        /* The actual collision-checking. */
        registerUpdateHandler(new IUpdateHandler() {

            @Override
            public void reset() {
            }

            @Override
            public void onUpdate(float pSecondsElapsed) {

                if(mGameStarted && !mGameOver) {
                    timeAlive += pSecondsElapsed;
                    score = (int) timeAlive;
                    mHudTextScore.setText(String.valueOf(score));
                    mHudTextScore.setX((SCREEN_WIDTH - mHudTextScore.getWidth()) / 2);

                    //handle collectable
                    if(collectables.size() == 0) {
                        collectableOnScreenCounter = 0;
                        collectableOffScreenCounter += pSecondsElapsed;

                        //adding new collectables handled in dropCrap()

                    } else if (collectables.size() >= 1) {
                        collectableOffScreenCounter = 0;
                        collectableOnScreenCounter += pSecondsElapsed;
                        if(collectableOnScreenCounter > COLLECTABLE_ON_SCREEN_TIME) {
                            recycleCollectable(collectables.get(0));
                        }
                    }

                    //handle slow motion time-out
                    if(slowMotionActivated) {
                        slowMotionCounter += pSecondsElapsed;
                        if(slowMotionCounter > SLOW_MOTION_TIME) {
                            setSlowMotionActivated(false);
                        }
                    }

                    //handle machine gun
                    if(machineGun.isActivated()) {

                        float machineGunKickY = (float)(MACHINE_GUN_KICK * Math.sin(timeAlive * 30) * Math.sin(MACHINE_GUN_ANGLE_RADIANS));
                        float machineGunKickX = (float)(MACHINE_GUN_KICK * Math.sin(timeAlive * 30) * Math.cos(MACHINE_GUN_ANGLE_RADIANS));
                        machineGun.setY(RectangularShape.Side.CENTER, mPerson.getY() - MACHINE_GUN_DISPLACEMENT_Y + machineGunKickY);
                        if(machineGun.getDirection() == Direction.LEFT) {
                            machineGun.setX(RectangularShape.Side.CENTER, SCREEN_WIDTH / 2 - MACHINE_GUN_DISPLACEMENT_X + machineGunKickX);
                        } else {
                            machineGun.setX(RectangularShape.Side.CENTER, SCREEN_WIDTH / 2 + MACHINE_GUN_DISPLACEMENT_X - machineGunKickX);
                        }

                        //machineGun.setVelocityY(mPerson.getVelocityY());

                        //handle adding new bullets... current bullets looped through below
                        bulletCounter += pSecondsElapsed;
                        if(bulletCounter > TIME_BETWEEN_MACHINE_GUN_SHOTS) {
                            bulletCounter = 0;
                            Bullet bullet = bulletPool.obtainPoolItem();
                            bullet.shoot(machineGun);
                            attachChild(bullet);
                            bullets.add(bullet);
                        }

                        //handle sound
                        machineGunSoundCounter += pSecondsElapsed;
                        if(machineGunSoundCounter >= MACHINE_GUN_SOUND_LENGTH) {
                            mResourceManager.mGunSound.play();
                            machineGunSoundCounter = 0;
                        }

                        machineGunCounter += pSecondsElapsed;
                        if(machineGunCounter > MACHINE_GUN_TIME) {
                            setMachineGunActivated(false);
                        }
                    }

                    //handle umbrella time-out and position
                    if(umbrella.isActivated()) {
                        umbrella.setX(RectangularShape.Side.CENTER, mPerson.getX(RectangularShape.Side.CENTER));
                        umbrella.setY(RectangularShape.Side.BOTTOM, mPerson.getY(RectangularShape.Side.TOP));
                        umbrella.setVelocityY(mPerson.getVelocityY());

                        umbrellaCounter += pSecondsElapsed;
                        if(umbrellaCounter > UMBRELLA_TIME) {
                            setUmbrellaActivated(false);
                        } else if(umbrellaCounter > UMBRELLA_TIME - UMBRELLA_WARNING_TIME) {
                            umbrella.runningOut();
                        }
                    }

                    //handle tree touch achievement
                    if(mPerson.collidesWith(tree1) || mPerson.collidesWith(tree2) || mPerson.collidesWith(tree3)) {
                        treeTouchTime += pSecondsElapsed;
                    }

                }

                sortChildren(); //put person in front of everything and machine gun in front of person

                //update scrolling bg
                mParallaxBackground.setParallaxChangePerSecond(mParallaxChangePerSecond);

                if(craps.size() >= CRAPS_TO_ALLOCATE - BIRDS_TO_ALLOCATE) {
                    recycleCrap(craps.get(0)); //remove oldest dropped crap if there is more than 50
                }

                //add more birds
                float birdSpawnTime;
                if(timeAlive > 2500) {
                    birdSpawnTime = 0.2f;
                } else if(timeAlive > 1500) {
                    birdSpawnTime = 0.25f;
                } else if(timeAlive > 1000) {
                    birdSpawnTime = 0.3f;
                } else if(timeAlive > 750) {
                    birdSpawnTime = 0.35f;
                } else if(timeAlive > 500) {
                    birdSpawnTime = 0.4f;
                } else if(timeAlive > 300) {
                    birdSpawnTime = 0.45f;
                } else if(timeAlive > 120) {
                    birdSpawnTime = 0.5f;
                } else {
                    birdSpawnTime = 2 * (float) Math.pow(0.5, timeAlive / 60.0); //2 * 0.5^(t/60)
                }

                if(slowMotionActivated) {
                    birdSpawnTime *= 2;
                }

                birdSpawnCounter += pSecondsElapsed;
                if(birdSpawnCounter > birdSpawnTime && birds.size() < BIRDS_TO_ALLOCATE && mGameStarted && !mGameOver) {
                    addBird(timeAlive);
                    birdSpawnCounter = 0;
                }

                //check for jumping person contact with ground
                if (mPerson.getJumping() && mPerson.getY(RectangularShape.Side.BOTTOM) > GROUND) {
                    mPerson.hitGround();
                    if(mGameOver) {
                        mPerson.hitWithCrap();
                        displayScore();
                    }
                }

                //check for person passing walls
                if (mPerson.getX() < leftWall.getX(RectangularShape.Side.RIGHT)) {
                    mPerson.setX(leftWall.getX(RectangularShape.Side.RIGHT));
                    stopPerson();
                    if(lastWallTouched != Direction.LEFT) {
                        lastWallTouched = Direction.LEFT;
                        achNumWallsTouched++;
                    }
                }
                if (mPerson.getX(RectangularShape.Side.RIGHT) > rightWall.getX()) {
                    mPerson.setX(RectangularShape.Side.RIGHT, rightWall.getX());
                    stopPerson();
                    if(lastWallTouched != Direction.RIGHT) {
                        lastWallTouched = Direction.RIGHT;
                        achNumWallsTouched++;
                    }
                }


                //loop through entities

                //loop through birds
                for (int i = birds.size() - 1; i >= 0; i--) {
                    Bird bird = birds.get(i);

                    //handle leaving screen
                    if(bird.getX() < (leftWall.getX(RectangularShape.Side.RIGHT) - SCREEN_WIDTH / 2)) {
                        recycleBird(bird);
                        continue;
                    } else if (bird.getX() > (rightWall.getX() + SCREEN_WIDTH / 2)) {
                        recycleBird(bird);
                        continue;
                    } else if (bird.isBlastedOff() && (bird.getY(RectangularShape.Side.BOTTOM) < 0 || bird.getY(RectangularShape.Side.TOP) > SCREEN_HEIGHT)) {
                        recycleBird(bird);
                        continue;
                    }

                    //jump and drop crap
                    if(!bird.isBlastedOff() && bird.getY() > bird.getLowerHeightLimit()) {
                        bird.setY(bird.getLowerHeightLimit());
                        if(bird.getMachineCrapActivated()) {
                            bird.jumpMachineCrap(timeAlive);
                        } else {
                            bird.jump(bird.getMegaCrapActivated());
                        }
                        dropCrap(bird);
                    }

                    //handle machine crappers
                    if(!bird.isBlastedOff() && bird.getMachineCrapActivated()) {
                        if(bird.getY(RectangularShape.Side.BOTTOM) < 0 || bird.getNumMachineCrapsDropped() >= bird.getMaxMachineCrapsToDrop()) {
                            bird.finishMachineCrapping();
                        }

                        if (!slowMotionActivated && timeAlive - bird.getLastMachineCrapTime() > TIME_BETWEEN_MACHINE_CRAPS && !bird.getMachineCrapDone()) {
                            bird.jumpMachineCrap(timeAlive);
                            dropCrap(bird);
                        } else if (slowMotionActivated && timeAlive - bird.getLastMachineCrapTime() > TIME_BETWEEN_MACHINE_CRAPS * 2 && !bird.getMachineCrapDone()) {
                            bird.jumpMachineCrap(timeAlive);
                            dropCrap(bird);
                        }
                    }

                    //handle slow motion activation
                    if(slowMotionActivated && !bird.isSlowMotionActivated()) {
                        bird.setSlowMotionActivated(true);
                    } else if(!slowMotionActivated && bird.isSlowMotionActivated()) {
                        bird.setSlowMotionActivated(false);
                    }

                    //rotate bird with changing velocity
                    if(!bird.isBlastedOff() && bird.getY() + bird.getHeight() < GROUND) {
                        if(!slowMotionActivated) {
                            if (bird.getMovingDirection() == Direction.RIGHT) {
                                bird.setRotation(bird.getVelocityY() / 15 - 10);
                            } else if (bird.getMovingDirection() == Direction.LEFT) {
                                bird.setRotation(-(bird.getVelocityY() / 15 - 10));
                            }
                        } else {
                            if (bird.getMovingDirection() == Direction.RIGHT) {
                                bird.setRotation(bird.getVelocityY() * 2 / 15 - 10);
                            } else if (bird.getMovingDirection() == Direction.LEFT) {
                                bird.setRotation(-(bird.getVelocityY() * 2 / 15 - 10));
                            }
                        }
                    }

                    if(motherShip.isFlyingBy() && motherShip.collidesWith(bird)) {
                        if(bird.getY(RectangularShape.Side.CENTER) < motherShip.getY(RectangularShape.Side.CENTER)) {
                            //if collision is with top half of mothership, blast off up
                            bird.blastOff(-BIRD_BLAST_OFF_FROM_MOTHERSHIP_VELOCITY_Y);
                        } else {
                            //bottom half, blast off down
                            bird.blastOff(BIRD_BLAST_OFF_FROM_MOTHERSHIP_VELOCITY_Y);
                        }
                        //handle achievment
                        achBirdsKilledWithMothership++;
                    }

                    //scroll with background)
                    if (mPerson.getMovingDirection() != bird.getPersonMovingDirection()) {
                        bird.scroll(mPerson.getMovingDirection());
                    }
                }

                //loop through craps
                for (int i = craps.size() - 1; i >= 0; i--) {
                    Crap crap = craps.get(i);
                    //check for person contact
                    if(!mGameOver && crap.getFalling() && crap.collidesWith(mPerson)) {
                        mGameOver = true;

                        //handle machine gun
                        if(machineGun.isActivated()) {
                            setMachineGunActivated(false);
                        }

                        //handle umbrella
                        if(umbrella.isActivated()) {
                            setUmbrellaActivated(false);
                        }

                        //handle achievments
                        if(crap.getFromFirstBird()) {
                            achKilledByFirstBird = true;
                        }
                        if(crap.getCurrentBirdIndex() == PIRATE_BIRD_INDEX) {
                            achKilledByPirateBird = true;
                        }
                        if(crap.getCrapType() == Crap.crapType.MEGA) {
                            achKilledByMegaCrap = true;
                        }

                        stopPerson();
                        if(mPerson.getY(RectangularShape.Side.BOTTOM) >= GROUND) {
                            mPerson.hitWithCrap();
                            displayScore();
                        } else {
                            mPerson.hitWithCrap(true); //person is in the air so wait for him to hit ground before displaying score
                            achHitInAir = true; //achievement
                        }
                        pauseButton.setVisible(false);
                        recycleCrap(crap);
                        continue;
                    }

                    //handle umbrella
                    if(umbrella.isActivated()) {
                        if(crap.collidesWith(umbrella) && !crap.isBouncingOffUmbrella()) {
                            crap.bounceOffUmbrella(umbrella);
                        } else if(!crap.collidesWith(umbrella) && crap.isBouncingOffUmbrella()) {
                            crap.setBouncingOffUmbrella(false);
                        }

                        if(crap.getNumUmbrellaBounces() >= ACH_NUM_UMBRELLA_BOUNCES) {
                            achUmbrellaBounces = true;
                        }
                    }

                    //handle slow motion
                    if(slowMotionActivated && !crap.isSlowMotionActivated()) {
                        crap.setSlowMotionActivated(true);
                    } else if(!slowMotionActivated && crap.isSlowMotionActivated()) {
                        crap.setSlowMotionActivated(false);
                    }

                    //check for ground contact
                    if(crap.getFalling() && crap.getY(RectangularShape.Side.BOTTOM) > GROUND) {
                        //achievement
                        if(crap.getCurrentBirdIndex() == NINJA_BIRD_INDEX && !mGameOver && crap.getX(RectangularShape.Side.LEFT) > 0 && crap.getX(RectangularShape.Side.LEFT) > leftWall.getX(RectangularShape.Side.RIGHT) && crap.getX(RectangularShape.Side.RIGHT) < SCREEN_WIDTH && crap.getX(RectangularShape.Side.RIGHT) < rightWall.getX(RectangularShape.Side.LEFT)) {
                            //if crap is on screen and within walls
                            achNumNinjaCrapsDodged++;
                        }
                        crap.hitsGround();
                    }
                    //scroll with background
                    if (mPerson.getMovingDirection() != crap.getPersonMovingDirection()) {
                        crap.scroll(mPerson.getMovingDirection());
                    }
                }

                //loop through scenery
                for (int i = sceneries.size() - 1; i >= 0; i--) {
                    Scenery scenery = sceneries.get(i);
                    //scroll with background
                    if (mPerson.getMovingDirection() != scenery.getPersonMovingDirection()) {
                        scenery.scroll(mPerson.getMovingDirection());
                    }
                }

                //loop through collectables
                for (int i = collectables.size() - 1; i >= 0; i--) {
                    Collectable collectable = collectables.get(i);

                    //handle falling collectables
                    if(collectable.isFalling() && collectable.getY(RectangularShape.Side.BOTTOM) > GROUND - COLLECTABLE_HEIGHT_ABOVE_GROUND) {
                        collectable.stopFalling();
                    }

                    //new collectables and collectable time-out handled above

                    //check for collectable-person contact
                    if(collectable.collidesWith(mPerson) && !mGameOver) {
                        //check collectable type and activate corresponding powerup
                        if(collectable.getCollectableType() == Collectable.collectableType.MOTHERSHIP && !motherShip.isFlyingBy()) {
                            callMotherShip();
                        } else if(collectable.getCollectableType() == Collectable.collectableType.SLOW_MOTION && !slowMotionActivated) {
                            setSlowMotionActivated(true);
                        } else if(collectable.getCollectableType() == Collectable.collectableType.MACHINE_GUN) {
                            setMachineGunActivated(true);
                        } else if(collectable.getCollectableType() == Collectable.collectableType.UMBRELLA) {
                            setUmbrellaActivated(true);
                        }
                        recycleCollectable(collectable);
                        mResourceManager.mCollectionSound.play();

                        //handle achievement
                        achNumPowerUps++;

                        continue;
                    }
                    //scroll with background
                    if (mPerson.getMovingDirection() != collectable.getPersonMovingDirection()) {
                        collectable.scroll(mPerson.getMovingDirection());
                    }
                }

                //loop through walls
                for (int i = walls.size() - 1; i >= 0; i--) {
                    Wall wall = walls.get(i);
                    //scroll with background
                    if (mPerson.getMovingDirection() != wall.getPersonMovingDirection()) {
                        wall.scroll(mPerson.getMovingDirection());
                    }
                }

                //loop through bullets
                    for(int i = bullets.size() - 1; i >= 0; i--) {
                        Bullet bullet = bullets.get(i);

                        //adding new bullets handled above


                        //scroll with background
                        if (mPerson.getMovingDirection() != bullet.getPersonMovingDirection()) {
                            bullet.scroll(mPerson.getMovingDirection());
                        }

                        //handle bullet leaving screen
                        if(bullet.getY(RectangularShape.Side.BOTTOM) < -bullet.getHeight()) {
                            recycleBullet(bullet);
                            continue;
                        }

                        //handle bullet-bird contact
                        for (int j = birds.size() - 1; j >= 0; j--) {
                            Bird bird = birds.get(j);
                            if(bird.collidesWith(bullet)) {
                                bird.blastOff(-BIRD_BLAST_OFF_FROM_MOTHERSHIP_VELOCITY_Y);
                                recycleBullet(bullet);

                                //handle achievement
                                achBirdsShotWithMachineGun++;

                                break;
                            }
                        }
                    }


                //handle mothership
                if(motherShip.isFlyingBy()) {
                    //handle mothership leaving screen
                    if(motherShip.getX(RectangularShape.Side.RIGHT) < leftWall.getX() - (SCREEN_WIDTH / 2)) {
                        resetMotherShip();
                    }

                    //bird contact handled in bird loop above so that we don't loop through birds twice

                    //handle slow motion
                    if(slowMotionActivated && !motherShip.isSlowMotionActivated()) {
                        motherShip.setSlowMotionActivated(true);
                    } else if(!slowMotionActivated && motherShip.isSlowMotionActivated()) {
                        motherShip.setSlowMotionActivated(false);
                    }

                    //scroll with BG
                    if (mPerson.getMovingDirection() != motherShip.getPersonMovingDirection()) {
                        motherShip.scroll(mPerson.getMovingDirection());
                    }
                }

                handleInGameAchievements();

            }
        });

        //create HUD for score
        HUD gameHUD = new HUD();
        // CREATE SCORE TEXT
        mHudTextScore = new Text(SCREEN_WIDTH/2, 20, mResourceManager.mFont5, "0123456789", new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
        mHudTextScore.setX((SCREEN_WIDTH - mHudTextScore.getWidth()) / 2);
        mHudTextScore.setVisible(true);
        gameHUD.attachChild(mHudTextScore);

        mCamera.setHUD(gameHUD);


        //create CameraScene for game over
        final float gameOverBoardX = (SCREEN_WIDTH - mResourceManager.mScoreBoardTextureRegion.getWidth()) / 2;
        final float gameOverBoardY = (SCREEN_HEIGHT - mResourceManager.mScoreBoardTextureRegion.getHeight()) / 2;

        final float gameOverTitleX = (SCREEN_WIDTH - mResourceManager.mGameOverTextTextureRegion.getWidth()) / 2;
        final float gameOverTitleY = (gameOverBoardY - mResourceManager.mGameOverTextTextureRegion.getHeight());

        final float playX = SCREEN_WIDTH / 2 - mResourceManager.mPlayButtonTextureRegion.getWidth() / 2;
        final float playY = gameOverBoardY + mResourceManager.mScoreBoardTextureRegion.getHeight();

        final float scoreX = gameOverBoardX + 55;
        final float scoreY = gameOverBoardY + 40;

        final float mostX = gameOverBoardX + 165;
        final float mostY = scoreY;

        mGameOverScene = new CameraScene(mCamera);
        mGameOverScene.setBackgroundEnabled(false);

        final Sprite gameOverTitle = new Sprite(gameOverTitleX, gameOverTitleY, mResourceManager.mGameOverTextTextureRegion, mVertexBufferObjectManager);
        mGameOverScene.attachChild(gameOverTitle);

        final Sprite boardSprite = new Sprite(gameOverBoardX, gameOverBoardY, mResourceManager.mScoreBoardTextureRegion, mVertexBufferObjectManager);
        mGameOverScene.attachChild(boardSprite);

        scoreText = new Text(scoreX, scoreY, mResourceManager.mFont4, "0123456789", new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
        scoreText.setText("0");
        mGameOverScene.attachChild(scoreText);

        mostText = new Text(mostX, mostY, mResourceManager.mFont4, "0123456789", new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
        mostText.setText(String.valueOf(most));
        mGameOverScene.attachChild(mostText);



        playButton = new TiledSprite(playX, playY, mResourceManager.mPlayButtonTextureRegion, mVertexBufferObjectManager) {

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
        playButton.setScale(0.75f);
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


        pauseButton = new TiledSprite(SCREEN_WIDTH - mResourceManager.mPauseButtonTextureRegion.getWidth(), 0, mResourceManager.mPauseButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    if(isVisible()) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();
                    }
                }
                if (pSceneTouchEvent.isActionUp()) {
                    if(isVisible()) {
                        setCurrentTileIndex(0);
                        setPause(true);
                    }
                }
                return true;
            }
        };
        pauseButton.setCurrentTileIndex(0);
        pauseButton.setScale(0.25f);
        registerTouchArea(pauseButton);
        attachChild(pauseButton);

        volumeButton = new TiledSprite(0, 0, mResourceManager.mVolumeButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (isVisible() && pSceneTouchEvent.isActionUp()) {
                    if(getCurrentTileIndex() == 0) {
                        setCurrentTileIndex(1);
                    } else {
                        setCurrentTileIndex(0);
                    }
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
        volumeButton.setScale(0.25f);
        registerTouchArea(volumeButton);
        mPauseScene.registerTouchArea(volumeButton);
        mGameOverScene.registerTouchArea(volumeButton);
        attachChild(volumeButton);


        mTutorialScene = new CameraScene(mCamera);
        mTutorialScene.setBackgroundEnabled(false);

        final Text jumpText = new Text(0, 0, mResourceManager.mFont5, mActivity.getString(R.string.jump), new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);

        final Rectangle jumpButton = new Rectangle(10, 10, SCREEN_WIDTH - 20, SCREEN_HEIGHT * 2 / 3 - 15, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setAlpha(0.25f);
                    if(!mPerson.getJumping()) {
                        mPerson.jump();
                    }
                } else if (pSceneTouchEvent.isActionUp()) {
                    setVisible(false);
                    jumpText.setVisible(false);
                    jumpPressed = true;
                    if(jumpPressed && leftPressed && rightPressed) {
                        showTutorial(false);
                    }
                }
                return true;
            }
        };
        jumpButton.setColor(Color.BLACK);
        jumpButton.setAlpha(0.5f); //make transparent
        mTutorialScene.registerTouchArea(jumpButton);
        mTutorialScene.attachChild(jumpButton);
        mTutorialScene.attachChild(jumpText);

        jumpText.setX(RectangularShape.Side.CENTER, jumpButton.getX(RectangularShape.Side.CENTER));
        jumpText.setY(RectangularShape.Side.CENTER, jumpButton.getY(RectangularShape.Side.CENTER));


        final Text leftText = new Text(0, 0, mResourceManager.mFont5, mActivity.getString(R.string.left), new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);

        final Rectangle leftButton = new Rectangle(10, SCREEN_HEIGHT * 2 / 3 + 5, SCREEN_WIDTH / 2 - 15, SCREEN_HEIGHT / 3 - 15, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setAlpha(0.25f);
                    mPerson.move(Direction.LEFT);
                } else if (pSceneTouchEvent.isActionUp()) {
                    setVisible(false);
                    leftText.setVisible(false);
                    mPerson.stop();
                    leftPressed = true;
                    if(jumpPressed && leftPressed && rightPressed) {
                        showTutorial(false);
                    }
                }
                return true;
            }
        };
        leftButton.setColor(Color.BLACK);
        leftButton.setAlpha(0.5f); //make transparent
        mTutorialScene.registerTouchArea(leftButton);
        mTutorialScene.attachChild(leftButton);
        mTutorialScene.attachChild(leftText);

        leftText.setX(RectangularShape.Side.CENTER, leftButton.getX(RectangularShape.Side.CENTER));
        leftText.setY(RectangularShape.Side.CENTER, leftButton.getY(RectangularShape.Side.CENTER));


        final Text rightText = new Text(0, 0, mResourceManager.mFont5, mActivity.getString(R.string.right), new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);

        final Rectangle rightButton = new Rectangle(SCREEN_WIDTH / 2 + 5, SCREEN_HEIGHT * 2 / 3 + 5, SCREEN_WIDTH / 2 - 15, SCREEN_HEIGHT / 3 - 15, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setAlpha(0.25f);
                    mPerson.move(Direction.RIGHT);
                } else if (pSceneTouchEvent.isActionUp()) {
                    setVisible(false);
                    rightText.setVisible(false);
                    mPerson.stop();
                    rightPressed = true;
                    if(jumpPressed && leftPressed && rightPressed) {
                        showTutorial(false);
                    }
                }
                return true;
            }
        };
        rightButton.setColor(Color.BLACK);
        rightButton.setAlpha(0.5f); //make transparent
        mTutorialScene.registerTouchArea(rightButton);
        mTutorialScene.attachChild(rightButton);
        mTutorialScene.attachChild(rightText);

        rightText.setX(RectangularShape.Side.CENTER, rightButton.getX(RectangularShape.Side.CENTER));
        rightText.setY(RectangularShape.Side.CENTER, rightButton.getY(RectangularShape.Side.CENTER));



        if(mActivity.getShowingTutorial()) {
            showTutorial(true);
        } else {
            startGame();
        }


        setTouchAreaBindingOnActionDownEnabled(true);
    }

    @Override
    public void onBackKeyPressed() {
        if(mGooglePlaySceneOpen) {
            openGooglePlayButtons(false);
        } else {
            mHudTextScore.setVisible(false);
            mActivity.setShowingTutorial(false);
            mSceneManager.setScene(SceneManager.SceneType.SCENE_MENU);
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

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if(pSceneTouchEvent.isActionDown()) {
            numFingersOnScreen++;
        } else if(pSceneTouchEvent.isActionUp()) {
            numFingersOnScreen--;
        }

        if(!mGameOver) {

            if(numFingersOnScreen == 0) {
                stopPerson();
            }

            //if its on the bottom 1/3 of the screen, move left or right
            if (pSceneTouchEvent.getY() > SCREEN_HEIGHT * 2f / 3f) {
                if (pSceneTouchEvent.isActionDown()) {
                    if (pSceneTouchEvent.getX() < SCREEN_WIDTH / 2) {
                        if (mPerson.getX() > leftWall.getX(RectangularShape.Side.RIGHT)) {
                            movePerson(Direction.LEFT);
                        }
                    } else {
                        if (mPerson.getX(RectangularShape.Side.RIGHT) < rightWall.getX()) {
                            movePerson(Direction.RIGHT);
                        }
                    }
                } else if (pSceneTouchEvent.isActionUp() || pSceneTouchEvent.isActionOutside()) {
                    //only stop the person if there are no fingers on screen or user lifts their finger on the same side as person is moving
                    if ((pSceneTouchEvent.getX() < SCREEN_WIDTH / 2 && mPerson.getMovingDirection() == Direction.LEFT) ||
                           (pSceneTouchEvent.getX() > SCREEN_WIDTH / 2 && mPerson.getMovingDirection() == Direction.RIGHT)) {
                        stopPerson();
                    }
                }
            }
            //if the tap is in the top 2/3 of the screen, jump
            else {
                if (pSceneTouchEvent.isActionDown() && !mPerson.getJumping()) {
                    mPerson.jump();

                    //achievement
                    achNumJumps++;
                }
            }

            achNoMovement = false;
        }
        return false;
    }

    private void recycleCrap(Crap crap) {
        detachChild(crap);
        if(crap.getCrapType() == Crap.crapType.NORMAL) {
            crapPool.recyclePoolItem(crap);
        } else {
            megaCrapPool.recyclePoolItem(crap);
        }
        craps.remove(crap);
        crapPool.shufflePoolItems();
        megaCrapPool.shufflePoolItems();
    }

    private void recycleBird(Bird bird) {
        detachChild(bird);
        birdPool.recyclePoolItem(bird);
        birds.remove(bird);
        birdPool.shufflePoolItems();
    }

    private void recycleCollectable(Collectable collectable) {
        detachChild(collectable);
        collectablePool.recyclePoolItem(collectable);
        collectables.remove(collectable);
        collectablePool.shufflePoolItems();
    }

    private void recycleBullet(Bullet bullet) {
        detachChild(bullet);
        bulletPool.recyclePoolItem(bullet);
        bullets.remove(bullet);
        bulletPool.shufflePoolItems();
    }

    private void addBird(float timeAlive) {
        Bird bird = birdPool.obtainPoolItem();
        randomizeBirdDirectionAndSpeed(bird);
        bird.randomizeCurrentBirdIndex((int) timeAlive / 5);
        randomizeMegaCrap(bird, timeAlive);
        randomizeMachineCrap(bird, timeAlive);
        attachChild(bird);
        birds.add(bird);

        //achievement
        if(bird.getCurrentBirdIndex() == GHOST_BIRD_INDEX) {
            achNumGhostBirdsEncountered++;
        }
    }

    private void randomizeMegaCrap(Bird bird, float timeAlive) {
        float odds = (timeAlive - 30) / 10;
        if(odds < 0) {
            odds = 0;
        } else if(odds > 10) {
            odds = 10;
        }
        float rand = (float) Math.random() * 20;
        if(rand <= odds) {
            bird.setMegaCrapActivated(true);
        }
    }

    private void randomizeMachineCrap(Bird bird, float timeAlive) {
        float odds = (timeAlive - 100) / 10;
        if(odds < 0) {
            odds = 0;
        } else if(odds > 10) {
            odds = 10;
        }
        float rand = (float) Math.random() * 20;
        if(rand <= odds) {
            bird.setMachineCrapActivated(true);
            bird.randomizeMaxMachineCrapsToDrop();
            if(bird.getMegaCrapActivated()) {
                if(timeAlive < 150) {
                    bird.setMegaCrapActivated(false);
                }
            }
        }
    }

    private void randomizeBirdDirectionAndSpeed(Bird bird) {
        int rand = (int) (Math.random() * 2);
        if(rand == 0) {
            bird.setX(leftWall.getX(RectangularShape.Side.RIGHT) - SCREEN_WIDTH / 2);
            bird.setVelocityX((float) (Math.random() * BIRD_VELOCITY_X_RANGE) + BIRD_MINIMUM_VELOCITY_X);
            bird.setFlippedHorizontal(false);
            bird.setMovingDirection(Direction.RIGHT);
        } else {
            bird.setX(rightWall.getX() + SCREEN_WIDTH / 2);
            bird.setVelocityX(-((float) (Math.random() * BIRD_VELOCITY_X_RANGE) + BIRD_MINIMUM_VELOCITY_X));
            bird.setFlippedHorizontal(true);
            bird.setMovingDirection(Direction.LEFT);
        }
    }


    private void dropCrap(Bird bird) {
        //if bird is on screen and within bounds and its time to drop a collectable... bombs away!
        if(!mGameOver && collectableOffScreenCounter > COLLECTABLE_OFF_SCREEN_TIME && (bird.getX() > 0 && bird.getX() > leftWall.getX(RectangularShape.Side.RIGHT) && bird.getX(RectangularShape.Side.RIGHT) < SCREEN_WIDTH && bird.getX(RectangularShape.Side.RIGHT) < rightWall.getX())) {
            dropCollectable(bird);
            return;
        }

        if(!bird.getMegaCrapActivated()) {
            craps.add(crapPool.obtainPoolItem());
            if(bird.getX(RectangularShape.Side.RIGHT) > 0 && bird.getX() < SCREEN_WIDTH) {
                mResourceManager.mJumpSound.play();
            }
        } else {
            craps.add(megaCrapPool.obtainPoolItem());
            if(bird.getX(RectangularShape.Side.RIGHT) > 0 && bird.getX() < SCREEN_WIDTH) {
                mResourceManager.mMegaCrapSound.play();
            }
        }

        Crap crap = craps.get(craps.size() - 1);

        if(bird.isFlippedHorizontal()) {
            crap.setPosition(bird.getX(RectangularShape.Side.RIGHT) - crap.getWidth(), bird.getY(RectangularShape.Side.BOTTOM));
        } else {
            crap.setPosition(bird.getX(), bird.getY(RectangularShape.Side.BOTTOM));
        }

        if(bird.getFirstBird()) {
            crap.setFromFirstBird(true);
        }


        attachChild(crap);

        crap.setCurrentBirdIndex(bird.getCurrentBirdIndex());
    }

    private void dropCollectable(Bird bird) {
        collectableOffScreenCounter = 0;
        collectableOnScreenCounter = 0;
        Collectable collectable = collectablePool.obtainPoolItem();
        if(bird.isFlippedHorizontal()) {
            collectable.setPosition(bird.getX(RectangularShape.Side.RIGHT) - collectable.getWidth(), bird.getY(RectangularShape.Side.BOTTOM));
        } else {
            collectable.setPosition(bird.getX(), bird.getY(RectangularShape.Side.BOTTOM));
        }
        attachChild(collectable);
        collectables.add(collectable);
    }



    private void movePerson(Direction direction) {
        if(machineGun.isActivated()) {
            machineGun.setDirection(direction);
        }
        if(direction == Direction.LEFT) {
            mParallaxChangePerSecond = -PARALLAX_CHANGE_PER_SECOND;
            mPerson.move(Direction.LEFT);
        } else if(direction == Direction.RIGHT){
            mParallaxChangePerSecond = PARALLAX_CHANGE_PER_SECOND;
            mPerson.move(Direction.RIGHT);
        }
    }

    private void stopPerson() {
        mParallaxChangePerSecond = 0;
        if(!machineGun.isActivated()) {
            mPerson.stop();
        } else {
            mPerson.stopFacingDirection(machineGun.getDirection());
        }
    }

    private void callMotherShip() {
        attachChild(motherShip);
        motherShip.setX(rightWall.getX() + SCREEN_WIDTH / 2);
        motherShip.fly();
        mResourceManager.mMotherShipSound.play();

        //handle achievement
        achMotherShipFirstTime = true;
    }

    private void resetMotherShip() {
        detachChild(motherShip);
        motherShip.reset();
    }

    private void setSlowMotionActivated(boolean bool) {
        if(bool) {
            slowMotionActivated = true;
            slowMotionCounter = 0;

            //entities slowed down in onUpdate

            //handle achievment
            achNumSlowMotions++;
        } else {
            slowMotionActivated = false;

            //entities sped up in onUpdate
        }
    }

    private void setMachineGunActivated(boolean bool) {
        if(bool) {
            machineGunCounter = 0;
            machineGunSoundCounter = 0;
            machineGun.activate(true);
            machineGun.setDirection(mPerson.getMovingDirection());
            attachChild(machineGun);
            machineGun.setZIndex(MACHINE_GUN_Z_INDEX);
            mResourceManager.mGunSound.play();
            if(mPerson.getMovingDirection() == Direction.STOPPED) {
                mPerson.stopFacingDirection(Direction.RIGHT);
            }
        } else {
            machineGun.activate(false);
            detachChild(machineGun);
            mResourceManager.mGunSound.stop();
            if(mPerson.getMovingDirection() == Direction.STOPPED) {
                mPerson.stop();
            }
        }
    }

    private void setUmbrellaActivated(boolean bool) {
        if(bool) {
            umbrellaCounter = 0;
            umbrella.activate(true);
            attachChild(umbrella);
            umbrella.setZIndex(UMBRELLA_Z_INDEX);
        } else {
            umbrella.activate(false);
            detachChild(umbrella);
        }
    }

    public void startGame() {
        mGameStarted = true;
        addBird(timeAlive);
        birds.get(0).setFirstBird(true); //this is the first bird... used for achievement
    }

    public void setPause(boolean bool) {
        if(mGameStarted && !mGameOver) {
            if (bool) {
                if (pauseButton.isVisible()) {
                    pauseButton.setVisible(false);
                    setIgnoreUpdate(true);
                    setChildScene(mPauseScene, false, true, true);

                    mResourceManager.mMusic.pause();

                }
            } else {
                clearChildScene();
                setIgnoreUpdate(false);
                pauseButton.setVisible(true);

                mResourceManager.mMusic.resume();
            }


        }
    }


    public void showTutorial(boolean bool) {
        if(bool) {
            setChildScene(mTutorialScene, false, false, true);
            mHudTextScore.setVisible(false);
            volumeButton.setVisible(false);
            pauseButton.setVisible(false);
        } else {
            mActivity.setShowingTutorial(false);

            clearChildScene();
            mHudTextScore.setVisible(true);
            volumeButton.setVisible(true);
            pauseButton.setVisible(true);

            startGame();
        }
    }


    private void displayScore() {

        most = mActivity.getMaxScore();

        if (score > most) {
            most = score;
            mActivity.setMaxScore(most);
        }

        scoreText.setText(String.valueOf(score)); //update values
        mostText.setText(String.valueOf(most));

        scoreText.setX(scoreText.getX() - (scoreText.getWidth() / 2)); //adjust margins
        mostText.setX(mostText.getX() - (mostText.getWidth() / 2));

        //hide score and crapMeter
        mHudTextScore.setVisible(false);

        setChildScene(mGameOverScene, false, false, true);

        mActivity.handleGameFinished();

        mActivity.submitScoreToLeaderboard(score);

        mActivity.logGameFinished(score);

        handlePostGameAchievements();

    }

    private void openGooglePlayButtons(boolean bool) {
        if(bool) {
            mGooglePlaySceneOpen = true;
            playButton.setVisible(false);
            googlePlayButton.setVisible(false);
            leaderboardButton.setVisible(true);
            achievementsButton.setVisible(true);
            backButton.setVisible(true);
            mGameOverScene.unregisterTouchArea(playButton);
            mGameOverScene.unregisterTouchArea(googlePlayButton);
            mGameOverScene.registerTouchArea(leaderboardButton);
            mGameOverScene.registerTouchArea(achievementsButton);
            mGameOverScene.registerTouchArea(backButton);
        } else {
            mGooglePlaySceneOpen = false;
            playButton.setVisible(true);
            googlePlayButton.setVisible(true);
            leaderboardButton.setVisible(false);
            achievementsButton.setVisible(false);
            backButton.setVisible(false);
            mGameOverScene.unregisterTouchArea(leaderboardButton);
            mGameOverScene.unregisterTouchArea(achievementsButton);
            mGameOverScene.unregisterTouchArea(backButton);
            mGameOverScene.registerTouchArea(playButton);
            mGameOverScene.registerTouchArea(googlePlayButton);

        }
    }

    private void handleInGameAchievements() {


        //check wall touches
        if(achNumWallsTouched > ACH_WALL_TO_WALL_1 && !gotAchWallToWall1) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_wall_to_wall_1));
            gotAchWallToWall1 = true;
        }

        if(achNumWallsTouched > ACH_WALL_TO_WALL_2 && !gotAchWallToWall2) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_wall_to_wall_2));
            gotAchWallToWall2 = true;
        }


        //check tree touch time
        if(treeTouchTime >= ACH_TREE_TOUCH_1 && !gotAchTreeTouch1) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_tree_touch_1));
            gotAchTreeTouch1 = true;
        }
        if(treeTouchTime >= ACH_TREE_TOUCH_2 && !gotAchTreeTouch2) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_tree_touch_2));
            gotAchTreeTouch2 = true;
        }
        if(treeTouchTime >= ACH_TREE_TOUCH_3 && !gotAchTreeTouch3) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_tree_touch_3));
            gotAchTreeTouch3 = true;
        }


        //powerups
        if(achBirdsKilledWithMothership >= ACH_MOTHERSHIP_BIRDS_KILLED && !gotAchMotherShip) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_mothership));
            gotAchMotherShip = true;
        }
        if(achMotherShipFirstTime && !gotAchMotherShipFirstTime) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_mothership_first_time));
            gotAchMotherShipFirstTime = true;
        }
        if(achNumSlowMotions >= ACH_SLOW_MOTION_COUNT && !gotAchSlowMotion) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_slow_motion));
            gotAchSlowMotion = true;
        }
        if(achUmbrellaBounces && !gotAchUmbrellaBounces) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_umbrella_bounces));
            gotAchUmbrellaBounces = true;
        }
        if(achBirdsShotWithMachineGun >= ACH_NUM_BIRDS_SHOT_WITH_MACHINE_GUN && !gotAchMachineGun) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_machine_gun));
            gotAchMachineGun = true;
        }


    }

    private void handlePostGameAchievements() {

        //Check game times
        if(score >= ACH_TIME_SINGLE_GAME_1) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_time_single_game_1));
        }
        if(score >= ACH_TIME_SINGLE_GAME_2) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_time_single_game_2));
        }
        if(score >= ACH_TIME_SINGLE_GAME_3) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_time_single_game_3));
        }
        if(score >= ACH_TIME_SINGLE_GAME_4) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_time_single_game_4));
        }
        if(score >= ACH_TIME_SINGLE_GAME_5) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_time_single_game_5));
        }

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
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_in_a_row_1));
        }
        if(mActivity.getAchievementInARow2Count() >= ACH_IN_A_ROW_NUM) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_in_a_row_2));
        }


        //total time achievements
        int time = score + mActivity.getLeftOverSeconds();
        int minutes = time / 60;
        mActivity.setLeftOverSeconds(time % 60);
        if(minutes > 0) {
            mActivity.incrementAchievement(mActivity.getString(R.string.ach_time_total_1), minutes);
            mActivity.incrementAchievement(mActivity.getString(R.string.ach_time_total_2), minutes);
            mActivity.incrementAchievement(mActivity.getString(R.string.ach_time_total_3), minutes);
        }

        //special bird achievements
        if(achNumGhostBirdsEncountered > 0)
        mActivity.incrementAchievement(mActivity.getString(R.string.ach_ghost_birds), achNumGhostBirdsEncountered);
        if(achNumNinjaCrapsDodged > 0)
        mActivity.incrementAchievement(mActivity.getString(R.string.ach_ninja_dodge), achNumNinjaCrapsDodged);

        if(achKilledByPirateBird) {
            mActivity.incrementAchievement(mActivity.getString(R.string.ach_pirate_booty), 1);
        }

        //handle death achievements
        mActivity.incrementAchievement(mActivity.getString(R.string.ach_deaths), 1);
        if(achKilledByMegaCrap) {
            mActivity.incrementAchievement(mActivity.getString(R.string.ach_deaths_by_mega_crap), 1);
        }

        //jumps
        if(achNumJumps > 0)
        mActivity.incrementAchievement(mActivity.getString(R.string.ach_jumping), achNumJumps);

        //death by first bird
        if(achKilledByFirstBird) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_first_bird_dead));
        }

        //killed in air
        if(achHitInAir) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_killed_in_air));
        }

        //no movement
        if(achNoMovement) {
            mActivity.unlockAchievement(mActivity.getString(R.string.ach_no_moving));
        }

        //powerups
        if(achNumPowerUps > 0) {
            mActivity.incrementAchievement(mActivity.getString(R.string.ach_total_powerups), achNumPowerUps);
        }

    }

}
