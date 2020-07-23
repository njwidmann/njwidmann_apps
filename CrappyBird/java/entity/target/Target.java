package com.application.nick.crappybird.entity.target;

import com.application.nick.crappybird.GameActivity;
import com.application.nick.crappybird.entity.GameEntity;
import com.application.nick.crappybird.scene.GameScene;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 4/5/2015.
 */
public abstract class Target extends GameEntity {

    private boolean passedAddXValue = false, hit = false;

    public enum targetType {PERSON1}

    public Target(TiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, float pGroundY) {
        super(PERSON_STARTING_POSITION, pGroundY - pTiledTextureRegion.getHeight(), pTiledTextureRegion, pVertexBufferObjectManager);

        setVelocity(-SCROLL_VELOCITY, 0);

        setZIndex(10); //put the targets in front of the obstacles and other sprites

        setCurrentTileIndex(0);

    }

    public void randomizeMovement() {
        int rand = (int) (Math.random() * 4);
        if (rand == 0) {
            setVelocity(-SCROLL_VELOCITY, 0);
            setCurrentTileIndex(0); //make target stationary
        } else {
            randomizeVelocity();
        }
    }

    public void randomizeVelocity() {
        float rand = (float) Math.random() - 0.5f;
        float deltaV = rand * PERSON_VELOCITY_RANGE;
        setVelocity(-(SCROLL_VELOCITY + deltaV), 0);
    }


    @Override
    public void reset() {
        super.reset();
        setX(PERSON_STARTING_POSITION);
        passedAddXValue = false;
        randomizeMovement();
        hit = false;
    }

    /**
     * sets passedAddXValue boolean to true
     */
    public void passedAddXValue() {
        passedAddXValue = true;
    }

    /**
     * gets value for passedAddXValue, the boolean that says whether this object has passed the x value that makes a new
     * obstacle get created (used so that too many obstacles are not created)
     * @return
     */
    public boolean getPassedAddXValue() {
        return passedAddXValue;
    }

    public boolean getHitValue() {return hit;}

    /**
     * called when person is hit with crap
     */
    public void hitWithCrap() {
        hit = true;
        if(isSlowMotionActivated()) {
            setVelocity(-SCROLL_VELOCITY / 2, 0);
        } else {
            setVelocity(-SCROLL_VELOCITY, 0);
        }
    }

    /**
     * overloaded method handles setting velocity to 0 if gameover (scrolling stops)
     * @param gameOver true = gameover
     */
    public void hitWithCrap(boolean gameOver) {
        if(gameOver) {
            hit = true;
            setVelocity(0,0);
        } else {
            hitWithCrap();
        }
    }


    public abstract targetType getTargetType();

    public Entities getEntityType() {
        return Entities.PERSON;
    }

}
