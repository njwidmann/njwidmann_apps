package com.application.nick.crappybird;

import android.graphics.Color;
import android.graphics.Typeface;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
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

    private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
    public ITextureRegion mParallaxLayerBack;
    public ITextureRegion mParallaxLayerMiddle;
    public ITextureRegion mParallaxLayerFront;

    private BitmapTextureAtlas mBitmapTextureAtlas;
    public TiledTextureRegion mBirdTextureRegion;
    public TiledTextureRegion mPipeTextureRegion;

    public TiledTextureRegion mCrapTextureRegion;
    public TiledTextureRegion mMegaCrapTextureRegion;
    public TiledTextureRegion mObstacleHouseTextureRegion;
    public TiledTextureRegion mObstacleTreesTextureRegion;
    public TiledTextureRegion mObstaclePlanesTextureRegion;
    public TiledTextureRegion mObstacleBalloonTextureRegion;

    public TiledTextureRegion mCollectablePizzaTextureRegion;
    public TiledTextureRegion mCollectableTacoTextureRegion;
    public TiledTextureRegion mCollectableHamTextureRegion;
    public TiledTextureRegion mCollectableMelonTextureRegion;
    public TiledTextureRegion mCollectableMuffinTextureRegion;
    public TiledTextureRegion mCollectableBurgerTextureRegion;

    public TiledTextureRegion mTargetPerson1TextureRegion;

    public TiledTextureRegion mAlertTextureRegion;
    public TiledTextureRegion mPlusTwoTextureRegion;


    private BitmapTextureAtlas mBitmapTextureAtlas2;
    public TiledTextureRegion mObstacleMotherShipTextureRegion;
    public TiledTextureRegion mObstacleTrainCarFrontTextureRegion;
    public TiledTextureRegion mObstacleTrainCarSecondTextureRegion;
    public TiledTextureRegion mObstacleTrainCarPassengerTextureRegion;

    private BitmapTextureAtlas mTutorialBitmapTextureAtlas;
    public TiledTextureRegion mTutorialTextureRegion;
    public ITextureRegion mTutorialBoardTextureRegion;


    public BitmapTextureAtlas mBirdsBitmapTextureAtlas;
    public TiledTextureRegion mBirdsTextureRegion;
    public TiledTextureRegion mMarketBirdsTextureRegion;

    private BitmapTextureAtlas mSubBitmapTextureAtlas;
    public TiledTextureRegion mStateTextureRegion;
    public ITextureRegion mScoreBoardTextureRegion;
    public ITextureRegion mBoardTextureRegion;
    public TiledTextureRegion mMeterTextureRegion;
    public TiledTextureRegion mMeter2TextureRegion;
    public ITextureRegion mHelpTextureRegion;
    public TiledTextureRegion mCountdownTextureRegion;
    public ITextureRegion mGooglePlayIconTextureRegion;


    public TiledTextureRegion mPlayButtonTextureRegion;
    public TiledTextureRegion mHelpButtonTextureRegion;
    public TiledTextureRegion mBackButtonTextureRegion;
    public TiledTextureRegion mTweetButtonTextureRegion;
    public TiledTextureRegion mFacebookButtonTextureRegion;
    public TiledTextureRegion mOtherButtonTextureRegion;
    public TiledTextureRegion mRateButtonTextureRegion;
    public TiledTextureRegion mShareButtonTextureRegion;
    public TiledTextureRegion mPauseButtonTextureRegion;
    public TiledTextureRegion mLoginButtonTextureRegion;
    public TiledTextureRegion mLaterButtonTextureRegion;
    public TiledTextureRegion mSignUpButtonTextureRegion;
    public TiledTextureRegion mLogoutButtonTextureRegion;
    public TiledTextureRegion mNextButtonTextureRegion;
    public TiledTextureRegion mCloseButtonTextureRegion;
    public TiledTextureRegion mRestartButtonTextureRegion;
    public TiledTextureRegion mArrowLeftButtonTextureRegion;
    public TiledTextureRegion mArrowRightButtonTextureRegion;
    public TiledTextureRegion mBirdsButtonTextureRegion;
    public TiledTextureRegion mPowerUpsMarketButtonTextureRegion;
    public TiledTextureRegion mPurchaseButtonTextureRegion;
    public TiledTextureRegion mSelectButtonTextureRegion;
    public TiledTextureRegion mMarketButtonTextureRegion;
    public TiledTextureRegion mYesButtonTextureRegion;
    public TiledTextureRegion mNoButtonTextureRegion;
    public TiledTextureRegion mGetMorePizzaButtonTextureRegion;
    public TiledTextureRegion mMenuButtonTextureRegion;
    public TiledTextureRegion mVolumeButtonTextureRegion;
    public TiledTextureRegion mGooglePlayButtonTextureRegion;
    public TiledTextureRegion mAchievementsButtonTextureRegion;
    public TiledTextureRegion mMoreOptionsButtonTextureRegion;
    public TiledTextureRegion mAdFreeButtonTextureRegion;
    public TiledTextureRegion mLeaderboardButtonTextureRegion;
    public TiledTextureRegion mAboutButtonTextureRegion;


    public ITextureRegion mTitleTextureRegion;

    public Font mFont1;
    public Font mFont2;
    public Font mFont3;
    public Font mFont4;
    public Font mFont5;
    public Font mFont6;

    public Sound mHitSound, mCoinSound, mButtonSound, mJumpSound, mMegaCrapSound, mMotherShipSound, mPropellerSound, mAlertSound,
            mCollectionSound, mWilhelmScreamSound, mRespawnSound, mTrainSound;
    public Music mMusic, mMariachiFast, mMariachiSlow;

    private ResourceManager() {}

    public static ResourceManager getInstance() {
        return INSTANCE;
    }

    public void prepare(GameActivity activity) {
        INSTANCE.mActivity = activity;
    }

    public void loadSplashResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/splash/");
        mSplashTextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 320, 533, TextureOptions.BILINEAR);
        mSplashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSplashTextureAtlas, mActivity, "logo.png", 0, 0);
        mSplashTextureAtlas.load();

        mFont1 = FontFactory.create(mActivity.getFontManager(), mActivity.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 10, Color.GRAY);
        mFont1.load();
    }

    public void unloadSplashResources() {
        mSplashTextureAtlas.unload();
        mSplashTextureRegion = null;

        mFont1.unload();
        mFont1 = null;
    }

    public void loadGameResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
            //load gfx
        mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 512, 1100);
        mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAutoParallaxBackgroundTexture, mActivity, "ground.png", 0, 0);
        mParallaxLayerMiddle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAutoParallaxBackgroundTexture, mActivity, "grass.png", 0, 150);
        mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mAutoParallaxBackgroundTexture, mActivity, "background.png", 0, 175);
        mAutoParallaxBackgroundTexture.load();

        mBitmapTextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 450, 1100, TextureOptions.BILINEAR);

        mBirdTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "birds.png", 0, 0, 3, 1);

        mTargetPerson1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "person1.png", 0, 30, 8, 4);

        mMeter2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "crap_supply_meter2.png", 0, 290, 1, 7);

        mObstacleHouseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "house.png", 0, 450, 1, 1);
        mObstacleTreesTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "two-trees.png", 100, 450, 2, 1);
        mObstacleBalloonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "hot_air_balloon.png", 200, 450, 2, 1);
        mObstaclePlanesTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "planes.png", 0, 600, 4, 1);

        mCollectablePizzaTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "pizza.png", 0, 650, 2, 1);
        mCollectableTacoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "taco.png", 75, 650, 1, 1);
        mCollectableHamTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "ham.png", 110, 650, 1, 1);
        mCollectableMelonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "melon.png", 160, 650, 1, 1);
        mCollectableMuffinTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "muffin.png", 225, 650, 1, 1);
        mCollectableBurgerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "burger.png", 260, 650, 1, 1);

        mAlertTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "alert_sign.png", 0, 700, 1, 1);
        mPlusTwoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "plus_two.png", 50, 700, 1, 1);

        mPauseButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "pause_button.png", 0, 750, 2, 1);
        mRestartButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mActivity, "restart_button.png", 0, 825, 2, 1);

        mBitmapTextureAtlas.load();

        mBitmapTextureAtlas2 = new BitmapTextureAtlas(mActivity.getTextureManager(), 1200, 310, TextureOptions.BILINEAR);
        mObstacleMotherShipTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas2, mActivity, "mothership.png", 0, 0, 1, 1);
        mObstacleTrainCarFrontTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas2, mActivity, "train_car_front.png", 0, 200, 1, 1);
        mObstacleTrainCarSecondTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas2, mActivity, "train_car_second.png", 150, 200, 1, 1);
        mObstacleTrainCarPassengerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas2, mActivity, "train_cars_passenger.png", 250, 200, 6, 1);

        mBitmapTextureAtlas2.load();

        mTutorialBitmapTextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 300, 1800, TextureOptions.BILINEAR);
        mTutorialTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mTutorialBitmapTextureAtlas, mActivity, "tutorial.png", 0, 0, 1, 4);
        mTutorialBoardTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTutorialBitmapTextureAtlas, mActivity, "tutorial_board.png", 0, 1210);
        mNextButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mTutorialBitmapTextureAtlas, mActivity, "next_button.png", 0, 1600, 2, 1);
        mCloseButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mTutorialBitmapTextureAtlas, mActivity, "close_button.png", 0, 1700, 2, 1);

        mTutorialBitmapTextureAtlas.load();

        mBirdsBitmapTextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 400, 1600, TextureOptions.BILINEAR);
        mBirdsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBirdsBitmapTextureAtlas, mActivity, "birds-master.png", 0, 0, 3, 18);
        mMarketBirdsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBirdsBitmapTextureAtlas, mActivity, "market_birds.png", 130, 0, 1, 19);
        mCrapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBirdsBitmapTextureAtlas, mActivity, "crap.png", 260, 0, 2, 18);
        mMegaCrapTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBirdsBitmapTextureAtlas, mActivity, "mega_crap.png", 300, 0, 2, 18);

        mBirdsBitmapTextureAtlas.load();

        mSubBitmapTextureAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 512, 1525, TextureOptions.BILINEAR);
        mStateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "ready_over.png", 0, 0, 2, 1);
        mScoreBoardTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSubBitmapTextureAtlas, mActivity, "score_board.png", 0, 60);
        mHelpTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSubBitmapTextureAtlas, mActivity, "help.png", 0, 200);
        mPlayButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "play_button.png", 0, 350, 2, 1);
        mHelpButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "help_button.png", 250, 350, 2, 1);
        mBackButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "back_button.png", 0, 425, 2, 1);
        //mTweetButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "tweet_button.png", 250, 425, 2, 1);
        //mFacebookButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "facebook_button.png", 0, 500, 2, 1);
        //mRateButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "rate_button.png", 250, 500, 2, 1);
        //mOtherButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "other_button.png", 0, 575, 2, 1);
        //mShareButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "share_button.png", 250, 575, 2, 1);
        mTitleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSubBitmapTextureAtlas, mActivity, "title.png", 0, 650);
        //mCountdownTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "countdown.png", 0, 750, 6, 1);
        //mBoardTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSubBitmapTextureAtlas, mActivity, "board.png", 0, 810);
        mBirdsButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "birds_button.png", 0, 950, 2, 1);
        mSelectButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "select_button.png", 250, 950, 2, 1);
        //mYesButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "yes_button.png", 0, 950, 2, 1);
        //mNoButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "no_button.png", 250, 950, 2, 1);
        mMenuButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "menu_button.png", 0, 1025, 2, 1);
        mVolumeButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "volume_button.png", 250, 1025, 2, 1);
        mAchievementsButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "achievements_button.png", 0, 1100, 2, 1);
        mGooglePlayButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "google_play_button.png", 250, 1100, 2, 1);
        mMoreOptionsButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "more_options_button.png", 0, 1175, 2, 1);
        mAdFreeButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "ad_free_button.png", 250, 1175, 2, 1);
        mArrowLeftButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "arrow_button_left.png", 0, 1250, 2, 1);
        mArrowRightButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "arrow_button_right.png", 150, 1250, 2, 1);
        mGooglePlayIconTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSubBitmapTextureAtlas, mActivity, "google_play_icon.png", 300, 1250);
        mLeaderboardButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "leaderboard_button.png", 0, 1375, 2, 1);
        mLogoutButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "logout_button.png", 250, 1375, 2, 1);
        mAboutButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mSubBitmapTextureAtlas, mActivity, "about_button.png", 0, 1450, 2, 1);

        mSubBitmapTextureAtlas.load();
            //load fonts
        mFont4 = FontFactory.create(mActivity.getFontManager(), mActivity.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 30, Color.BLACK);
        mFont4.load();

        FontFactory.setAssetBasePath("font/");
        //ITexture fontTexture2 = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        //mFont2 = FontFactory.createStrokeFromAsset(mActivity.getFontManager(), fontTexture2, mActivity.getAssets(), "GrutchShaded.ttf", 40, true, Color.YELLOW, 2, Color.DKGRAY);
        mFont2 = FontFactory.create(mActivity.getFontManager(), mActivity.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 20, Color.WHITE);
        mFont2.load();

        //ITexture fontTexture3 = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        //mFont3 = FontFactory.createFromAsset(mActivity.getFontManager(), fontTexture3, mActivity.getAssets(), "Archistico_Bold.ttf", 24, true, Color.WHITE);
        mFont3 = FontFactory.create(mActivity.getFontManager(), mActivity.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 15, Color.BLACK);
        mFont3.load();

        //ITexture fontTexture5 = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        //mFont5 = FontFactory.createStrokeFromAsset(mActivity.getFontManager(), fontTexture5, mActivity.getAssets(), "GrutchShaded.ttf", 36, true, Color.WHITE, 2, Color.DKGRAY);
        mFont5 = FontFactory.create(mActivity.getFontManager(), mActivity.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 30, Color.WHITE);
        mFont5.load();

        mFont6 = FontFactory.create(mActivity.getFontManager(), mActivity.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 15, Color.WHITE);
        mFont6.load();
            //load sound/music
        SoundFactory.setAssetBasePath("mfx/");
        try {
            mHitSound = SoundFactory.createSoundFromAsset(mActivity.getEngine().getSoundManager(), mActivity, "metal_hit.ogg");
            mHitSound.setVolume(0.5f);
            mCoinSound = SoundFactory.createSoundFromAsset(mActivity.getEngine().getSoundManager(), mActivity, "coin.ogg");
            mCoinSound.setVolume(0.5f);
            mButtonSound = SoundFactory.createSoundFromAsset(mActivity.getEngine().getSoundManager(), mActivity, "button.ogg");
            mJumpSound = SoundFactory.createSoundFromAsset(mActivity.getEngine().getSoundManager(), mActivity, "jump.ogg");
            mMegaCrapSound = SoundFactory.createSoundFromAsset(mActivity.getEngine().getSoundManager(), mActivity, "mega_crap_drop.ogg");
            mMotherShipSound = SoundFactory.createSoundFromAsset(mActivity.getEngine().getSoundManager(), mActivity, "space_ship.ogg");
            mPropellerSound = SoundFactory.createSoundFromAsset(mActivity.getEngine().getSoundManager(), mActivity, "propeller.ogg");
            mAlertSound = SoundFactory.createSoundFromAsset(mActivity.getEngine().getSoundManager(), mActivity, "alert.ogg");
            mCollectionSound = SoundFactory.createSoundFromAsset(mActivity.getEngine().getSoundManager(), mActivity, "collection.ogg");
            mWilhelmScreamSound = SoundFactory.createSoundFromAsset(mActivity.getEngine().getSoundManager(), mActivity, "wilhelm.ogg");
            mRespawnSound = SoundFactory.createSoundFromAsset(mActivity.getEngine().getSoundManager(), mActivity, "respawn.ogg");
            mTrainSound = SoundFactory.createSoundFromAsset(mActivity.getEngine().getSoundManager(), mActivity, "train.ogg");
        } catch (final IOException e) {
            Debug.e(e);
        }

        MusicFactory.setAssetBasePath("mfx/");
        try {
            mMusic = MusicFactory.createMusicFromAsset(mActivity.getEngine().getMusicManager(), mActivity, "music.ogg");
            mMusic.setLooping(true);
            mMusic.play();
            mMusic.pause();
            mMariachiFast = MusicFactory.createMusicFromAsset(mActivity.getEngine().getMusicManager(), mActivity, "mariachi-short-fast.ogg");
            mMariachiFast.setLooping(true);
            mMariachiFast.setVolume(0.5f);
            mMariachiFast.play();
            mMariachiFast.pause();
            mMariachiSlow = MusicFactory.createMusicFromAsset(mActivity.getEngine().getMusicManager(), mActivity, "mariachi-short.ogg");
            mMariachiSlow.setLooping(true);
            mMariachiSlow.setVolume(0.5f);
            mMariachiSlow.play();
            mMariachiSlow.pause();
        } catch (final IOException e) {
            Debug.e(e);
        }
    }

    public void unloadGameResources() {
            //unload gfx
        mAutoParallaxBackgroundTexture.unload();
        mBitmapTextureAtlas.unload();
        mSubBitmapTextureAtlas.unload();
        mTutorialBitmapTextureAtlas.unload();

        mAutoParallaxBackgroundTexture = null;
        mParallaxLayerFront = null;
        mParallaxLayerMiddle = null;
        mParallaxLayerBack = null;

        mBitmapTextureAtlas = null;
        mBirdTextureRegion = null;
        mCrapTextureRegion = null;
        mMegaCrapTextureRegion = null;
        mPipeTextureRegion = null;

        mObstacleHouseTextureRegion = null;
        mObstaclePlanesTextureRegion = null;
        mObstacleTreesTextureRegion = null;
        mObstacleBalloonTextureRegion = null;
        mObstacleMotherShipTextureRegion = null;
        mObstacleTrainCarFrontTextureRegion = null;
        mObstacleTrainCarSecondTextureRegion = null;
        mObstacleTrainCarPassengerTextureRegion = null;

        mCollectablePizzaTextureRegion = null;
        mCollectableTacoTextureRegion = null;
        mCollectableHamTextureRegion = null;
        mCollectableMelonTextureRegion = null;
        mCollectableMuffinTextureRegion = null;
        mCollectableBurgerTextureRegion = null;

        mTargetPerson1TextureRegion = null;

        mAlertTextureRegion = null;
        mPlusTwoTextureRegion = null;

        mTutorialTextureRegion = null;
        mTutorialBoardTextureRegion = null;

        mBirdsTextureRegion = null;
        mMarketBirdsTextureRegion = null;

        mStateTextureRegion = null;
        mMeterTextureRegion = null;
        mMeter2TextureRegion = null;
        mScoreBoardTextureRegion = null;
        mBoardTextureRegion = null;
        mHelpTextureRegion = null;
        mCountdownTextureRegion = null;
        mGooglePlayIconTextureRegion = null;

        mPlayButtonTextureRegion = null;
        mHelpButtonTextureRegion = null;
        mBackButtonTextureRegion = null;
        mTweetButtonTextureRegion = null;
        mFacebookButtonTextureRegion = null;
        mOtherButtonTextureRegion = null;
        mShareButtonTextureRegion = null;
        mRateButtonTextureRegion = null;
        mCloseButtonTextureRegion = null;
        mNextButtonTextureRegion = null;
        mLoginButtonTextureRegion = null;
        mSignUpButtonTextureRegion = null;
        mLaterButtonTextureRegion = null;
        mLeaderboardButtonTextureRegion = null;
        mLogoutButtonTextureRegion = null;
        mPauseButtonTextureRegion = null;
        mRestartButtonTextureRegion = null;
        mArrowLeftButtonTextureRegion = null;
        mArrowRightButtonTextureRegion = null;
        mBirdsButtonTextureRegion = null;
        mPowerUpsMarketButtonTextureRegion = null;
        mPurchaseButtonTextureRegion = null;
        mSelectButtonTextureRegion = null;
        mMarketButtonTextureRegion = null;
        mYesButtonTextureRegion = null;
        mNoButtonTextureRegion = null;
        mGetMorePizzaButtonTextureRegion = null;
        mMenuButtonTextureRegion = null;
        mVolumeButtonTextureRegion = null;
        mAchievementsButtonTextureRegion = null;
        mGooglePlayButtonTextureRegion = null;
        mMoreOptionsButtonTextureRegion = null;
        mAdFreeButtonTextureRegion = null;
        mAboutButtonTextureRegion = null;

        mTitleTextureRegion = null;
            //unload fonts
        mFont4.unload();
        mFont4 = null;

        mFont2.unload();
        mFont2 = null;

        mFont3.unload();
        mFont3 = null;

        mFont5.unload();
        mFont5 = null;

        mFont6.unload();
        mFont6 = null;

            //unload sound/music
        mHitSound.release();
        mHitSound = null;

        mCoinSound.release();
        mCoinSound = null;

        mButtonSound.release();
        mButtonSound = null;

        mJumpSound.release();
        mJumpSound = null;

        mMegaCrapSound.release();
        mMegaCrapSound = null;

        mMotherShipSound.release();
        mMotherShipSound = null;

        mPropellerSound.release();
        mPropellerSound = null;

        mAlertSound.release();
        mAlertSound = null;

        mCollectionSound.release();
        mCollectionSound = null;

        mWilhelmScreamSound.release();
        mWilhelmScreamSound = null;

        mRespawnSound.release();
        mRespawnSound = null;

        mTrainSound.release();
        mTrainSound = null;

        mMusic.stop();
        mMusic.release();
        mMusic = null;

        mMariachiFast.stop();
        mMariachiFast.release();
        mMariachiFast = null;

        mMariachiSlow.stop();
        mMariachiSlow.release();
        mMariachiSlow = null;

    }
}
