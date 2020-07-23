package com.application.nick.dumpydodge.entity.powerup;

import com.application.nick.dumpydodge.entity.GameEntity;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 8/16/2015.
 */
public class Umbrella extends GameEntity {

    boolean activated, runningOut;


    public Umbrella(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        runningOut = false;
    }

    public void activate(boolean bool) {
        if (bool) {
            activated = true;
            setVisible(true);
            setCurrentTileIndex(0); //no transparency
        } else {
            activated = false;
            setVisible(false);
            stopAnimation();
            runningOut = false;
        }
    }

    /**
     * called when umbrella is almost gone
     */
    public void runningOut() {
        if(!runningOut) {
            runningOut = true;
            animate(UMBRELLA_FLASH_FRAME_DURATION);
        }

    }

    public boolean isActivated() {
        return activated;
    }

    @Override
    public Entities getEntityType() {
        return Entities.UMBRELLA;
    }
}
