package com.application.nick.pizzaplanet.scene;

import com.application.nick.pizzaplanet.SceneManager;

import org.andengine.entity.sprite.TiledSprite;

/**
 * Created by Nick on 4/5/2015.
 */
public class GameScenePlanet extends GameScene {

    @Override
    public void createScene() {
        super.createScene();

        final TiledSprite planet = new TiledSprite(PLANET_X, PLANET_Y, mResourceManager.mPlanetsTextureRegion, mVertexBufferObjectManager);
        planet.setCurrentTileIndex(mActivity.getStorageNumber());
        attachChild(planet);

    }

    @Override
    public SceneManager.GameSceneType getGameSceneType() {
        return SceneManager.GameSceneType.PLANET;
    }


}
