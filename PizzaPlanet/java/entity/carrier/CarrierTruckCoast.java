package com.application.nick.pizzaplanet.entity.carrier;

import com.application.nick.pizzaplanet.GameValues;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 4/5/2015.
 */
public class CarrierTruckCoast extends Carrier implements GameValues{

    public enum Orientation {UP, DOWN}

    boolean parkedOutsideFactory = true, parkedOutsideStorage = false, driving = false;

    public CarrierTruckCoast(TiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pTiledTextureRegion, pVertexBufferObjectManager);

        setOrientation(Orientation.UP);
        parkOutsideFactory();
    }

    @Override
    public carrierType getCarrierType() {return carrierType.TRUCK;}

    public void setOrientation(Orientation orientation) {
        if(orientation == Orientation.UP) {
            setCurrentTileIndex(1);
        } else {
            setCurrentTileIndex(0);
        }
    }

    private void drive() {
        driving = true;
    }

    public void drive(Orientation direction) {
        if(direction == Orientation.UP) {
            setVelocityY(-TRUCK_VELOCITY_Y);
            setOrientation(Orientation.UP);
            parkedOutsideStorage = false;
            drive();

        } else {
            setVelocityY(TRUCK_VELOCITY_Y);
            setOrientation(Orientation.DOWN);
            parkedOutsideFactory = false;
            drive();
        }
    }

    public void parkOutsideFactory() {
        setVelocity(0,0);
        parkedOutsideFactory = true;
    }

    public void parkOutsideStorage() {
        setVelocity(0,0);
        parkedOutsideStorage = true;
    }

    public boolean isParkedOutsideFactory() {
        return parkedOutsideFactory;
    }

    public boolean isParkedOutsideStorage() {
        return parkedOutsideStorage;
    }

    public boolean isDriving() {
        return driving;
    }
}
