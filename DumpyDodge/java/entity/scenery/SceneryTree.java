package com.application.nick.dumpydodge.entity.scenery;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 7/30/2015.
 */
public class SceneryTree extends Scenery {

    public SceneryTree(float x, float y, TiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(x, y, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public SceneryType getSceneryType() {
        return SceneryType.TREE;
    }
}
