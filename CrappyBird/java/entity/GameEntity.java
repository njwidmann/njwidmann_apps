package com.application.nick.crappybird.entity;

import com.application.nick.crappybird.GameValues;
import com.application.nick.crappybird.scene.GameScene;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 7/29/2015.
 */
public abstract class GameEntity extends AnimatedSprite implements GameValues {

    private final PhysicsHandler mPhysicsHandler;


    boolean slowMotionActivated = false, hyperSpeedActivated = false;

    public GameEntity(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

        mPhysicsHandler = new PhysicsHandler(this);
        registerUpdateHandler(mPhysicsHandler);
    }

    @Override
    public void reset() {
        slowMotionActivated = false;
        hyperSpeedActivated = false;
        setVelocity(0, 0);
        setAcceleration(0, 0);
        setRotation(0);
        setAngularVelocity(0);
    }

    @Override
    public boolean collidesWith(IShape pOtherShape) {

        Sprite sprite = (Sprite) pOtherShape;

        float spriteLeft = sprite.getX();
        float spriteRight = spriteLeft + sprite.getWidth();
        float spriteTop = sprite.getY();
        float spriteBottom = spriteTop + sprite.getHeight();
        float left = this.getX();
        float right = left + this.getWidth();
        float top = this.getY();
        float bottom = top + this.getHeight();

        if ((spriteRight > left && spriteLeft < right) && (spriteTop < bottom && spriteBottom > top)) {
            return true;
        }
        return false;
    }


    public void setVelocity(float x, float y) {mPhysicsHandler.setVelocity(x, y);}

    public void setVelocityX(float x) {mPhysicsHandler.setVelocityX(x);}

    public void setVelocityY(float y) {mPhysicsHandler.setVelocityY(y);}

    public void setAcceleration(float x, float y) {mPhysicsHandler.setAcceleration(x, y);}

    public void setAccelerationY(float y) {mPhysicsHandler.setAccelerationY(y);}

    public void setAccelerationX(float x) {mPhysicsHandler.setAccelerationX(x);}

    public void setAngularVelocity(float w) { mPhysicsHandler.setAngularVelocity(w);}


    public float getVelocityX() {return mPhysicsHandler.getVelocityX();}

    public float getVelocityY() {return mPhysicsHandler.getVelocityY();}

    public float getAccelerationX() {return mPhysicsHandler.getAccelerationX();}

    public float getAccelerationY() {return mPhysicsHandler.getAccelerationY();}

    public void die() {unregisterUpdateHandler(mPhysicsHandler);}

    public void alive() {
        registerUpdateHandler(mPhysicsHandler);
    }


    public void setSlowMotionActivated(boolean bool) {
        if(bool) {
            slowMotionActivated = true;
            setVelocityX(getVelocityX() / 2); // cut relative x velocity in half
            setVelocityY(getVelocityY() / 2);
            setAccelerationY(getAccelerationY() / 4);
        } else {
            slowMotionActivated = false;
            setVelocityX(getVelocityX() * 2); // double relative x velocity
            setVelocityY(getVelocityY() * 2);
            setAccelerationY(getAccelerationY() * 4);
        }
    }

    public boolean isSlowMotionActivated() {
        return slowMotionActivated;
    }

    public void setHyperSpeedActivated(boolean bool) {
        if(bool) {
            if(!hyperSpeedActivated) {
                hyperSpeedActivated = true;
                setVelocityX(getVelocityX() - GameScene.HYPER_SPEED_VELOCITY_SHIFT);
            }
        } else {
            if(hyperSpeedActivated) {
                hyperSpeedActivated = false;
                setVelocityX(getVelocityX() + GameScene.HYPER_SPEED_VELOCITY_SHIFT);
            }
        }
    }

    public boolean isHyperSpeedActivated() {
        return hyperSpeedActivated;
    }

    public abstract Entities getEntityType();


}
