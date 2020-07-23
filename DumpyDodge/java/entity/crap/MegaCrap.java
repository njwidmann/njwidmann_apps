package com.application.nick.dumpydodge.entity.crap;

import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**
 * Created by Nick on 5/25/2015.
 */
public class MegaCrap extends Crap {

    public MegaCrap(TiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pTiledTextureRegion, pVertexBufferObjectManager);
    }

    @Override
    public boolean collidesWith(IShape pOtherShape) {
        Sprite sprite = (Sprite) pOtherShape;

        float spriteLeft = sprite.getX();
        float spriteRight = spriteLeft + sprite.getWidth();
        float spriteTop = sprite.getY();
        float spriteBottom = spriteTop + sprite.getHeight();
        float left = this.getX();
        float top = this.getY();

        //create regions to handle the irregular shape of the obstacle
        ArrayList<Integer> regionX = new ArrayList<>();
        ArrayList<Integer> regionYTop = new ArrayList<>();
        ArrayList<Integer> regionYBottom = new ArrayList<>();

        if (getCurrentBirdIndex() == BLOCKY_BIRD_INDEX) {
            regionX.add(3);
            regionX.add(45);
            regionYTop.add(3);
            regionYBottom.add(45);
        } else {
            regionX.add(3);
            regionX.add(9);
            regionX.add(15);
            regionX.add(33);
            regionX.add(39);
            regionX.add(45);
            regionYTop.add(15);
            regionYTop.add(9);
            regionYTop.add(3);
            regionYTop.add(9);
            regionYTop.add(15);
            regionYBottom.add(33);
            regionYBottom.add(39);
            regionYBottom.add(45);
            regionYBottom.add(39);
            regionYBottom.add(33);

        }

        for(int i = 0; i < regionYTop.size(); i++) {
            if ((spriteRight > left + regionX.get(i) && spriteLeft < left + regionX.get(i+1)) && (spriteTop < top + regionYBottom.get(i) && spriteBottom > top + regionYTop.get(i))) {
                return true;
            }
        }
        return false;
    }


    @Override
    public crapType getCrapType() {return crapType.MEGA;}


}
