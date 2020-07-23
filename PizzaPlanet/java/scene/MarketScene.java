package com.application.nick.pizzaplanet.scene;

import android.util.Log;

import com.application.nick.pizzaplanet.GameValues;
import com.application.nick.pizzaplanet.SceneManager;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;

/**
 * Created by Nick on 6/18/2015.
 */
public class MarketScene extends BaseScene implements GameValues{

    private Text totalMoneyText, priceTopText, priceMiddleText, priceBottomText;
    private TiledSprite purchaseButtonTop, purchaseButtonMiddle, purchaseButtonBottom, distributionCenterGraphic;

    private float pizzaTimeCounter;

    @Override
    public void createScene() {
        mEngine.registerUpdateHandler(new FPSLogger());

        final Sprite background = new Sprite(0, 0, mResourceManager.mMarketBackgroundTextureRegion, mVertexBufferObjectManager);
        attachChild(background);

        final float totalMoneyX = 171;
        final float totalMoneyY = 53;

        final float priceTopX = 73;
        final float priceTopY = 311;
        final float priceMiddleX = 447;
        final float priceMiddleY = 645;
        final float priceBottomX = 114;
        final float priceBottomY = 991;

        final float purchaseButtonTopX = 362;
        final float purchaseButtonTopY = 300;
        final float purchaseButtonMiddleX = 176;
        final float purchaseButtonMiddleY = 569;
        final float purchaseButtonBottomX = 450;
        final float purchaseButtonBottomY = 948;

        totalMoneyText = new Text(totalMoneyX, totalMoneyY, mResourceManager.mFontBlack45, "0123456789kmbtq", new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
        attachChild(totalMoneyText);

        priceTopText = new Text(priceTopX, priceTopY, mResourceManager.mFontBlack32, "$0123456789kmbtq012345679", new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
        attachChild(priceTopText);

        priceMiddleText = new Text(priceMiddleX, priceMiddleY, mResourceManager.mFontBlack32, "$0123456789kmbtq0123456789", new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
        attachChild(priceMiddleText);

        priceBottomText = new Text(priceBottomX, priceBottomY, mResourceManager.mFontBlack32, "$0123456789kmbtq0123456789", new TextOptions(HorizontalAlign.LEFT), mVertexBufferObjectManager);
        attachChild(priceBottomText);

        purchaseButtonTop = new TiledSprite(purchaseButtonTopX, purchaseButtonTopY, mResourceManager.mPurchaseButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

                if (pSceneTouchEvent.isActionDown() && getCurrentTileIndex() == 0 && mActivity.getEmployeeNumber() < employeePurchases.length - 1 && mActivity.getMoney() >= getEmployeeUpgradePrice()) {
                    setCurrentTileIndex(2);
                    mResourceManager.mButtonSound.play();
                } else if(pSceneTouchEvent.isActionDown() && getCurrentTileIndex() == 0 && mActivity.getEmployeeNumber() < employeePurchases.length - 1 && mActivity.getMoney() < getEmployeeUpgradePrice()) {
                    mActivity.alert("Not enough money.");
                }
                if (pSceneTouchEvent.isActionUp() && getCurrentTileIndex() == 2) {
                    setCurrentTileIndex(0);

                    mActivity.subtractMoney(getEmployeeUpgradePrice());
                    mActivity.setEmployeeNumber(mActivity.getEmployeeNumber() + 1);
                    updateTextAndGraphics();

                }

                return true;
            }

        };
        purchaseButtonTop.setCurrentTileIndex(0);
        registerTouchArea(purchaseButtonTop);
        attachChild(purchaseButtonTop);


        purchaseButtonMiddle = new TiledSprite(purchaseButtonMiddleX, purchaseButtonMiddleY, mResourceManager.mPurchaseButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown() && getCurrentTileIndex() == 0 && mActivity.getMoney() >= getStorageUpgradePrice()) {
                    setCurrentTileIndex(2);
                    mResourceManager.mButtonSound.play();
                } else if(pSceneTouchEvent.isActionDown() && getCurrentTileIndex() == 0 && mActivity.getMoney() < getStorageUpgradePrice()) {
                    mActivity.alert("Not enough money.");
                }
                if (pSceneTouchEvent.isActionUp() && getCurrentTileIndex() == 2) {
                    setCurrentTileIndex(0);

                    mActivity.subtractMoney(getStorageUpgradePrice());
                    mActivity.setStorageNumber(mActivity.getStorageNumber() + 1);

                    updateTextAndGraphics();

                }

                return true;
            }

        };
        purchaseButtonMiddle.setCurrentTileIndex(0);
        registerTouchArea(purchaseButtonMiddle);
        attachChild(purchaseButtonMiddle);


        purchaseButtonBottom = new TiledSprite(purchaseButtonBottomX, purchaseButtonBottomY, mResourceManager.mPurchaseButtonTextureRegion, mVertexBufferObjectManager) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown() && getCurrentTileIndex() == 0 && mActivity.getMoney() >= getLocationUpgradePrice()) {
                    setCurrentTileIndex(2);
                    mResourceManager.mButtonSound.play();
                } else if(pSceneTouchEvent.isActionDown() && getCurrentTileIndex() == 0 && mActivity.getMoney() < getLocationUpgradePrice()) {
                    mActivity.alert("Not enough money.");
                }
                if (pSceneTouchEvent.isActionUp() && getCurrentTileIndex() == 2) {
                    setCurrentTileIndex(0);

                    mActivity.subtractMoney(getLocationUpgradePrice());
                    mActivity.setLocationNumber(mActivity.getLocationNumber() + 1);
                    mActivity.setStorageNumber(0);
                    purchaseButtonMiddle.setCurrentTileIndex(0);

                    updateTextAndGraphics();

                }

                return true;
            }

        };
        purchaseButtonBottom.setCurrentTileIndex(0);
        registerTouchArea(purchaseButtonBottom);
        attachChild(purchaseButtonBottom);

        distributionCenterGraphic = new TiledSprite(MARKET_DISTRIBUTION_CENTER_GRAPHIC_X, MARKET_DISTRIBUTION_CENTER_GRAPHIC_Y, mResourceManager.mMarketDistributionCenterGraphicTextureRegion, mVertexBufferObjectManager);
        distributionCenterGraphic.setCurrentTileIndex(mActivity.getLocationNumber());
        attachChild(distributionCenterGraphic);

        updateTextAndGraphics();

        registerUpdateHandler(new IUpdateHandler() {

            @Override
            public void reset() {
            }

            @Override
            public void onUpdate(float pSecondsElapsed) {

                pizzaTimeCounter += pSecondsElapsed;
                if (pizzaTimeCounter >= 1.0f) {
                    mActivity.addPizza(mActivity.getEmployeeNumber());
                    long maxPizza = locations[mActivity.getLocationNumber()].getStorages()[mActivity.getStorageNumber()].getPizzaCapacity() * SLICES_PER_PIZZA;
                    if (mActivity.getPizza() > maxPizza) {
                        mActivity.setPizza(maxPizza);
                    }
                    pizzaTimeCounter = 0;
                }

                updateTextAndGraphics();


            }
        });


    }


    @Override
    public void onBackKeyPressed() {
        mActivity.setGameScene();
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_MARKET;
    }

    @Override
    public void disposeScene() {

    }

    public void updateTextAndGraphics() {
        totalMoneyText.setText(String.valueOf(mActivity.convertNumberToFriendlyString(mActivity.getMoney())));
        if(mActivity.getEmployeeNumber() == employeePurchases.length - 1) {
            purchaseButtonTop.setCurrentTileIndex(4);
            priceTopText.setText("NA");
        } else {
            priceTopText.setText("$" + mActivity.convertNumberToFriendlyString(getEmployeeUpgradePrice()));
        }
        if(mActivity.getStorageNumber() == locations[mActivity.getLocationNumber()].getStorages().length - 1) {
            purchaseButtonMiddle.setCurrentTileIndex(4);
            priceMiddleText.setText("NA");
        } else {
            priceMiddleText.setText("$" + mActivity.convertNumberToFriendlyString(getStorageUpgradePrice()));
        }
        if(mActivity.getLocationNumber() == locations.length - 1) {
            purchaseButtonBottom.setCurrentTileIndex(4);
            priceBottomText.setText("NA");
        } else {
            priceBottomText.setText("$" + mActivity.convertNumberToFriendlyString(getLocationUpgradePrice()));
        }
        distributionCenterGraphic.setCurrentTileIndex(mActivity.getLocationNumber());
    }

    public long getEmployeeUpgradePrice() {
        return employeePurchases[mActivity.getEmployeeNumber() + 1].getPrice();
    }

    public long getStorageUpgradePrice() {
        return locations[mActivity.getLocationNumber()].getStorages()[mActivity.getStorageNumber() + 1].getPrice();
    }

    public long getLocationUpgradePrice() {
        return locations[mActivity.getLocationNumber() + 1].getPrice();
    }
}
