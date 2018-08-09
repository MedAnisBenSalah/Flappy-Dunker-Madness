package com.ormisiclapps.flappydunkermadness.graphics.effects;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by OrMisicL on 1/5/2016.
 * Will handle general screen effects (fading)
 */
public class ScreenEffects
{
    private boolean faded;
    private Color fadeColor;

    private static ScreenEffects instance;

    public ScreenEffects()
    {
        // Reset flags
        faded = false;
        // Reset values
        // Create the color instances
        fadeColor = new Color();

        // Set instance
        instance = this;
    }

    public void fadeIn(Color color, float alpha)
    {
        // Set the fade parameters
        faded = true;
        fadeColor.set(color.r, color.g, color.b, alpha);
    }

    public void fadeOut()
    {
        // Ensure the we're already faded
        if(!faded)
            return;

        // Reset the fade parameters
        faded = false;
    }

    public boolean isFaded() { return faded; }

    public Color getFadeColor() { return fadeColor; }

    public static ScreenEffects getInstance() { return instance; }
}
