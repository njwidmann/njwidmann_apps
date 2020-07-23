package com.application.nick.pizzaplanet.entity.Purchases;

/**
 * Created by Nick on 6/18/2015.
 */
public class PizzaStorageCoast extends PizzaStorage {

    private int numWindows;

    public PizzaStorageCoast(long pizzaCapacity, String pizzaCapacityString, long price) {
        super(pizzaCapacity, pizzaCapacityString, price);
        this.numWindows = numWindows;
    }

    @Override
    public int getSceneTileIndex(){
        return 0;
    }

    @Override
    public storageType getStorageType() {
        return storageType.COAST;
    }

}
