package com.ormisiclapps.flappydunkermadness.game.world;

import com.badlogic.gdx.physics.box2d.*;
import com.ormisiclapps.flappydunkermadness.game.core.GameIntelligence;
import com.ormisiclapps.flappydunkermadness.game.core.GameLogic;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.Player;

public class CollisionListener implements ContactListener
{
    private int playerToHoopCollisions;

    protected CollisionListener()
    {
        // Reset values
        playerToHoopCollisions = 0;
    }

    @Override
    public void endContact(Contact contact)
    {
		// Get the bodies
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();
        // No collision for the wings
        if(bodyA.getUserData().equals("Wing") || bodyB.getUserData().equals("Wing"))
        {
            contact.setEnabled(false);
            return;
        }
        // Is it a player to hoop collision ?
        if((bodyA.getUserData().equals("Player") && bodyB.getUserData().equals("Hoop")) ||
                (bodyA.getUserData().equals("Hoop") && bodyB.getUserData().equals("Player")))
        {
            // Decrease collision counter
            playerToHoopCollisions--;
            // Restore movement
            if(playerToHoopCollisions == 0 && GameLogic.getInstance().isHorizontal())
                Player.getInstance().setMovingState(true);
        }
        // Process collision
        GameIntelligence.getInstance().endCollision(bodyA, bodyB, contact.getFixtureA(), contact.getFixtureB());
    }

	@Override
    public void beginContact(Contact contact)
	{
        // Get the bodies
		Body bodyA = contact.getFixtureA().getBody();
		Body bodyB = contact.getFixtureB().getBody();
        // No collision for the wings
        if(bodyA.getUserData().equals("Wing") || bodyB.getUserData().equals("Wing"))
        {
            contact.setEnabled(false);
            return;
        }
        // Is it a player to hoop collision ?
        if((bodyA.getUserData().equals("Player") && bodyB.getUserData().equals("Hoop")) ||
                (bodyA.getUserData().equals("Hoop") && bodyB.getUserData().equals("Player")))
        {
            // Get hoop body
            Fixture hoopFixture = contact.getFixtureA();
            if(bodyB.getUserData().equals("Hoop"))
                hoopFixture = contact.getFixtureB();

            // Ignore sensors
            if(Terrain.getInstance().getNextObject() != null && (hoopFixture.getUserData() == null || !((String)hoopFixture.getUserData()).contains("Sensor")))
            {
                // Disable restitution
                if(contact.getWorldManifold().getNormal().y < 0f)
                    contact.setRestitution(0f);
            }
            // Increase collision counter
            playerToHoopCollisions++;
            // Stop the player's movement
            if(GameLogic.getInstance().isHorizontal())
                Player.getInstance().setMovingState(false);
        }
        // Process player collision
        if((bodyA.getUserData().equals("Player") || bodyB.getUserData().equals("Player")))
            Player.getInstance().beginCollision(bodyA, bodyB, contact.getFixtureA(), contact.getFixtureB());

		// Process collision
        GameIntelligence.getInstance().beginCollision(bodyA, bodyB, contact.getFixtureA(), contact.getFixtureB());
    }

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
    {
        // Get the bodies
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        // No collision for the wings
        if(bodyA.getUserData().equals("Wing") || bodyB.getUserData().equals("Wing"))
        {
            contact.setEnabled(false);
            return;
        }
        // No collision for the sensors
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if((fixtureA.getUserData() != null && ((String)fixtureA.getUserData()).contains("Sensor")) ||
                (fixtureB.getUserData() != null && ((String)fixtureB.getUserData()).contains("Sensor")))
        {
            contact.setEnabled(false);
            return;
        }
        // Disable collision for player and objects if its not the next one
        if(Player.getInstance().getBody() != bodyA && Player.getInstance().getBody() != bodyB)
            return;

        if(!bodyA.getUserData().equals("Hoop") && !bodyB.getUserData().equals("Hoop"))
            return;

        if(Terrain.getInstance().getNextObject() != null && Terrain.getInstance().getNextObject().getBody() != bodyA &&
                Terrain.getInstance().getNextObject().getBody() != bodyB)
            contact.setEnabled(false);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
    {
	    // Nothing to do here
	}
}
