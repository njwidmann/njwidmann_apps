package com.application.nick.crappybird;

/**
 * Created by Nick on 7/28/2015.
 */
public interface GameValues {
    int SCREEN_WIDTH = GameActivity.CAMERA_WIDTH;
    int SCREEN_HEIGHT = GameActivity.CAMERA_HEIGHT;


    boolean DEBUGGING = false;

    String TAG = "CrappyBird";

    enum Direction{LEFT, RIGHT, STOPPED, UP, DOWN, CENTER}
    enum Entities{CRAP, BIRD, SCENERY, COLLECTABLE, PERSON, WALL, MOTHERSHIP, OBSTACLE}

    int BIRD_FRAME_DURATION = 200;
    float BIRD_GRAVITY = 15f * 30;
    float BIRD_JUMP_VELOCITY = 5 * 30;
    float SCROLL_VELOCITY = 150.0f;
    float PERSON_VELOCITY_RANGE = 100.0f;
    float PERSON_STARTING_POSITION = 1.5f* GameActivity.CAMERA_WIDTH;
    float OBSTACLE_STARTING_POSITION = 1.1f * SCREEN_WIDTH;
    float MOTHER_SHIP_VELOCITY = 1500.0f;
    float GROUND_HEIGHT = 112;
    float PLANE_VELOCITY = 200f;
    float MAX_PLANE_VERTICAL_VELOCITY = 50;
    int PLANE_FRAME_DURATION = 100;
    float TRAIN_DRAG_CONSTANT = .5f;
    float TRAIN_VELOCITY = 450;
    float CRAP_FALLING_X_VELOCITY = 32.0f;
    float CRAP_FALLING_Y_VELOCITY_INITIAL = 32.0f * 5;
    float CRAP_FALLING_Y_ACCELERATION = 9.8f * 32;
    float COLLECTABLE_VELOCITY_RANGE = 100.0f;
    float COLLECTABLE_STARTING_X = 1.5f* GameActivity.CAMERA_WIDTH;
    float COLLECTABLE_ANGULAR_VELOCITY = 50;
    float HYPER_SPEED_VELOCITY_SHIFT = 300;
    float HYPER_SPEED_CRAP_VELOCITY_X_SHIFT = 500;
    float SCROLL_SPEED = 150;
    float GRAVITY = 15f * 30;
    float HYPER_SPEED_Y_ACCELERATION = 5f * 30;
    float DEFAULT_MACHINE_CRAPPING_TIME = 5;
    float MAX_INVINCIBILITY_TIME = 2;
    float MACHINE_CRAPPING_TIME_BETWEEN_CRAPS = 0.1f;
    float HYPER_SPEED_TIME_BETWEEN_CRAPS = 0.1f;
    float DEFAULT_DOUBLE_POINTS_TIME = 5;
    float MAX_TARGETS_ON_SCREEN = 10;
    float DOUBLE_POINTS_TIME_TO_SHOW_PLUS_TWO = 0.4f;
    float MAX_COLLECTABLES_ON_SCREEN = 5;
    float MIN_PLANE_SPEED_ON_GAMEOVER = 60;
    int PARALLAX_CHANGE_PER_SECOND = 10;
    int NUM_OBSTACLES = 20; //number of obstacle to allocate to pool
    int NUM_TARGETS = 10; //number of targets (people) to allocate
    int NUM_CRAPS = 15; //number of craps to allocate
    int MOTHERSHIP_OBSTACLE_INDEX = 35; //the obstacle index to have the mothership fly across the screen
    int TRAIN_OBSTACLE_INDEX = 80; //the obstacle index to have the train come across the screen
    int RESPAWN_PRICE = 200;
    int CRAP_METER_SHRINK_STOP_OBSTACLE_INDEX = 50; //the obstacle index to stop having the crap meter rate of shrinkage increase
    float TRAIN_INCOMING_TIME = 3f;
    int MAX_TUTORIAL_TILE_INDEX = 3;
    float MAX_POWER_UP_TIME = 10; //seconds

    int GAMES_TO_PLAY_BEFORE_SHOWING_AD = 3;



    int[] MAX_OBSTACLES_ON_SCREEN = {1, 2, 3, 4, 5, 6};

    int[] MAX_OBSTACLES_ON_SCREEN_OBSTACLE_INDEX = {0, 3, 10, 100, 250, 450};

    int[] NUM_COLLECTABLES = { //number of generic collectables (pizza) to allocate to pool
            20, //if player only has first 2 power-ups unlocked
            25, //if player has three power-ups
            30,  //if player has 4 power-ups
            35 //if player has 5 power-ups
    };


    //ACHIEVEMENTS
    int ACH_SCORE_1 = 20;
    int ACH_SCORE_2 = 150;
    int ACH_SCORE_3 = 300;
    int ACH_SCORE_4 = 600;
    int ACH_SCORE_5 = 1000;
    int ACH_FOOD_IN_ONE_GAME = 50;
    int ACH_IN_A_ROW_NUM = 3;
    int ACH_IN_A_ROW_1 = 100;
    int ACH_IN_A_ROW_2 = 300;
    int ACH_BALLOON_KILLS = 20;
    int ACH_BURGERS = 3;
    int ACH_KILLS_IN_GAME = 50;






}
