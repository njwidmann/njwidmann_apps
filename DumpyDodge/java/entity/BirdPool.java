package com.application.nick.dumpydodge.entity;

/**
 * Created by Nick on 7/30/2015.
 */
import com.application.nick.dumpydodge.GameValues;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

/**
 * Created by Nick on 4/5/2015.
 */
public class BirdPool extends GenericPool<Bird> implements GameValues {

    private TiledTextureRegion mBirdTextureRegion;
    private VertexBufferObjectManager mVertexBufferObjectManager;
    private int mBirdIndex;

    public BirdPool(TiledTextureRegion pBirdTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super();
        this.mBirdTextureRegion = pBirdTextureRegion;
        this.mVertexBufferObjectManager = pVertexBufferObjectManager;
        mBirdIndex = -1;
    }

    @Override
    protected Bird onAllocatePoolItem() {
        return new Bird(0,0, mBirdTextureRegion, mVertexBufferObjectManager);
    }


    @Override
    protected void onHandleObtainItem(Bird pItem) {
        pItem.reset();
    }

    @Override
    public synchronized Bird obtainPoolItem() {
        mBirdIndex++;
        return super.obtainPoolItem();
    }

    public int getBirdIndex() {
        return mBirdIndex;
    }


}
