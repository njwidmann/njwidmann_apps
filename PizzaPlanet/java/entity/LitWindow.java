package com.application.nick.pizzaplanet.entity;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 4/5/2015.
 */
public class LitWindow extends Sprite {


    public LitWindow(ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(0, 0, pTextureRegion, pVertexBufferObjectManager);


    }

    @Override
    public void reset() {
        super.reset();
        setX(-getWidth());
    }


}
