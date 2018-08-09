package com.ormisiclapps.flappydunkermadness.game.entities.notphysical;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.game.entities.Camera;
import com.ormisiclapps.flappydunkermadness.utility.Configuration;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;

/**
 * Created by OrMisicL on 9/13/2016.
 */
public class TerrainObject
{
    private TextureRegion texture;
    private Vector2 position;
    protected Vector2 screenPosition;
    protected Vector2 screenSize;
    private Vector2 size;

    public TerrainObject(String model, Vector2 position)
    {
        // Get the object's terrain
        texture = Core.getInstance().getResourcesManager().getResource(model, ResourceType.RESOURCE_TYPE_MODEL);
        // Save the object's coordinates
        this.position = new Vector2(position);
        // Get the model's size
        float x = MathUtils.random(Configuration.TERRAIN_OBJECT_HEIGHT * 2f, Configuration.TERRAIN_OBJECT_HEIGHT * 4f);
        float y = Configuration.TERRAIN_OBJECT_HEIGHT;
        // Set the size
        size = new Vector2(y, x);
        // Convert to screen coordinates
        screenSize = new Vector2(GameMath.pixelsPerMeters(size));
        // Create the screen position vector
        screenPosition = new Vector2();
    }

    public void process()
    {
        // Convert to screen coordinates
        screenPosition.set(Camera.getInstance().worldToScreen(position));
    }

    public void render()
    {
        // Validate rendering
        if(screenPosition != null && screenSize != null)
        {
            // Restore color
            Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
            // Draw the texture
            Core.getInstance().getGraphicsManager().drawTextureRegion(texture, screenPosition, screenSize, 270f);
        }
    }

    public Vector2 getPosition() { return position; }
    public Vector2 getSize() { return size; }
}
