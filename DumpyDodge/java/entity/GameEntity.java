package com.application.nick.dumpydodge.entity;

import com.application.nick.dumpydodge.GameValues;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 7/29/2015.
 */
public abstract class GameEntity extends AnimatedSprite implements GameValues {

    private final PhysicsHandler mPhysicsHandler;

    Direction personMovingDirection = Direction.STOPPED;

    boolean slowMotionActivated = false;

    public GameEntity(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

        mPhysicsHandler = new PhysicsHandler(this);
        registerUpdateHandler(mPhysicsHandler);
    }

    @Override
    public void reset() {
        slowMotionActivated = false;
        setVelocity(0, 0);
        personMovingDirection = Direction.STOPPED;
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

    public Direction getPersonMovingDirection() {
        return personMovingDirection;
    }

    public void setPersonMovingDirection(Direction direction) {personMovingDirection = direction;}

    public void die() {unregisterUpdateHandler(mPhysicsHandler);}

    public void alive() {
        registerUpdateHandler(mPhysicsHandler);
    }


    public void setSlowMotionActivated(boolean bool) {
        if(bool) {
            slowMotionActivated = true;
            Direction direction = personMovingDirection;
            scroll(Direction.STOPPED);
            setVelocityX(getVelocityX() / 2); // cut relative x velocity in half
            scroll(direction);
            setVelocityY(getVelocityY() / 2);
            setAccelerationY(getAccelerationY() / 4);
        } else {
            slowMotionActivated = false;
            Direction direction = personMovingDirection;
            scroll(Direction.STOPPED);
            setVelocityX(getVelocityX() * 2); // double relative x velocity
            scroll(direction);
            setVelocityY(getVelocityY() * 2);
            setAccelerationY(getAccelerationY() * 4);
        }
    }

    public boolean isSlowMotionActivated() {
        return slowMotionActivated;
    }

    /**
     * for moving the entity with the scrolling background when person moves
     * @param direction the direction the person is moving
     */
    public void scroll(Direction direction) {
        if(direction == Direction.LEFT) {
            if(personMovingDirection == Direction.RIGHT) {
                setVelocityX(getVelocityX() + PERSON_VELOCITY_X * 2);
            } else if (personMovingDirection == Direction.STOPPED) {
                setVelocityX(getVelocityX() + PERSON_VELOCITY_X);
            }
            personMovingDirection = Direction.LEFT;
        } else if(direction == Direction.RIGHT) {
            if(personMovingDirection == Direction.LEFT) {
                setVelocityX(getVelocityX() - PERSON_VELOCITY_X * 2);
            } else if (personMovingDirection == Direction.STOPPED) {
                setVelocityX(getVelocityX() - PERSON_VELOCITY_X);
            }
            personMovingDirection = Direction.RIGHT;
        } else if(direction == Direction.STOPPED) {
            if(personMovingDirection == Direction.RIGHT) {
                setVelocityX(getVelocityX() + PERSON_VELOCITY_X);
            } else if(personMovingDirection == Direction.LEFT) {
                setVelocityX(getVelocityX() - PERSON_VELOCITY_X);
            }
            personMovingDirection = Direction.STOPPED;
        }

    }

    public abstract Entities getEntityType();


}
