package com.ormisiclapps.flappydunkermadness.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.game.core.GameIntelligence;
import com.ormisiclapps.flappydunkermadness.game.core.GameLogic;
import com.ormisiclapps.flappydunkermadness.game.entities.Camera;
import com.ormisiclapps.flappydunkermadness.game.entities.notphysical.TerrainObject;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.Player;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.base.GameObject;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.base.MovableGameObject;
import com.ormisiclapps.flappydunkermadness.utility.Configuration;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;

import java.util.Iterator;

/**
 * Created by OrMisicL on 9/29/2015.
 */
public class Terrain
{
    private Body body;
    private float lastObjectPosition;
    private float lastTerrainPosition;
    private float terrainObjectsLastPosition;
    private Array<GameObject> objects;
    private TextureRegion emptyTexture;
    private Color backgroundColor, leftWallColor, rightWallColor;
    private Array<TerrainObject> terrainObjects;
    private Vector2 tmpVector, tmpVector2, tmpVector3;
    private boolean renderObjects;
    private TextureRegion hoopsTexture, frontHoopsTexture;

    private static final int TERRAIN_OBJECTS_COUNT = 20;
    private static final float TERRAIN_OBJECTS_STREAMING_DISTANCE = Configuration.WORLD_WIDTH * 2f;

    private static Terrain instance;

    public Terrain()
    {
        // Reset instances
        body = null;
        objects = null;
        terrainObjects = null;
        emptyTexture = null;
        backgroundColor = null;
        leftWallColor = null;
        rightWallColor = null;
        // Reset values
        lastObjectPosition = 0f;
        lastTerrainPosition = 0f;
        terrainObjectsLastPosition = 0f;
        // Reset flags
        renderObjects = false;
        // Create vectors
        tmpVector = new Vector2();
        tmpVector2 = new Vector2();
        tmpVector3 = new Vector2();

        // Set the instance
        instance = this;
    }

    public void initialize()
    {
        // Create the terrain body def
        BodyDef bodyDef = new BodyDef();
        // Setup the body def
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Create the terrain body
        body = GameWorld.getInstance().addToWorld(bodyDef);
        // Set name
        body.setUserData("Terrain");
        // Create the objects array
        objects = new Array<GameObject>();
        // Create the terrain objects array
        terrainObjects = new Array<TerrainObject>();
        // Get the empty texture
        emptyTexture = Core.getInstance().getResourcesManager().getResource("Empty", ResourceType.RESOURCE_TYPE_MODEL);
        // Set hoops texture
        setHoopsTexture(Core.getInstance().getStatsSaver().savedData.hoop);
        // Initialize colors
        backgroundColor = new Color(0.7607f, 0.6941f, 0.5921f, 1f);
        leftWallColor = new Color(0.5490f, 0.5490f, 0.5490f, 1f);
        rightWallColor = new Color(1f, 0.7411f, 0.0078f, 1f);
    }

    public void setup()
    {
        // Reset values
        lastTerrainPosition = 0f;
        terrainObjectsLastPosition = 0f;
        lastObjectPosition = GameLogic.getInstance().isHorizontal() ? Configuration.WORLD_WIDTH - Configuration.WORLD_WIDTH * 0.75f :
                lastTerrainPosition - GameLogic.getInstance().getWorldHeight() * 0.5f;

        // Clear object arrays
        objects.clear();
        terrainObjects.clear();
        // Create the first terrain's chunk
        createTerrain(lastTerrainPosition);
        // Generate the first two objects
        generateNextObject();
        generateNextObject();
        // Generate the first set of terrain objects
        generateTerrainObjects(true);
    }

    /*
        Will generate a terrain along the length given
    */
    private void createTerrain(float start)
    {
        // Create horizontal terrain
        if(GameLogic.getInstance().isHorizontal())
            createHorizontalTerrain(start);
        else
            // Create vertical terrain
            createVerticalTerrain(start);
    }

    private void createVerticalTerrain(float start)
    {
        // Get the world's height
        float worldHeight = GameLogic.getInstance().getWorldHeight();
        // Create a chain shape
        ChainShape leftWallShape = new ChainShape();
        // Setup the chain shape
        leftWallShape.createChain(new Vector2[] { new Vector2(1.5f, start),
                new Vector2(1.5f, start - worldHeight)});

        // Create the fixture def
        FixtureDef fixtureDef = new FixtureDef();
        // Setup the fixture def
        fixtureDef.shape = leftWallShape;
        // Create the fixture
        body.createFixture(fixtureDef);

        // Create a roof shape
        ChainShape rightWallShape = new ChainShape();
        // Setup the chain shape
        rightWallShape.createChain(new Vector2[] { new Vector2(Configuration.WORLD_WIDTH - 1.5f, start),
                new Vector2(Configuration.WORLD_WIDTH - 1.5f, start - worldHeight)});

        // Setup the fixture def
        fixtureDef.shape = rightWallShape;
        // Create the fixture
        body.createFixture(fixtureDef);

        // Create a top shape
        ChainShape topShape = new ChainShape();
        // Setup the chain shape
        topShape.createChain(new Vector2[] { new Vector2(1.5f, start),
                new Vector2(Configuration.WORLD_WIDTH - 1.5f, start)});

        // Setup the fixture def
        fixtureDef.shape = topShape;
        // Create the fixture
        body.createFixture(fixtureDef).setUserData("TopWall");

        // Create a bottom shape
        ChainShape bottomShape = new ChainShape();
        // Setup the chain shape
        bottomShape.createChain(new Vector2[] { new Vector2(1.5f, start - worldHeight),
                new Vector2(Configuration.WORLD_WIDTH - 1.5f, start - worldHeight)});

        // Setup the fixture def
        fixtureDef.shape = bottomShape;
        // Create the fixture
        body.createFixture(fixtureDef).setUserData("BottomWall");
    }

    private void createHorizontalTerrain(float start)
    {
        // Create the fixture def
        FixtureDef fixtureDef = new FixtureDef();
        // Create a top shape
        ChainShape topShape = new ChainShape();
        // Get screen coordinates
        float width = Configuration.WORLD_WIDTH;
        float height = GameLogic.getInstance().getWorldHeight();
        // Setup the chain shape
        topShape.createChain(new Vector2[] { new Vector2(start, height - 1.5f), new Vector2(width, height - 1.5f)});
        // Setup the fixture def
        fixtureDef.shape = topShape;
        // Create the fixture
        body.createFixture(fixtureDef);

        // Create a bottom shape
        ChainShape bottomShape = new ChainShape();
        // Setup the chain shape
        bottomShape.createChain(new Vector2[] { new Vector2(start, 3f), new Vector2(width, 3f)});
        // Setup the fixture def
        fixtureDef.shape = bottomShape;
        // Create the fixture
        body.createFixture(fixtureDef);
    }

    private void generateNextObject()
    {
        // Vertical objects
        if(!GameLogic.getInstance().isHorizontal())
        {
            // Generate the next hoop
            MovableGameObject hoop = new MovableGameObject("Hoop");
            float rotation = GameIntelligence.getInstance().getNextObjectRotation();
            hoop.create(new Vector2(Configuration.WORLD_WIDTH / 4f, lastObjectPosition), rotation,
                    GameIntelligence.getInstance().getNextObjectSizeFactor(), rotation > 90f ? 1 : 0,
                    rotation > 270f ? rotation - 270f : rotation, 5f);

            // Restore the old object's alpha
            if(objects.size > 1)
            {
                objects.get(1).setColorAlpha(1f);
                hoop.setColorAlpha(0.5f);
            }
            // Add it to the game objects
            objects.add(hoop);
            // Increase the last object's position
            lastObjectPosition -= GameLogic.getInstance().getWorldHeight() / 2f;

        }
        else
        {
            GameObject hoop;
            // Increase the last object's position
            lastObjectPosition += Configuration.DISTANCE_BETWEEN_OBJECTS;
            // Is it a movable object ?
            if(!GameIntelligence.getInstance().isMovable())
            {
                // Generate the next hoop
                hoop = new GameObject("Hoop");
                hoop.create(GameIntelligence.getInstance().getNextObjectRotation(), GameIntelligence.getInstance().getNextObjectSizeFactor());
                hoop.setPosition(new Vector2(lastObjectPosition, GameIntelligence.getInstance().getNextObjectPosition()));
            }
            else
            {
                // Get the movement
                int movement = GameIntelligence.getInstance().getMovement();
                // Get rotation
                float rotation = GameIntelligence.getInstance().getNextObjectRotation();
                // Generate the next hoop
                hoop = new MovableGameObject("Hoop");
                ((MovableGameObject)hoop).create(new Vector2(lastObjectPosition, GameIntelligence.getInstance().getNextMovingObjectPosition()),
                        rotation, GameIntelligence.getInstance().getNextObjectSizeFactor(), movement, rotation > 270f ? rotation - 270f : rotation,
                        Configuration.MOVABLE_OBJECTS_SPEED);

                // Increase the last object's position
                lastObjectPosition = ((MovableGameObject)hoop).getMovement().getHighestPoint().x;
            }
            // Restore the old object's alpha
            if(objects.size > 1)
            {
                objects.get(1).setColorAlpha(1f);
                hoop.setColorAlpha(0.5f);
            }
            // Add it to the game objects
            objects.add(hoop);
        }
    }

    private void generateTerrainObjects(boolean start)
    {
        // Horizontal terrain objects
        if(GameLogic.getInstance().isHorizontal())
        {
            // Generate random terrain objects
            if(terrainObjectsLastPosition - Player.getInstance().getPosition().x <= TERRAIN_OBJECTS_STREAMING_DISTANCE / 4f)
            {
                // Find the current limits
                Vector2 startPosition;
                if (start)
                    startPosition = Camera.getInstance().screenToWorld(tmpVector.set(0f, 0f));
                else
                    startPosition = Camera.getInstance().screenToWorld(tmpVector.set(Core.getInstance().getGraphicsManager().WIDTH, 0f));

                // Set the model's size
                tmpVector.set(2.5f, 4.5f);
                // Create the terrain objects
                for (int i = 0; i < TERRAIN_OBJECTS_COUNT; i++)
                {
                    // Find a random position
                    float x = MathUtils.random(startPosition.x, terrainObjectsLastPosition + TERRAIN_OBJECTS_STREAMING_DISTANCE + 2.5f);
                    float y = MathUtils.random(3f + Configuration.TERRAIN_OBJECT_HEIGHT,
                            GameLogic.getInstance().getWorldHeight() - 4f);

                    // Ensure that it doesn't collide with other terrain objects
                    if (checkTerrainObjectCollision(x, y, tmpVector))
                        // Create the terrain object
                        terrainObjects.add(new TerrainObject("Brick", tmpVector2.set(x, y)));
                }
                // Set the terrain objects last position
                terrainObjectsLastPosition += TERRAIN_OBJECTS_STREAMING_DISTANCE;
                // Clean objects
                cleanTerrainObjects();
            }
        }
        else
        {
            // Generate random terrain objects
            if(terrainObjectsLastPosition - Player.getInstance().getPosition().y >= -TERRAIN_OBJECTS_STREAMING_DISTANCE / 4f)
            {
                // Set the model's size
                tmpVector.set(2.5f, 4.5f);
                // Create the terrain objects
                for (int i = 0; i < TERRAIN_OBJECTS_COUNT; i++)
                {
                    // Find a random position
                    float x = MathUtils.random(3f, Configuration.WORLD_WIDTH - 3f);
                    float y = MathUtils.random(terrainObjectsLastPosition - 3f,
                            terrainObjectsLastPosition - TERRAIN_OBJECTS_STREAMING_DISTANCE);

                    // Ensure that it doesn't collide with other terrain objects
                    if (checkTerrainObjectCollision(x, y, tmpVector))
                        // Create the terrain object
                        terrainObjects.add(new TerrainObject("Brick", tmpVector2.set(x, y)));
                }
                // Set the terrain objects last position
                terrainObjectsLastPosition -= TERRAIN_OBJECTS_STREAMING_DISTANCE;
                // Clean objects
                cleanTerrainObjects();
            }
        }
    }

    /*
       Will check the collision between terrain objects
    */
    private boolean checkTerrainObjectCollision(float x, float y, Vector2 size)
    {
        // Loop through all the terrain objects
        for(TerrainObject object : terrainObjects)
        {
            // Check object's collision with a radius
            if(GameMath.checkCollision(tmpVector2.set(x, y), size, object.getPosition(), tmpVector3.set(object.getSize()).scl(2f)))
                return false;
        }
        return true;
    }

    private void cleanTerrainObjects()
    {
        // Find the screen's beginning
        Vector2 startPosition = Camera.getInstance().screenToWorld(tmpVector.set(0f, 0f));
        // Loop through all the objects
        Iterator<TerrainObject> iterator = terrainObjects.iterator();
        while (iterator.hasNext())
        {
            // Get the object
            TerrainObject object = iterator.next();
            // Check position
            if(object.getPosition().x + object.getSize().x <= startPosition.x)
                // Destroy object
                iterator.remove();
        }
    }

    public void process()
    {
        // Update objects
        updateObjects();
        // Update the terrain
        update();
    }

    private void update()
    {
        // Did we change the camera's position ?
        if(Camera.getInstance().isChangedPosition())
        {
            // Create vertical terrain
            if(GameLogic.getInstance().isHorizontal())
            {
                // Generate terrain objects
                generateTerrainObjects(false);
                // Get terrain's coordinates
                float left = Camera.getInstance().screenToWorld(tmpVector.set(0f, 0f)).x;
                // Move the terrain
                body.setTransform(left, body.getPosition().y, 0f);
            }
            else
            {
                // Generate terrain objects
                generateTerrainObjects(false);
                // Get terrain's coordinates
                float up = Camera.getInstance().screenToWorld(tmpVector.set(0f, 0f)).y;
                // Move the terrain
                body.setTransform(body.getPosition().x, up, 0f);
            }
        }
    }

    private void updateObjects()
    {
        // Update all terrain objects
        for(TerrainObject object : terrainObjects)
            object.process();

        // Skip if we're not rendering objects
        if(!renderObjects)
            return;

        // Update all objects
        Iterator<GameObject> iterator = objects.iterator();
        while(iterator.hasNext())
        {
            // Get the object
            GameObject object = iterator.next();
            // Destroy or update
            if(object.shouldDestroy())
            {
                object.destroyNow();
                iterator.remove();
            }
            else
                object.process();
        }
    }

    public void render()
    {
        // Draw the background
        drawBackground();
        // Draw objects
        drawObjects();
        // Render the walls
        drawWalls();
    }

    private void drawBackground()
    {
        // Draw the background
        Core.getInstance().getGraphicsManager().setColor(backgroundColor);
        Core.getInstance().getGraphicsManager().drawTextureRegion(emptyTexture, Core.getInstance().getGraphicsManager().EMPTY_VECTOR,
                Core.getInstance().getGraphicsManager().SCREEN_VECTOR, 0f);

        Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
    }

    private void drawObjects()
    {
        // Draw all terrain objects
        for(TerrainObject object : terrainObjects)
            object.render();

        // Skip if we're not rendering objects
        if(!renderObjects)
            return;

        // Draw all objects
        for(GameObject object : objects)
            object.render();
    }

    public void drawObjectsAfterPlayer()
    {
        // Draw all objects
        for(GameObject object : objects)
            object.renderAfterPlayer();
    }

    private void drawWalls()
    {
        // Get wall size
        float wallSize = GameMath.pixelsPerMeters(1.5f);
        float wallLayerSize = GameMath.pixelsPerMeters(0.25f);
        float width = Core.getInstance().getGraphicsManager().WIDTH;
        float height = Core.getInstance().getGraphicsManager().HEIGHT;
        // Draw horizontal walls
        if(GameLogic.getInstance().isHorizontal())
        {
            // Draw top side
            Core.getInstance().getGraphicsManager().setColor(leftWallColor);
            Core.getInstance().getGraphicsManager().drawTextureRegion(emptyTexture, 0f, height - wallSize, width, wallSize, 0f);
            // Draw top side layer
            Core.getInstance().getGraphicsManager().setColor(Color.BLACK);
            Core.getInstance().getGraphicsManager().drawTextureRegion(emptyTexture, 0f, height - wallSize, width, wallLayerSize, 0f);

            // Draw bottom side
            wallSize *= 2f;
            Core.getInstance().getGraphicsManager().setColor(rightWallColor);
            Core.getInstance().getGraphicsManager().drawTextureRegion(emptyTexture, 0f, 0f, width, wallSize, 0f);
            // Draw bottom side layer
            Core.getInstance().getGraphicsManager().setColor(Color.BLACK);
            Core.getInstance().getGraphicsManager().drawTextureRegion(emptyTexture, 0f, wallSize - wallLayerSize, width, wallLayerSize, 0f);

            // Restore color
            Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
        }
        else
        {
            // Draw left side
            Core.getInstance().getGraphicsManager().setColor(leftWallColor);
            Core.getInstance().getGraphicsManager().drawTextureRegion(emptyTexture, 0f, 0f, wallSize, height, 0f);
            // Draw right side
            Core.getInstance().getGraphicsManager().setColor(rightWallColor);
            Core.getInstance().getGraphicsManager().drawTextureRegion(emptyTexture, width - wallSize, 0f, wallSize, height, 0f);

            // Draw left side layer
            Core.getInstance().getGraphicsManager().setColor(Color.BLACK);
            Core.getInstance().getGraphicsManager().drawTextureRegion(emptyTexture, wallSize - wallLayerSize, 0f, wallLayerSize, height, 0f);
            // Draw right side
            Core.getInstance().getGraphicsManager().drawTextureRegion(emptyTexture, width - wallSize, 0f, wallLayerSize, height, 0f);

            // Restore color
            Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
        }
    }

    public void onObjectPassed()
    {
        // Destroy the passed object
        objects.first().destroy();
        // Create a new object
        generateNextObject();
    }

    public void destroyNextObject()
    {
        // Destroy the next object
        objects.first().destroyNow();
        objects.removeIndex(0);
        objects.first().setColorAlpha(1f);
        // Create a new object
        generateNextObject();
        objects.get(1).setColorAlpha(0.5f);
    }

    /*
        Will clean all of the terrain fixtures
    */
    private void cleanTerrain()
    {
        if(body.getFixtureList().size < 2)
            return;

        // Destroy all the fixtures
        body.destroyFixture(body.getFixtureList().first());
        body.destroyFixture(body.getFixtureList().first());
        if(!GameLogic.getInstance().isHorizontal() && body.getFixtureList().size >= 4)
        {
            body.destroyFixture(body.getFixtureList().first());
            body.destroyFixture(body.getFixtureList().first());
        }
    }

    /*
        Will clean all of the terrain objects
    */
    private void cleanObjects()
    {
        // Destroy all the objects
        for(GameObject object : objects)
            object.destroy();

        objects.clear();
    }

    public void reset()
    {
        // Clean the terrain
        cleanTerrain();
        // Reset terrain's position
        body.setTransform(0f, 0f, 0f);
        // Clean all objects
        cleanObjects();
        // Reset values
        lastTerrainPosition = 0f;
        terrainObjectsLastPosition = 0f;
        lastObjectPosition = GameLogic.getInstance().isHorizontal() ? Configuration.WORLD_WIDTH - Configuration.WORLD_WIDTH * 0.75f :
                lastTerrainPosition - GameLogic.getInstance().getWorldHeight() * 0.5f;
    }

    public void destroy()
    {
        // Reset the terrain
        reset();
        // Destroy the body
        GameWorld.getInstance().removeFromWorld(body);
    }

    public void setRenderObjects(boolean renderObjects) { this.renderObjects = renderObjects; }

    public TextureRegion getHoopsTexture() {
        return hoopsTexture;
    }

    public TextureRegion getFrontHoopsTexture() {
        return frontHoopsTexture;
    }

    public void setHoopsTexture(String name)
    {
        hoopsTexture = Core.getInstance().getResourcesManager().getResource("Hoops/" + name,
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

        frontHoopsTexture = Core.getInstance().getResourcesManager().getResource("HoopsFronts/" + name,
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION);
    }

    public GameObject getNextObject()
    {
        return objects.size == 0 ? null : (objects.first().isDestroying() ? objects.get(1) : objects.first());
    }

    public static Terrain getInstance() { return instance; }
}
