package com.ormisiclapps.flappydunkermadness.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIText;
import com.ormisiclapps.flappydunkermadness.utility.Configuration;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIProgressBar;

/**
 * Created by OrMisicL on 1/1/2016.
 * Used to render the first loading screen of the game.
 */
public class LoadingScreen implements Screen
{
    private Texture developerLogoTexture, emptyTexture;
    private Vector2 logoPosition, logoSize;
    private UIProgressBar uiProgressBar;
    private UIText loadingText;

    public LoadingScreen()
    {
        // Reset instances
        developerLogoTexture = null;
        emptyTexture = null;
        uiProgressBar = null;
        logoPosition = null;
        logoSize = null;
    }

    @Override
    public void initialize()
    {
        // Compute the loading bar position and size
        Vector2 loadingBarSize = new Vector2(Core.getInstance().getGraphicsManager().WIDTH / 2f,
                Core.getInstance().getGraphicsManager().HEIGHT / 50f);
        
        Vector2 loadingBarPosition = new Vector2(GameMath.getCenteredPosition(
                new Vector2(Core.getInstance().getGraphicsManager().WIDTH / 2, Core.getInstance().getGraphicsManager().HEIGHT / 6),
                loadingBarSize));

        // Create the progress bar instance
        uiProgressBar = new UIProgressBar(loadingBarPosition, loadingBarSize,
                Configuration.PROGRESSBAR_CONTAINER_COLOR, Configuration.PROGRESSBAR_BACKGROUND_COLOR,
                Configuration.PROGRESSBAR_COLOR);

        // Set it to be visible
        uiProgressBar.toggle(true);
        // Create the loading text
        loadingText = new UIText(Core.getInstance().getGraphicsManager().HEIGHT / 40, Color.BLACK, 0f, null);
        loadingText.toggle(true);
        // Add to the UI main
        Core.getInstance().getGraphicsManager().getUIMain().addWidget(uiProgressBar);
        Core.getInstance().getGraphicsManager().getUIMain().addWidget(loadingText);
        // Get the developer logo texture
        developerLogoTexture = Core.getInstance().getResourcesManager().getResource("DeveloperLogo", ResourceType.RESOURCE_TYPE_TEXTURE);
        emptyTexture = Core.getInstance().getResourcesManager().getResource("Empty", ResourceType.RESOURCE_TYPE_TEXTURE);
        // Create vectors
        logoSize = new Vector2(Core.getInstance().getGraphicsManager().WIDTH / 1.2f, Core.getInstance().getGraphicsManager().WIDTH / 1.2f);
        logoPosition = new Vector2(Core.getInstance().getGraphicsManager().WIDTH / 2f - logoSize.x / 2f,
                Core.getInstance().getGraphicsManager().HEIGHT * 0.6f - logoSize.y / 2f);
    }

    @Override
    public void activate()
    {
        // Load all resources
        Core.getInstance().getResourcesManager().loadResources();
    }

    @Override
    public void deactivate()
    {
        // Hide the progress bar
        uiProgressBar.toggle(false);
        loadingText.toggle(false);
    }

    @Override
    public void update()
    {
        // If the resources has finished loading then transit to the main menu
        if(!Core.getInstance().getResourcesManager().isLoading())
        {
            // Initialize all screen
            Core.getInstance().getScreensManager().initializeAllScreens();
            // Sign in to Google Play
            if(Core.getInstance().getStatsSaver().savedData.GPGConnected &&
                    !Core.getInstance().getOSUtility().isSignedIn() && Core.getInstance().getOSUtility().isNetworkConnected())
                Core.getInstance().getOSUtility().signIn();

            // Set to main menu
            Core.getInstance().getScreensManager().setMainMenuScreen();
        }
        // Get loading progress
        int progress = Core.getInstance().getResourcesManager().getLoadingProgress();
        // Set the progress bar's progress
        uiProgressBar.setProgress(progress);
        // Draw the loading text
        Vector2 textSize = loadingText.getSize("Loading (" + progress + " %) ...");
        loadingText.drawText("Loading (" + progress + " %) ... ",
                uiProgressBar.getPosition().x + uiProgressBar.getSize().x / 2 - textSize.x / 2,
                uiProgressBar.getPosition().y - textSize.y / 1.5f);
    }

    @Override
    public void render()
    {
        // Draw background
        Core.getInstance().getGraphicsManager().setColor(0.5490f, 0.5490f, 0.5490f, 1f);
        Core.getInstance().getGraphicsManager().drawTexture(emptyTexture, Core.getInstance().getGraphicsManager().EMPTY_VECTOR,
                Core.getInstance().getGraphicsManager().SCREEN_VECTOR, 0f);

        Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
        // Draw the logo
        Core.getInstance().getGraphicsManager().drawTexture(developerLogoTexture, logoPosition, logoSize, 0f);
    }

    @Override
    public void postFadeRender() {

    }

    @Override
    public void dispose()
    {
        // Dispose of the loading progress bar
        uiProgressBar.dispose();
    }
}
