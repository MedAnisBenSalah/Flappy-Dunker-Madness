package com.ormisiclapps.flappydunkermadness.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.game.screens.GameScreen;
import com.ormisiclapps.flappydunkermadness.game.screens.MainMenuScreen;
import com.ormisiclapps.flappydunkermadness.input.InputHandler;
import com.ormisiclapps.flappydunkermadness.managers.*;
import com.ormisiclapps.flappydunkermadness.os.OSUtility;
import com.ormisiclapps.flappydunkermadness.utility.ModelSettings;
import com.ormisiclapps.flappydunkermadness.utility.StatsSaver;

/**
 * Created by OrMisicL on 5/29/2016.
 */
public class Core
{
    private ResourcesManager resourcesManager;
    private GraphicsManager graphicsManager;
    private ModelManager modelManager;
    private ScreensManager screensManager;
    private FileManager fileManager;
    private InputHandler inputHandler;
    private ModelSettings modelSettings;
    private StatsSaver statsSaver;
    private OSUtility osUtility;

    private Texture emptyTexture;

    private static Core instance;

    public int FPS = 0;

    public final String VERSION = "1.0";

    public Core(OSUtility osUtility)
    {
        // Set the instance
        instance = this;
        // Set the os utility instance
        this.osUtility = osUtility;
    }

    public void initialize()
    {
        // Create the file manager instance
        fileManager = new FileManager();
        // Create the stats saved
        statsSaver = new StatsSaver();
        // Load save
        statsSaver.load();
        // Create the resources manager instance
        resourcesManager = new ResourcesManager();
        // Load initial resources
        resourcesManager.loadInitialResources();
        // Create the model manager instance
        modelManager = new ModelManager();
        // Create the graphics manager instance
        graphicsManager = new GraphicsManager();
        // Initialize graphics manager
        graphicsManager.initialize();
        // Create the model settings instance
        modelSettings = new ModelSettings();
        // Create the screens manager
        screensManager = new ScreensManager();
        // Create the input handler
        inputHandler = new InputHandler();
        // Reset instances
        emptyTexture = null;
    }

    public void update()
    {
        // Update FPS
        FPS = Gdx.graphics.getFramesPerSecond();
        // Update the resources manager
        resourcesManager.update();
        // Update the graphics manager
        graphicsManager.update();
        // Update the screens manager
        screensManager.update();
        // Update the UI
        graphicsManager.updateUI();
        // Get the texture
        if(emptyTexture == null && resourcesManager.isResourceLoaded("Empty", ResourceType.RESOURCE_TYPE_TEXTURE))
            // Get the empty texture
            emptyTexture = resourcesManager.getResource("Empty", ResourceType.RESOURCE_TYPE_TEXTURE);

        // Clear input
        inputHandler.clear();
    }

    public void render()
    {
        // Prepare rendering
        graphicsManager.prepareRendering();
        // Begin rendering
        graphicsManager.beginRendering();
        // Render the active screen
        screensManager.render();
        // PreFade UI rendering
        graphicsManager.preFadeUIRender();
        // Render the faded screen
        if(graphicsManager.getScreenEffects().isFaded())
        {
            // Ensure the empty texture
            if(emptyTexture != null)
            {
                // Set the color
                graphicsManager.setColor(graphicsManager.getScreenEffects().getFadeColor());
                // Render a full screen rectangle
                graphicsManager.drawTexture(emptyTexture, graphicsManager.EMPTY_VECTOR, graphicsManager.SCREEN_VECTOR, 0f);
                // Restore the color
                graphicsManager.setColor(Color.WHITE);
            }
        }
        // PostFade rendering cycle
        screensManager.postFadeRender();
        // PostFade UI rendering
        graphicsManager.postFadeUIRender();
        // Special case for unlock window rendering
        if(screensManager.getCurrentScreen() instanceof MainMenuScreen)
            screensManager.getMainMenuScreen().postUIRender();

        // Finish rendering
        graphicsManager.finishRendering();
    }

    public void pause()
    {
        // Save data
        if(statsSaver.savedData != null)
            statsSaver.save();

        // Pause the game
        if(screensManager.getCurrentScreen() instanceof GameScreen)
            screensManager.getGameScreen().pause();
    }

    public void resume()
    {
        // Nothing to do here
    }

    public void terminate()
    {
        // Dispose of the debug stuff
        /*if(isDebug)
            UIDebug.dispose();*/

        // Save data
        if(statsSaver.savedData != null)
            statsSaver.save();

        // Dispose of the screen manager
        screensManager.dispose();
        // Dispose all of the loaded resources
        resourcesManager.dispose();
        // Dispose of the graphics manager
        graphicsManager.dispose();
    }

    public ResourcesManager getResourcesManager() { return resourcesManager; }
    public GraphicsManager getGraphicsManager() { return graphicsManager; }
    public ScreensManager getScreensManager() { return screensManager; }
    public InputHandler getInputHandler() { return inputHandler; }
    public FileManager getFileManager() { return fileManager; }
    public ModelManager getModelManager() { return modelManager; }
    public ModelSettings getModelSettings() { return modelSettings; }
    public StatsSaver getStatsSaver() { return statsSaver; }

    public OSUtility getOSUtility() { return osUtility; }

    public static Core getInstance()
    {
        return instance;
    }
}
