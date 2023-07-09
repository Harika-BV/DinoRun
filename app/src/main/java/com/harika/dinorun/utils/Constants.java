package com.harika.dinorun.utils;

import android.graphics.Color;

public class Constants {
    public static final int SCREEN_WIDTH = 720; // Replace with your desired screen width
    public static final int SCREEN_HEIGHT = 1280; // Replace with your desired screen height

    public static final int CACTUS_MIN_HEIGHT = SCREEN_HEIGHT / 4; // Minimum height of a spawned cactus
    public static final int CACTUS_MAX_HEIGHT = SCREEN_HEIGHT / 2; // Maximum height of a spawned cactus

    public static final int CACTUS_SPEED = 10;
    public static final int BACKGROUND_COLOR = Color.WHITE;
    public static final int FPS = 60;
    public static final int GAME_SPEED = 1000 / FPS;

    public static final int DINO_HEIGHT = 100;

    public static final int CACTUS_HEIGHT = 100;

    public static final int CACTUS_SPAWN_DISTANCE = 500;

    public static final boolean DEBUG_MODE = true;

    public static final float EARTH_GROUND_STROKE_WIDTH = 3f;
    public static final float EARTH_Y_POSITION = SCREEN_HEIGHT;

    public static final float MIN_Y_POSITION = 200f;
    public static final float MAX_Y_POSITION = 400f;

    public static final float CLOUD_SPACING = 200f;
    public static final float CLOUD_VELOCITY = -2f;


}
