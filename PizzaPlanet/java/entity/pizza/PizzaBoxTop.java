package com.application.nick.pizzaplanet.entity.pizza;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 4/5/2015.
 */
public class PizzaBoxTop extends Sprite {

    private float mGroundY;


    public PizzaBoxTop(ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, float pGroundY) {
        super(-pTextureRegion.getWidth(), pGroundY - pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager);

        mGroundY = pGroundY;

    }

    @Override
    public void reset() {
        super.reset();
        setX(-getWidth());
    }


}
