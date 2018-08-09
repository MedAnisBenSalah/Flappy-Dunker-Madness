package com.ormisiclapps.flappydunkermadness.graphics.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ScreenTouchType;
import com.ormisiclapps.flappydunkermadness.input.InputHandler;

/**
 * Created by OrMisicL on 1/5/2016.
 */
public abstract class UIWidget
{
    private boolean visible;
    private boolean enabled;
    private Rectangle rectangle, clickRectangle;
    protected boolean clicked;
    protected boolean clicking;
    private boolean clickable;
    private boolean fadeEffect;
    private boolean rectangular;
    private Vector2 tmpVector = new Vector2();

    public UIWidget(boolean rectangular, boolean clickable)
    {
        // Reset values
        visible = false;
        clicked = false;
        clicking = false;
        fadeEffect = true;
        enabled = true;
        // Set flags
        this.clickable = clickable;
        this.rectangular = rectangular;
        // Create the rectangle instance
        rectangle = new Rectangle();
        clickRectangle = new Rectangle();
    }

    public UIWidget(boolean clickable, Vector2 position, Vector2 size)
    {
        this(false, clickable);
        // Set coordinates
        setPosition(position);
        setSize(size);
    }

    public UIWidget(boolean clickable)
    {
        this(false, clickable);
    }

    public abstract void dispose();
    public abstract void render();

    public void process()
    {
        // Ensure we're clickable
        if(!clickable || !enabled)
            return;

        // Reset the clicked flags
        clicked = false;
        clicking = false;
        // Get the clicked position
        Vector2 position = InputHandler.getInstance().getScreenTouchPosition();
        // Check if its clicked
        if(InputHandler.getInstance().isScreenTouched() && clickRectangle.contains(position.x, position.y))
        {
            // Handle click type
            if(InputHandler.getInstance().getScreenTouchType() == ScreenTouchType.SCREEN_TOUCH_DOWN ||
                    InputHandler.getInstance().getScreenTouchType() == ScreenTouchType.SCREEN_TOUCH_DRAGGED)
            {
                // Stop the down press reporting
                Core.getInstance().getInputHandler().unreportTouch();
                // Set the clicking flag
                clicking = true;
            }
            else if(InputHandler.getInstance().getScreenTouchType() == ScreenTouchType.SCREEN_TOUCH_UP)
            {
                // Set the clicked flag
                clicked = true;
                // Reset the clicking flag
                clicking = false;
            }
        }
    }

    public Vector2 getPosition()
    {
        rectangle.getPosition(tmpVector);
        return tmpVector;
    }

    public Vector2 getSize()
    {
        rectangle.getSize(tmpVector);
        return tmpVector;
    }

    public boolean isVisible() { return visible; }
    public boolean isClicked() { return clicked; }
    public boolean isClicking() { return clicking; }
    public boolean isFadeEffected() { return fadeEffect; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public void setFadeEffected(boolean toggle) { fadeEffect = toggle; }

    public void toggle(boolean toggle)
    {
        visible = toggle;
        if(!visible)
            clicked = false;
    }

    public void setPosition(Vector2 position)
    {
        rectangle.setPosition(position);
        //clickRectangle.setPosition(position.x - rectangle.x * 0.25f, position.y - rectangle.y * 0.25f);

        if(rectangular)
            clickRectangle.set(rectangle.x - rectangle.width * 0.375f, rectangle.y + rectangle.height * 1.5f,
                    rectangle.width, rectangle.height);
        else
            clickRectangle.set(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void setSize(Vector2 size)
    {
        rectangle.setSize(size.x, size.y);
        //clickRectangle.setSize(size.x * 1.5f, size.y * 1.5f);
        //clickRectangle.setPosition(rectangle.x - size.x * 0.25f, rectangle.y - size.y * 0.25f);
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public void setClicking(boolean clicking) {
        this.clicking = clicking;
    }
}
