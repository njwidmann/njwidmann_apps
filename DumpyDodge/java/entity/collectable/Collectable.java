package com.application.nick.dumpydodge.entity.collectable;

import com.application.nick.dumpydodge.entity.GameEntity;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 4/5/2015.
 */
public abstract class Collectable extends GameEntity {


    boolean collected = false, falling = true;

    public Collectable(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        setAngularVelocity(COLLECTABLE_ANGULAR_VELOCITY);
        setAccelerationY(GRAVITY);
    }

    public enum collectableType {SLOW_MOTION, MOTHERSHIP, MACHINE_GUN, UMBRELLA}


    /*public void randomizeLocation(float leftBound, float rightBound) {
        rightBound -= getWidth();
        float rand = ((float)Math.random()) * (rightBound - leftBound);
        setX(rand);
    }*/

    @Override
    public void reset() {
        super.reset();
        collected = false;
        falling = true;
        setAcceleration(0, GRAVITY);
        setPosition(0, 0);
    }

    public void stopFalling() {
        falling = false;
        setAccelerationY(0);
        setVelocityY(0);
    }

    public boolean isFalling() {
        return falling;
    }

    /**
     * set collected true
     */
    public void collect() {collected = true;}

    /**
     * @return bool whether or not this has been collected
     */
    public boolean isCollected() {return collected;}

    public abstract collectableType getCollectableType();

    public Entities getEntityType() {
        return Entities.COLLECTABLE;
    }

}
