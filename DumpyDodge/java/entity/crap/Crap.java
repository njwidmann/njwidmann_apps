package com.application.nick.dumpydodge.entity.crap;

import com.application.nick.dumpydodge.entity.GameEntity;
import com.application.nick.dumpydodge.entity.powerup.Umbrella;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 4/5/2015.
 */
public class Crap extends GameEntity {

    public enum crapType {NORMAL, MEGA}

    private boolean falling = true, fromFirstBird = false, bouncingOffUmbrella = false;

    private int currentBirdIndex, numUmbrellaBounces = 0;

    public Crap(TiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0, 0, pTiledTextureRegion, pVertexBufferObjectManager);

        currentBirdIndex = 0;
    }


    @Override
    public void reset() {
        super.reset();
        falling = true;
        setAcceleration(0, GRAVITY);
        setPosition(0, 0);
        setCurrentBirdIndex(0);
        fromFirstBird = false;
        bouncingOffUmbrella = false;
        numUmbrellaBounces = 0;
    }


    @Override
    public void setSlowMotionActivated(boolean bool) {
        if(bool) {
            super.setSlowMotionActivated(true);
            if(currentBirdIndex == NINJA_BIRD_INDEX && falling) { //if ninja bird, spin throwing star crap
                setAngularVelocity(NINJA_CRAP_ANGULAR_VELOCITY / 2);
            }
        } else {
            super.setSlowMotionActivated(false);
            if(currentBirdIndex == NINJA_BIRD_INDEX && falling) { //if ninja bird, spin throwing star crap
                setAngularVelocity(NINJA_CRAP_ANGULAR_VELOCITY);
            }
        }
    }

    public void setCurrentBirdIndex(int currentBirdIndex) {
        setCurrentTileIndex(2 * currentBirdIndex);
        this.currentBirdIndex = currentBirdIndex;

        if(currentBirdIndex == NINJA_BIRD_INDEX) { //if ninja bird, spin throwing star crap
            setAngularVelocity(NINJA_CRAP_ANGULAR_VELOCITY);
        }
    }

    public int getCurrentBirdIndex() {
        return currentBirdIndex;
    }

    /**
     * stops crap from falling.
     * splats crap
     */
    public void hitsGround() {
        falling = false;
        setCurrentTileIndex(currentBirdIndex * 2 + 1);
        setAcceleration(0, 0);
        setAngularVelocity(0);
        setRotation(0);
        setVelocityY(0);
        Direction direction = getPersonMovingDirection();
        scroll(Direction.STOPPED);
        setVelocityX(0);
        scroll(direction);
    }

    public void bounceOffUmbrella(Umbrella umbrella) {
        bouncingOffUmbrella = true;
        //handle physics to determine angle of bounce off umbrella
        double velocity = Math.abs(getVelocityY());
        double x = Math.abs(getX(Side.CENTER) - umbrella.getX(Side.CENTER)); //x is distance from center of crap to center of umbrella
        double d = umbrella.getWidth() / 2 + getWidth() / 2; //d is max value of x
        double theta = -Math.PI / 2.0 * (x / d - 1.0); //angle of bounce

        velocity -= umbrella.getVelocityY();
        setVelocityY(-(float)(velocity * Math.sin(theta)));

        if(getX(Side.CENTER) < umbrella.getX(Side.CENTER)) {
            setVelocityX((float)(getVelocityX() - velocity * Math.cos(theta)));
        } else {
            setVelocityX((float)(getVelocityX() + velocity * Math.cos(theta)));
        }
        numUmbrellaBounces++;
    }

    public boolean isBouncingOffUmbrella() {
        return bouncingOffUmbrella;
    }

    public void setBouncingOffUmbrella(boolean bouncingOffUmbrella) {
        this.bouncingOffUmbrella = bouncingOffUmbrella;
    }

    public int getNumUmbrellaBounces() {
        return numUmbrellaBounces;
    }

    /**
     * @return whether or not the crap is still falling
     */
    public boolean getFalling() {return falling;}


    public boolean getFromFirstBird() {
        return fromFirstBird;
    }

    public void setFromFirstBird(boolean fromFirstBird) {
        this.fromFirstBird = fromFirstBird;
    }

    public crapType getCrapType() {return crapType.NORMAL;}

    public Entities getEntityType() {return Entities.CRAP;}

}
