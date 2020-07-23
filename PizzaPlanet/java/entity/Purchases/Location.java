package com.application.nick.pizzaplanet.entity.Purchases;

/**
 * Created by Nick on 6/18/2015.
 */
public class Location {

    private String name;
    private int sceneTileIndex;
    private long price;
    private PizzaStorage[] storages;

    public Location(String name, int sceneTileIndex, long price, PizzaStorage[] storages) {
        this.name = name;
        this.sceneTileIndex = sceneTileIndex;
        this.price = price;
        this.storages = storages;
    }

    public String getName() {
        return name;
    }

    public int getSceneTileIndex() {
        return sceneTileIndex;
    }

    public long getPrice() {
        return price;
    }

    public PizzaStorage[] getStorages() {
        return storages;
    }
}
