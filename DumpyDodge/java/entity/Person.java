package com.application.nick.dumpydodge.entity;

import com.application.nick.dumpydodge.GameValues;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 7/28/2015.
 */
public class Person extends GameEntity implements GameValues{

    Direction movingDirection = Direction.STOPPED;
    boolean jumping = false;

    public Person(float x, float y, TiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(x, y, pTiledTextureRegion, pVertexBufferObjectManager);

    }

    public void move(Direction direction) {
        long fDur = PERSON_FRAME_DURATION;
            if (direction == Direction.LEFT) {
                this.animate(new long[]{fDur, fDur, fDur, fDur, fDur, fDur, fDur, fDur}, 16, 23, true); //walk facing left
                movingDirection = Direction.LEFT;
            } else {
                this.animate(new long[]{fDur, fDur, fDur, fDur, fDur, fDur, fDur, fDur}, 24, 31, true); //walk facing right
                movingDirection = Direction.RIGHT;
            }
    }

    public void stop() {
        this.stopAnimation(0);
        movingDirection = Direction.STOPPED;
    }

    public void stopFacingDirection(Direction direction) {
        if(direction == Direction.LEFT) {
            this.stopAnimation(16);
        } else if(direction == Direction.RIGHT) {
            this.stopAnimation(24);
        }
        movingDirection = Direction.STOPPED;
    }

    public void jump() {
        setVelocityY(-PERSON_JUMP_VELOCITY);
        setAccelerationY(GRAVITY_PERSON);
        jumping = true;
    }

    public void hitGround() {
        setY(Side.BOTTOM, GROUND);
        setVelocityY(0);
        setAccelerationY(0);
        jumping = false;
    }

    public void hitWithCrap() {
        stop();
        setRotation(0);
        setY(Side.BOTTOM, GROUND);
        setCurrentTileIndex(7);
    }

    public void hitWithCrap(boolean inTheAir) {
        if(inTheAir) {
            long fDur = PERSON_FRAME_DURATION;
            setRotation(180);
            int rand = (int) (Math.random() * 2);
            if (rand == 0) {
                this.animate(new long[]{fDur, fDur, fDur, fDur, fDur, fDur, fDur, fDur}, 16, 23, true); //walk facing left
            } else if (rand == 1) {
                this.animate(new long[]{fDur, fDur, fDur, fDur, fDur, fDur, fDur, fDur}, 24, 31, true); //walk facing right
            }
        } else {
            hitWithCrap();
        }

    }

    public boolean getJumping() {
        return jumping;
    }

    public Direction getMovingDirection() {
        return movingDirection;
    }

    public Entities getEntityType() {
        return Entities.PERSON;
    }
}
