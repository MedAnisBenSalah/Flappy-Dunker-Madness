package com.ormisiclapps.flappydunkermadness.managers;

import com.ormisiclapps.flappydunkermadness.game.screens.GameScreen;
import com.ormisiclapps.flappydunkermadness.game.screens.LoadingScreen;
import com.ormisiclapps.flappydunkermadness.game.screens.MainMenuScreen;
import com.ormisiclapps.flappydunkermadness.game.screens.Screen;
import com.ormisiclapps.flappydunkermadness.game.screens.ShopScreen;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIMain;


/**
 * Created by OrMisicL on 5/29/2016.
 * Will handle screen initializing, activating, deactivating
 */
public class ScreensManager
{
    private Screen currentScreen;
    private LoadingScreen loadingScreen;
    private MainMenuScreen mainMenuScreen;
    private GameScreen gameScreen;
    private ShopScreen shopScreen;

    public ScreensManager()
    {
        // Create screen instances
        loadingScreen = new LoadingScreen();
        mainMenuScreen = new MainMenuScreen();
        gameScreen = new GameScreen();
        shopScreen = new ShopScreen();
        // Initialize the loading screen
        loadingScreen.initialize();
        // Activate the loading screen
        loadingScreen.activate();
        // Set the current screen
        currentScreen = loadingScreen;
    }

    public void initializeAllScreens()
    {
        // Initialize all screens
        mainMenuScreen.initialize();
        gameScreen.initialize();
        shopScreen.initialize();
        //creditsScreen.initialize();
    }

    public void update()
    {
        // Update the current screen
        currentScreen.update();
    }

    public void postFadeRender()
    {
        // PostFade rendering cycle for the current screen
        currentScreen.postFadeRender();
    }

    public void render()
    {
        // Render the current screen
        currentScreen.render();
    }

    public void dispose()
    {
        // Destroy the current screen
        currentScreen.dispose();
    }

    public void setMainMenuScreen()
    {
        // Deactivate the current screen
        currentScreen.deactivate();
        // Activate main menu
        mainMenuScreen.activate();
        // Set it as the current screen
        currentScreen = mainMenuScreen;
    }

    public void setGameScreen()
    {
        // Deactivate the current screen
        currentScreen.deactivate();
        // Activate game screen
        gameScreen.activate();
        // Set it as the current screen
        currentScreen = gameScreen;
    }

    public void setShopScreen()
    {
        // Deactivate the current screen
        currentScreen.deactivate();
        // Activate shop screen
        shopScreen.activate();
        // Set it as the current screen
        currentScreen = shopScreen;
    }

    public Screen getCurrentScreen() { return currentScreen; }
    public GameScreen getGameScreen() { return gameScreen; }
    public MainMenuScreen getMainMenuScreen() { return mainMenuScreen; }
    public ShopScreen getShopScreen() { return shopScreen; }
}
