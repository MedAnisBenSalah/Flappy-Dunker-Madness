package com.ormisiclapps.flappydunkermadness.graphics.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;
import com.ormisiclapps.flappydunkermadness.audio.GameSound;

/**
 * Created by OrMisicL on 9/22/2015.
 */
public class UIMain
{
    private Array<UIWidget> widgets;
    private FreeTypeFontGenerator fontGenerator;
    private GameSound buttonClickSound;
    private BitmapFont defaultFont;

    private static UIMain instance;

    public UIMain()
    {
        // Reset instances
        widgets = null;
        buttonClickSound = null;
        fontGenerator = null;
        defaultFont = null;
        // Set the instance
        instance = this;
    }

    public void initialize()
    {
        // Create the widgets array
        widgets = new Array<UIWidget>();
        // Get the font
        FileHandle font = Gdx.files.internal("Fonts/Main.ttf");
        // Create font type generators
        FreeTypeFontGenerator.setMaxTextureSize(FreeTypeFontGenerator.NO_MAXIMUM);
        fontGenerator = new FreeTypeFontGenerator(font);
        // Create the font parameter instance
        FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        // Set the parameters
        parameters.color = Color.BLACK;
        parameters.size = 15;
        parameters.borderWidth = 0f;
        parameters.borderColor = null;
        // Generate a default font to use whenever an exception occurs
        defaultFont = fontGenerator.generateFont(parameters);
    }

    public void dispose()
    {
        // Destroy all the widgets
        for(UIWidget widget : widgets)
            widget.dispose();

        // Destroy the font generator
        fontGenerator.dispose();
    }

    public void preFadeRender()
    {
        // Render the main text
        //mainText.render();
        // Loop through all the widgets
        for(UIWidget widget : widgets)
        {
            // Render it
            if(widget.isVisible() && widget.isFadeEffected())
                widget.render();
        }
    }

    public void postFadeRender()
    {
        // Loop through all the widgets
        for(UIWidget widget : widgets)
        {
            // Render it
            if(widget.isVisible() && !widget.isFadeEffected())
                widget.render();
        }
    }

    public void process()
    {
        // Get the button click sound
        if(buttonClickSound == null && com.ormisiclapps.flappydunkermadness.core.Core.getInstance().getResourcesManager().isResourceLoaded("ButtonClick", com.ormisiclapps.flappydunkermadness.enumerations.ResourceType.RESOURCE_TYPE_SOUND))
            buttonClickSound = new com.ormisiclapps.flappydunkermadness.audio.GameSound("ButtonClick");

        // Loop through all the widgets
        for(UIWidget widget : widgets)
        {
            // Update it
            if(widget.isVisible())
                widget.process();
        }
    }

    protected BitmapFont generateFont(FreeTypeFontGenerator.FreeTypeFontParameter parameter)
    {
        try
        {
            return fontGenerator.generateFont(parameter);
        }
        catch(Exception e)
        {
            // Simply use the default font if we run into this exception
            return defaultFont;
        }
    }

    public void addWidget(UIWidget widget)
    {
        widgets.add(widget);
    }

    public GameSound getButtonClickSound() { return buttonClickSound; }

    public static UIMain getInstance()
    {
        return instance;
    }
}
