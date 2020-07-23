package com.application.nick.pizzaplanet.entity.Purchases;

/**
 * Created by Nick on 6/18/2015.
 */
public class PizzaStoragePlanet extends PizzaStorage{

    public PizzaStoragePlanet(long pizzaCapacity, String pizzaCapacityString, long price) {
        super(pizzaCapacity, pizzaCapacityString, price);

    }

    @Override
    public int getSceneTileIndex(){
        return 0;
    }

    @Override
    public storageType getStorageType() {
        return storageType.PLANET;
    }

}
