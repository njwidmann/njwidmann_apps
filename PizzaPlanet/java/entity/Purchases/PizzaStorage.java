package com.application.nick.pizzaplanet.entity.Purchases;

/**
 * Created by Nick on 6/16/2015.
 */
public abstract class PizzaStorage {

    public enum storageType {HOME, WAREHOUSE, COAST, ISLAND, PLANET}

    private long pizzaCapacity, price;
    private String capacityString;

    public PizzaStorage(long pizzaCapacity, String capacityString, long price) {

        this.pizzaCapacity = pizzaCapacity / 10;
        this.capacityString = capacityString;
        this.price = price;
    }

    public abstract int getSceneTileIndex();

    public long getPizzaCapacity() {
        return pizzaCapacity;
    }

    public long getPrice() {
        return price;
    }

    public String getCapacityString() {
        return capacityString;
    }

    public abstract storageType getStorageType();

}
