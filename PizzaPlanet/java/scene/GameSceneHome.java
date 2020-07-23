package com.application.nick.pizzaplanet.scene;

import com.application.nick.pizzaplanet.SceneManager;
import com.application.nick.pizzaplanet.entity.Purchases.PizzaStorageHome;
import com.application.nick.pizzaplanet.entity.carrier.Carrier;
import com.application.nick.pizzaplanet.entity.carrier.CarrierPerson;
import com.application.nick.pizzaplanet.entity.carrier.CarrierPersonPool;
import com.application.nick.pizzaplanet.entity.pizza.PizzaBoxFront;
import com.application.nick.pizzaplanet.entity.pizza.PizzaBoxFrontPool;
import com.application.nick.pizzaplanet.entity.pizza.PizzaBoxTop;
import com.application.nick.pizzaplanet.entity.pizza.PizzaBoxTopPool;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.TiledSprite;

import java.util.ArrayList;

/**
 * Created by Nick on 4/5/2015.
 */
public class GameSceneHome extends GameScene {

    private CarrierPersonPool carrierPersonPool;
    private ArrayList<Carrier> carriers;

    private PizzaBoxFrontPool pizzaBoxFrontPool;
    private ArrayList<ArrayList<PizzaBoxFront>> pizzaBoxFronts;

    private PizzaBoxTopPool pizzaBoxTopPool;
    private ArrayList<PizzaBoxTop> pizzaBoxTops;

    private TiledSprite pizzaStorageBuilding;

    @Override
    public void createScene() {
        super.createScene();

            PizzaStorageHome building = (PizzaStorageHome) pizzaStorage;
            pizzaStorageBuilding = new TiledSprite(building.getX(), building.getY(), mResourceManager.mBuildingsTextureRegion, mVertexBufferObjectManager);
            pizzaStorageBuilding.setCurrentTileIndex(building.getTileIndex());

            attachChild(pizzaStorageBuilding);

            carrierPersonPool = new CarrierPersonPool(mResourceManager, mVertexBufferObjectManager, HOME_GROUND_Y);
            carrierPersonPool.batchAllocatePoolItems(MAX_VISIBLE_CARRIER_PEOPLE);
            carriers = new ArrayList<Carrier>();

            pizzaBoxFrontPool = new PizzaBoxFrontPool(mResourceManager, mVertexBufferObjectManager, HOME_GROUND_Y);
            pizzaBoxFrontPool.batchAllocatePoolItems((int) building.getPizzaCapacity());
            pizzaBoxFronts = new ArrayList<ArrayList<PizzaBoxFront>>();

            pizzaBoxTopPool = new PizzaBoxTopPool(mResourceManager, mVertexBufferObjectManager, HOME_GROUND_Y);
            pizzaBoxTopPool.batchAllocatePoolItems(building.getMaxPizzaX());
            pizzaBoxTops = new ArrayList<PizzaBoxTop>();


            addPizzaBox((int) mActivity.getPizza() / SLICES_PER_PIZZA);




         /* Handle moving entities */
        registerUpdateHandler(new IUpdateHandler() {

            @Override
            public void reset() {
            }

            @Override
            public void onUpdate(float pSecondsElapsed) {

                handlePizzaCarrierPeople();


            }
        });

    }

    @Override
    public SceneManager.GameSceneType getGameSceneType() {
        return SceneManager.GameSceneType.HOME;
    }


    private void handlePizzaCarrierPeople() {
        //add new
        if(carriers.size() < employeePurchases[mActivity.getEmployeeNumber()].getNumEmployees() && carriers.size() < MAX_VISIBLE_CARRIER_PEOPLE && getPizzaNotPickedUp() >= SLICES_PER_PIZZA && (carriers.size() == 0 || carriers.get(carriers.size() - 1).getX() > pizzaStorageBuilding.getX() || carriers.get(carriers.size() - 1).getVelocityX() < 0)) {
            carriers.add(carrierPersonPool.obtainPoolItem());
            CarrierPerson person = (CarrierPerson)carriers.get(carriers.size() - 1);
            person.setX(HOME_PERSON_START_X);
            person.setVelocityX(PERSON_VELOCITY_X);
            person.setPizzaBoxesPickedUp(getPizzaNotPickedUp() / SLICES_PER_PIZZA);
            setPizzaNotPickedUp(getPizzaNotPickedUp() % SLICES_PER_PIZZA);
            attachChild(person);
            person.setZIndex(HOME_PERSON_Z_INDEX);
        }
        //handle current
        for(int i = carriers.size() - 1; i >= 0; i--) {
            CarrierPerson person = (CarrierPerson)carriers.get(i);
            PizzaStorageHome building = (PizzaStorageHome)pizzaStorage;
            if((pizzaBoxTops.size() > 0 && person.getX() > pizzaBoxTops.get(pizzaBoxTops.size() - 1).getX()) || (pizzaBoxTops.size() == 0 && person.getX() > building.getX() + building.getOverX())) {
                addPizzaBox(person.getPizzaBoxesPickedUp());
                person.setPizzaBoxesPickedUp(0);
                person.setVelocityX(-PERSON_VELOCITY_X);
            } else if(person.getX() < HOME_PERSON_START_X) {
                detachChild(person);
                carrierPersonPool.recyclePoolItem(person);
                carriers.remove(i);
            }
        }
    }

    /**
     * for adding a number of pizza boxes to a pizza storage building
     * @param numPizzaBoxes the number of pizza boxes to add
     */
    private void addPizzaBox(int numPizzaBoxes) {
        for (int i = 0; i < numPizzaBoxes; i++) {
            addPizzaBox();
        }
    }

    /**
     * for adding a pizza box to the last/next stack in a pizza storage building
     */
    private void addPizzaBox() {

        PizzaStorageHome building = (PizzaStorageHome)pizzaStorage;
        if(pizzaBoxFronts.size() == 0 || pizzaBoxFronts.size() < building.getMaxPizzaX() || pizzaBoxFronts.get(pizzaBoxFronts.size() - 1).size() < building.getMaxPizzaY()) {
            if (pizzaBoxFronts.size() == 0 || pizzaBoxFronts.get(pizzaBoxFronts.size() - 1).size() >= building.getMaxPizzaY()) {
                pizzaBoxFronts.add(new ArrayList<PizzaBoxFront>());
            }


            ArrayList<PizzaBoxFront> pizzaBoxStack = pizzaBoxFronts.get(pizzaBoxFronts.size() - 1);
            pizzaBoxStack.add(pizzaBoxFrontPool.obtainPoolItem());
            PizzaBoxFront pizzaBoxFront = pizzaBoxStack.get(pizzaBoxStack.size() - 1);
            attachChild(pizzaBoxFront);
            pizzaBoxFront.setZIndex(PIZZA_BOX_Z_INDEX);

            if (pizzaBoxStack.size() == 1) { //if this is the first box in this stack
                pizzaBoxFront.setX((pizzaBoxFronts.size() - 1) * (mResourceManager.mPizzaBoxFrontTextureRegion.getWidth() + SPACE_BETWEEN_PIZZA_BOXES) + building.getOverX() + pizzaStorageBuilding.getX());
                pizzaBoxFront.setY(HOME_GROUND_Y - mResourceManager.mPizzaBoxFrontTextureRegion.getHeight());
                pizzaBoxTops.add(pizzaBoxTopPool.obtainPoolItem());
                attachChild(pizzaBoxTops.get(pizzaBoxTops.size() - 1));
            } else {
                pizzaBoxFront.setX(pizzaBoxStack.get(pizzaBoxStack.size() - 2).getX());
                pizzaBoxFront.setY(pizzaBoxStack.get(pizzaBoxStack.size() - 2).getY() - mResourceManager.mPizzaBoxFrontTextureRegion.getHeight());
            }

            PizzaBoxTop pizzaBoxTop = pizzaBoxTops.get(pizzaBoxTops.size() - 1);
            pizzaBoxTop.setX(pizzaBoxFront.getX());
            pizzaBoxTop.setY(pizzaBoxFront.getY() - mResourceManager.mPizzaBoxTopTextureRegion.getHeight());
            pizzaBoxTop.setZIndex(PIZZA_BOX_Z_INDEX);
            sortChildren();
        }

    }

}
