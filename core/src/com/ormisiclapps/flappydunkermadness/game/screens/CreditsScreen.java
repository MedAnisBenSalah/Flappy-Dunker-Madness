package com.ormisiclapps.flappydunkermadness.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIText;

/**
 * Created by OrMisicL on 7/30/2017.
 */

// Will be implemented in the next version
public class CreditsScreen implements Screen
{
    private UIText titleText, text;
    private Texture backgroundTexture;

    public CreditsScreen()
    {
        // Reset instances
        titleText = null;
        text = null;
        backgroundTexture = null;
    }

    @Override
    public void initialize()
    {
        // Create texts
        titleText = new UIText(Core.getInstance().getGraphicsManager().HEIGHT / 20, Color.WHITE, 0f, Color.WHITE);
        text = new UIText(Core.getInstance().getGraphicsManager().HEIGHT / 40, Color.WHITE, 0f, Color.WHITE);
        // Get the background texture
        backgroundTexture = Core.getInstance().getResourcesManager().getResource("Empty", ResourceType.RESOURCE_TYPE_TEXTURE);
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void update() {

    }

    @Override
    public void render()
    {
        // Set the background color
        Core.getInstance().getGraphicsManager().setColor(Color.BLACK);
        // Draw the background
        Core.getInstance().getGraphicsManager().drawTexture(backgroundTexture, Core.getInstance().getGraphicsManager().EMPTY_VECTOR,
                Core.getInstance().getGraphicsManager().SCREEN_VECTOR, 0f);

        // Restore the color
        Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
    }

    @Override
    public void postFadeRender() {

    }

    @Override
    public void dispose() {

    }
}
