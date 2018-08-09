package com.ormisiclapps.flappydunkermadness.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by OrMisicL on 9/22/2015.
 */
class InputProcessorHandler implements InputProcessor
{
    private Vector2 downPosition;

    public InputProcessorHandler()
    {
        // Create the down position vector
        downPosition = new Vector2();
    }

    public boolean touchDown(int x, int y, int pointer, int button)
    {
        // Record the down position
        downPosition.set(x, y);
        // Report the touch
        com.ormisiclapps.flappydunkermadness.core.Core.getInstance().getInputHandler().registerInputEvent(com.ormisiclapps.flappydunkermadness.enumerations.ScreenTouchType.SCREEN_TOUCH_DOWN, x, y);
        return false;
    }

    public boolean touchUp(int x, int y, int pointer, int button)
    {
        // Report the touch
        com.ormisiclapps.flappydunkermadness.core.Core.getInstance().getInputHandler().registerInputEvent(com.ormisiclapps.flappydunkermadness.enumerations.ScreenTouchType.SCREEN_TOUCH_UP, x, y);
        return false;
    }

    public boolean touchDragged(int x, int y, int pointer)
    {
        // If the dragging coordinates are the same as down then don't trigger it
        if(downPosition.x != x || downPosition.y != y)
            com.ormisiclapps.flappydunkermadness.core.Core.getInstance().getInputHandler().registerInputEvent(com.ormisiclapps.flappydunkermadness.enumerations.ScreenTouchType.SCREEN_TOUCH_DRAGGED, x, y);
        else
        {
            // Record the down position
            downPosition.set(x, y);
            // Report a down press
            com.ormisiclapps.flappydunkermadness.core.Core.getInstance().getInputHandler().registerInputEvent(com.ormisiclapps.flappydunkermadness.enumerations.ScreenTouchType.SCREEN_TOUCH_DOWN, x, y);
        }
        return false;
    }

    /* Begin of keyboard events (used for desktop testing) */
    public boolean keyDown (int keycode)
    {
    	//Core.getInstance().getInputHandler().registerKeyBoardEvent(keycode, true);
        return false;
    }

    public boolean keyUp (int keycode)
    {
    	//Core.getInstance().getInputHandler().registerKeyBoardEvent(keycode, false);
        return false;
    }

    public boolean keyTyped (char character) {
        return false;
    }
    /* End of keyboard events */

    /* Begin of mouse events (unused) */
    public boolean mouseMoved (int x, int y)
    {
    	com.ormisiclapps.flappydunkermadness.core.Core.getInstance().getInputHandler().registerMouseMovement(x, y);
        return false;
    }

    public boolean scrolled (int amount) {
        return false;
    }

    /* End of mouse events */
}

