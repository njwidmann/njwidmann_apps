package com.application.nick.dumpydodge.entity;

import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 4/5/2015.
 */
public class Bird extends GameEntity {

    Direction movingDirection;

    int mCurrentBirdIndex;
    float lowerHeightLimit;

    boolean megaCrapActivated, machineCrapActivated, machineCrapDone = false, firstBird = false, blastedOff = false;

    float lastMachineCrapTime = 0;
    int numMachineCrapsDropped = 0, maxMachineCrapsToDrop;

    public Bird(float x, float y, TiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(x, y, pTiledTextureRegion, pVertexBufferObjectManager);

        setAcceleration(0, GRAVITY);

        mCurrentBirdIndex = 0;
        animate(mCurrentBirdIndex);
        randomizeLowerHeightLimit();

        megaCrapActivated = false;

        setPersonMovingDirection(Direction.STOPPED);
    }

    @Override
    public void reset() {
        super.reset();
        setAccelerationY(GRAVITY);
        setAngularVelocity(0);
        randomizeLowerHeightLimit();
        animate(mCurrentBirdIndex);
        megaCrapActivated = false;
        machineCrapActivated = false;
        machineCrapDone = false;
        numMachineCrapsDropped = 0;
        firstBird = false;
        blastedOff = false;
    }

    public void randomizeMaxMachineCrapsToDrop() {
        int rand = (int)(Math.random() * (MAX_MACHINE_CRAPS_IN_A_ROW - MIN_MACHINE_CRAPS_IN_A_ROW + 1));
        maxMachineCrapsToDrop = MIN_MACHINE_CRAPS_IN_A_ROW + rand;
    }

    private void randomizeLowerHeightLimit() {
        lowerHeightLimit = (float)Math.random() * (SCREEN_HEIGHT / 2);
        setY(lowerHeightLimit);
    }

    public void randomizeCurrentBirdIndex() {
        mCurrentBirdIndex = (int)(Math.random() * NUMBIRDS);
        animate(mCurrentBirdIndex);
    }

    /**
     * randomely chooses a bird with index less than or equal to maxIndex
     * @param maxIndex the max index of the birds to choose from
     */
    public void randomizeCurrentBirdIndex(int maxIndex) {
        if(maxIndex > NUMBIRDS) {
            randomizeCurrentBirdIndex();
        } else {
            mCurrentBirdIndex = (int) (Math.random() * maxIndex);
            animate(mCurrentBirdIndex);
        }
    }

    public int getCurrentBirdIndex() {
        return mCurrentBirdIndex;
    }

    public Direction getMovingDirection() {return movingDirection;}

    public void setMovingDirection(Direction direction) {movingDirection = direction;}

    public void animate(int selectedBird) {
        long fDur = BIRD_FRAME_DURATION;
        this.animate(new long[]{fDur, fDur, fDur}, selectedBird * 3, selectedBird * 3 + 2, true);

    }

    public float getLowerHeightLimit() {
        return lowerHeightLimit;
    }

    public void jump() {
        if(getVelocityY() > 0) {
            setVelocityY(-BIRD_JUMP_VELOCITY);
        }
    }

    public void jump(boolean megaCrapActivated) {
        if(!isSlowMotionActivated()) {
            float currentYVelocity = getVelocityY();
            if (currentYVelocity < 0) {
                if (!megaCrapActivated) {
                    setVelocityY(currentYVelocity - (BIRD_JUMP_VELOCITY * 4 + currentYVelocity) * (1.0f / 4.0f));
                } else {
                    setVelocityY(currentYVelocity - (BIRD_JUMP_VELOCITY * 4 + currentYVelocity) * (1.0f / 2.0f));
                }
            } else {
                if (!megaCrapActivated) {
                    jump();
                } else {
                    setVelocityY(-BIRD_JUMP_VELOCITY * 2);
                }
            }
        } else {
            float currentYVelocity = getVelocityY();
            if (currentYVelocity < 0) {
                if (!megaCrapActivated) {
                    setVelocityY(currentYVelocity - (BIRD_JUMP_VELOCITY * 4 + currentYVelocity) * (1.0f / 8.0f));
                } else {
                    setVelocityY(currentYVelocity - (BIRD_JUMP_VELOCITY * 4 + currentYVelocity) * (1.0f / 4.0f));
                }
            } else {
                if (!megaCrapActivated) {
                    setVelocityY(-BIRD_JUMP_VELOCITY / 2);
                } else {
                    setVelocityY(-BIRD_JUMP_VELOCITY);
                }
            }
        }
        if(movingDirection == Direction.RIGHT) {
            setRotation(-15);
        } else if (movingDirection == Direction.LEFT){
            setRotation(15);
        }
    }

    public boolean getMachineCrapActivated() {
        return machineCrapActivated;
    }

    public void setMachineCrapActivated(boolean machineCrapActivated) {
        this.machineCrapActivated = machineCrapActivated;
    }

    public boolean getMachineCrapDone() {
        return machineCrapDone;
    }

    public void setMachineCrapDone(boolean machineCrapDone) {
        this.machineCrapDone = machineCrapDone;
    }

    public float getLastMachineCrapTime() {
        return lastMachineCrapTime;
    }

    public void setLastMachineCrapTime(float lastMachineCrapTime) {
        this.lastMachineCrapTime = lastMachineCrapTime;
    }

    public int getNumMachineCrapsDropped() {
        return numMachineCrapsDropped;
    }

    public void setNumMachineCrapsDropped(int numMachineCrapsDropped) {
        this.numMachineCrapsDropped = numMachineCrapsDropped;
    }

    public int getMaxMachineCrapsToDrop() {
        return maxMachineCrapsToDrop;
    }

    public void jumpMachineCrap(float timeAlive) {
        jump(megaCrapActivated);
        lastMachineCrapTime = timeAlive;
        numMachineCrapsDropped++;
        machineCrapDone = false;
    }

    public void finishMachineCrapping() {
        machineCrapDone = true;
        numMachineCrapsDropped = 0;
        randomizeMaxMachineCrapsToDrop();
    }

    public void blastOff(float velocity) {
        blastedOff = true;
        setVelocityY(velocity);
        setAngularVelocity(BIRD_BLAST_OFF_ANGULAR_VELOCITY);
    }

    public boolean isBlastedOff() {
        return blastedOff;
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

    public boolean getMegaCrapActivated() {
        return megaCrapActivated;
    }

    public void setMegaCrapActivated(boolean megaCrapActivated) {
        this.megaCrapActivated = megaCrapActivated;
    }

    /**
     * whether or not this is the first bird in the game
     * @return
     */
    public boolean getFirstBird() {
        return firstBird;
    }

    public void setFirstBird(boolean firstBird) {
        this.firstBird = firstBird;
    }

    public Entities getEntityType() {
        return Entities.BIRD;
    }




}
