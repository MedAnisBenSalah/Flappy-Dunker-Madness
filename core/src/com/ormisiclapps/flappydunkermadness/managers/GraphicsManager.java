package com.ormisiclapps.flappydunkermadness.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIMain;
import com.ormisiclapps.flappydunkermadness.utility.Configuration;
import com.ormisiclapps.flappydunkermadness.graphics.effects.ScreenEffects;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;

/**
 * Created by OrMisicL on 5/29/2016.
 * Will handle graphics related stuff (Camera, PPM, FBOs ...)
 */
public class GraphicsManager
{
    // Graphics general variables
    public int WIDTH;
    public int HEIGHT;
    public Vector2 EMPTY_VECTOR;
    public Vector2 SCREEN_VECTOR;
    public Vector2 HALF_SCREEN_VECTOR;
    public float DELTA_TIME = 0;

    private SpriteBatch spriteBatch;
    private ScreenEffects screenEffects;
    private UIMain uiMain;
    private Box2DDebugRenderer debugRenderer;

    public GraphicsManager()
    {
        // Create the sprite batch instance
        spriteBatch = new SpriteBatch();
        // Create the screen effects instance
        screenEffects = new ScreenEffects();
        // Create the UIMain instance
        uiMain = new UIMain();
        // Initial variables update
        updateGraphicsVariables();
        // Initialize PPM
        GameMath.initializePPM(WIDTH);
        // Create the debug renderer instances
       // if(Core.getInstance().isDebug)
            debugRenderer = new Box2DDebugRenderer(true, true, false, false, false, false);
    }

    public void initialize()
    {
        // Initialize the UIMain
        uiMain.initialize();
    }

    private void updateGraphicsVariables()
    {
        // Update the graphics variables
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();
        DELTA_TIME = Gdx.graphics.getDeltaTime();
        // Create vectors
        if(SCREEN_VECTOR == null)
        {
            SCREEN_VECTOR = new Vector2();
            HALF_SCREEN_VECTOR = new Vector2();
            EMPTY_VECTOR = new Vector2();
        }
        // Update vectors
        SCREEN_VECTOR.set(WIDTH, HEIGHT);
        HALF_SCREEN_VECTOR.set(WIDTH / 2, HEIGHT / 2);
    }

    public void update()
    {
        // Update the graphics variables
        updateGraphicsVariables();
    }

    public void updateUI()
    {
        // Update UI
        uiMain.process();
    }

    public void prepareRendering()
    {
        // Clear the screen
        Gdx.gl.glClearColor(Configuration.BACKGROUND_COLOR, Configuration.BACKGROUND_COLOR, Configuration.BACKGROUND_COLOR, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
        // Enable GL blending
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void beginRendering()
    {
        // Begin sprite batch rendering
        spriteBatch.begin();
    }

    public void finishRendering()
    {
        // Finish sprite batch rendering
        spriteBatch.end();

        // Render Box2D debug
        //if(GameWorld.getInstance() != null)
          //  debugRenderer.render(GameWorld.getInstance().getWorld(), Camera.getInstance().getInterface().combined);
    }

    /*
        This will render the UI affected by screen fading
    */
    public void preFadeUIRender()
    {
        // Render UI
        uiMain.preFadeRender();
    }

    /*
        This will render the UI on top of the faded screen (not affected)
    */
    public void postFadeUIRender()
    {
        // Render UI
        uiMain.postFadeRender();
        // Render screen transition effect
        /*if(screenEffects.isTransitioning() && Core.getInstance().emptyTexture != null)
        {
            // Set the rendering color
            setColor(screenEffects.getTransitionColor());
            // Render a full screen texture
            drawTexture(Core.getInstance().emptyTexture, EMPTY_VECTOR, SCREEN_VECTOR, 0f);
        }*/
    }

    /*public void renderDebugUI()
    {
        // Render debug UI
        UIDebug.render();
    }

    public void renderDebugScene()
    {
        // Render Box2D debug
        if(Core.getInstance().isDebug && Core.getInstance().drawDebugObjects)
            debugRenderer.render(GameWorld.getInstance().getWorld(), Camera.getInstance().getInterface().combined);
    }*/

    public void dispose()
    {
        // Dispose of the UI
        uiMain.dispose();
        // Dispose of sprite batch
        spriteBatch.dispose();
    }

    /* Will use the sprite batch to render a rectangular texture
    * Warning: Use it only after sprite batch's begin */
    public void drawTexture(Texture texture, Vector2 position, Vector2 size, float rotation)
    {
        // Verify if the sprite batch has began rendering
        if(!spriteBatch.isDrawing()/* || (Core.getInstance().isDebug && !Core.getInstance().drawTexture)*/)
            return;

        // Render the texture
        drawTexture(texture, position.x, position.y, size.x, size.y, rotation);
    }

    /* Will use the sprite batch to render a rectangular texture
    * Warning: Use it only after sprite batch's begin */
    public void drawTexture(Texture texture, Vector2 position, Vector2 size, float rotation, boolean flipX, boolean flipY)
    {
        // Validate debug
        /*if(Core.getInstance().isDebug && !Core.getInstance().drawTexture)
            return;*/

        // Verify if the sprite batch has began rendering
        if(!spriteBatch.isDrawing())
            return;

        // Render the texture
        spriteBatch.draw(texture, position.x, position.y, size.x / 2, size.y / 2, size.x, size.y, 1f, 1f, rotation, 0, 0,
                texture.getWidth(), texture.getHeight(), flipX, flipY);
    }

    /* Will use the sprite batch to render a rectangular texture
    * Warning: Use it only after sprite batch's begin */
    public void drawTexture(Texture texture, float x, float y, float sizeX, float sizeY, float rotation)
    {
        // Validate debug
        /*if(Core.getInstance().isDebug && !Core.getInstance().drawTexture)
            return;*/

        // Verify if the sprite batch has began rendering
        if(!spriteBatch.isDrawing())
            return;

        // Render the texture
        spriteBatch.draw(texture, x, y, sizeX / 2, sizeY / 2, sizeX, sizeY, 1f, 1f, rotation, 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
    }

    /* Will use the sprite batch to render a rectangular texture and rotate it around a specific point
    * Warning: Use it only after sprite batch's begin */
    public void drawTexture(Texture texture, Vector2 position, Vector2 size, float rotation, Vector2 origin)
    {
        // Validate debug
        /*if(Core.getInstance().isDebug && !Core.getInstance().drawTexture)
            return;*/

        // Verify if the sprite batch has began rendering
        if(!spriteBatch.isDrawing())
            return;

        // Render the texture
        spriteBatch.draw(texture, position.x, position.y, origin.x, origin.y, size.x, size.y, 1f, 1f, rotation, 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
    }

    /* Will use the sprite batch to render a part of a texture (region)
    * Warning: Use it only after sprite batch's begin */
    public void drawTextureRegion(TextureRegion region, Vector2 position, Vector2 size, float rotation)
    {
        // Validate debug
        /*if(Core.getInstance().isDebug && !Core.getInstance().drawTexture)
            return;*/

        // Verify if the sprite batch has began rendering
        if(!spriteBatch.isDrawing())
            return;

        // Render the texture
        spriteBatch.draw(region, position.x, position.y, size.x / 2, size.y / 2, size.x, size.y, 1f, 1f, rotation, false);
    }

    /* Will use the sprite batch to render a texture region
    * Warning: Use it only after sprite batch's begin */
    public void drawTextureRegion(TextureRegion region, float x, float y, float sizeX, float sizeY, float rotation)
    {
        // Validate debug
        /*if(Core.getInstance().isDebug && !Core.getInstance().drawTexture)
            return;*/

        // Verify if the sprite batch has began rendering
        if(!spriteBatch.isDrawing())
            return;

        // Render the texture region
        spriteBatch.draw(region, x, y, sizeX / 2, sizeY / 2, sizeX, sizeY, 1f, 1f, rotation, false);
    }

    /* Will use the sprite batch to render a texture region and rotate it around a specific point
    * Warning: Use it only after sprite batch's begin */
    public void drawTextureRegion(TextureRegion region, Vector2 position, Vector2 size, float rotation, Vector2 origin)
    {
        // Validate debug
        /*if(Core.getInstance().isDebug && !Core.getInstance().drawTexture)
            return;*/

        // Verify if the sprite batch has began rendering
        if(!spriteBatch.isDrawing())
            return;

        // Render the texture
        spriteBatch.draw(region, position.x, position.y, origin.x, origin.y, size.x, size.y, 1f, 1f, rotation);
    }

    /* Will use the sprite batch to draw a text from a font
    * Warning: Use it only after sprite batch's begin */
    public void drawText(BitmapFont font, String text, Vector2 position)
    {
        // Verify if the sprite batch has began rendering
        if(!spriteBatch.isDrawing())
            return;

        // Render the font
        font.draw(spriteBatch, text, position.x, position.y);
    }

    /* Will use sprite batch to draw a mesh
    * Warning: Use it only after sprite batch's begin */
    public void drawTexture(Texture texture, float[] vertices, int verticesCount)
    {
        // Validate debug
        /*if(Core.getInstance().isDebug && !Core.getInstance().drawTexture)
            return;*/

        // Verify if the sprite batch has began rendering
        if(!spriteBatch.isDrawing())
            return;

        // Draw to the sprite batch
        spriteBatch.draw(texture, vertices, 0, verticesCount);
    }

    /* Will use sprite batch to draw a particle effect
    * Warning: Use it only after sprite batch's begin */
    public void drawParticleEffect(ParticleEffect effect)
    {
        // Validate debug
        /*if(Core.getInstance().isDebug && !Core.getInstance().drawTexture)
            return;*/

        // Verify if the sprite batch has began rendering
        if(!spriteBatch.isDrawing())
            return;

        // Draw to the sprite batch
        effect.draw(spriteBatch, DELTA_TIME);
    }

    /* Will use sprite batch to draw a sprite
    * Warning: Use it only after sprite batch's begin */
    public void drawSprite(Sprite sprite)
    {
        // Validate debug
        /*if(Core.getInstance().isDebug && !Core.getInstance().drawTexture)
            return;*/

        // Verify if the sprite batch has began rendering
        if(!spriteBatch.isDrawing())
            return;

        // Draw to the sprite batch
        sprite.draw(spriteBatch);
    }

    /* Will set the sprite batch's color
    * Warning: Use it only after sprite batch's begin */
    public void setColor(Color color)
    {
        // Verify if the sprite batch has began rendering
        if(!spriteBatch.isDrawing())
            return;

        // Change the color
        spriteBatch.setColor(color);
    }

    /* Will set the sprite batch's color
    * Warning: Use it only after sprite batch's begin */
    public void setColor(float r, float g, float b, float a)
    {
        // Verify if the sprite batch has began rendering
        if(!spriteBatch.isDrawing())
            return;

        // Change the color
        spriteBatch.setColor(r, g, b, a);
    }

    public ScreenEffects getScreenEffects() { return screenEffects; }
    public UIMain getUIMain() { return uiMain; }
}
