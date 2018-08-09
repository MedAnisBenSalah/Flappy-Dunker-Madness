package com.ormisiclapps.tools.generator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by OrMisicL on 6/2/2016.
 * This will create a fake physics world in order to generate the JSON models inside
 */
public class GeneratorWorld {
    private static World physicsWorld;

    public GeneratorWorld()
    {
        // Reset instances
        physicsWorld = null;
    }

    public void setup()
    {
        // Initialize Box2D
        //Box2D.init();
        // Create the physics world
        physicsWorld = new World(new Vector2(0, 0), true);
    }

    public Body addToWorld(BodyDef bodyDef)
    {
        // Create the body instance
        return physicsWorld.createBody(bodyDef);
    }
}
