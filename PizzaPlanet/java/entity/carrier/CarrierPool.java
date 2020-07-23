package com.application.nick.pizzaplanet.entity.carrier;

import com.application.nick.pizzaplanet.ResourceManager;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

/**
 * Created by Nick on 4/5/2015.
 */
public abstract class CarrierPool extends GenericPool<Carrier> {

    private int mTargetIndex;


    @Override
    protected void onHandleObtainItem(Carrier pItem) {
        pItem.reset();
    }

    @Override
    public synchronized Carrier obtainPoolItem() {
        mTargetIndex++;
        return super.obtainPoolItem();
    }

    public int getTargetIndex() {
        return mTargetIndex;
    }

}