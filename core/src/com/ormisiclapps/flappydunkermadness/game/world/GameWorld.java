package com.ormisiclapps.flappydunkermadness.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ormisiclapps.flappydunkermadness.core.Core;

import java.util.Iterator;

/**
 * Created by OrMisicL on 9/29/2015.
 */
public class GameWorld
{
    private World world;
    private Array<Body> bodiesToDestroy;
    private double accumulator;

    private static final float GRAVITY = -75f;
    private static final float NORMAL_STEP = 1f / 60f;
    private static final float SLOW_MOTION_STEP = 1f / 150f;

    private static GameWorld instance;

    public GameWorld()
    {
        // Initialize Box2D
        Box2D.init();
        // Create the physics world
        world = new World(new Vector2(0f, 0f), true);
        // Set the collision listener
        world.setContactListener(new CollisionListener());
        // Create arrays
        bodiesToDestroy = new Array<Body>();
        // Set instance
        instance = this;
    }

    public Body addToWorld(BodyDef bodyDef)
    {
        // Create the body instance
        return world.createBody(bodyDef);
    }

    public void removeFromWorld(Body body)
    {
        // Add the body to the destroying array
        if(body != null)
    	    bodiesToDestroy.add(body);
    }

    public void process()
    {
        // Add the accumulator
        accumulator += Math.min(Core.getInstance().getGraphicsManager().DELTA_TIME, 0.25f);
        // Do we need to step ?
        while(accumulator >= NORMAL_STEP)
        {
            // Step the physics world
            world.step(NORMAL_STEP, 8, 5);
            // Do we still have some time left ?
            accumulator -= NORMAL_STEP;
        }
    }
    
    public void postProcess()
    {
    	// Loop through all the bodies to destroy
    	Iterator<Body> bodyIterator = bodiesToDestroy.iterator();
    	while(bodyIterator.hasNext())
    	{
    		// Destroy the body
    		world.destroyBody(bodyIterator.next());
    		// Remove it from the array
    		bodyIterator.remove();
    	}
    }

    public void destroy()
    {
        // Destroy the world
        world.dispose();
    }

    public void toggleGravity(boolean toggle)
    {
        world.setGravity(new Vector2(0f, toggle ? GRAVITY : 0f));
    }

    public void rayCast(RayCastCallback callback, Vector2 fromPoint, Vector2 toPoint)
    {
        // Perform a ray cast
        world.rayCast(callback, fromPoint, toPoint);
    }

    public boolean isLocked()
    {
        return world.isLocked();
    }

    public World getWorld() { return world; }

    public static GameWorld getInstance()
    {
        return instance;
    }
}
