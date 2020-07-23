package com.application.nick.dumpydodge.entity.powerup;

import com.application.nick.dumpydodge.entity.GameEntity;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 8/13/2015.
 */
public class MotherShip extends GameEntity {


    boolean flyingBy = false;

    public MotherShip(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void fly() {
        flyingBy = true;
        setVelocityX(-MOTHERSHIP_VELOCITY_X);
    }

    public void reset() {
        flyingBy = false;
        setVelocityX(0);
    }

    public boolean isFlyingBy() {
        return flyingBy;
    }


    @Override
    public Entities getEntityType() {
        return Entities.MOTHERSHIP;
    }
}
