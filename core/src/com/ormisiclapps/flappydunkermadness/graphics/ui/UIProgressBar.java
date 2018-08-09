package com.ormisiclapps.flappydunkermadness.graphics.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;

/**
 * Created by OrMisicL on 5/31/2016.
 */
public class UIProgressBar extends UIWidget
{
    private Color containerColor;
    private Color backgroundColor;
    private Color color;
    private Texture emptyTexture;
    private int progress;
    private Vector2 currentSize;
    private float containerSize;

    public UIProgressBar(Vector2 position, Vector2 size, Color containerColor, Color backgroundColor, Color color)
    {
        // Call the UIWidget constructor
        super(false, position, size);
        // Save colors
        this.color = color;
        this.backgroundColor = backgroundColor;
        this.containerColor = containerColor;
        // Create size vector
        currentSize = new Vector2();
        // Reset values
        progress = 0;
        containerSize = 0f;
        // Get the texture
        emptyTexture = Core.getInstance().getResourcesManager().getResource("Empty", ResourceType.RESOURCE_TYPE_TEXTURE);
        // Initialize the container's size
        containerSize = getSize().y / 10f;
    }

    @Override
    public void dispose()
    {
        // We don't have any disposable materials for this widget
    }

    @Override
    public void render()
    {
        // Calculate the current size based on the progress
        currentSize.set(getSize().x * progress / 100f, getSize().y);
        // Set the background color
        Core.getInstance().getGraphicsManager().setColor(backgroundColor);
        // Draw the background
        Core.getInstance().getGraphicsManager().drawTexture(emptyTexture, getPosition().x, getPosition().y, getSize().x, getSize().y, 0f);
        // Set the main color
        Core.getInstance().getGraphicsManager().setColor(color);
        // Draw the shape
        Core.getInstance().getGraphicsManager().drawTexture(emptyTexture, getPosition(), currentSize, 0f);
        // Set the container color
        Core.getInstance().getGraphicsManager().setColor(containerColor);
        // Draw the container

        // Left
        Core.getInstance().getGraphicsManager().drawTexture(emptyTexture, getPosition().x - containerSize, getPosition().y,
                containerSize, getSize().y, 0f);

        // Right
        Core.getInstance().getGraphicsManager().drawTexture(emptyTexture, getPosition().x + getSize().x, getPosition().y,
                containerSize, getSize().y, 0f);

        // Top
        Core.getInstance().getGraphicsManager().drawTexture(emptyTexture, getPosition().x - containerSize, getPosition().y + getSize().y,
                getSize().x + containerSize * 2f, containerSize, 0f);

        // Bottom
        Core.getInstance().getGraphicsManager().drawTexture(emptyTexture, getPosition().x - containerSize, getPosition().y - containerSize,
                getSize().x + containerSize * 2f, containerSize, 0f);

        // Restore color
        Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
    }

    public void setProgress(int progress)
    {
        if(progress > 100)
            this.progress = 100;
        else if(progress < 0)
            this.progress = 0;
        else
            this.progress = progress;
    }
}
