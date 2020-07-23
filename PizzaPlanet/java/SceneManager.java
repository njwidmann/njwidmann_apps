package com.application.nick.pizzaplanet;

import com.application.nick.pizzaplanet.scene.BaseScene;
import com.application.nick.pizzaplanet.scene.GameScene;
import com.application.nick.pizzaplanet.scene.GameSceneCoast;
import com.application.nick.pizzaplanet.scene.GameSceneHome;
import com.application.nick.pizzaplanet.scene.GameSceneIsland;
import com.application.nick.pizzaplanet.scene.GameScenePlanet;
import com.application.nick.pizzaplanet.scene.GameSceneWarehouse;
import com.application.nick.pizzaplanet.scene.MarketScene;
import com.application.nick.pizzaplanet.scene.SplashScene;

/**
 * Created by Nick on 4/5/2015.
 */
public class SceneManager {

    private static final SceneManager INSTANCE = new SceneManager();

    public enum SceneType {SCENE_SPLASH, SCENE_GAME, SCENE_LEADERBOARD, SCENE_MARKET }

    public enum GameSceneType {HOME, WAREHOUSE, COAST, ISLAND, PLANET}

    private BaseScene mSplashScene;
    private BaseScene mMarketScene;
    private BaseScene mGameScene;

    private SceneType mCurrentSceneType;
    private BaseScene mCurrentScene;

    private SceneManager() {}

    public static SceneManager getInstance() {
        return INSTANCE;
    }

    public void setScene(SceneType sceneType) {
        switch (sceneType) {
            case SCENE_SPLASH:
                setScene(createSplashScene());
                break;
        }
        switch (sceneType) {
            case SCENE_MARKET:
                setScene(createMarketScene());
                break;
        }
    }

    public void setGameScene(GameSceneType type) {
        setScene(createGameScene(type));
    }



    private void setScene(BaseScene scene) {
        ResourceManager.getInstance().mActivity.getEngine().setScene(scene);
        mCurrentScene = scene;
        mCurrentSceneType = scene.getSceneType();
    }

    public SceneType getCurrentSceneType() {
        return mCurrentSceneType;
    }

    public BaseScene getCurrentScene() {
        return mCurrentScene;
    }

    public BaseScene createSplashScene() {
        mSplashScene = new SplashScene();
        return mSplashScene;
    }

    public BaseScene createMarketScene() {
        mMarketScene = new MarketScene();
        return mMarketScene;
    }


    private BaseScene createGameScene(GameSceneType type) {
        switch(type) {
            case HOME:
                mGameScene = new GameSceneHome();
                break;
            case WAREHOUSE:
                mGameScene = new GameSceneWarehouse();
                break;
            case COAST:
                mGameScene = new GameSceneCoast();
                break;
            case ISLAND:
                mGameScene = new GameSceneIsland();
                break;
            case PLANET:
                mGameScene = new GameScenePlanet();
                break;
        }
        return mGameScene;
    }

}
