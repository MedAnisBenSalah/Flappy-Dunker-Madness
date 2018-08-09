package com.ormisiclapps.flappydunkermadness.game.entities.movement;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ormisiclapps.flappydunkermadness.enumerations.MovementPointType;
import com.ormisiclapps.flappydunkermadness.game.core.GameLogic;
import com.ormisiclapps.flappydunkermadness.utility.Configuration;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;

/**
 * Created by Anis on 10/30/2016.
 */

public class MovementPoint
{
    private MovementPointType pointX;
    private MovementPointType pointY;
    private Vector2 position;

    public MovementPoint(Vector2 position, float movementRotation, String pointX, String pointY)
    {
        // Find the movement types for each axis
        this.pointX = getMovementPointType(pointX);
        this.pointY = getMovementPointType(pointY);
        // Get the roof position
        float distance = GameLogic.getInstance().isHorizontal() ? Configuration.WORLD_WIDTH / 3f :
                Configuration.WORLD_WIDTH / 2f;

        // Get the x axis position
        float x = position.x;
        float y = position.y;
        if (this.pointX == MovementPointType.MOVEMENT_POINT_TYPE_LEFT)
        {
            x -= distance * MathUtils.cos(GameMath.degreesToRadians(movementRotation));
            y -= distance * MathUtils.sin(GameMath.degreesToRadians(movementRotation));
        }
        else if (this.pointX == MovementPointType.MOVEMENT_POINT_TYPE_RIGHT)
        {
            x += distance * MathUtils.cos(GameMath.degreesToRadians(movementRotation));
            y += distance * MathUtils.sin(GameMath.degreesToRadians(movementRotation));
        }
        else if (this.pointY == MovementPointType.MOVEMENT_POINT_TYPE_DOWN)
        {
            x += distance * MathUtils.sin(GameMath.degreesToRadians(movementRotation));
            y += distance * MathUtils.cos(GameMath.degreesToRadians(movementRotation));
        }
        else if (this.pointY == MovementPointType.MOVEMENT_POINT_TYPE_UP)
        {
            x -= distance * MathUtils.sin(GameMath.degreesToRadians(movementRotation));
            y -= distance * MathUtils.cos(GameMath.degreesToRadians(movementRotation));
        }
        // Create the point vector
        this.position = new Vector2(x, y);
    }

    public Vector2 getPosition() { return position; }

    public MovementPointType getPointTypeY() { return pointY; }

    private static MovementPointType getMovementPointType(String movementType)
    {
        if(movementType.equals("Up"))
            return MovementPointType.MOVEMENT_POINT_TYPE_UP;
        else if(movementType.equals("Down"))
            return MovementPointType.MOVEMENT_POINT_TYPE_DOWN;
        else if(movementType.equals("Left"))
            return MovementPointType.MOVEMENT_POINT_TYPE_LEFT;
        else if(movementType.equals("Right"))
            return MovementPointType.MOVEMENT_POINT_TYPE_RIGHT;
        else if(movementType.equals("Middle"))
            return MovementPointType.MOVEMENT_POINT_TYPE_MIDDLE;
        else
            return MovementPointType.MOVEMENT_POINT_TYPE_NONE;
    }
}
