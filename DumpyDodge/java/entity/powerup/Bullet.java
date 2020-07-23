package com.application.nick.dumpydodge.entity.powerup;

import com.application.nick.dumpydodge.entity.GameEntity;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 8/15/2015.
 */
public class Bullet extends GameEntity {



    public Bullet(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void shoot(MachineGun machineGun) {
        float displacement = MACHINE_GUN_BARREL_CENTER_DISPLACEMENT - MACHINE_GUN_BARREL_WIDTH / 2 + (float)(Math.random() * MACHINE_GUN_BARREL_WIDTH);
        if(machineGun.getDirection() == Direction.LEFT) {
            setX(Side.CENTER, machineGun.getX(Side.CENTER) - (machineGun.getWidth() / 2) * (float) Math.cos(MACHINE_GUN_ANGLE_RADIANS) + displacement * (float) Math.sin(MACHINE_GUN_ANGLE_RADIANS));
            setY(Side.CENTER, machineGun.getY(Side.CENTER) - (machineGun.getWidth() / 2) * (float)Math.sin(MACHINE_GUN_ANGLE_RADIANS) - displacement * (float)Math.cos(MACHINE_GUN_ANGLE_RADIANS));
            setFlippedHorizontal(true);
            setRotation(MACHINE_GUN_ANGLE_DEGREES);
            setVelocityY(-BULLET_VELOCITY_Y);
            setVelocityX(-BULLET_VELOCITY_X);
        } else {
            setX(Side.CENTER, machineGun.getX(Side.CENTER) + (machineGun.getWidth() / 2) * (float) Math.cos(MACHINE_GUN_ANGLE_RADIANS) - displacement * (float) Math.sin(MACHINE_GUN_ANGLE_RADIANS));
            setY(Side.CENTER, machineGun.getY(Side.CENTER) - (machineGun.getWidth() / 2) * (float)Math.sin(MACHINE_GUN_ANGLE_RADIANS) - displacement * (float)Math.cos(MACHINE_GUN_ANGLE_RADIANS));
            setFlippedHorizontal(false);
            setRotation(-MACHINE_GUN_ANGLE_DEGREES);
            setVelocityY(-BULLET_VELOCITY_Y);
            setVelocityX(BULLET_VELOCITY_X);
        }
    }




    @Override
    public Entities getEntityType() {
        return null;
    }
}
