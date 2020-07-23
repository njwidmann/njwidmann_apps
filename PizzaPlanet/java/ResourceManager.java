package com.application.nick.pizzaplanet;

import android.graphics.Color;
import android.graphics.Typeface;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;

import java.io.IOException;

/**
 * Created by Nick on 4/5/2015.
 */
public class ResourceManager {

    private static final ResourceManager INSTANCE = new ResourceManager();

    public GameActivity mActivity;

    private BitmapTextureAtlas mSplashTextureAtlas;
    public ITextureRegion mSplashTextureRegion;

    private BitmapTextureAtlas mBuildingsTextureAtlas;
    public TiledTextureRegion mBuildingsTextureRegion;

    private BitmapTextureAtlas mScenesTextureAtlas;
    public TiledTextureRegion mScenesTextureRegion;
    public ITextureRegion mTutorialSceneTextureRegion;

    private BitmapTextureAtlas mScenes2TextureAtlas;
    public TiledTextureRegion mScenes2TextureRegion;

    public BitmapTextureAtlas mIslandsTextureAtlas;
    public TiledTextureRegion mIslandsTextureRegion;

    private BitmapTextureAtlas mScenes3TextureAtlas;
    public TiledTextureRegion mScenes3TextureRegion;
    public TiledTextureRegion mPlanetsTextureRegion;

    private BitmapTextureAtlas mGameResourcesTextureAtlas;
    public ITextureRegion mPizzaBoxFrontTextureRegion;
    public ITextureRegion mPizzaBoxTopTextureRegion;
    public TiledTextureRegion mPersonTextureRegion;
    public ITextureRegion mHUDTextureRegion;
    public TiledTextureRegion mTruckTextureRegion;
    public TiledTextureRegion mTruckVerticalTextureRegion;
    public ITextureRegion mWarehouseClosedSignTextureRegion;
    public ITextureRegion mLitWindowTextureRegion;
    public ITextureRegion mCoastClosedSignTextureRegion;

    private BitmapTextureAtlas mMarketResourcesTextureAtlas;
    public ITextureRegion mMarketBackgroundTextureRegion;
    public TiledTextureRegion mMarketDistributionCenterGraphicTextureRegion;

    private BitmapTextureAtlas mButtonTextureAtlas;
    public TiledTextureRegion mSellButtonTextureRegion;
    public TiledTextureRegion mMarketButtonTextureRegion;
    public TiledTextureRegion mPurchaseButtonTextureRegion;
    public TiledTextureRegion mVolumeButtonTextureRegion;
    public TiledTextureRegion mHelpButtonTextureRegion;
    public TiledTextureRegion mCloseButtonTextureRegion;


    public Font mFontBlack45, mFontBlack32;
    public Font mFontGrey20;

    public Sound mButtonSound;
    public Music mMusic;

    private ResourceManager() {}

    public static ResourceManager getInstance() {
        return INSTANCE;
    }

    public void prepare(GameActivity activity) {
        INSTANCE.mActivity = activity;
    }

    public void loadSplashResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/splash/");
        mSplashTextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 720, 1280, TextureOptions.BILINEAR);
        mSplashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSplashTextureAtlas, mActivity, "splash.png", 0, 0);
        mSplashTextureAtlas.load();

        mFontGrey20 = FontFactory.create(mActivity.getFontManager(), mActivity.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 20, Color.GRAY);
        mFontGrey20.load();
    }

    public void unloadSplashResources() {
        mSplashTextureAtlas.unload();
        mSplashTextureRegion = null;

        mFontGrey20.unload();
        mFontGrey20 = null;
    }

    public void loadGameResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
            //load gfx
        mBuildingsTextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 1000, 1000, TextureOptions.BILINEAR);
        mBuildingsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBuildingsTextureAtlas, mActivity, "buildings.png", 0, 0, 2, 2);
        mBuildingsTextureAtlas.load();


        mScenesTextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 2160, 1280, TextureOptions.BILINEAR);
        mScenesTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mScenesTextureAtlas, mActivity, "scenes1.png", 0, 0, 2, 1);
        mTutorialSceneTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mScenesTextureAtlas, mActivity, "tutorial.png", 1440, 0);
        mScenesTextureAtlas.load();

        mScenes2TextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 1440, 1280, TextureOptions.BILINEAR);
        mScenes2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mScenes2TextureAtlas, mActivity, "scenes2.png", 0, 0, 2, 1);
        mScenes2TextureAtlas.load();

        mIslandsTextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 1296, 736, TextureOptions.BILINEAR);
        mIslandsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mIslandsTextureAtlas, mActivity, "islands.png", 0, 0, 3, 2);
        mIslandsTextureAtlas.load();

        mScenes3TextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 1708, 2130, TextureOptions.BILINEAR);
        mScenes3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mScenes3TextureAtlas, mActivity, "scenes3.png", 0, 0, 1, 1);
        mPlanetsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mScenes3TextureAtlas, mActivity, "planets.png", 0, 1283, 4, 2);
        mScenes3TextureAtlas.load();

        mGameResourcesTextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 720, 850, TextureOptions.BILINEAR);
        mHUDTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mGameResourcesTextureAtlas, mActivity, "HUD.png", 0, 0);
        mPersonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mGameResourcesTextureAtlas, mActivity, "person1.png", 0, 450, 8, 4);
        mPizzaBoxTopTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mGameResourcesTextureAtlas, mActivity, "pizza_box_top.png", 0, 590);
        mPizzaBoxFrontTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mGameResourcesTextureAtlas, mActivity, "pizza_box_front.png", 0, 600);
        mLitWindowTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mGameResourcesTextureAtlas, mActivity, "window_light.png", 25, 600);
        mTruckTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mGameResourcesTextureAtlas, mActivity, "trucks.png", 0, 610, 2, 1);
        mTruckVerticalTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mGameResourcesTextureAtlas, mActivity, "trucks_vertical.png", 105, 610, 2, 1);
        mWarehouseClosedSignTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mGameResourcesTextureAtlas, mActivity, "warehouse_closed_sign.png", 0, 700);
        mCoastClosedSignTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mGameResourcesTextureAtlas, mActivity, "coast_closed_sign.png", 200, 700);

        mGameResourcesTextureAtlas.load();


        mMarketResourcesTextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 1250, 1530, TextureOptions.BILINEAR);
        mMarketBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMarketResourcesTextureAtlas, mActivity, "market_scene_background.png", 0, 0);
        mMarketDistributionCenterGraphicTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mMarketResourcesTextureAtlas, mActivity, "market_distribution_center_graphic.png", 0, 1300, 5, 1);
        mMarketResourcesTextureAtlas.load();


        mButtonTextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 500, 450, TextureOptions.BILINEAR);
        mSellButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mButtonTextureAtlas, mActivity, "sell_button.png", 0, 0, 2, 1);
        mMarketButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mButtonTextureAtlas, mActivity, "market_button.png", 250, 0, 2, 1);
        mPurchaseButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mButtonTextureAtlas, mActivity, "purchase_button.png", 0, 75, 3, 2);
        mVolumeButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mButtonTextureAtlas, mActivity, "volume_button.png", 0, 225, 2, 1);
        mHelpButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mButtonTextureAtlas, mActivity, "help_button.png", 0, 300, 2, 1);
        mCloseButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mButtonTextureAtlas, mActivity, "close_button.png", 0, 375, 2, 1);

        mButtonTextureAtlas.load();

        //load fonts
        mFontBlack45 = FontFactory.create(mActivity.getFontManager(), mActivity.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 45, Color.BLACK);
        mFontBlack45.load();

        mFontBlack32 = FontFactory.create(mActivity.getFontManager(), mActivity.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 32, Color.BLACK);
        mFontBlack32.load();

            //load sound/music
        SoundFactory.setAssetBasePath("mfx/");
        try {
            mButtonSound = SoundFactory.createSoundFromAsset(mActivity.getEngine().getSoundManager(), mActivity, "button.ogg");
        } catch (final IOException e) {
            Debug.e(e);
        }

        MusicFactory.setAssetBasePath("mfx/");
        try {
            mMusic = MusicFactory.createMusicFromAsset(mActivity.getEngine().getMusicManager(), mActivity, "music.ogg");
            mMusic.setLooping(true);
            mMusic.play();
            mMusic.pause();
        } catch (final IOException e) {
            Debug.e(e);
        }
    }

    public void unloadGameResources() {

            //unload gfx
        mGameResourcesTextureAtlas.unload();
        mGameResourcesTextureAtlas = null;
        mHUDTextureRegion = null;
        mPizzaBoxTopTextureRegion = null;
        mPizzaBoxFrontTextureRegion = null;
        mPersonTextureRegion = null;
        mTruckTextureRegion = null;
        mTruckVerticalTextureRegion = null;
        mWarehouseClosedSignTextureRegion = null;
        mLitWindowTextureRegion = null;
        mCoastClosedSignTextureRegion = null;

        mBuildingsTextureAtlas.unload();
        mBuildingsTextureAtlas = null;
        mBuildingsTextureRegion = null;

        mScenesTextureAtlas.unload();
        mScenesTextureAtlas = null;
        mScenesTextureRegion = null;
        mTutorialSceneTextureRegion = null;

        mScenes2TextureAtlas.unload();
        mScenes2TextureAtlas = null;
        mScenes2TextureRegion = null;

        mIslandsTextureAtlas.unload();
        mIslandsTextureAtlas = null;
        mIslandsTextureRegion = null;

        mScenes3TextureAtlas.unload();
        mScenes3TextureAtlas = null;
        mScenes3TextureRegion = null;
        mPlanetsTextureRegion = null;

        mMarketResourcesTextureAtlas.unload();
        mMarketResourcesTextureAtlas = null;
        mMarketBackgroundTextureRegion = null;
        mMarketDistributionCenterGraphicTextureRegion = null;

        mButtonTextureAtlas.unload();
        mSellButtonTextureRegion = null;
        mMarketButtonTextureRegion = null;
        mPurchaseButtonTextureRegion = null;
        mVolumeButtonTextureRegion = null;
        mCloseButtonTextureRegion = null;
        mHelpButtonTextureRegion = null;


            //unload fonts
        mFontBlack45.unload();
        mFontBlack45 = null;

        mFontBlack32.unload();
        mFontBlack32 = null;


            //unload sound/music
        mButtonSound.release();
        mButtonSound = null;

        mMusic.stop();
        mMusic.release();
        mMusic = null;


    }
}
