package com.ormisiclapps.flappydunkermadness.utility;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by OrMisicL on 5/29/2016.
 */
public class Configuration
{
        //**** Graphics configuration ****//
    public static final float BACKGROUND_COLOR = 47f / 255f;
    public static final Color PROGRESSBAR_CONTAINER_COLOR = new Color(0f, 0f, 0f, 1f);
    public static final Color PROGRESSBAR_BACKGROUND_COLOR = new Color(0.7f, 0.7f, 0.7f, 1f);
    public static final Color PROGRESSBAR_COLOR = new Color(0.2f, 0.4f, 0.6f, 1f);

        //**** Physics configuration ****//
    // Terrain properties
    public static final float WORLD_WIDTH = 22f;

    // Objects properties
    public static final float HOOP_SIDE_SIZE = 0.6722f;
    public static final float MOVABLE_OBJECTS_SPEED = 3f;
    public static final float DISTANCE_BETWEEN_OBJECTS = WORLD_WIDTH * 0.65f;
    public static final float TERRAIN_OBJECT_HEIGHT = 1f;

    // Player properties
    public static final float MAX_HORIZONTAL_VELOCITY = 6.75f;
    public static final float FLAP_IMPULSE = 1550f;
}
