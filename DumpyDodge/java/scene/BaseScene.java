package com.application.nick.dumpydodge.scene;

import com.application.nick.dumpydodge.GameActivity;
import com.application.nick.dumpydodge.GameValues;
import com.application.nick.dumpydodge.ResourceManager;
import com.application.nick.dumpydodge.SceneManager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Nick on 4/5/2015.
 */
public abstract class BaseScene extends Scene implements GameValues {

    protected GameActivity mActivity;
    protected Engine mEngine;
    protected Camera mCamera;
    protected VertexBufferObjectManager mVertexBufferObjectManager;
    protected ResourceManager mResourceManager;
    protected SceneManager mSceneManager;

    public BaseScene() {
        mResourceManager = ResourceManager.getInstance();
        mActivity = mResourceManager.mActivity;
        mVertexBufferObjectManager = mActivity.getVertexBufferObjectManager();
        mEngine = mActivity.getEngine();
        mCamera = mEngine.getCamera();
        mSceneManager = SceneManager.getInstance();
        createScene();
    }

    public abstract void createScene();
    public abstract void onBackKeyPressed();
    public abstract SceneManager.SceneType getSceneType();
    public abstract void disposeScene();

}
