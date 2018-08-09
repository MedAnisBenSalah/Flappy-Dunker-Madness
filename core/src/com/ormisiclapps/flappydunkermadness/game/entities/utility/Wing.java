package com.ormisiclapps.flappydunkermadness.game.entities.utility;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.game.entities.Camera;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.Player;
import com.ormisiclapps.flappydunkermadness.game.world.GameWorld;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;

/**
 * Created by OrMisicL on 10/11/2017.
 */

public class Wing
{
    private TextureRegion texture;
    private Body body;
    private Vector2 position, size, screenPosition, screenSize, tmpVector, tmpVector2, tmpVector3;
    private boolean detached;
    private boolean flapping, flappingDown;
    private float rotation;

    public Wing(Vector2 position, Vector2 size, Body body)
    {
        // Get the texture
        texture = null;//Core.getInstance().getResourcesManager().getResource("Wing", ResourceType.RESOURCE_TYPE_MODEL);
        // Set vectors
        this.position = new Vector2(position).sub(size.x / 2f, size.y / 2f);
        this.size = size;
        // Create vectors
        screenPosition = new Vector2();
        screenSize = new Vector2(GameMath.pixelsPerMeters(size));
        // Reset flags
        detached = false;
        flapping = false;
        flappingDown = false;
        // Reset values
        rotation = 0f;
        // Create the wing
        create(body);
        // Create vectors
        tmpVector = new Vector2();
        tmpVector2 = new Vector2();
        tmpVector3 = new Vector2();
    }

    private void create(Body playerBody)
    {
        // Create the body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(playerBody.getPosition()).add(position);
        body = GameWorld.getInstance().addToWorld(bodyDef);
        body.setUserData("Wing");
        body.setBullet(true);
        // Create shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / 2f, size.y / 2f);
        // Create fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        body.createFixture(fixtureDef);
    }

    public void setTexture(TextureRegion texture)
    {
        this.texture = texture;
    }

    public void process()
    {
        // Make sure we're not dead yet
        if(!detached)
        {
            // Are we flapping ?
            if(flapping)
            {
                // Are we flapping down ?
                if(flappingDown)
                {
                    // Increase rotation
                    rotation += 18f;
                    // Did we reach the rotation limit ?
                    if(rotation >= 105f)
                    {
                        // Reset down flag
                        flappingDown = false;
                        rotation = 105f;
                    }
                }
                else
                {
                    // Decrease rotation
                    rotation -= 8f;
                    // Did we reach the rotation limit ?
                    if(rotation <= 0f)
                    {
                        // Reset flapping flag
                        flapping = false;
                        rotation = 0f;
                    }
                }
            }
            // Get the rotation
            float flapRotation = GameMath.degreesToRadians(Player.getInstance().getRotationInDegrees() + rotation);
            // Update the wing's position
            body.setTransform(Player.getInstance().getBody().getWorldPoint(position),
                    Player.getInstance().getRotationInRadians());

            // Get the flapping position
            tmpVector.set(body.getWorldPoint(tmpVector.set(size.x * 0.1f, -size.y * 0.1f)));
            // Rotate the wing
            body.setTransform(body.getPosition(), flapRotation);
            // Get the new position
            tmpVector2.set(body.getWorldPoint(tmpVector2.set(size.x * 0.1f, -size.y * 0.1f)));
            // Calculate the position
            tmpVector3.set(body.getPosition()).add(tmpVector.x - tmpVector2.x, tmpVector.y - tmpVector2.y);
            // Set the position
            body.setTransform(tmpVector3, flapRotation);
        }
        // Set the screen position
        screenPosition.set(Camera.getInstance().worldToScreen(body.getPosition())).sub(screenSize.x / 2f, screenSize.y / 2f);
    }

    public void render()
    {
        if(texture != null)
            // Draw the texture
            Core.getInstance().getGraphicsManager().drawTextureRegion(texture, screenPosition, screenSize,
                GameMath.adjustAngleInDegrees(GameMath.radiansToDegrees(body.getAngle()) + 270f));
    }

    public void detach(boolean front, boolean up)
    {
        // Set the detached flag
        detached = true;
        // Reset velocity
        body.setLinearVelocity(0f, 0f);
        // Throw the wing away
        body.applyLinearImpulse(new Vector2(front ? 5f : 10f, up ? 50f : 0f), body.getPosition(), true);
        // Rotate it
        body.setAngularVelocity(front ? 3f : -3f);
    }

    public void flap()
    {
        // Set flags
        flapping = true;
        flappingDown = true;
    }

    public void resize(float sizeFactor)
    {
        // Calculate the new size
        size.scl(sizeFactor, sizeFactor);
        // Calculate the new position
        position.scl(sizeFactor, sizeFactor);
        // Get the body's fixture
        ((PolygonShape)body.getFixtureList().first().getShape()).setAsBox(size.x / 2f, size.y / 2f);
        // Set the screen size
        screenSize.set(GameMath.pixelsPerMeters(size));
    }


    public void reset()
    {
        // Reset detached flag
        detached = false;
        // Reset velocity
        body.setLinearVelocity(0f, 0f);
        body.setAngularVelocity(0f);
    }
}
