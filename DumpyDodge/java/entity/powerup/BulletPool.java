package com.application.nick.dumpydodge.entity.powerup;

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
public class BulletPool extends GenericPool<Bullet> implements GameValues {

    private TiledTextureRegion mBulletTextureRegion;
    private VertexBufferObjectManager mVertexBufferObjectManager;

    public BulletPool(TiledTextureRegion pBulletTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super();
        this.mBulletTextureRegion = pBulletTextureRegion;
        this.mVertexBufferObjectManager = pVertexBufferObjectManager;
    }

    @Override
    protected Bullet onAllocatePoolItem() {
        return new Bullet(0,0, mBulletTextureRegion, mVertexBufferObjectManager);
    }


    @Override
    protected void onHandleObtainItem(Bullet pItem) {
        pItem.reset();
    }


}
