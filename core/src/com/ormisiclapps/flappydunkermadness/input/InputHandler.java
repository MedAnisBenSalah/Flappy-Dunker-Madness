package com.ormisiclapps.flappydunkermadness.input;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ScreenTouchType;

/**
 * Created by OrMisicL on 9/22/2015.
 */
public class InputHandler
{
    private Vector2 lastScreenTouchPosition;
    private Vector2 currentScreenTouchPosition;
    private ScreenTouchType screenTouchType;
    private boolean screenTouched;
    private boolean unreportedTouch;
    private boolean tapped, holdingDown;

    private static InputHandler instance;

    public InputHandler()
    {
        // Create the position vectors
        currentScreenTouchPosition = new Vector2();
        lastScreenTouchPosition = new Vector2();
        // Reset screen move type
        screenTouchType = ScreenTouchType.SCREEN_TOUCH_NONE;
        // Reset flags
        screenTouched = false;
        unreportedTouch = false;
        tapped = false;
        holdingDown = false;
        // Set the input processor handler
        Gdx.input.setInputProcessor(new InputProcessorHandler());
        // Set instance
        instance = this;
    }

    public void clear()
    {
        // If the screen is not touched anymore then clear the input
        if(screenTouchType == ScreenTouchType.SCREEN_TOUCH_UP)
        {
            // Reset screen move type
            screenTouchType = ScreenTouchType.SCREEN_TOUCH_NONE;
            // Set the touch position
            lastScreenTouchPosition.set(currentScreenTouchPosition);
            currentScreenTouchPosition.set(0, 0);
            // Reset the touched flag
            screenTouched = false;
        }
        // Reset the unreported touch flag
        unreportedTouch = false;
        // Reset the tapped flag
        tapped = false;
    }

    /*
        This is used to resolve the conflict between the player's and UI input
        Mostly used to to safely pause the game without interfering with the game's input
    */
    public void unreportTouch()
    {
        // Set the unreported touch flag
        unreportedTouch = true;
    }

    protected void registerInputEvent(ScreenTouchType touchType, float x, float y)
    {
        // Set the screen touch type
        screenTouchType = touchType;
        // Set last touch position
        lastScreenTouchPosition.set(currentScreenTouchPosition);
        // Set the touch position
        currentScreenTouchPosition.set(x, Math.abs(Core.getInstance().getGraphicsManager().HEIGHT - y));
        // Set the touched flag
        screenTouched = true;
        // Touch down ?
        if(touchType == ScreenTouchType.SCREEN_TOUCH_DOWN || touchType == ScreenTouchType.SCREEN_TOUCH_DRAGGED)
        {
            if(!holdingDown)
                tapped = true;

            holdingDown = true;
        }
        else
            holdingDown = false;
    }
    
    protected void registerMouseMovement(float x, float y)
    {
       	// Are we on windows ?
    	if(Gdx.app.getType() == ApplicationType.Desktop && screenTouchType == ScreenTouchType.SCREEN_TOUCH_DOWN)
    		// Set the movement
    		registerInputEvent(ScreenTouchType.SCREEN_TOUCH_DRAGGED, x, y);
    }
    
    public boolean isScreenTouched() { return screenTouched; }
    public Vector2 getScreenTouchPosition() { return currentScreenTouchPosition; }
    public Vector2 getTouchMovementPosition() { return lastScreenTouchPosition.x == 0 && lastScreenTouchPosition.y == 0 ? new Vector2() : new Vector2(currentScreenTouchPosition).sub(lastScreenTouchPosition); }
    public ScreenTouchType getScreenTouchType() { return screenTouchType; }

    public boolean isUnreportedTouch() {
        return unreportedTouch;
    }

    public boolean isTapped() { return tapped || Gdx.input.isKeyJustPressed(Input.Keys.A); }

    public static InputHandler getInstance() { return instance; }
}

