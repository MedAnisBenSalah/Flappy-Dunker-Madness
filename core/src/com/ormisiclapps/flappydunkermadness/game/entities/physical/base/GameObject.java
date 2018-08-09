package com.ormisiclapps.flappydunkermadness.game.entities.physical.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.EntityType;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.game.entities.Camera;
import com.ormisiclapps.flappydunkermadness.game.world.Terrain;
import com.ormisiclapps.flappydunkermadness.utility.Configuration;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;

public class GameObject extends Entity
{
    private boolean destroying;
    private Vector2 position, size;
    private float sizeIncreasePerFrame;

    public GameObject(String model)
    {
        // Create the entity
        super(model, EntityType.ENTITY_TYPE_OBJECT);
        // Reset flags
        destroying = false;
        // Reset values
        sizeIncreasePerFrame = 0f;
    }

    public void create(float rotation, float sizeFactor)
    {
        super.create(sizeFactor);
        // Create sensor
        createSensor();
        // Set its rotation
        setRotationInDegrees(rotation);
    }

    @Override
    public void destroy()
    {
        // Set coordinates
        position = new Vector2(getPosition());
        size = new Vector2(getSize());
        // Set the size increase per frame
        sizeIncreasePerFrame = size.y / 15f;
        // Set flags
        destroying = true;
    }

    private void createSensor()
    {
        // Setup the top sensor
        ChainShape shape = new ChainShape();
        shape.createChain(new Vector2[] { new Vector2(-getSize().x / 2f + Configuration.HOOP_SIDE_SIZE * sizeFactor, getSize().y * 0.25f),
                                new Vector2(getSize().x / 2f - Configuration.HOOP_SIDE_SIZE * sizeFactor, getSize().y * 0.25f) });

        // Create the fixture def
        FixtureDef fixtureDef = new FixtureDef();
        //fixtureDef.isSensor = true;
        fixtureDef.shape = shape;
        // Create the fixture
        getBody().createFixture(fixtureDef).setUserData("HoopTopSensor");

        // Setup the bottom sensor
        shape = new ChainShape();
        shape.createChain(new Vector2[] { new Vector2(-getSize().x / 2f + Configuration.HOOP_SIDE_SIZE * sizeFactor, -getSize().y * 0.15f),
                new Vector2(getSize().x / 2f - Configuration.HOOP_SIDE_SIZE * sizeFactor, -getSize().y * 0.15f) });

        // Setup the fixture def
        fixtureDef.shape = shape;
        // Create the fixture
        getBody().createFixture(fixtureDef).setUserData("HoopBottomSensor");
    }

    @Override
    public void process()
    {
        // Is it destroying ?
        if(destroying)
        {
            // Reduce the alpha
            getColor().a -= 0.1f;
            // Increase size
            size.add(sizeIncreasePerFrame, sizeIncreasePerFrame);
            // Calculate the screen size
            Vector2 pixels = GameMath.pixelsPerMeters(size);
            screenSize.set(pixels.y, pixels.x);
            // Calculate the screen position
            screenPosition.set(Camera.getInstance().worldToScreen(position)).sub(screenSize.x / 2, screenSize.y / 2);
        }
        else
            super.process();
    }

    @Override
    public void render()
    {
        if(!shouldRender)
            return;

        // Set the color
        Core.getInstance().getGraphicsManager().setColor(getColor());
        // Draw the rear
        Core.getInstance().getGraphicsManager().drawTextureRegion(Terrain.getInstance().getHoopsTexture(), screenPosition.x, screenPosition.y,
                screenSize.x, screenSize.y, GameMath.adjustAngleInDegrees(getRotationInDegrees() + 270f));

        // Restore the color
        Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
    }

    public void renderAfterPlayer()
    {
        if(!shouldRender)
            return;

        // Set the color
        Core.getInstance().getGraphicsManager().setColor(getColor());
        // Draw the front
        Core.getInstance().getGraphicsManager().drawTextureRegion(Terrain.getInstance().getFrontHoopsTexture(), screenPosition.x, screenPosition.y,
                screenSize.x, screenSize.y, GameMath.adjustAngleInDegrees(getRotationInDegrees() + 270f));

        // Restore the color
        Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
    }

    public boolean isDestroying() { return destroying; }

    public boolean shouldDestroy()
    {
        return destroying && getColor().a <= 0f;
    }

    public void destroyNow()
    {
        // Destroy entity
        super.destroy();
    }
}
