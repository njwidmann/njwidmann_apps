package com.application.nick.pizzaplanet.entity.Purchases;

/**
 * Created by Nick on 6/18/2015.
 */
public class PizzaStorageIsland extends PizzaStorage {

    public PizzaStorageIsland(long pizzaCapacity, String pizzaCapacityString, long price) {
        super(pizzaCapacity, pizzaCapacityString, price);
    }

    @Override
    public int getSceneTileIndex(){
        return 1;
    }

    @Override
    public storageType getStorageType() {
        return storageType.ISLAND;
    }

}
