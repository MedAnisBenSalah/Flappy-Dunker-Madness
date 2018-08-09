package com.ormisiclapps.flappydunkermadness.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.game.core.GameLogic;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.Player;
import com.ormisiclapps.flappydunkermadness.utility.Configuration;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;

/**
 * Created by OrMisicL on 9/18/2015.
 */
public class Camera
{
    private OrthographicCamera camera;
    private boolean changedPosition;

    private static Camera instance;

    /*
        These vectors are used to reduce the number of allocations (new) inside the frequently used methods
    */
    private final Vector2 position;
    private final Vector2 project;
    private final Vector2 tmpVector;
    private final Vector3 vector3Tmp;

    public Camera()
    {
        // Create vectors
        position = new Vector2();
        tmpVector = new Vector2();
        project = new Vector2();
        vector3Tmp = new Vector3();
        // Reset flags
        changedPosition = false;
        // Get width and height
        float width = Core.getInstance().getGraphicsManager().WIDTH;
        float height = Core.getInstance().getGraphicsManager().HEIGHT;
        // Create the camera instance
        camera = new OrthographicCamera(GameMath.metersPerPixels(width), GameMath.metersPerPixels(height));
        // Set the camera view
        camera.setToOrtho(false, GameMath.metersPerPixels(width), GameMath.metersPerPixels(height));
        // Set the instance
        instance = this;
    }

    public void update()
    {
        // Reset flags
        changedPosition = false;
        if(GameLogic.getInstance().isHorizontal())
        {
            // Center the camera
            position.y = GameLogic.getInstance().getWorldHeight() / 2f;
            // Center the camera horizontally
            float screenFourth = screenToWorld(tmpVector.set(Core.getInstance().getGraphicsManager().WIDTH / 4f, 0f)).x;
            // Don't allow the player to get past the 1/4th of the screen
            if(Player.getInstance().isFollowedByCamera() && GameLogic.getInstance().isGameStarted() &&
                    Player.getInstance().getPosition().x != screenFourth)
            {
                position.x += Player.getInstance().getPosition().x - screenFourth;
                changedPosition = true;
            }
        }
        else
        {
            // Center the camera vertically
            position.x = Configuration.WORLD_WIDTH / 2f;
            float screenHalf = screenToWorld(tmpVector.set(0f, Core.getInstance().getGraphicsManager().HEIGHT / 2f)).y;
            // Don't allow the player to fall below the screen's half
            if(!GameLogic.getInstance().isGameOver() && GameLogic.getInstance().isGameStarted() &&
                    Player.getInstance().getPosition().y < screenHalf)
            {
                position.y = Player.getInstance().getPosition().y;
                changedPosition = true;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.R))
            camera.zoom += 0.01f;

        // Update the camera position
        setPosition(position);
        // Update the camera
        //camera.update();
    }

    public OrthographicCamera getInterface() { return camera; }

    public Vector2 getPosition()
    {
        return position;
    }

    public void setPosition(Vector2 position)
    {
        vector3Tmp.set(position.x, position.y, 0);
        camera.position.set(vector3Tmp);
        this.position.set(position);
        camera.update();
    }

    /*private void updatePosition(Vector2 position)
    {
        vector3Tmp.set(position.x, position.y, 0);
        camera.position.set(vector3Tmp);
    }*/

    public Vector2 worldToScreen(Vector2 coordinates)
    {
        // Project the camera
        vector3Tmp.set(coordinates.x, coordinates.y, 0);
    	Vector3 camProjection = camera.project(vector3Tmp);
    	return project.set(camProjection.x, camProjection.y);
    }
    
    public Vector2 screenToWorld(Vector2 coordinates)
    {
        // Project the camera
        vector3Tmp.set(coordinates.x, coordinates.y, 0);
    	Vector3 camProjection = camera.unproject(vector3Tmp);
    	return project.set(camProjection.x, camProjection.y);
    }

    public boolean isChangedPosition() { return changedPosition; }

    public static Camera getInstance()
    {
        return instance;
    }
}
