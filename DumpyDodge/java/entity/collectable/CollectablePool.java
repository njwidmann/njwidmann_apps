package com.application.nick.dumpydodge.entity.collectable;

/**
 * Created by Nick on 7/30/2015.
 */
import com.application.nick.dumpydodge.GameValues;
import com.application.nick.dumpydodge.ResourceManager;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

/**
 * Created by Nick on 4/5/2015.
 */
public class CollectablePool extends GenericPool<Collectable> implements GameValues {

    private ResourceManager mResourceManager;
    private VertexBufferObjectManager mVertexBufferObjectManager;

    public CollectablePool(ResourceManager pResourceManager, VertexBufferObjectManager pVertexBufferObjectManager) {
        super();
        this.mResourceManager = pResourceManager;
        this.mVertexBufferObjectManager = pVertexBufferObjectManager;
    }

    @Override
    protected Collectable onAllocatePoolItem() {
        return new CollectableMothership(0,0, mResourceManager.mCollectableMelonTextureRegion, mVertexBufferObjectManager);
    }


    @Override
    protected void onHandleObtainItem(Collectable pItem) {
        pItem.reset();
    }



}
