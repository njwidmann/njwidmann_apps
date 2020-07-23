package com.application.nick.pizzaplanet;

import com.application.nick.pizzaplanet.entity.Purchases.EmployeePurchase;
import com.application.nick.pizzaplanet.entity.Purchases.Location;
import com.application.nick.pizzaplanet.entity.Purchases.PizzaStorageCoast;
import com.application.nick.pizzaplanet.entity.Purchases.PizzaStorageHome;
import com.application.nick.pizzaplanet.entity.Purchases.PizzaStorage;
import com.application.nick.pizzaplanet.entity.Purchases.PizzaStorageIsland;
import com.application.nick.pizzaplanet.entity.Purchases.PizzaStoragePlanet;
import com.application.nick.pizzaplanet.entity.Purchases.PizzaStorageWarehouse;

/**
 * Created by Nick on 6/17/2015.
 */
public interface GameValues {
    float SCENE_Y = 0; //the y coordinate for the scene (with the houses and people)
    float HOME_GROUND_Y = 895; //the y coordinate for the ground in the scene
    float HOME_BUILDING_X = 200;
    float HOME_BUILDING_Y = HOME_GROUND_Y - 450;
    float SPACE_BETWEEN_PIZZA_BOXES = 2;
    int MAX_VISIBLE_CARRIER_PEOPLE = 10;
    int SLICES_PER_PIZZA = 10;
    float HOME_PERSON_START_X = 120;
    float PERSON_VELOCITY_X = 100;
    float PERSON_VELOCITY_Y = 100;
    float TRUCK_VELOCITY_X = 100;
    float TRUCK_VELOCITY_Y = 100;
    int MONEY_PER_PIZZA = 10;
    int CAMERA_WIDTH = 720;
    int CAMERA_HEIGHT = 1280;
    int HOME_PERSON_Z_INDEX = 100;
    int PIZZA_BOX_Z_INDEX = 99;
    float WAREHOUSE_TRUCK_MOVING_LEFT_Y = 888;
    float WAREHOUSE_TRUCK_MOVING_RIGHT_Y = 918;
    float WAREHOUSE_PERSON_KITCHEN_GROUND_Y = 888;
    float WAREHOUSE_PERSON_WAREHOUSE_GROUND_Y = 884; //the y coordinate for the person to enter the warehouse (disapear)
    float WAREHOUSE_LEFT_TRUCK_KITCHEN_X = 330; //the x coordinate for the left truck to be stopped outside the kitchen (facing right)
    float WAREHOUSE_RIGHT_TRUCK_KITCHEN_X = 403;
    float WAREHOUSE_LEFT_TRUCK_WAREHOUSE_X = 150;
    float WAREHOUSE_RIGHT_TRUCK_WAREHOUSE_X = 530;
    float TIME_IN_WAREHOUSE = 1; //second
    float TIME_IN_KITCHEN = 1; //second
    float WAREHOUSE_CLOSED_SIGN_MIDDLE_X = 588;
    float MARKET_DISTRIBUTION_CENTER_GRAPHIC_X = 80;
    float MARKET_DISTRIBUTION_CENTER_GRAPHIC_Y = 750;
    float COAST_TRUCK_FACTORY_Y = 590;
    float COAST_TRUCK_STORAGE_Y = 840;
    float COAST_TRUCK_LEFT_X = 310;
    float COAST_TRUCK_RIGHT_X = 504;
    float PLANET_X = 146;
    float PLANET_Y = 566;
    float ISLAND_X = 148;
    float ISLAND_Y = 547;


    EmployeePurchase[] employeePurchases = {
            new EmployeePurchase(1, 0),
            new EmployeePurchase(2, 5),
            new EmployeePurchase(4, 25),
            new EmployeePurchase(10, 100),
            new EmployeePurchase(25, 500),
            new EmployeePurchase(50, 3000),
            new EmployeePurchase(100, 50000),
            new EmployeePurchase(500, 400000),
            new EmployeePurchase(1000, 3000000),
            new EmployeePurchase(5000, 8000000),
            new EmployeePurchase(10000, 60000000),
            new EmployeePurchase(25000, 150000000),
            new EmployeePurchase(50000, 500000000l),
            new EmployeePurchase(100000, 1500000000l),
            new EmployeePurchase(500000, 5000000000l),
            new EmployeePurchase(2000000, 30000000000l),
            new EmployeePurchase(10000000, 150000000000l),
            new EmployeePurchase(50000000, 800000000000l),
            new EmployeePurchase(200000000, 5000000000000l),
            new EmployeePurchase(400000000, 35000000000000l),
            new EmployeePurchase(600000000, 150000000000000l),
            new EmployeePurchase(800000000, 400000000000000l),
            new EmployeePurchase(1000000000, 1000000000000000l)

    };

    Location[] locations = {
            new Location("Mom's House", 0, 0, new PizzaStorage[] {
                    new PizzaStorageHome(0, 100, "100", 1, 10, 10, 0),
                    new PizzaStorageHome(1, 2960, "2,960", 8, 37, 22, 100),
                    new PizzaStorageHome(2, 9000, "9,000", 15, 60, 14, 1000),
                    new PizzaStorageHome(3, 21340, "21,340", 22, 97, 8, 10000)}),
            new Location("The Warehouse", 1, 100000, new PizzaStorage[] {
                    new PizzaStorageWarehouse(100000, "100k", 0),
                    new PizzaStorageWarehouse(500000, "500k", 500000)
            }),
            new Location("The Coast", 0, 2500000, new PizzaStorage[] {
                    new PizzaStorageCoast(2000000, "2mil", 0),
                    new PizzaStorageCoast(5000000, "5mil", 10000000),
                    new PizzaStorageCoast(10000000, "10mil", 25000000)
            }),
            new Location("Island", 1, 50000000, new PizzaStorage[] {
                    new PizzaStorageIsland(50000000, "50mil", 0),
                    new PizzaStorageIsland(100000000, "100mil", 250000000),
                    new PizzaStorageIsland(200000000, "200mil", 500000000),
                    new PizzaStorageIsland(300000000, "300mil", 1200000000),
                    new PizzaStorageIsland(400000000, "400mil", 2000000000),
                    new PizzaStorageIsland(500000000, "500mil", 3000000000l),
            }),
            new Location("Planet", 0, 4000000000l, new PizzaStorage[] {
                    new PizzaStoragePlanet(750000000, "750mil", 0),
                    new PizzaStoragePlanet(1500000000, "1.5bil", 6000000000l),
                    new PizzaStoragePlanet(5000000000l, "5bil", 15000000000l),
                    new PizzaStoragePlanet(25000000000l, "25bil", 55000000000l),
                    new PizzaStoragePlanet(100000000000l, "100bil", 300000000000l),
                    new PizzaStoragePlanet(500000000000l, "500bil", 1300000000000l),
                    new PizzaStoragePlanet(2000000000000l, "2tril", 7000000000000l),
                    new PizzaStoragePlanet(5000000000000l, "5tril", 30000000000000l),

            })
    };



}
