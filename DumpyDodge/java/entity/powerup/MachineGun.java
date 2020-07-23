package com.application.nick.dumpydodge.entity.powerup;

import com.application.nick.dumpydodge.entity.GameEntity;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 8/15/2015.
 */
public class MachineGun extends GameEntity {

    Direction direction;
    boolean activated;

    public MachineGun(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        activated = false;
    }

    public void activate(boolean bool) {
        if(bool) {
            activated = true;
            setVisible(true);
            animate(MACHINE_GUN_FRAME_DURATION);
        } else {
            activated = false;
            setVisible(false);
            stopAnimation();
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        if(direction == Direction.LEFT) {
            setX(Side.CENTER, SCREEN_WIDTH / 2 - MACHINE_GUN_DISPLACEMENT_X);
            setFlippedHorizontal(true);
            setRotation(MACHINE_GUN_ANGLE_DEGREES);
        } else { //set direction right
            setX(Side.CENTER, SCREEN_WIDTH / 2 + MACHINE_GUN_DISPLACEMENT_X);
            setFlippedHorizontal(false);
            setRotation(-MACHINE_GUN_ANGLE_DEGREES);
        }
    }

    @Override
    public Entities getEntityType() {
        return Entities.MACHINE_GUN;
    }
}
