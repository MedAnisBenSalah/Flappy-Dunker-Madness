package com.ormisiclapps.flappydunkermadness.utility;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by OrMisicL on 5/29/2016.
 */
public class GameMath
{
    public static float PPM;
    private static final Vector2 tmpVector = new Vector2();

    public static void initializePPM(float width)
    {
        PPM = width / 22f;
    }

    public static Vector2 getCenteredPosition(Vector2 position, Vector2 size)
    {
        return tmpVector.set(position.x - size.x / 2, position.y - size.y / 2);
    }

    /*****************************************************
     * Pixels and Meters conversion
     ****************************************************/

    public static float pixelsPerMeters(float meters)
    {
        return meters*PPM;
    }

    public static float metersPerPixels(float pixels)
    {
        return pixels/PPM;
    }

    public static Vector2 pixelsPerMeters(Vector2 meters)
    {
        return tmpVector.set(meters.x*PPM, meters.y*PPM);
    }

    /*****************************************************
     * Rotation and angle operations
     ****************************************************/

    public static float radiansToDegrees(float angle)
    {
        // Calculate the new angle
        float newAngle = angle * MathUtils.radiansToDegrees;
        if(newAngle < 0)
            newAngle += 360;
        else if(newAngle >= 360)
            newAngle -= 360;

        return newAngle;
    }

    public static float degreesToRadians(float angle)
    {
        // Calculate the new angle
        float newAngle = angle * MathUtils.degreesToRadians;
        if(newAngle % (Math.PI * 2) >= 0 && newAngle >= Math.PI)
            newAngle -= Math.PI * 2;
        else if(angle < -Math.PI)
            newAngle += Math.PI * 2;

        return newAngle;
    }

    public static float adjustAngleInDegrees(float angle)
    {
        // Fix the angle interval
        if(angle >= 360)
            return angle - 360;
        else if(angle < 0)
            return angle + 360;
        else
            return angle;
    }

    /*****************************************************
     * Vectors operations
     ****************************************************/

    public static double getDistanceBetweenVectors(Vector2 firstVector, Vector2 secondVector)
    {
        // Get the distance between points
        //tmpVector.set(secondVector.x - firstVector.x, secondVector.y - firstVector.y);
        return firstVector.dst(secondVector);
    }

    public static float getAngleBetweenVectors(Vector2 origin, Vector2 target)
    {
        float angle = radiansToDegrees((float)Math.atan2(target.y - origin.y, target.x - origin.x));
        return angle < 0 ? angle + 360 : angle;
    }

    /*****************************************************
     * Other math utils
     ****************************************************/

    public static boolean checkCollision(Vector2 position1, Vector2 size1, Vector2 position2, Vector2 size2)
    {
        // Calculate the sizes sum
        float width = (size1.x + size2.x) / 2f;
        float height = (size1.y + size2.y) / 2f;
        // Calculate the position difference
        float x = Math.abs(position1.x - position2.x);
        float y = Math.abs(position1.y - position2.y);
        // Check collision
        return x <= width && y <= height;
    }
}
