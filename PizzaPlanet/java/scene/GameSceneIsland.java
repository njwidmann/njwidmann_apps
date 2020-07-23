package com.application.nick.pizzaplanet.scene;

import com.application.nick.pizzaplanet.SceneManager;

import org.andengine.entity.sprite.TiledSprite;

/**
 * Created by Nick on 4/5/2015.
 */
public class GameSceneIsland extends GameScene {

    @Override
    public void createScene() {
        super.createScene();


        final TiledSprite island = new TiledSprite(ISLAND_X, ISLAND_Y, mResourceManager.mIslandsTextureRegion, mVertexBufferObjectManager);
        island.setCurrentTileIndex(mActivity.getStorageNumber());
        attachChild(island);
    }

    @Override
    public SceneManager.GameSceneType getGameSceneType() {
        return SceneManager.GameSceneType.ISLAND;
    }


}
