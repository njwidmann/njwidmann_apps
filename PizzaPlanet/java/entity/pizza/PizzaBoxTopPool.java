package com.application.nick.pizzaplanet.entity.pizza;

import com.application.nick.pizzaplanet.ResourceManager;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

/**
 * Created by Nick on 4/5/2015.
 */
public class PizzaBoxTopPool extends GenericPool<PizzaBoxTop> {

    private int mPizzaBoxIndex;
    private ResourceManager mResourceManager;
    private VertexBufferObjectManager mVertexBufferObjectManager;
    private float mGroundY;

    public PizzaBoxTopPool(ResourceManager pResourceManager, VertexBufferObjectManager pVertexBufferObjectManager, float pGroundY) {
        super();
        this.mResourceManager = pResourceManager;
        this.mVertexBufferObjectManager = pVertexBufferObjectManager;
        this.mGroundY = pGroundY;
    }

    @Override
    protected PizzaBoxTop onAllocatePoolItem() {

        return new PizzaBoxTop(mResourceManager.mPizzaBoxTopTextureRegion, mVertexBufferObjectManager, mGroundY);

    }

    @Override
    protected void onHandleObtainItem(PizzaBoxTop pItem) {
        pItem.reset();
    }

    @Override
    public synchronized PizzaBoxTop obtainPoolItem() {
        mPizzaBoxIndex++;
        return super.obtainPoolItem();
    }

    public int getIndex() {
        return mPizzaBoxIndex;
    }

}