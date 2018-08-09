package com.ormisiclapps.flappydunkermadness.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.GameMode;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.enumerations.ScreenTouchType;
import com.ormisiclapps.flappydunkermadness.game.core.GameLogic;
import com.ormisiclapps.flappydunkermadness.game.entities.Camera;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.Player;
import com.ormisiclapps.flappydunkermadness.game.nodes.save.SaveNode;
import com.ormisiclapps.flappydunkermadness.game.world.Terrain;
import com.ormisiclapps.flappydunkermadness.graphics.effects.ScreenEffects;
import com.ormisiclapps.flappydunkermadness.graphics.others.GameModeContainer;
import com.ormisiclapps.flappydunkermadness.graphics.others.RatingDialog;
import com.ormisiclapps.flappydunkermadness.graphics.others.UnlockWindow;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIButton;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIMain;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIText;
import com.ormisiclapps.flappydunkermadness.os.OSUtility;
import com.ormisiclapps.flappydunkermadness.utility.Configuration;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by OrMisicL on 6/1/2016.
 */
public class MainMenuScreen implements Screen
{
    private UIButton achievementsButton, leaderboardsButton, rateButton, challengesButton, shopButton, soundButton, vibrateButton, nextButton,
        previousButton, playGamesButton;

    private UIText scoreText, scoreNumberText, lastScoreText, newRecordText;
    private GameModeContainer[] gameModeContainers;
    private float currentScreenPosition, maximumScreenPosition, scrollPadding, scrollTo, scrollStep, lastScrollMovement;
    private UnlockWindow unlockWindow;
    private RatingDialog ratingDialog;
    private TextureRegion emptyTexture;
    private Map<String, String> unlockedItems;
    private boolean skipInput;
    
    private static final float FADE_ALPHA = 0.55f;
    private static final String[] gameModeNames = { "Flappy", "Falling" };

    public MainMenuScreen()
    {
        // Reset instances
        achievementsButton = null;
        leaderboardsButton = null;
        rateButton = null;
        challengesButton = null;
        soundButton = null;
        vibrateButton = null;
        shopButton = null;
        scoreText = null;
        playGamesButton = null;
        scoreNumberText = null;
        lastScoreText = null;
        newRecordText = null;
        gameModeContainers = null;
        // Reset flags
        skipInput = false;
    }

    @Override
    public void initialize()
    {
        // Create unlocked items map
        unlockedItems = new HashMap<String, String>();
        // Get height and width
        float w = Core.getInstance().getGraphicsManager().WIDTH;
        float h = Core.getInstance().getGraphicsManager().HEIGHT;
        // Create buttons
        leaderboardsButton = new UIButton((TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/LeaderboardsButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        achievementsButton = new UIButton((TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/AchievementsButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        rateButton = new UIButton((TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/RateButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        shopButton = new UIButton((TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/ShopButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        challengesButton = new UIButton((TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/ChallengesButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        TextureRegion texture = Core.getInstance().getResourcesManager().getResource(Core.getInstance().getStatsSaver().savedData.soundState
                ? "UI/SoundOnButton" : "UI/SoundOffButton", ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

        soundButton = new UIButton(texture);

        texture = Core.getInstance().getResourcesManager().getResource(Core.getInstance().getStatsSaver().savedData.vibrationState
                ? "UI/VibrationOnButton" : "UI/VibrationOffButton", ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

        vibrateButton = new UIButton(texture);

        playGamesButton = new UIButton((TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/PlayGamesButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        // Calculate the button's size
        float size = w / 8f;
        float halfScreen = w / 2f - size / 2f;
        // Setup buttons
        rateButton.setFadeEffected(false);
        rateButton.setSize(new Vector2(size, size));
        rateButton.setPosition(new Vector2(new Vector2(halfScreen, h / 6f - size / 2f)));

        shopButton.setFadeEffected(false);
        shopButton.setSize(new Vector2(size, size));
        shopButton.setPosition(new Vector2(halfScreen + size * 1.25f, h / 6f - size / 2f));

        achievementsButton.setFadeEffected(false);
        achievementsButton.setSize(new Vector2(size, size));
        achievementsButton.setPosition(new Vector2(halfScreen - size * 1.25f, h / 6f - size / 2f));

        leaderboardsButton.setFadeEffected(false);
        leaderboardsButton.setSize(new Vector2(size, size));
        leaderboardsButton.setPosition(new Vector2(halfScreen - size * 2.5f, h / 6f - size / 2f));

        challengesButton.setFadeEffected(false);
        challengesButton.setSize(new Vector2(size, size));
        challengesButton.setPosition(new Vector2(new Vector2(halfScreen + size * 2.5f, h / 6f - size / 2f)));

        soundButton.setFadeEffected(false);
        soundButton.setSize(new Vector2(size, size));
        soundButton.setPosition(new Vector2(new Vector2(size * 0.25f, h * 0.9f - size / 2f)));

        vibrateButton.setFadeEffected(false);
        vibrateButton.setSize(new Vector2(size, size));
        vibrateButton.setPosition(new Vector2(new Vector2(size * 0.25f, h * 0.9f - size * 1.75f)));

        playGamesButton.setFadeEffected(false);
        playGamesButton.setSize(new Vector2(size, size));
        playGamesButton.setPosition(new Vector2(new Vector2(size * 0.25f, h * 0.9f - size * 3f)));

        TextureRegion buttonTexture = Core.getInstance().getResourcesManager().getResource("UI/NextButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

        // Create texts
        scoreText = new UIText((int)h / 14, Color.BLACK, 0f, null);
        scoreNumberText = new UIText((int)h / 14, new Color(0.1137f, 0.5647f, 0.0039f, 1f), 0f, null);
        lastScoreText = new UIText((int)h / 22, Color.BLACK, 0f, null);
        newRecordText = new UIText((int)h / 30, new Color(0.1137f, 0.5647f, 0.0039f, 1f), 0f, null);
        scoreText.setFadeEffected(false);
        scoreNumberText.setFadeEffected(false);
        lastScoreText.setFadeEffected(false);
        newRecordText.setFadeEffected(false);

        // Create game mode container array
        gameModeContainers = new GameModeContainer[SaveNode.GAMEMODES_COUNT];
        // Create game mode containers
        for(int i = 0; i < SaveNode.GAMEMODES_COUNT; i++)
            gameModeContainers[i] = new GameModeContainer(i, gameModeNames[i], scoreText, scoreNumberText, lastScoreText, newRecordText);

        nextButton = new UIButton(buttonTexture);
        previousButton = new UIButton(buttonTexture);
        // Setup buttons
        float buttonSize = Core.getInstance().getGraphicsManager().HEIGHT / 17f;
        float playButtonPosition = gameModeContainers[0].getPlayButton().getPosition().y;
        float playButtonSize = gameModeContainers[0].getPlayButton().getSize().y;
        nextButton.setFadeEffected(false);
        nextButton.setSize(new Vector2(buttonSize, buttonSize));
        nextButton.setPosition(new Vector2(Core.getInstance().getGraphicsManager().WIDTH - buttonSize * 1.25f,
                playButtonPosition + playButtonSize / 2f - buttonSize / 2f));

        previousButton.setFadeEffected(false);
        previousButton.setSize(new Vector2(buttonSize, buttonSize));
        previousButton.setPosition(new Vector2(buttonSize * 0.25f, playButtonPosition + playButtonSize / 2f - buttonSize / 2f));
        previousButton.setRotation(90f);

        // Hide elements
        rateButton.toggle(false);
        leaderboardsButton.toggle(false);
        achievementsButton.toggle(false);
        shopButton.toggle(false);
        challengesButton.toggle(false);
        nextButton.toggle(false);
        previousButton.toggle(false);
        scoreText.toggle(false);
        scoreNumberText.toggle(false);
        lastScoreText.toggle(false);
        newRecordText.toggle(false);
        soundButton.toggle(false);
        vibrateButton.toggle(false);
        playGamesButton.toggle(false);
        // Disable buttons
        challengesButton.setEnabled(false);
        // Add them to the UI
        UIMain.getInstance().addWidget(rateButton);
        UIMain.getInstance().addWidget(leaderboardsButton);
        UIMain.getInstance().addWidget(achievementsButton);
        UIMain.getInstance().addWidget(shopButton);
        UIMain.getInstance().addWidget(challengesButton);
        UIMain.getInstance().addWidget(soundButton);
        UIMain.getInstance().addWidget(vibrateButton);
        UIMain.getInstance().addWidget(playGamesButton);
        UIMain.getInstance().addWidget(nextButton);
        UIMain.getInstance().addWidget(previousButton);
        UIMain.getInstance().addWidget(scoreText);
        UIMain.getInstance().addWidget(scoreNumberText);
        UIMain.getInstance().addWidget(lastScoreText);
        UIMain.getInstance().addWidget(newRecordText);

        // Create unlock window
        unlockWindow = new UnlockWindow();
        // Create the rating dialog
        ratingDialog = new RatingDialog();
        // Get empty texture
        emptyTexture = Core.getInstance().getResourcesManager().getResource("UI/Empty", ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

        // Set the current screen position
        currentScreenPosition = 0f;
        // Calculate the maximum screen position
        maximumScreenPosition = Core.getInstance().getGraphicsManager().WIDTH * (gameModeContainers.length - 1);
        // calculate the scroll padding
        scrollPadding = Core.getInstance().getGraphicsManager().WIDTH * 0.15f;
        // Calculate the scroll step
        scrollStep = Core.getInstance().getGraphicsManager().WIDTH * 0.05f;
        // Reset values
        scrollTo = -1f;
        lastScrollMovement = 0f;
    }

    @Override
    public void activate()
    {
        // Show the UI elements
        shopButton.toggle(true);
        leaderboardsButton.toggle(true);
        achievementsButton.toggle(true);
        rateButton.toggle(true);
        challengesButton.toggle(true);
        soundButton.toggle(true);
        vibrateButton.toggle(true);
        playGamesButton.toggle(true);
        nextButton.toggle(true);
        previousButton.toggle(true);
        scoreText.toggle(true);
        scoreNumberText.toggle(true);
        lastScoreText.toggle(true);
        newRecordText.toggle(true);
        // Reset flags
        skipInput = false;
        // Disable rendering objects
        Terrain.getInstance().setRenderObjects(false);
        // Reset camera position
        Camera.getInstance().setPosition(new Vector2(!GameLogic.getInstance().isHorizontal() ? Configuration.WORLD_WIDTH / 2f : 0f, 0f));
        // Get the play button size
        float buttonSize = gameModeContainers[GameLogic.getInstance().getGameModeId()].getPlayButton().getSize().x;
        // Resize the player
        Player.getInstance().resize(GameMath.metersPerPixels(buttonSize / 3f));
        // Set the player position
        UIButton playButton = gameModeContainers[GameLogic.getInstance().getGameModeId()].getPlayButton();
        Vector2 playerPosition = Camera.getInstance().screenToWorld(new Vector2(playButton.getPosition()).add(buttonSize / 2f, 0f));
        Player.getInstance().setPosition(playerPosition);
        // Set new record
        gameModeContainers[GameLogic.getInstance().getGameModeId()].setNewRecord(GameLogic.getInstance().isNewRecord());
        // Setup the player
        Player.getInstance().setup();
        // Rotate the player accordingly
        Player.getInstance().setRotationInDegrees(GameLogic.getInstance().isHorizontal() ? 0f : 270f);

        // Unlock balls
        for(int i = 0; i < ShopScreen.ballsPrices.length; i++)
        {
            // Don't loop if we're below the price
            if(Core.getInstance().getStatsSaver().savedData.xp < ShopScreen.ballsPrices[i])
                break;

            // Unlock it if its locked
            if(!Core.getInstance().getStatsSaver().savedData.ballsStates[i])
            {
                Core.getInstance().getStatsSaver().savedData.ballsStates[i] = true;
                // Add it to the unlocked queue
                unlockedItems.put(ShopScreen.ballsOrder[i], "ball");
            }
        }

        // Unlock wings
        for(int i = 0; i < ShopScreen.wingsPrices.length; i++)
        {
            // Don't loop if we're below the price
            if(Core.getInstance().getStatsSaver().savedData.xp < ShopScreen.wingsPrices[i])
                break;

            // Unlock it if its locked
            if(!Core.getInstance().getStatsSaver().savedData.wingsStates[i])
            {
                Core.getInstance().getStatsSaver().savedData.wingsStates[i] = true;
                // Add it to the unlocked queue
                unlockedItems.put(ShopScreen.wingsOrder[i], "wing");
            }
        }

        // Unlock hoops
        for(int i = 0; i < ShopScreen.hoopsPrices.length; i++)
        {
            // Don't loop if we're below the price
            if(Core.getInstance().getStatsSaver().savedData.xp < ShopScreen.hoopsPrices[i])
                break;

            // Unlock it if its locked
            if(!Core.getInstance().getStatsSaver().savedData.hoopsStates[i])
            {
                Core.getInstance().getStatsSaver().savedData.hoopsStates[i] = true;
                // Add it to the unlocked queue
                unlockedItems.put(ShopScreen.hoopsOrder[i], "hoop");
            }
        }

        // Save
        Core.getInstance().getStatsSaver().save();

        // Do we have an unlocked item ?
        checkUnlocks();
        // Display the banner ad
        Core.getInstance().getOSUtility().showBannerAd();
}

    @Override
    public void deactivate()
    {
        // Hide the UI elements
        leaderboardsButton.toggle(false);
        achievementsButton.toggle(false);
        rateButton.toggle(false);
        challengesButton.toggle(false);
        shopButton.toggle(false);
        soundButton.toggle(false);
        vibrateButton.toggle(false);
        playGamesButton.toggle(false);
        nextButton.toggle(false);
        previousButton.toggle(false);
        scoreText.toggle(false);
        scoreNumberText.toggle(false);
        lastScoreText.toggle(false);
        newRecordText.toggle(false);
    }

    @Override
    public void update()
    {
        // Fade the screen
        if(!ScreenEffects.getInstance().isFaded())
            ScreenEffects.getInstance().fadeIn(Color.WHITE, FADE_ALPHA);

        // Show the unlock window if necessary
        checkUnlocks();
        // Don't process button clicks if the unlock window is showen
        if(!unlockWindow.isShowen() && !ratingDialog.isShowen() && !skipInput)
        {
            // Do we have a button click ?
            if (nextButton.isClicked() && currentScreenPosition < Core.getInstance().getGraphicsManager().WIDTH * (gameModeContainers.length - 1))
                scrollTo = currentScreenPosition + Core.getInstance().getGraphicsManager().WIDTH;
            else if (previousButton.isClicked() && currentScreenPosition > 0f)
                scrollTo = currentScreenPosition - Core.getInstance().getGraphicsManager().WIDTH;

            // Check button press
            if (soundButton.isClicked()) {
                // Toggle sound state
                Core.getInstance().getStatsSaver().savedData.soundState = !Core.getInstance().getStatsSaver().savedData.soundState;
                // Update texture
                TextureRegion texture = Core.getInstance().getResourcesManager().getResource(Core.getInstance().getStatsSaver().savedData.soundState
                        ? "UI/SoundOnButton" : "UI/SoundOffButton", ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

                soundButton.setTexture(texture);
                // Save
                Core.getInstance().getStatsSaver().save();
            } else if (vibrateButton.isClicked()) {
                // Toggle music state
                Core.getInstance().getStatsSaver().savedData.vibrationState = !Core.getInstance().getStatsSaver().savedData.vibrationState;
                // Update texture
                TextureRegion texture = Core.getInstance().getResourcesManager().getResource(Core.getInstance().getStatsSaver().savedData.vibrationState
                        ? "UI/VibrationOnButton" : "UI/VibrationOffButton", ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

                vibrateButton.setTexture(texture);
                // Save
                Core.getInstance().getStatsSaver().save();
            }
            else if (playGamesButton.isClicked())
            {
                // Sign in to Google Play
                if(!Core.getInstance().getOSUtility().isSignedIn() && Core.getInstance().getOSUtility().isNetworkConnected())
                    Core.getInstance().getOSUtility().signIn();

                // Set the GPG connected flag
                Core.getInstance().getStatsSaver().savedData.GPGConnected = true;
                Core.getInstance().getStatsSaver().save();
            }
            // If the leaderboards button is clicked then advance
            else if (leaderboardsButton.isClicked())
                // Show leaderboards
                Core.getInstance().getOSUtility().showLeaderboards();
                // If the achievements button is clicked then advance
            else if (achievementsButton.isClicked())
                // Show achievements
                Core.getInstance().getOSUtility().showAchievements();
            else if (rateButton.isClicked())
                // Show achievements
                Core.getInstance().getOSUtility().rateGame();
            else if (shopButton.isClicked())
                // Show achievements
                Core.getInstance().getScreensManager().setShopScreen();

            // If we're dragging then update the current screen position
            if(Core.getInstance().getInputHandler().getScreenTouchType() == ScreenTouchType.SCREEN_TOUCH_DRAGGED)
            {
                // Scroll to the right if possible
                if(Core.getInstance().getInputHandler().getTouchMovementPosition().x < 0f && currentScreenPosition < maximumScreenPosition + scrollPadding)
                {
                    // Scroll to the right
                    currentScreenPosition += -Core.getInstance().getInputHandler().getTouchMovementPosition().x * 1.75f;
                    // Don't allow it to exceed the limit
                    if(currentScreenPosition > maximumScreenPosition + scrollPadding)
                        currentScreenPosition = maximumScreenPosition + scrollPadding;
                }
                // Scroll to the right if possible
                else if(Core.getInstance().getInputHandler().getTouchMovementPosition().x > 0f && currentScreenPosition > -scrollPadding)
                {
                    // Scroll to the right
                    currentScreenPosition += -Core.getInstance().getInputHandler().getTouchMovementPosition().x * 1.75f;
                    // Don't allow it to exceed the limit
                    if(currentScreenPosition < -scrollPadding)
                        currentScreenPosition = -scrollPadding;
                }
                // Save the last scroll movement
                lastScrollMovement = Core.getInstance().getInputHandler().getTouchMovementPosition().x;
            }
            else
            {
                // Scroll to the next one if necessary
                if(Math.abs(lastScrollMovement) > Core.getInstance().getGraphicsManager().WIDTH / 50f)
                {
                    if(lastScrollMovement > 0f && currentScreenPosition > 0f)
                        scrollTo = currentScreenPosition - Core.getInstance().getGraphicsManager().WIDTH;
                    else if(lastScrollMovement < 0f && currentScreenPosition < Core.getInstance().getGraphicsManager().WIDTH *
                            (gameModeContainers.length - 1))
                        scrollTo = currentScreenPosition + Core.getInstance().getGraphicsManager().WIDTH;
                }
                else if(currentScreenPosition % Core.getInstance().getGraphicsManager().WIDTH != 0 && scrollTo == -1)
                    scrollTo = Core.getInstance().getGraphicsManager().WIDTH * (int)(currentScreenPosition /
                            (Core.getInstance().getGraphicsManager().WIDTH * 0.5f));

                // Fix position
                if(currentScreenPosition > maximumScreenPosition)
                    scrollTo = maximumScreenPosition;
                else if(currentScreenPosition < 0f)
                    scrollTo = 0f;

                // Reset the last scroll movement
                lastScrollMovement = 0f;
            }
            // Process automatic scrolling
            if(scrollTo != -1f)
            {
                // Get the difference between the current position and target
                float step = scrollTo - currentScreenPosition > 0f ? scrollStep : -scrollStep;
                // Update our current position
                currentScreenPosition += step;
                // Did we finish ?
                if(currentScreenPosition == scrollTo || (currentScreenPosition > scrollTo && step > 0f) || (currentScreenPosition < scrollTo && step < 0f))
                {
                    // Fix position
                    currentScreenPosition = scrollTo;
                    // Reset scrolling
                    scrollTo = -1f;
                }
            }
        }
        else
        {
            // Reset skip input
            skipInput = false;
            // Update unlock window
            if(unlockWindow.isShowen())
                skipInput = unlockWindow.process();

            // Update rating dialog
            if(ratingDialog.isShowen())
                skipInput = ratingDialog.process();
        }
        // Get the current game mode id
        int currentGameModeId = (int)currentScreenPosition / (Core.getInstance().getGraphicsManager().WIDTH / 2);
        if(currentGameModeId >= gameModeContainers.length)
            currentGameModeId = gameModeContainers.length - 1;
        else if(currentGameModeId < 0)
            currentGameModeId = 0;

        // Did we change game mode ?
        if(currentGameModeId != GameLogic.getInstance().getGameModeId())
            // Rotate the player
            Player.getInstance().rotate(currentGameModeId == 0f ? 0f : 270f);

        // Set the current game mode
        GameLogic.getInstance().setGameMode(GameMode.values()[currentGameModeId]);
        // Update terrain
        Terrain.getInstance().process();
        // Always skip player's input
        Player.getInstance().setSkipInput();
        // Update player
        Player.getInstance().process();
        // Update game mode containers
        for(GameModeContainer gameModeContainer : gameModeContainers)
        {
            if(gameModeContainer.update(currentScreenPosition) && !unlockWindow.isShowen()&& !ratingDialog.isShowen())
                Core.getInstance().getScreensManager().setGameScreen();
        }
    }

    @Override
    public void render()
    {
        // Draw the terrain
        Terrain.getInstance().render();
    }

    @Override
    public void postFadeRender()
    {
        // Render game mode containers
        for(GameModeContainer gameModeContainer : gameModeContainers)
            gameModeContainer.render();

        // Draw the player
        Player.getInstance().render();
    }

    @Override
    public void dispose() {

    }

    public void postUIRender()
    {
        // Draw unlock window
        if(unlockWindow.isShowen())
        {
            Core.getInstance().getGraphicsManager().setColor(1f, 1f, 1f, FADE_ALPHA);
            Core.getInstance().getGraphicsManager().drawTextureRegion(emptyTexture,
                    Core.getInstance().getGraphicsManager().EMPTY_VECTOR, Core.getInstance().getGraphicsManager().SCREEN_VECTOR, 0f);

            Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
            unlockWindow.render();
        }

        // Draw rating dialog
        if(ratingDialog.isShowen())
        {
            Core.getInstance().getGraphicsManager().setColor(1f, 1f, 1f, FADE_ALPHA);
            Core.getInstance().getGraphicsManager().drawTextureRegion(emptyTexture,
                    Core.getInstance().getGraphicsManager().EMPTY_VECTOR, Core.getInstance().getGraphicsManager().SCREEN_VECTOR, 0f);

            Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
            ratingDialog.render();
        }
    }

    private void checkUnlocks()
    {
        // Do we have an unlocked item ?
        if(!unlockWindow.isShowen() && !unlockedItems.isEmpty())
        {
            // Get capitalized type
            String name = (String)unlockedItems.keySet().toArray()[0];
            String type = unlockedItems.get(name);
            String cType = type.substring(0, 1).toUpperCase() + type.substring(1) + "s";
            // Show the window
            TextureRegion textureRegion = Core.getInstance().getResourcesManager().getResource(cType + "/" + name,
                    ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

            unlockWindow.show(textureRegion, type.equals("hoop") ? new Vector2(0.3f, 0.8f) :
                    new Vector2(0.5f,  0.5f), name, type);

            // Remove the entry
            unlockedItems.remove(name);
        }
    }

    public void showRatingDialog()
    {
        ratingDialog.show();
    }
}
