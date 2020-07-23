package com.application.nick.pizzaplanet.entity.Purchases;

import com.application.nick.pizzaplanet.GameValues;

/**
 * Created by Nick on 6/16/2015.
 */
public class PizzaStorageHome extends PizzaStorage implements GameValues {

    private int tileIndex, maxPizzaY, maxPizzaX, sceneTileIndex = 0;
    private float overX, x = HOME_BUILDING_X, y = HOME_BUILDING_Y; //how far to the right of the texture region to place the first pizza box

    public PizzaStorageHome(int tileIndex, long pizzaCapacity, String capacityString, int maxPizzaX, int maxPizzaY, float overX, long price) {
        super(pizzaCapacity, capacityString, price);

        this.tileIndex = tileIndex;
        this.maxPizzaX = maxPizzaX;
        this.maxPizzaY = maxPizzaY;
        this.overX = overX;

    }

    public int getSceneTileIndex() {
        return sceneTileIndex;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    public int getMaxPizzaY() {
        return maxPizzaY;
    }

    public int getMaxPizzaX() {
        return maxPizzaX;
    }

    public float getOverX() {
        return overX;
    }

    @Override
    public storageType getStorageType() {
        return storageType.HOME;
    }
}