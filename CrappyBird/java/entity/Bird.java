package com.application.nick.crappybird.entity;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 4/5/2015.
 */
public class Bird extends GameEntity {

    public Bird(float x, float y, TiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(x, y, pTiledTextureRegion, pVertexBufferObjectManager);

        setAcceleration(0, BIRD_GRAVITY);
        setVelocity(0, 0);

    }

    public void animate(int selectedBird) {
        long fDur = BIRD_FRAME_DURATION;
        this.animate(new long[]{fDur, fDur, fDur}, selectedBird * 3, selectedBird * 3 + 2, true);

    }

    public void jump() {
        float currentYVelocity = getVelocityY();
        if(currentYVelocity > 0) {
            setVelocityY(-BIRD_JUMP_VELOCITY);
        }
    }

    public void jump(boolean megaCrapActivated) {
        float currentYVelocity = getVelocityY();
        if(currentYVelocity < 0) {
            if(!megaCrapActivated) {
                setVelocityY(currentYVelocity - (BIRD_JUMP_VELOCITY * 4 + currentYVelocity) * (1.0f / 4.0f));
            } else {
                setVelocityY(currentYVelocity - (BIRD_JUMP_VELOCITY * 4 + currentYVelocity) * (1.0f / 2.0f));
            }
        } else {
            if(!megaCrapActivated) {
                setVelocityY(-BIRD_JUMP_VELOCITY);
            } else {
                setVelocityY(-BIRD_JUMP_VELOCITY * 2);
            }
        }
    }


    @Override
    public Entities getEntityType() {
        return Entities.BIRD;
    }


}
