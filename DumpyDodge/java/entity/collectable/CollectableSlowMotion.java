package com.application.nick.dumpydodge.entity.collectable;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 4/9/2015.
 */
public class CollectableSlowMotion extends Collectable {


    public CollectableSlowMotion(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public Collectable.collectableType getCollectableType() {return collectableType.SLOW_MOTION;}

}
