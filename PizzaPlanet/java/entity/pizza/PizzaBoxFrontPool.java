package com.application.nick.pizzaplanet.entity.pizza;

import com.application.nick.pizzaplanet.ResourceManager;
import com.application.nick.pizzaplanet.entity.carrier.Carrier;
import com.application.nick.pizzaplanet.entity.carrier.CarrierPerson;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

/**
 * Created by Nick on 4/5/2015.
 */
public class PizzaBoxFrontPool extends GenericPool<PizzaBoxFront> {

    private int mPizzaBoxIndex;
    private ResourceManager mResourceManager;
    private VertexBufferObjectManager mVertexBufferObjectManager;
    private float mGroundY;

    public PizzaBoxFrontPool(ResourceManager pResourceManager, VertexBufferObjectManager pVertexBufferObjectManager, float pGroundY) {
        super();
        this.mResourceManager = pResourceManager;
        this.mVertexBufferObjectManager = pVertexBufferObjectManager;
        this.mGroundY = pGroundY;
    }

    @Override
    protected PizzaBoxFront onAllocatePoolItem() {

        return new PizzaBoxFront(mResourceManager.mPizzaBoxFrontTextureRegion, mVertexBufferObjectManager, mGroundY);

    }

    @Override
    protected void onHandleObtainItem(PizzaBoxFront pItem) {
        pItem.reset();
    }

    @Override
    public synchronized PizzaBoxFront obtainPoolItem() {
        mPizzaBoxIndex++;
        return super.obtainPoolItem();
    }

    public int getPizzaBoxIndex() {
        return mPizzaBoxIndex;
    }

}