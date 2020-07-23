package com.application.nick.pizzaplanet.scene;

import android.util.Log;

import com.application.nick.pizzaplanet.SceneManager;
import com.application.nick.pizzaplanet.entity.Purchases.PizzaStorageCoast;
import com.application.nick.pizzaplanet.entity.carrier.CarrierTruckCoast;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;

/**
 * Created by Nick on 4/5/2015.
 */
public class GameSceneCoast extends GameScene {

    private CarrierTruckCoast truck1, truck2;

    private float truck1FactoryCounter = 0, truck2FactoryCounter = 0, truck1StorageCounter = 0, truck2StorageCounter = 0;

    @Override
    public void createScene() {
        super.createScene();

        PizzaStorageCoast pizzaStorageCoast = (PizzaStorageCoast) pizzaStorage;

        truck1 = new CarrierTruckCoast(mResourceManager.mTruckVerticalTextureRegion, mVertexBufferObjectManager);
        truck1.setX(COAST_TRUCK_LEFT_X);
        truck1.setY(COAST_TRUCK_FACTORY_Y);
        attachChild(truck1);

        if(mActivity.getStorageNumber() > 0) {
            truck2 = new CarrierTruckCoast(mResourceManager.mTruckVerticalTextureRegion, mVertexBufferObjectManager);
            truck2.setX(COAST_TRUCK_RIGHT_X);
            truck2.setY(COAST_TRUCK_FACTORY_Y);
            attachChild(truck2);
        }

        if(mActivity.getStorageNumber() < 2) {
            final Sprite closedSign1 = new Sprite(357, 705, mResourceManager.mCoastClosedSignTextureRegion, mVertexBufferObjectManager);
            attachChild(closedSign1);
        }
        if(mActivity.getStorageNumber() == 0) {
            final Sprite closedSign2 = new Sprite(551, 705, mResourceManager.mCoastClosedSignTextureRegion, mVertexBufferObjectManager);
            attachChild(closedSign2);
        }


         /* Handle moving entities */
        registerUpdateHandler(new IUpdateHandler() {

            @Override
            public void reset() {
            }

            @Override
            public void onUpdate(float pSecondsElapsed) {

                handleTruck1(pSecondsElapsed);

                if(mActivity.getStorageNumber() > 0) {
                    handleTruck2(pSecondsElapsed);
                }

            }
        });

    }

    @Override
    public SceneManager.GameSceneType getGameSceneType() {
        return SceneManager.GameSceneType.COAST;
    }

    private void handleTruck1(float secondsElapsed) {

        if(truck1.isDriving()) {
            if(truck1.getY() > COAST_TRUCK_STORAGE_Y) {
                truck1.setY(COAST_TRUCK_STORAGE_Y);
                truck1.parkOutsideStorage();
            }
            if(truck1.getY() < COAST_TRUCK_FACTORY_Y) {
                truck1.setY(COAST_TRUCK_FACTORY_Y);
                truck1.parkOutsideFactory();
            }
        }
        if(truck1.isParkedOutsideStorage()) {
            truck1StorageCounter += secondsElapsed;
            if(truck1StorageCounter > TIME_IN_WAREHOUSE) {
                truck1StorageCounter = 0;
                Log.i("Exits warehouse", "at warehouse");
                truck1.drive(CarrierTruckCoast.Orientation.UP);
            }
        }
        if(truck1.isParkedOutsideFactory()) {
            truck1FactoryCounter += secondsElapsed;
            if (truck1FactoryCounter > TIME_IN_KITCHEN && getPizzaNotPickedUp() > SLICES_PER_PIZZA) {
                setPizzaNotPickedUp(getPizzaNotPickedUp() % SLICES_PER_PIZZA);
                truck1FactoryCounter = 0;
                Log.i("Exits Kitchen", "at kitchen");
                truck1.drive(CarrierTruckCoast.Orientation.DOWN);

            }
        }



    }

    private void handleTruck2(float secondsElapsed) {

        if(truck2.isDriving()) {
            if(truck2.getY() > COAST_TRUCK_STORAGE_Y) {
                truck2.setY(COAST_TRUCK_STORAGE_Y);
                truck2.parkOutsideStorage();
            }
            if(truck2.getY() < COAST_TRUCK_FACTORY_Y) {
                truck2.setY(COAST_TRUCK_FACTORY_Y);
                truck2.parkOutsideFactory();
            }
        }
        if(truck2.isParkedOutsideStorage()) {
            truck2StorageCounter += secondsElapsed;
            if(truck2StorageCounter > TIME_IN_WAREHOUSE) {
                truck2StorageCounter = 0;
                Log.i("Exits warehouse", "at warehouse");
                truck2.drive(CarrierTruckCoast.Orientation.UP);
            }
        }
        if(truck2.isParkedOutsideFactory()) {
            truck2FactoryCounter += secondsElapsed;
            if (truck2FactoryCounter > TIME_IN_KITCHEN && getPizzaNotPickedUp() > SLICES_PER_PIZZA) {
                setPizzaNotPickedUp(getPizzaNotPickedUp() % SLICES_PER_PIZZA);
                truck2FactoryCounter = 0;
                Log.i("Exits Kitchen", "at kitchen");
                truck2.drive(CarrierTruckCoast.Orientation.DOWN);

            }
        }



    }

    /*private void lightWindowLeft(int numWindows) {
        for(int i = 0; i < numWindows; i++) {
            if(leftStorageWindowsFront.size() < 36) {
                lightWindow(StorageLocation.LEFT_FRONT);
            }
        }
    }

    private void lightWindow(StorageLocation location) {
        if(location == StorageLocation.LEFT_FRONT) {
            LitWindow window = litWindowPool.obtainPoolItem();
            window.setX(COAST_STORAGE_LEFTMOST_WINDOW_X_DISPLACEMENT[0] + COAST_STORAGE_WINDOW_X_DISPLACEMENT_RELATIVE_TO_LEFTMOST_WINDOW_X[leftStorageWindowsFront.size() / 6] );
            window.setY(COAST_STORAGE_WINDOW_Y[leftStorageWindowsFront.size() % 6]);
            leftStorageWindowsFront.add(window);
            attachChild(window);
        }

    }*/
}
