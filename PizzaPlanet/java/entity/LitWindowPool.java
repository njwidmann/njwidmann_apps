package com.application.nick.pizzaplanet.entity;

import com.application.nick.pizzaplanet.ResourceManager;
import com.application.nick.pizzaplanet.entity.pizza.PizzaBoxFront;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

/**
 * Created by Nick on 4/5/2015.
 */
public class LitWindowPool extends GenericPool<LitWindow> {

    private int mLitWindowIndex;
    private ResourceManager mResourceManager;
    private VertexBufferObjectManager mVertexBufferObjectManager;

    public LitWindowPool(ResourceManager pResourceManager, VertexBufferObjectManager pVertexBufferObjectManager) {
        super();
        this.mResourceManager = pResourceManager;
        this.mVertexBufferObjectManager = pVertexBufferObjectManager;
    }

    @Override
    protected LitWindow onAllocatePoolItem() {

        return new LitWindow(mResourceManager.mPizzaBoxFrontTextureRegion, mVertexBufferObjectManager);

    }

    @Override
    protected void onHandleObtainItem(LitWindow pItem) {
        pItem.reset();
    }

    @Override
    public synchronized LitWindow obtainPoolItem() {
        mLitWindowIndex++;
        return super.obtainPoolItem();
    }

    public int getLitWindowIndex() {
        return mLitWindowIndex;
    }

}