package com.application.nick.dumpydodge.entity.scenery;

import com.application.nick.dumpydodge.entity.GameEntity;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 7/29/2015.
 */
public abstract class Scenery extends GameEntity {



    public Scenery(float x, float y, TiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(x, y, pTiledTextureRegion, pVertexBufferObjectManager);

    }

    public Entities getEntityType() {
        return Entities.SCENERY;
    }

    public abstract SceneryType getSceneryType();
}
