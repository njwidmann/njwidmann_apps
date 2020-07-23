package com.application.nick.pizzaplanet.entity.carrier;

import android.hardware.ConsumerIrManager;

import com.application.nick.pizzaplanet.ResourceManager;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 6/16/2015.
 */
public class CarrierPersonPool extends CarrierPool {

    private ResourceManager mResourceManager;
    private VertexBufferObjectManager mVertexBufferObjectManager;
    private float mGroundY;

    public CarrierPersonPool(ResourceManager pResourceManager, VertexBufferObjectManager pVertexBufferObjectManager, float pGroundY) {
        super();
        this.mResourceManager = pResourceManager;
        this.mVertexBufferObjectManager = pVertexBufferObjectManager;
        this.mGroundY = pGroundY;
    }

    @Override
    protected Carrier onAllocatePoolItem() {

        return new CarrierPerson(mResourceManager.mPersonTextureRegion, mVertexBufferObjectManager, mGroundY);

    }
}
