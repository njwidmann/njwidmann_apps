package com.application.nick.crappybird.scene;

import android.util.Log;

import com.application.nick.crappybird.R;
import com.application.nick.crappybird.SceneManager;
import com.application.nick.crappybird.entity.MarketBird;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 4/5/2015.
 */
public class MarketScene extends BaseScene {

    private CameraScene mBirdMarketScene;

    private int currentBird, numBirds;

    private List<MarketBird> myBirds;

    private AutoParallaxBackground autoParallaxBackground;
    private ParallaxBackground.ParallaxEntity parallaxLayerBack;
    private ParallaxBackground.ParallaxEntity parallaxLayerMiddle;
    private ParallaxBackground.ParallaxEntity parallaxLayerFront;

    @Override
    public void createScene() {
        mEngine.registerUpdateHandler(new FPSLogger());

        autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 10);
        parallaxLayerBack = new ParallaxBackground.ParallaxEntity(0, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerBack.getHeight(), mResourceManager.mParallaxLayerBack, mVertexBufferObjectManager));
        parallaxLayerMiddle = new ParallaxBackground.ParallaxEntity(0, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerFront.getHeight() - mResourceManager.mParallaxLayerMiddle.getHeight(), mResourceManager.mParallaxLayerMiddle, mVertexBufferObjectManager));
        parallaxLayerFront = new ParallaxBackground.ParallaxEntity(0, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxLayerFront.getHeight(), mResourceManager.mParallaxLayerFront, mVertexBufferObjectManager));

        autoParallaxBackground.attachParallaxEntity(parallaxLayerBack);
        autoParallaxBackground.attachParallaxEntity(parallaxLayerFront);
        autoParallaxBackground.attachParallaxEntity(parallaxLayerMiddle);

        setBackground(autoParallaxBackground);

        myBirds = new ArrayList<MarketBird>();

        for(int i = 0; i < mActivity.marketBirds.length; i++) {
            if(mActivity.marketBirds[i].isUnlocked()) {
                myBirds.add(mActivity.marketBirds[i]);
            }
        }
        for(int i = 0; i < myBirds.size(); i++) {
            if (myBirds.get(i).getBirdNum() == mActivity.getSelectedBird()) {
                currentBird = i;
                break;
            }
        }

        numBirds = myBirds.size();


        if (!mResourceManager.mMusic.isPlaying()) {
            mResourceManager.mMusic.resume();
        }


        createBirdMarketScene();

        setChildScene(mBirdMarketScene);

        if(mActivity.isConnectedToGooglePlay())
            mActivity.checkOwnedBirds(); //get any birds that are from new achievements
    }



    private void createBirdMarketScene() {


        mBirdMarketScene = new CameraScene(mCamera);
        mBirdMarketScene.setBackgroundEnabled(false);

        final float menuButtonX = 0;
        final float menuButtonY = -5;


        final float boardX = (SCREEN_WIDTH - mResourceManager.mTutorialBoardTextureRegion.getWidth()) / 2;
        final float boardY = menuButtonY + mResourceManager.mBackButtonTextureRegion.getHeight();


        final float arrowLeftX = boardX;
        final float arrowRightX = boardX + mResourceManager.mTutorialBoardTextureRegion.getWidth() - mResourceManager.mArrowLeftButtonTextureRegion.getWidth();

        final float titleTextY = boardY + 20;


        final Sprite popUpBoard = new Sprite(boardX, boardY, mResourceManager.mTutorialBoardTextureRegion, mVertexBufferObjectManager);
        mBirdMarketScene.attachChild(popUpBoard);


        final Text titleText = (new Text(0, titleTextY, mResourceManager.mFont5, mActivity.getString(R.string.my_birds), new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager));
        titleText.setX(RectangularShape.Side.CENTER, SCREEN_WIDTH / 2);
        mBirdMarketScene.attachChild(titleText);


        final float birdNameY = titleTextY + titleText.getHeight() + mResourceManager.mFont2.getLineHeight();

        final String initText = "0123456789 qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

        final Text birdNameText = (new Text(0, birdNameY, mResourceManager.mFont2, initText, new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager));
        birdNameText.setText(myBirds.get(currentBird).getBirdName());
        mBirdMarketScene.attachChild(birdNameText);


        birdNameText.setX(RectangularShape.Side.CENTER, (SCREEN_WIDTH / 2));


        final TiledSprite menuButton = new TiledSprite(menuButtonX, menuButtonY, mResourceManager.mMenuButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    setCurrentTileIndex(1);
                    mResourceManager.mButtonSound.play();

                }
                if (pSceneTouchEvent.isActionUp()) {
                    setCurrentTileIndex(0);

                    mSceneManager.setScene(SceneManager.SceneType.SCENE_MENU);
                }
                return true;
            }
        };
        menuButton.setCurrentTileIndex(0);
        menuButton.setScale(0.75f);
        mBirdMarketScene.registerTouchArea(menuButton);
        mBirdMarketScene.attachChild(menuButton);


        final TiledSprite birdsSprite = new TiledSprite(0, 0, mResourceManager.mMarketBirdsTextureRegion, mVertexBufferObjectManager);
        birdsSprite.setCurrentTileIndex(myBirds.get(currentBird).getBirdNum());
        birdsSprite.setX(RectangularShape.Side.CENTER, SCREEN_WIDTH / 2);
        birdsSprite.setY(RectangularShape.Side.CENTER, birdNameText.getY(RectangularShape.Side.BOTTOM) + mResourceManager.mMarketBirdsTextureRegion.getHeight() / 2);
        mBirdMarketScene.attachChild(birdsSprite);


        final TiledSprite selectButton = new TiledSprite(0, 0, mResourceManager.mSelectButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (isVisible() && getCurrentTileIndex() == 0) {
                    if (pSceneTouchEvent.isActionDown()) {
                        setCurrentTileIndex(1);
                        mActivity.setSelectedBird(myBirds.get(currentBird).getBirdNum());
                        mResourceManager.mButtonSound.play();

                    }
                }
                return true;
            }
        };
        selectButton.setCurrentTileIndex(1);
        selectButton.setScale(0.75f);
        selectButton.setX(RectangularShape.Side.CENTER, SCREEN_WIDTH / 2);
        selectButton.setY(RectangularShape.Side.TOP, birdsSprite.getY(RectangularShape.Side.BOTTOM));
        mBirdMarketScene.registerTouchArea(selectButton);
        mBirdMarketScene.attachChild(selectButton);


        final TiledSprite arrowLeftButton = new TiledSprite(arrowLeftX, 0, mResourceManager.mArrowLeftButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(isVisible()) {
                    if (pSceneTouchEvent.isActionDown()) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();

                    }

                    if (pSceneTouchEvent.isActionUp()) {
                        setCurrentTileIndex(0);
                        if (currentBird != 0) {
                            currentBird--;
                        } else {
                            currentBird = numBirds - 1;
                        }
                        birdsSprite.setCurrentTileIndex(myBirds.get(currentBird).getBirdNum());

                        if (myBirds.get(currentBird).getBirdNum() == mActivity.getSelectedBird()) {
                            selectButton.setCurrentTileIndex(1);
                        } else {
                            selectButton.setCurrentTileIndex(0);
                        }

                        birdNameText.setText(myBirds.get(currentBird).getBirdName());

                        birdNameText.setX(Side.CENTER, (SCREEN_WIDTH / 2));
                    }
                }
                return true;
            }
        };
        arrowLeftButton.setCurrentTileIndex(0);
        arrowLeftButton.setScale(0.75f);
        arrowLeftButton.setY(RectangularShape.Side.CENTER, birdsSprite.getY(RectangularShape.Side.CENTER));
        mBirdMarketScene.registerTouchArea(arrowLeftButton);
        mBirdMarketScene.attachChild(arrowLeftButton);

        final TiledSprite arrowRightButton = new TiledSprite(arrowRightX, 0, mResourceManager.mArrowRightButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(isVisible()) {
                    if (pSceneTouchEvent.isActionDown()) {
                        setCurrentTileIndex(1);
                        mResourceManager.mButtonSound.play();
                    }

                    if (pSceneTouchEvent.isActionUp()) {
                        setCurrentTileIndex(0);
                        if (currentBird != numBirds - 1) {
                            currentBird++;
                        } else {
                            currentBird = 0;
                        }
                        birdsSprite.setCurrentTileIndex(myBirds.get(currentBird).getBirdNum());

                        if (myBirds.get(currentBird).getBirdNum() == mActivity.getSelectedBird()) {
                            selectButton.setCurrentTileIndex(1);
                        } else {
                            selectButton.setCurrentTileIndex(0);
                        }

                        birdNameText.setText(myBirds.get(currentBird).getBirdName());

                        birdNameText.setX(Side.CENTER, (SCREEN_WIDTH / 2));
                    }
                }
                return true;
            }
        };
        arrowRightButton.setCurrentTileIndex(0);
        arrowRightButton.setScale(0.75f);
        arrowRightButton.setY(RectangularShape.Side.CENTER, birdsSprite.getY(RectangularShape.Side.CENTER));
        mBirdMarketScene.registerTouchArea(arrowRightButton);
        mBirdMarketScene.attachChild(arrowRightButton);

        final Sprite googlePlayIcon = new Sprite(0, 0, mResourceManager.mGooglePlayIconTextureRegion, mVertexBufferObjectManager);
        googlePlayIcon.setX(RectangularShape.Side.CENTER, boardX + popUpBoard.getWidth() / 8);
        googlePlayIcon.setY(selectButton.getY(RectangularShape.Side.BOTTOM));
        googlePlayIcon.setScale(0.5f);
        mBirdMarketScene.attachChild(googlePlayIcon);

        final Text achievementText = (new Text(0, 0, mResourceManager.mFont6, mActivity.getString(R.string.complete_achievements_to_unlock_more_birds), new TextOptions(HorizontalAlign.CENTER), mVertexBufferObjectManager));
        achievementText.setX(RectangularShape.Side.CENTER, SCREEN_WIDTH / 2);
        achievementText.setY(googlePlayIcon.getY());
        mBirdMarketScene.attachChild(achievementText);

        final TiledSprite achievementsButton = new TiledSprite(0, 0, mResourceManager.mAchievementsButtonTextureRegion, mVertexBufferObjectManager) {

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
        achievementsButton.setScale(0.75f);
        achievementsButton.setX(RectangularShape.Side.CENTER, SCREEN_WIDTH / 2);
        achievementsButton.setY(achievementText.getY(RectangularShape.Side.BOTTOM));
        mBirdMarketScene.registerTouchArea(achievementsButton);
        mBirdMarketScene.attachChild(achievementsButton);



        if(numBirds == 1) {
            arrowLeftButton.setVisible(false);
            arrowRightButton.setVisible(false);
        }

        registerUpdateHandler(new IUpdateHandler() {

            @Override
            public void reset() {
            }

            @Override
            public void onUpdate(float pSecondsElapsed) {
                syncNewBirds();
            }
        });

    }


    @Override
    public void onBackKeyPressed() {
        mSceneManager.setScene(SceneManager.SceneType.SCENE_MENU);

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_MARKET;
    }

    @Override
    public void disposeScene() {
        //TODO
    }

    /**
     * Called after getting unlocked achievements
     */
    public void syncNewBirds() {
        for(int i = 1; i < mActivity.marketBirds.length; i++) {
            MarketBird updatedBird = mActivity.marketBirds[i];
            if(updatedBird.isUnlocked()) {
                boolean newBird = true;
                for(int j = 1; j < myBirds.size(); j++) {
                    MarketBird bird = myBirds.get(j);
                    if(updatedBird.equals(bird)) {
                        newBird = false;
                        break;
                    }
                }
                if(newBird) {
                    myBirds.add(updatedBird);
                }
            }
        }
        numBirds = myBirds.size();
    }

}
