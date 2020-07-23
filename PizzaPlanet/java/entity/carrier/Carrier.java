package com.application.nick.pizzaplanet.entity.carrier;

import com.application.nick.pizzaplanet.GameActivity;
import com.application.nick.pizzaplanet.scene.GameScene;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 4/5/2015.
 */
public abstract class Carrier extends AnimatedSprite {

    private float mGroundY;

    public enum carrierType {PERSON, PLANE, TRUCK}

    private final PhysicsHandler mPhysicsHandler;

    private int pizzaBoxesPickedUp;

    public Carrier(TiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(-pTiledTextureRegion.getWidth(), -pTiledTextureRegion.getHeight(), pTiledTextureRegion, pVertexBufferObjectManager);

        mPhysicsHandler = new PhysicsHandler(this);
        registerUpdateHandler(mPhysicsHandler);

        setVelocity(0, 0);
    }


    public void setVelocity(float x, float y) {
        mPhysicsHandler.setVelocity(x, y);
    }

    public void setVelocityX(float x) {mPhysicsHandler.setVelocityX(x);}

    public void setVelocityY(float y) {mPhysicsHandler.setVelocityY(y);}

    public float getVelocityX() {return mPhysicsHandler.getVelocityX();}

    public float getVelocityY() {return mPhysicsHandler.getVelocityY();}

    public void setAcceleration(float x, float y) {
        mPhysicsHandler.setAcceleration(x, y);
    }

    public void setAccelerationY(float y) {mPhysicsHandler.setAccelerationY(y);}

    public void die() {
        unregisterUpdateHandler(mPhysicsHandler);
    }

    public void alive() {
        registerUpdateHandler(mPhysicsHandler);
    }

    public int getPizzaBoxesPickedUp() {
        return pizzaBoxesPickedUp;
    }

    public void setPizzaBoxesPickedUp(int pizzaBoxesPickedUp) {
        this.pizzaBoxesPickedUp = pizzaBoxesPickedUp;
    }

    @Override
    public void reset() {
        super.reset();
        setX(-getWidth());
        setVelocityX(0);
        pizzaBoxesPickedUp = 0;
    }

    public abstract carrierType getCarrierType();

}
