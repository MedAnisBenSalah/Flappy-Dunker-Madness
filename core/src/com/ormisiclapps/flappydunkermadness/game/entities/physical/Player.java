package com.ormisiclapps.flappydunkermadness.game.entities.physical;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.ormisiclapps.flappydunkermadness.enumerations.EntityType;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.game.core.GameLogic;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.base.Entity;
import com.ormisiclapps.flappydunkermadness.game.entities.utility.Wing;
import com.ormisiclapps.flappydunkermadness.game.screens.GameScreen;
import com.ormisiclapps.flappydunkermadness.input.InputHandler;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.utility.Configuration;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;

/**
 * Created by OrMisicL on 9/18/2015.
 */
public class Player extends Entity
{
    private Vector2 tmpVector;
    private TextureRegion skin, wingSkin;
    private boolean skipInput;
    private boolean movingState;
    private Wing frontWing, rearWing;
    private boolean followedByCamera;
    private boolean wingsDetached;
    private long deathTime;
    private boolean rotating;
    private float rotatingTo, rotatingSpeed;
    private Vector2 lastPosition;

    private static Player instance;

    private static final float ANGULAR_DAMPING = 0.6f;

    public Player()
    {
        // Create the actor instance
        super("Player", EntityType.ENTITY_TYPE_PLAYER);
        // Reset flags
        skipInput = false;
        movingState = false;
        followedByCamera = false;
        wingsDetached = false;
        rotating = false;
        // Reset values
        deathTime = 0;
        rotatingTo = 0f;
        // Create vectors
        tmpVector = new Vector2();
        lastPosition = new Vector2();
        // Set the instance
        instance = this;
    }

    public void create()
    {
        // Create the entity
        super.create(1f);
        // Set it to be a bullet body (for a pixel perfect collision)
        setBulletBody(true);
        // Reset player's position
        setPosition(new Vector2(0f, 0f));
        // Create wings
        frontWing = new Wing(new Vector2(0f, 1.4f), new Vector2(1.6f, 1.45f), getBody());
        rearWing = new Wing(new Vector2(1.4f, 1.5f), new Vector2(1.6f, 1.45f), getBody());
    }

    public void setup()
    {
        // Reset flags
        skipInput = false;
        movingState = true;
        followedByCamera = true;
        wingsDetached = false;
        // Reset the player's velocity
        setVelocity(new Vector2(0f, 0f));
        setAngularVelocity(0f);
        // Reset rotation
        setRotationInRadians(0f);
        // Reset wings
        frontWing.reset();
        rearWing.reset();
        // Reset death time
        deathTime = 0;
        // Load the player
        load();
    }

    public void load()
    {
        // Set it the saved skin
        setSkin(Core.getInstance().getStatsSaver().savedData.ball);
        setWingSkin(Core.getInstance().getStatsSaver().savedData.wing);
    }

    @Override
    public void destroy()
    {
    	// Destroy the entity
    	super.destroy();
    }

    @Override
    public void process()
    {
        // Set the last position
        lastPosition.set(getPosition());
        // Process input
        if(!GameLogic.getInstance().isGameOver())
            processInput();
        // Process dying
        else if(followedByCamera)
            followedByCamera = getVelocity().x > 5f;

        // Are we dying ?
        if(wingsDetached)
        {
            // Increase death time
            deathTime += Core.getInstance().getGraphicsManager().DELTA_TIME * 1000;
            // Did we finish ?
            if(deathTime >= 2500 && !GameLogic.getInstance().isSecondChance())
                GameLogic.getInstance().onLosingFinished();
        }
        else if(GameLogic.getInstance().isHorizontal() && GameLogic.getInstance().isGameOver() && getPosition().y < 0f)
        {
            // Detach wings
            boolean up = getPosition().y < GameLogic.getInstance().getWorldHeight() / 2f;
            frontWing.detach(true, up);
            rearWing.detach(false, up);
            // Set detached flag
            wingsDetached = true;
            // Reset death time
            deathTime = 0;
        }


        // Are we rotating ?
        if(rotating)
        {
            // Rotate
            setRotationInDegrees(GameMath.adjustAngleInDegrees(getRotationInDegrees() + rotatingSpeed));
            // Did we finish ?
            if((rotatingSpeed > 0f && getRotationInDegrees() < 10f) ||
                    (rotatingSpeed < 0f && getRotationInDegrees() <= rotatingTo))
            {
                rotating = false;
                setRotationInDegrees(rotatingTo);
            }
        }
        // Keep the horizontal velocity constant
        if(!GameLogic.getInstance().isGameOver() && GameLogic.getInstance().isGameStarted() &&
                GameLogic.getInstance().isHorizontal() && movingState)
            getBody().applyLinearImpulse(100f, 0f, getPosition().x, getPosition().y, true);

        // Keep the horizontal velocity within the limit
        if(GameLogic.getInstance().isGameStarted() && GameLogic.getInstance().isHorizontal() &&
                getVelocity().x > Configuration.MAX_HORIZONTAL_VELOCITY)
            setVelocity(tmpVector.set(Configuration.MAX_HORIZONTAL_VELOCITY, getVelocity().y));

        // Limit rotation
        getBody().setAngularDamping(ANGULAR_DAMPING);
        // Update wings
        frontWing.process();
        rearWing.process();
        // Process entity
        super.process();
    }

    @Override
    public void render()
    {
        // Draw the rear wing
        rearWing.render();
        // Draw the player
        //super.render();
        Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
        // Render the entity's texture
        Core.getInstance().getGraphicsManager().drawTextureRegion(skin, screenPosition, screenSize,
                GameMath.adjustAngleInDegrees(getRotationInDegrees() + 270f));

        // Restore the rendering color
        Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
        // Draw the front wing
        frontWing.render();
    }

    private void processInput()
    {
        // Skip input
        if(skipInput)
        {
            skipInput = false;
            return;
        }
        // Did we tap the screen ?
        if(InputHandler.getInstance().isTapped())
        {
            // Stop falling
            setVelocity(tmpVector.set(getVelocity().x, 0f));
            // Flap
            applyVerticalImpulse(Configuration.FLAP_IMPULSE);
            // Flap wings
            frontWing.flap();
            rearWing.flap();
            // Play flapping sound
            GameScreen.getInstance().playFlapSound();
        }
    }

    public void beginCollision(Body bodyA, Body bodyB, Fixture fixtureA, Fixture fixtureB)
    {
        // Is it the ground ?
        if(bodyA.getUserData().equals("Terrain") || bodyB.getUserData().equals("Terrain"))
            // Play bounce sound
            GameScreen.getInstance().playBounceSound();

        // Is it the ground ?
        if(wingsDetached || (!bodyA.getUserData().equals("Terrain") && !bodyB.getUserData().equals("Terrain")))
            return;

        // Get wall fixture
        Fixture wallFixture = fixtureA;
        if(bodyB.getUserData().equals("Terrain"))
            wallFixture = fixtureB;

        // Ignore side walls on vertical mode
        if(!GameLogic.getInstance().isHorizontal() && (wallFixture.getUserData() == null ||
                !((String)wallFixture.getUserData()).contains("Wall")))
            return;

        // Detach wings
        boolean up = getPosition().y < GameLogic.getInstance().getWorldHeight() / 2f;
        frontWing.detach(true, up);
        rearWing.detach(false, up);
        // Set detached flag
        wingsDetached = true;
        // Reset death time
        deathTime = 0;
    }

    public void resize(float newRadius)
    {
        // Check array's size
        // TODO: Check why this contains empty fixture's list (crash)
        if(getBody().getFixtureList().size == 0)
            return;

        // Get the body's fixture
        getBody().getFixtureList().first().getShape().setRadius(newRadius);
        // Get the wing size factor
        float wingSizeFactor = (newRadius * 2f) / size.x;
        // Resize wings
        frontWing.resize(wingSizeFactor);
        rearWing.resize(wingSizeFactor);
        // Set the new size
        size.set(newRadius * 2, newRadius * 2);
    }

    public void rotate(float targetRotation)
    {
        // Set values
        rotatingTo = targetRotation;
        rotating = true;
        rotatingSpeed = targetRotation > getRotationInDegrees() ? -5f : 5f;
    }

    public void reset()
    {
        // Reset flags
        skipInput = false;
        movingState = true;
        followedByCamera = true;
        wingsDetached = false;
        // Reset the player's velocity
        setVelocity(new Vector2(0f, 0f));
        setAngularVelocity(0f);
        // Reset rotation
        setRotationInRadians(0f);
        // Reset wings
        frontWing.reset();
        rearWing.reset();
        // Reset death time
        deathTime = 0;
    }

    public void setSkin(String name)
    {
        TextureRegion skinTexture = Core.getInstance().getResourcesManager().getResource("Balls/" + name,
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

        if(skinTexture != null)
            skin = skinTexture;
    }

    public void setWingSkin(String name)
    {
        TextureRegion skinTexture = Core.getInstance().getResourcesManager().getResource("Wings/" + name,
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

        if(skinTexture != null)
        {
            wingSkin = skinTexture;
            frontWing.setTexture(wingSkin);
            rearWing.setTexture(wingSkin);
        }
    }

    public void setSkipInput()
    {
        skipInput = true;
    }

    public void setMovingState(boolean movingState)
    {
        this.movingState = movingState;
    }

    public boolean isFollowedByCamera() {
        return followedByCamera;
    }

    public static Player getInstance()
    {
        return instance;
    }
}