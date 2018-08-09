package com.ormisiclapps.flappydunkermadness.game.entities.physical.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.game.entities.Camera;
import com.ormisiclapps.flappydunkermadness.game.entities.movement.Movement;
import com.ormisiclapps.flappydunkermadness.game.nodes.movement.MovementNode;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;

/**
 * Created by Anis on 11/5/2016.
 */

public class MovableGameObject extends GameObject
{
    private Movement movement;
    private TextureRegion railTexture;
    private boolean shouldRender;
    private Vector2 tmpVector;
    private Vector2 tmpVector2;
    private Vector2 railPosition, railSize;
    private Color color;

    private static final float RAIL_SIZE = 0.15f;

    public MovableGameObject(String model)
    {
        // Create the game object
        super(model);
        // Get the rail texture
        railTexture = Core.getInstance().getResourcesManager().getResource("Empty", ResourceType.RESOURCE_TYPE_MODEL);
        // Create vectors
        tmpVector = new Vector2();
        tmpVector2 = new Vector2();
        railPosition = new Vector2();
        railSize = new Vector2();
        // Create color
        color = new Color(0.5234f, 0.5234f, 0.5234f, 1f);
        // Reset flags
        shouldRender = false;
    }

    public void create(Vector2 position, float rotation, float sizeFactor, int movementId, float movementRotation, float speed)
    {
        // Create the object
        super.create(rotation, sizeFactor);
        // Get the movement nodes
        MovementNode[] movementNodes = Core.getInstance().getModelManager().getMovementNodes(getModel().getName());
        // Create the movement
        movement = new Movement(movementNodes[movementId], position, movementRotation, speed, this);
    }

    @Override
    public void process()
    {
        // Process movement
        if(movement != null)
            movement.process(this);

        // Get the screen coordinates for the lowest and highest points of the movement
        tmpVector.set(Camera.getInstance().worldToScreen(movement.getLowestPoint()));
        tmpVector2.set(Camera.getInstance().worldToScreen(movement.getHighestPoint()));
        // Check if we need to render the movement rails
        shouldRender = (tmpVector.x <= Core.getInstance().getGraphicsManager().WIDTH && tmpVector2.x >= 0 &&
                tmpVector.y <= Core.getInstance().getGraphicsManager().HEIGHT && tmpVector2.y >= 0);

        // Skip if we're not rendering this frame
        if(!shouldRender)
            return;

        // Process the game object
        super.process();
        // Convert the rail size
        float railHeight = GameMath.pixelsPerMeters(RAIL_SIZE);
        float railLength = GameMath.pixelsPerMeters(movement.getDistance());
        // Is it a horizontal movement ?
        if(movement.isHorizontal())
        {
            // Set the size
            railSize.set(railLength + screenSize.y, railHeight);
            // Set the rail's position
            railPosition.set(tmpVector).sub(screenSize.y / 2f, 0f);
        }
        else
        {
            // Set the size
            railSize.set(railLength + screenSize.x, railHeight);
            // Set the rail's position
            railPosition.set(tmpVector).sub(screenSize.x / 2f, 0f);
        }
        // Update color
        color.a = getColor().a;
    }

    private void drawPath()
    {
        // Skip if we're not rendering this frame
        if(!shouldRender)
            return;

        // Get points
        tmpVector.set(Camera.getInstance().worldToScreen(movement.getLowestPoint()));
        tmpVector2.set(Camera.getInstance().worldToScreen(movement.getHighestPoint()));
        // Draw the rail
        float rotation = movement.getRotation();
        Core.getInstance().getGraphicsManager().setColor(color);
        Core.getInstance().getGraphicsManager().drawTextureRegion(railTexture, railPosition, railSize, rotation,
                movement.isHorizontal() ? tmpVector.set(screenSize.y / 2f, 0f) : tmpVector.set(screenSize.x / 2f, 0f));

        // Restore color
        Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
    }

    @Override
    public void render()
    {
        // Draw path
        drawPath();
        // Draw the entity
        super.render();
    }

    public Movement getMovement() {
        return movement;
    }
}
