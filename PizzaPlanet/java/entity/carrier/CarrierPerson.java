package com.application.nick.pizzaplanet.entity.carrier;

import com.application.nick.pizzaplanet.GameValues;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 4/5/2015.
 */
public class CarrierPerson extends Carrier implements GameValues{

    private float mGroundY;
    private boolean inTruck = false, inWarehouse = false, inKitchen = false;


    public CarrierPerson(TiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, float pGroundY) {
        super(pTiledTextureRegion, pVertexBufferObjectManager);

        mGroundY = pGroundY;

        setY(pGroundY - pTiledTextureRegion.getHeight());

    }

    @Override
    public void setVelocityX(float x) {
        super.setVelocityX(x);

        setAnimationX();
    }

    @Override
    public void setVelocityY(float y) {
        super.setVelocityY(y);

        setAnimationY();
    }


    public void setAnimationX() {

        float velocity = Math.abs(getVelocityX());
        int fDur = (int)(5000 / velocity); //frame duration
        if(fDur < 50) {
            fDur = 50;
        } else if (fDur > 400) {
            fDur = 400;
        }

        if(this.getVelocityX() < 0) {
            this.animate(new long[]{fDur, fDur, fDur, fDur, fDur, fDur, fDur, fDur}, 16, 23, true); //walk facing left
        } else if(this.getVelocityX() > 0) {
            this.animate(new long[]{fDur, fDur, fDur, fDur, fDur, fDur, fDur, fDur}, 24, 31, true); //walk facing right
        } else if(this.getVelocityX() == 0) {
            this.setCurrentTileIndex(0);
        }
    }


    public void setAnimationY() {

        float velocity = Math.abs(getVelocityY());
        int fDur = (int)(5000 / velocity); //frame duration
        if(fDur < 50) {
            fDur = 50;
        } else if (fDur > 400) {
            fDur = 400;
        }

        if(this.getVelocityY() < 0) {
            this.animate(new long[]{fDur, fDur, fDur, fDur, fDur, fDur, fDur, fDur}, 8, 15, true); //walk facing top
        } else if(this.getVelocityY() > 0) {
            this.animate(new long[]{fDur, fDur, fDur, fDur, fDur, fDur, fDur, fDur}, 0, 7, true); //walk facing bottom
        } else if(this.getVelocityY() == 0) {
            this.setCurrentTileIndex(0);
        }
    }

    public void enterTruck() {
        inTruck = true;
        setVisible(false);
        setVelocity(0,0);
    }

    public void exitTruck() {
        inTruck = false;
        setVisible(true);
        setVelocityY(-PERSON_VELOCITY_Y);
    }

    public void enterWarehouse() {
        inWarehouse = true;
        setVisible(false);
        setVelocity(0, 0);
    }

    public void exitWarehouse() {
        inWarehouse = false;
        setVisible(true);
        setVelocityY(PERSON_VELOCITY_Y);
    }

    public void enterKitchen() {
        inKitchen = true;
        setVisible(false);
        setVelocity(0, 0);
    }

    public void exitKitchen() {
        inKitchen = false;
        setVisible(true);
        setVelocityY(PERSON_VELOCITY_Y);
    }

    public boolean getInKitchen() {
        return inKitchen;
    }

    public boolean getInWarehouse() {
        return inWarehouse;
    }

    public boolean getInTruck() {
        return inTruck;
    }

    @Override
    public void reset() {
        super.reset();
        setY(mGroundY - getHeight());
        inTruck = false;
    }


    @Override
    public carrierType getCarrierType() {return carrierType.PERSON;}

}
