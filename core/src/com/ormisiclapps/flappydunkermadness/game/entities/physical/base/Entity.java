package com.ormisiclapps.flappydunkermadness.game.entities.physical.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.EntityType;
import com.ormisiclapps.flappydunkermadness.game.entities.Camera;
import com.ormisiclapps.flappydunkermadness.game.entities.utility.Model;
import com.ormisiclapps.flappydunkermadness.game.nodes.entity.ModelNode;
import com.ormisiclapps.flappydunkermadness.game.world.GameWorld;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;

/**
 * Created by OrMisicL on 9/18/2015.
 *
 */
public abstract class Entity
{
    private Model model;
    private String modelName;
    private EntityType type;
    private Body body;
    protected Vector2 size;
    protected float sizeFactor;
    private Color color;
    protected Vector2 screenPosition;
    protected Vector2 screenSize;
    protected boolean shouldRender;
    private Vector2 tmpVector;
    private boolean nextFramePosition;
    private Vector2 entityPosition;

    public Entity(String name, EntityType type)
    {
        // Save the entity type
        this.type = type;
        // Set the model's name
        modelName = name;
        // Create vectors
        size = new Vector2();
        screenPosition = new Vector2();
        screenSize = new Vector2();
        tmpVector = new Vector2();
        entityPosition = new Vector2();
        // Reset instances
        model = null;
        body = null;
        color = null;
        // Reset flags
        shouldRender = true;
        nextFramePosition = false;
    }

    public void create(float sizeFactor)
    {
        // Create the model instance
        model = new Model(modelName);
        // Load the model
        model.load();
        // Set the entity's color
        color = Color.WHITE.cpy();
        // Set the size factor
        this.sizeFactor = sizeFactor;
        // Create the body
        createBody(new Vector2(model.getNode().size).scl(sizeFactor));
    }


    private void createBody(Vector2 size)
    {
        // Get the model node
        ModelNode node = model.getNode();
        // Save the entity's size
        this.size.set(size);
        // Create the body definition instance
        BodyDef bodyDef = new BodyDef();
        bodyDef.allowSleep = true;
        if(Core.getInstance().getModelSettings().getModelAttribute(modelName, "type").contains("Static"))
            bodyDef.type = BodyDef.BodyType.StaticBody;
        else
            bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(size.x / 2, size.y / 2);
        // Create the body instance
        body = GameWorld.getInstance().addToWorld(bodyDef);
        // Set name
        body.setUserData(modelName);
        // Reset velocity
        body.setLinearVelocity(0f, 0f);
        // Loop through the fixtures
        for(int i = 0; i < node.fixturePosition.length; i++)
        {
            // Create the circle shape instance
            CircleShape circleShape = new CircleShape();
            // Set the shape radius
            circleShape.setPosition(new Vector2(node.fixturePosition[i]).scl(sizeFactor));
            circleShape.setRadius(node.fixtureRadius[i] * sizeFactor);
            // Create the fixture definition
            FixtureDef fixtureDef = new FixtureDef();
            // Set the body physics
            fixtureDef.shape = circleShape;
            fixtureDef.density = node.mass / (MathUtils.PI * node.fixtureRadius[i] * node.fixtureRadius[i]);
            fixtureDef.friction = 0.5f;//node.friction;
            fixtureDef.restitution = 0.75f;//node.restitution;
            // Create the fixture
            body.createFixture(fixtureDef);
        }
        // Set a bullet body
        setBulletBody(true);
    }
    
    public void destroy()
    {
        // Destroy the body
        GameWorld.getInstance().removeFromWorld(body);
    }

    public void process()
    {
        // Set the position
        if(nextFramePosition)
        {
            // Set the entity's position
            setPosition(entityPosition);
            // Reset the next frame position
            nextFramePosition = false;
        }
        // Calculate the screen size
        Vector2 pixels = GameMath.pixelsPerMeters(size);
        screenSize.set(pixels.y, pixels.x);
        // Calculate the screen position
        screenPosition.set(Camera.getInstance().worldToScreen(getPosition())).sub(screenSize.x / 2, screenSize.y / 2);
        // Get the rendering area
        tmpVector.set(screenPosition).sub(screenSize.x / 2, screenSize.y / 2);
        // Check if we should render
        shouldRender = (tmpVector.x <= Core.getInstance().getGraphicsManager().WIDTH && tmpVector.x + screenSize.x >= 0 &&
                tmpVector.y <= Core.getInstance().getGraphicsManager().HEIGHT && tmpVector.y + screenSize.y >= 0);
    }

    public void render()
    {
        // Verify if we should render the entity
        if(model != null)
        {
            // Set the entity's color
            Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
            // Render the entity's texture
            Core.getInstance().getGraphicsManager().drawTextureRegion(model.getTexture(), screenPosition, screenSize,
                    GameMath.adjustAngleInDegrees(getRotationInDegrees() + 270f));

            // Restore the rendering color
            Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
        }
    }

    public void setPosition(Vector2 position)
    {
        // Is the world locked ?
        if(GameWorld.getInstance().isLocked())
        {
            // Set the next frame position flag
            nextFramePosition = true;
            // Set the position
            entityPosition.set(position);
            return;
        }
        if(body != null)
            body.setTransform(position, body.getAngle());
    }

    public Vector2 getPosition()
    {
        return body != null ? body.getPosition() : tmpVector.set(0f, 0f);
    }

    public void setRotationInRadians(float rotation)
    {
        if(body != null)
            body.setTransform(getPosition(), rotation);
    }

    public void setRotationInDegrees(float rotation)
    {
        // Set the entity's rotation
        setRotationInRadians(GameMath.degreesToRadians(rotation));
    }

    public float getRotationInRadians()
    {
        return body != null ? body.getAngle() : 0.0f;
    }

    public float getRotationInDegrees()
    {
        return GameMath.radiansToDegrees(getRotationInRadians());
    }

    public void setVelocity(Vector2 velocity)
    {
        // Set the entity's velocity
        if(body != null)
            body.setLinearVelocity(velocity);
    }

    public void setBulletBody(boolean toggle)
    {
        // Set the entity's bullet body
        if(body != null)
            body.setBullet(toggle);
    }

    public void applyVerticalImpulse(float impulse)
    {
        // Apply the impulse
        if(body != null)
            body.applyLinearImpulse(0f, impulse, body.getPosition().x, body.getPosition().y, true);
    }

    public Vector2 getVelocity() { return body.getLinearVelocity(); }

    public Body getBody()
    {
        return body;
    }


    public EntityType getType()
    {
        return type;
    }

    public Model getModel()
    {
        return model;
    }

    public Vector2 getSize() { return size; }

    public Color getColor() { return color; }

    public void setColor(Color color)
    {
        // Keep the alpha man!!
        float a = this.color.a;
        this.color.set(color);
        this.color.a = a;
    }

    public void setColorAlpha(float alpha)
    {
        this.color.a = alpha;
    }

    public void setAngularVelocity(float velocity)
    {
        if(body != null)
            body.setAngularVelocity(velocity);
    }
}
