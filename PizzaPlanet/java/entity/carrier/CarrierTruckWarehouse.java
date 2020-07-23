package com.application.nick.pizzaplanet.entity.carrier;

import com.application.nick.pizzaplanet.GameValues;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 4/5/2015.
 */
public class CarrierTruckWarehouse extends Carrier implements GameValues {

    public enum Orientation {RIGHT, LEFT}
    private Orientation orientation;

    private boolean hasPassenger = false, parkedOutsideWarehouse = false, parkedOutsideKitchen = true, driving = false;

    public CarrierTruckWarehouse(TiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, Orientation orientation) {
        super(pTiledTextureRegion, pVertexBufferObjectManager);

        setOrientation(orientation);

        this.orientation = orientation;

    }

    @Override
    public void setVelocityX(float x) {
        super.setVelocityX(x);

        if(getVelocityX() > 0) {
            setOrientation(Orientation.RIGHT);
        } else if(getVelocityX() < 0) {
            setOrientation(Orientation.LEFT);
        }
    }


    @Override
    public void reset() {
        super.reset();
    }


    @Override
    public carrierType getCarrierType() {return carrierType.TRUCK;}


    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        switch (orientation) {
            case RIGHT:
                setCurrentTileIndex(1);
                setY(WAREHOUSE_TRUCK_MOVING_RIGHT_Y);
                break;
            case LEFT:
                setCurrentTileIndex(0);
                setY(WAREHOUSE_TRUCK_MOVING_LEFT_Y);
                break;
        }

        this.orientation = orientation;
    }

    private void drive(Orientation direction) {
        setOrientation(direction);

        if(direction == Orientation.RIGHT) {
            setVelocityX(TRUCK_VELOCITY_X);
        } else {
            setVelocityX(-TRUCK_VELOCITY_X);
        }

        driving = true;
        hasPassenger = true;

    }

    public void driveToWarehouse(Orientation direction) {
        drive(direction);

        parkedOutsideKitchen = false;
    }

    public void driveToKitchen(Orientation direction) {
        drive(direction);

        parkedOutsideWarehouse = false;

    }

    private void park() {
        driving = false;
        hasPassenger = false;
        setVelocityX(0);
    }

    public void parkOutsideWarehouse() {
        park();
        parkedOutsideWarehouse = true;
    }

    public void parkOutsideKitchen() {
        park();
        parkedOutsideKitchen = true;
    }

    public boolean getParkedOutsideWarehouse() {
        return parkedOutsideWarehouse;
    }

    public void setParkedOutsideWarehouse(boolean parkedOutsideWarehouse) {
        this.parkedOutsideWarehouse = parkedOutsideWarehouse;
    }

    public boolean getParkedOutsideKitchen() {
        return parkedOutsideKitchen;
    }

    public void setParkedOutsideKitchen(boolean parkedOutsideKitchen) {
        this.parkedOutsideKitchen = parkedOutsideKitchen;
    }

    public boolean getDriving() {
        return driving;
    }

    public void setDriving(boolean driving) {
        this.driving = driving;
    }

    public boolean getHasPassenger() {
        return hasPassenger;
    }

    public void setHasPassenger(boolean hasPassenger) {
        this.hasPassenger = hasPassenger;
    }


}
