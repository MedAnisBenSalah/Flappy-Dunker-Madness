package com.ormisiclapps.flappydunkermadness.game.core;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.Player;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.base.GameObject;
import com.ormisiclapps.flappydunkermadness.game.world.Terrain;

/**
 * Created by OrMisicL on 9/1/2016.
 */
public class GameIntelligence
{
    private Array<String> terrainObjectModels;
    private boolean objectPassed, playerDied, playerEnteringObject, playerExitingObject, swished;

    private static GameIntelligence instance;

    public GameIntelligence()
    {
        // Create arrays
        terrainObjectModels = new Array<String>();
        // Initialize object model
        initializeObjectModels();
        // Set the instance
        instance = this;
    }

    private void initializeObjectModels()
    {
        // Loop through all the model's count
        for(int i = 0; i < Core.getInstance().getModelSettings().getModelsCount(); i++)
        {
            // Get the model
            String model = Core.getInstance().getModelSettings().getModel(i);
            if(Core.getInstance().getModelSettings().getModelAttribute(model, "type").equals("TerrainObject"))
                terrainObjectModels.add(model);
        }
    }

    public void initialize()
    {
        // Reset flags
        playerEnteringObject = false;
        playerExitingObject = false;
        objectPassed = false;
        playerDied = false;
        swished = true;
    }

    public float getNextObjectPosition()
    {
        long score = GameLogic.getInstance().getScore();
        float height = GameLogic.getInstance().getWorldHeight();
        if(score == 0)
            return height / 2f;
        else if(score < 10)
            return MathUtils.random(height / 3f, height / 3f * 2f);
        else if(score < 25)
            return MathUtils.random(height / 4f, height / 4f * 3f);
        else
            return MathUtils.random(height / 5f, height / 5f * 4f);
    }

    public float getNextMovingObjectPosition()
    {
        long score = GameLogic.getInstance().getScore();
        float height = GameLogic.getInstance().getWorldHeight();
        if(score == 0)
            return height / 2f;
        else
            return MathUtils.random(height * 0.4f, height * 0.575f);
    }

    public float getNextObjectRotation()
    {
        if(GameLogic.getInstance().isHorizontal())
        {
            long score = GameLogic.getInstance().getScore();
            if (score < 20)
                return 0f;
            else if (score < 40)
                return MathUtils.random(0f, 22.5f);
            else if (MathUtils.random(10) >= 7)
                return MathUtils.random(325f, 355f);
            else if (score < 60)
                return MathUtils.random(0f, 45f);
            else if (score < 80)
                return MathUtils.random(0f, 67.5f);
            else
                return MathUtils.random(0f, 75f);
        }
        else
        {
            long score = GameLogic.getInstance().getScore();
            if (score < 10)
                return 0f;
            else if(MathUtils.randomBoolean())
            {
                if(score < 20)
                    return MathUtils.random(0f, 22.5f);
                else if(score < 40)
                    return MathUtils.random(0f, 32.5f);
                else
                    return MathUtils.random(0f, 45f);
            }
            else
            {
                if(score < 20)
                    return MathUtils.random(355f, 332.5f);
                else if(score < 40)
                    return MathUtils.random(355f, 322.5f);
                else
                    return MathUtils.random(355f, 310f);
            }
        }
    }

    public float getNextObjectSizeFactor()
    {
        long score = GameLogic.getInstance().getScore();
        if(score < 20)
            return 1f;
        else if(score < 50)
            return 0.85f;
        else
            return 0.75f;
    }

    public boolean isMovable()
    {
        long score = GameLogic.getInstance().getScore();
        if(score < 30)
            return false;

        int chance = 0;
        if(score < 40)
            chance = 7;
        else if(score < 50)
            chance = 6;
        else if(score < 60)
            chance = 5;
        else if(score < 70)
            chance = 4;
        else if(score < 80)
            chance = 3;

        return GameLogic.getInstance().getScore() >= 30 && MathUtils.random(10) > chance;
    }

    public int getMovement()
    {
        return MathUtils.random(1);
    }

    public void process()
    {
        // Perform a ray cast
        /*if(GameMath.getDistanceBetweenVectors(Player.getInstance().getPosition(), Player.getInstance().getLastPosition()) > 0f &&
                !objectPassed && !playerDied)
        {
            GameWorld.getInstance().rayCast(new RayCastCallback() {
                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                    // Ignore no sensors
                    if(fixture.getUserData() == null)
                        return 1;

                    if(fixture.getUserData().equals("HoopTopSensor"))
                        playerEnteringObject = true;
                    else if(fixture.getUserData().equals("HoopBottomSensor") && normal.y == -1f)
                    {
                        // Are we entering ?
                        if(playerEnteringObject)
                        {
                            // Pass the object
                            objectPassed = true;
                            playerEnteringObject = false;
                            playerExitingObject = false;
                        }
                        else
                        {
                            // Kill the player
                            playerDied = true;
                            playerEnteringObject = false;
                            playerExitingObject = false;
                        }
                    }
                    return 1;
                }
            }, Player.getInstance().getLastPosition(), Player.getInstance().getPosition());
        }*/
        // Process death
        processDeath();
    }

    /*
        Will check if the player has died
    */
    private void processDeath()
    {
        // Get the next object
        GameObject object = Terrain.getInstance().getNextObject();
        if(object == null)
            return;

        // Get positions and sizes
        Vector2 playerPosition = Player.getInstance().getPosition();
        Vector2 objectPosition = object.getPosition();
        Vector2 playerSize = Player.getInstance().getSize();
        Vector2 objectSize = object.getSize();
        switch(GameLogic.getInstance().getGameMode())
        {
            case GAME_MODE_FLAPPY:
            {
                // Check if we looped the hoop
                if(playerPosition.x - playerSize.x > objectPosition.x + objectSize.x)
                {
                    playerDied = true;
                    // Reset flags
                    playerEnteringObject = false;
                    playerExitingObject = false;
                    objectPassed = false;
                    swished = true;
                }
                break;
            }

            case GAME_MODE_FALLING:
            {
                // Are we dying ?
                if(playerPosition.y + playerSize.y < objectPosition.y - objectSize.y)
                {
                    if(!playerEnteringObject && !objectPassed && !playerExitingObject && !playerDied)
                    {
                        playerDied = true;
                        // Reset flags
                        playerEnteringObject = false;
                        playerExitingObject = false;
                        objectPassed = false;
                        swished = true;
                    }

                }
                break;
            }
        }
    }

    public void beginCollision(Body bodyA, Body bodyB, Fixture fixtureA, Fixture fixtureB)
    {
        // Ignore if we're dead
        if(GameLogic.getInstance().isGameOver())
            return;

        // Validate the player
        if(!bodyA.getUserData().equals("Player") && !bodyB.getUserData().equals("Player"))
            return;

        // Is it the terrain ?
        if(bodyA.getUserData().equals("Terrain") || bodyB.getUserData().equals("Terrain"))
        {
            // Get wall fixture
            Fixture wallFixture = fixtureA;
            if(bodyB.getUserData().equals("Terrain"))
                wallFixture = fixtureB;

            // Ignore side walls on vertical mode
            if(!GameLogic.getInstance().isHorizontal() && (wallFixture.getUserData() == null ||
                    !((String)wallFixture.getUserData()).contains("Wall")))
                return;

            // Immediately kill the player
            playerDied = true;
            // Reset flags
            playerEnteringObject = false;
            playerExitingObject = false;
            objectPassed = false;
            swished = true;
            return;
        }

        // Is it the next object ?
        if(bodyA != Terrain.getInstance().getNextObject().getBody() && bodyB != Terrain.getInstance().getNextObject().getBody())
            return;

        // Get the sensor fixture
        Fixture sensorFixture = null;
        if((fixtureA.getUserData() != null && ((String)fixtureA.getUserData()).contains("Sensor")))
            sensorFixture = fixtureA;
        else if((fixtureB.getUserData() != null && ((String)fixtureB.getUserData()).contains("Sensor")))
            sensorFixture = fixtureB;

        // Validate the sensor
        if(sensorFixture != null && !objectPassed && !playerDied)
        {
            // Is it the top sensor ?
            if(sensorFixture.getUserData().equals("HoopTopSensor"))
                playerEnteringObject = true;
            else if(sensorFixture.getUserData().equals("HoopBottomSensor") && !playerEnteringObject)
            {
                playerDied = true;
                // Reset flags
                playerEnteringObject = false;
                playerExitingObject = false;
                objectPassed = false;
                swished = true;
            }
            else if(sensorFixture.getUserData().equals("HoopBottomSensor") && playerEnteringObject)
                playerExitingObject = true;
        }
        else
            swished = false;
    }

    public void endCollision(Body bodyA, Body bodyB, Fixture fixtureA, Fixture fixtureB)
    {
        // Ignore if we're dead
        if(GameLogic.getInstance().isGameOver())
            return;

        // Validate the player
        if(!bodyA.getUserData().equals("Player") && !bodyB.getUserData().equals("Player"))
            return;

        // Is it the terrain ?
        if(bodyA.getUserData().equals("Terrain") || bodyB.getUserData().equals("Terrain"))
            return;

        // Is it the next object ?
        if(Terrain.getInstance().getNextObject() != null && bodyA != Terrain.getInstance().getNextObject().getBody() &&
                bodyB != Terrain.getInstance().getNextObject().getBody())
            return;

        // Get the sensor fixture
        Fixture sensorFixture = null;
        if((fixtureA.getUserData() != null && ((String)fixtureA.getUserData()).contains("Sensor")))
            sensorFixture = fixtureA;
        else if((fixtureB.getUserData() != null && ((String)fixtureB.getUserData()).contains("Sensor")))
            sensorFixture = fixtureB;

        // Validate the sensor
        if(sensorFixture != null && !objectPassed && !playerDied)
        {
            // Is it the top sensor ?
            if(sensorFixture.getUserData().equals("HoopTopSensor") && playerExitingObject)
            {
                objectPassed = true;
                playerEnteringObject = false;
                playerExitingObject = false;
            }
            else if(sensorFixture.getUserData().equals("HoopTopSensor") && !playerExitingObject)
                playerEnteringObject = false;
            else if(sensorFixture.getUserData().equals("HoopBottomSensor") && playerExitingObject) {
                playerExitingObject = false;
                playerEnteringObject = false;
                objectPassed = false;
            }
        }
    }

    public void detectNextObject()
    {
        objectPassed = false;
        playerDied = false;
        swished = true;
    }

    public boolean isObjectPassed() { return objectPassed; }
    public boolean isPlayerDead() { return playerDied; }
    public boolean isSwished() { return swished; }

    public static GameIntelligence getInstance()
    {
        return instance;
    }
}
