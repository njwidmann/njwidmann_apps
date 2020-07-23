package com.application.nick.pizzaplanet.scene;

import android.util.Log;

import com.application.nick.pizzaplanet.SceneManager;
import com.application.nick.pizzaplanet.entity.carrier.CarrierPerson;
import com.application.nick.pizzaplanet.entity.carrier.CarrierTruckWarehouse;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;

/**
 * Created by Nick on 4/5/2015.
 */
public class GameSceneWarehouse extends GameScene {

    private CarrierPerson person1, person2;
    private CarrierTruckWarehouse truck1, truck2;

    private float person1WarehouseCounter = 0, person1KitchenCounter = 0, person2WarehouseCounter = 0, person2KitchenCounter = 0;

    @Override
    public void createScene() {
        super.createScene();


        truck1 = new CarrierTruckWarehouse(mResourceManager.mTruckTextureRegion, mVertexBufferObjectManager, CarrierTruckWarehouse.Orientation.RIGHT);
        truck1.setX(WAREHOUSE_LEFT_TRUCK_KITCHEN_X);
        truck1.parkOutsideKitchen();
        attachChild(truck1);

        person1 = new CarrierPerson(mResourceManager.mPersonTextureRegion, mVertexBufferObjectManager, WAREHOUSE_PERSON_KITCHEN_GROUND_Y);
        person1.setX(truck1.getX() + truck1.getWidth());
        attachChild(person1);
        person1.enterKitchen();

        if(mActivity.getStorageNumber() == 1) {
            truck2 = new CarrierTruckWarehouse(mResourceManager.mTruckTextureRegion, mVertexBufferObjectManager, CarrierTruckWarehouse.Orientation.LEFT);
            truck2.setX(WAREHOUSE_RIGHT_TRUCK_KITCHEN_X);
            truck2.parkOutsideKitchen();
            attachChild(truck2);

            person2 = new CarrierPerson(mResourceManager.mPersonTextureRegion, mVertexBufferObjectManager, WAREHOUSE_PERSON_KITCHEN_GROUND_Y);
            person2.setX(truck2.getX() - person2.getWidth());
            attachChild(person2);
            person2.enterKitchen();
        } else {
            final Sprite warehouseClosedSign = new Sprite(WAREHOUSE_CLOSED_SIGN_MIDDLE_X - mResourceManager.mWarehouseClosedSignTextureRegion.getWidth() / 2, 720, mResourceManager.mWarehouseClosedSignTextureRegion, mVertexBufferObjectManager);
            attachChild(warehouseClosedSign);
        }

         /* Handle moving entities */
        registerUpdateHandler(new IUpdateHandler() {

            @Override
            public void reset() {
            }

            @Override
            public void onUpdate(float pSecondsElapsed) {

                handleTruck1(pSecondsElapsed);

                if(mActivity.getStorageNumber() == 1) {
                    handleTruck2(pSecondsElapsed);
                }

            }
        });

    }

    @Override
    public SceneManager.GameSceneType getGameSceneType() {
        return SceneManager.GameSceneType.WAREHOUSE;
    }


    private void handleTruck1(float secondsElapsed) {
        //person entering truck
        if(!person1.getInTruck() && person1.getY() > truck1.getY()) {
            Log.i("Enters truck", "asd");
            if(truck1.getParkedOutsideKitchen()) {
                person1.enterTruck();
                truck1.driveToWarehouse(CarrierTruckWarehouse.Orientation.LEFT);
            } else {
                person1.enterTruck();
                truck1.driveToKitchen(CarrierTruckWarehouse.Orientation.RIGHT);
            }
        }
        //person exiting truck
        if(person1.getInTruck()) {
            if(truck1.getX() < WAREHOUSE_LEFT_TRUCK_WAREHOUSE_X) {
                Log.i("Exits truck", "at warehouse");
                person1.setX(truck1.getX() - person1.getWidth());
                person1.setY(truck1.getY());
                truck1.parkOutsideWarehouse();
                truck1.setX(WAREHOUSE_LEFT_TRUCK_WAREHOUSE_X);
                person1.exitTruck();
            } else if(truck1.getX() > WAREHOUSE_LEFT_TRUCK_KITCHEN_X) {
                Log.i("Exits truck", "at kitchen");

                person1.setX(truck1.getX() + truck1.getWidth());
                person1.setY(truck1.getY());
                truck1.parkOutsideKitchen();
                truck1.setX(WAREHOUSE_LEFT_TRUCK_KITCHEN_X);
                person1.exitTruck();
            }
        }
        if(!person1.getInTruck()) {
            //if person is entering warehouse
            if(truck1.getParkedOutsideWarehouse() && !person1.getInWarehouse() && person1.getY() < WAREHOUSE_PERSON_WAREHOUSE_GROUND_Y - person1.getHeight()) {
                Log.i("enters warehouse", "at warehouse");

                person1.enterWarehouse();
                person1WarehouseCounter = 0;
            }
            //if person is entering kitchen
            if(truck1.getParkedOutsideKitchen() && !person1.getInKitchen() && person1.getY() < WAREHOUSE_PERSON_KITCHEN_GROUND_Y - person1.getHeight()) {
                Log.i("Enters kitchen", "enters kitchen");

                person1.enterKitchen();
                person1KitchenCounter = 0;
            }
        }
        if(person1.getInWarehouse()) {
            person1WarehouseCounter += secondsElapsed;
            if(person1WarehouseCounter > TIME_IN_WAREHOUSE) {
                Log.i("Exits warehouse", "at warehouse");

                person1.setY(WAREHOUSE_PERSON_WAREHOUSE_GROUND_Y - person1.getHeight());
                person1.exitWarehouse();
            }
        }
        if(person1.getInKitchen()) {
            person1KitchenCounter += secondsElapsed;
            if (person1KitchenCounter > TIME_IN_KITCHEN && getPizzaNotPickedUp() > SLICES_PER_PIZZA) {
                Log.i("Exits Kitchen", "at kitchen");

                int pizzaBoxesPickedUp = getPizzaNotPickedUp() / SLICES_PER_PIZZA;
                setPizzaNotPickedUp(getPizzaNotPickedUp() % SLICES_PER_PIZZA);
                person1.setPizzaBoxesPickedUp(pizzaBoxesPickedUp);
                person1.setY(WAREHOUSE_PERSON_KITCHEN_GROUND_Y - person1.getHeight());
                person1.exitKitchen();
            }
        }



    }

    private void handleTruck2(float secondsElapsed) {
        //person entering truck
        if(!person2.getInTruck() && person2.getY() > truck2.getY()) {
            Log.i("Enters truck", "asd");
            if(truck2.getParkedOutsideKitchen()) {
                person2.enterTruck();
                truck2.driveToWarehouse(CarrierTruckWarehouse.Orientation.RIGHT);
            } else {
                person2.enterTruck();
                truck2.driveToKitchen(CarrierTruckWarehouse.Orientation.LEFT);
            }
        }
        //person exiting truck
        if(person2.getInTruck()) {
            if(truck2.getX() > WAREHOUSE_RIGHT_TRUCK_WAREHOUSE_X) {
                Log.i("Exits truck", "at warehouse");
                person2.setX(truck2.getX() + truck2.getWidth());
                person2.setY(truck2.getY());
                truck2.parkOutsideWarehouse();
                truck2.setX(WAREHOUSE_RIGHT_TRUCK_WAREHOUSE_X);
                person2.exitTruck();
            } else if(truck2.getX() < WAREHOUSE_RIGHT_TRUCK_KITCHEN_X) {
                Log.i("Exits truck", "at kitchen");

                person2.setX(truck2.getX() - person2.getWidth());
                person2.setY(truck2.getY());
                truck2.parkOutsideKitchen();
                truck2.setX(WAREHOUSE_RIGHT_TRUCK_KITCHEN_X);
                person2.exitTruck();
            }
        }
        if(!person2.getInTruck()) {
            //if person is entering warehouse
            if(truck2.getParkedOutsideWarehouse() && !person2.getInWarehouse() && person2.getY() < WAREHOUSE_PERSON_WAREHOUSE_GROUND_Y - person2.getHeight()) {
                Log.i("enters warehouse", "at warehouse");

                person2.enterWarehouse();
                person2WarehouseCounter = 0;
            }
            //if person is entering kitchen
            if(truck2.getParkedOutsideKitchen() && !person2.getInKitchen() && person2.getY() < WAREHOUSE_PERSON_KITCHEN_GROUND_Y - person2.getHeight()) {
                Log.i("Enters kitchen", "enters kitchen");

                person2.enterKitchen();
                person2KitchenCounter = 0;
            }
        }
        if(person2.getInWarehouse()) {
            person2WarehouseCounter += secondsElapsed;
            if(person2WarehouseCounter > TIME_IN_WAREHOUSE) {
                Log.i("Exits warehouse", "at warehouse");

                person2.setY(WAREHOUSE_PERSON_WAREHOUSE_GROUND_Y - person2.getHeight());
                person2.exitWarehouse();
            }
        }
        if(person2.getInKitchen()) {
            person2KitchenCounter += secondsElapsed;
            if (person2KitchenCounter > TIME_IN_KITCHEN && getPizzaNotPickedUp() > SLICES_PER_PIZZA) {
                Log.i("Exits Kitchen", "at kitchen");

                int pizzaBoxesPickedUp = getPizzaNotPickedUp() / SLICES_PER_PIZZA;
                setPizzaNotPickedUp(getPizzaNotPickedUp() % SLICES_PER_PIZZA);
                person2.setPizzaBoxesPickedUp(pizzaBoxesPickedUp);
                person2.setY(WAREHOUSE_PERSON_KITCHEN_GROUND_Y - person2.getHeight());
                person2.exitKitchen();
            }
        }



    }



}
