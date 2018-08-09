package com.ormisiclapps.flappydunkermadness.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.ormisiclapps.flappydunkermadness.audio.GameSound;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.game.core.GameLogic;
import com.ormisiclapps.flappydunkermadness.game.entities.Camera;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.Player;
import com.ormisiclapps.flappydunkermadness.game.world.GameWorld;
import com.ormisiclapps.flappydunkermadness.game.world.Terrain;
import com.ormisiclapps.flappydunkermadness.graphics.effects.ScreenEffects;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIButton;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIText;
import com.ormisiclapps.flappydunkermadness.input.InputHandler;
import com.ormisiclapps.flappydunkermadness.os.OSUtility;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIMain;

/**
 * Created by OrMisicL on 6/1/2016.
 */
public class GameScreen implements Screen
{
    private GameLogic gameLogic;
    private Camera camera;
    private UIText scoreText, pauseText, secondsText, swishText;
    private UIButton pauseButton, secondChanceButton;
    private TextureRegion gameOverTexture, secondChanceTimerTexture;
    private Vector2 tmpVector;
    private Vector2 pausedTextPosition;
    private boolean paused;
    private boolean resuming;
    private int seconds;
    private long secondTick;
    private boolean showSwishText;
    private float swishTextPosition, swishTextSpeed;
    private int swishMultiplier;
    private Color swishTextColor;
    private int gamesPlayedSinceStart;
    private boolean forceSkippableVideo;
    private long secondChanceTimer;
    private int initialTimerHeight;
    private boolean rewardedAdFinished;
    private Sprite timerSprite;
    private GameSound gameStartSound, gameOverSound, flapSound, bounceSound, maxSwishSound, swishSound, hoopPassSound;

    private static GameScreen instance;

    private static final float PAUSE_FADE = 0.5f;
    private static final int INTERSTITIAL_AD_ATTEMPTS = 2;
    private static final int SKIPPABLE_VIDEO_AD_ATTEMPTS = 3;
    private static final int RATING_ATTEMPTS = 10;
    private static final int SECOND_CHANCE_DURATION = 3000;

    public GameScreen()
    {
        // Reset instances
        gameLogic = null;
        camera = null;
        pauseButton = null;
        pausedTextPosition = null;
        secondsText = null;
        gameOverTexture = null;
        secondChanceButton = null;
        secondChanceTimerTexture = null;
        // Reset flags
        paused = false;
        resuming = false;
        rewardedAdFinished = false;
        // Reset values
        showSwishText = false;
        swishTextPosition = 0f;
        swishMultiplier = 0;
        secondChanceTimer = 0;
        // Create vectors
        tmpVector = new Vector2();
        // Create color
        swishTextColor = new Color(Color.WHITE);
        // Save the instance
        instance = this;
    }

    @Override
    public void initialize()
    {
        // Create the game logic instance
        camera = new Camera();
        gameLogic = new GameLogic();
        // Create the score text
        int size = Core.getInstance().getGraphicsManager().HEIGHT / 13;
        scoreText = new UIText(size, new Color(0f, 0f, 0f, 0.3f), 0f, null);
        // Create the pause text
        size = Core.getInstance().getGraphicsManager().HEIGHT / 20;
        pauseText = new UIText(size, Color.BLACK, 0, null);
        // Set it as not fade effected
        pauseText.setFadeEffected(false);
        // Create the seconds text
        size = Core.getInstance().getGraphicsManager().HEIGHT / 15;
        secondsText = new UIText(size, new Color(Color.WHITE), size / 10, new Color(Color.BLACK));
        // Create the swish text
        size = Core.getInstance().getGraphicsManager().HEIGHT / 20;
        swishText = new UIText(size, new Color(Color.WHITE), 0, null);

        // Create the pause button
        pauseButton = new UIButton((TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/PauseButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        // Set its parameters
        float pauseButtonSize = Core.getInstance().getGraphicsManager().HEIGHT / 24f;
        Vector2 buttonSize = new Vector2(pauseButtonSize, pauseButtonSize);
        pauseButton.setSize(buttonSize);
        pauseButton.setPosition(new Vector2(Core.getInstance().getGraphicsManager().WIDTH * 0.9f,
                Core.getInstance().getGraphicsManager().HEIGHT * 0.9f));

        // Create the second chance button
        secondChanceButton = new UIButton((TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/PlayButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        // Set its parameters
        float secondChanceButtonSize = Core.getInstance().getGraphicsManager().WIDTH / 2f;
        secondChanceButton.setSize(new Vector2(secondChanceButtonSize, secondChanceButtonSize));
        secondChanceButton.setPosition(new Vector2(Core.getInstance().getGraphicsManager().WIDTH / 2f - secondChanceButtonSize / 2f,
                Core.getInstance().getGraphicsManager().HEIGHT / 2f - secondChanceButtonSize / 2f));

        // Hide it
        secondChanceButton.toggle(false);
        // Add elements to the main UI
        UIMain.getInstance().addWidget(scoreText);
        UIMain.getInstance().addWidget(pauseText);
        UIMain.getInstance().addWidget(pauseButton);
        UIMain.getInstance().addWidget(secondChanceButton);
        UIMain.getInstance().addWidget(secondsText);
        UIMain.getInstance().addWidget(swishText);
        // Get the paused text size
        Vector2 pausedTextSize = pauseText.getSize("PAUSED");
        pausedTextPosition = new Vector2(Core.getInstance().getGraphicsManager().WIDTH / 2 - pausedTextSize.x / 2,
                Core.getInstance().getGraphicsManager().HEIGHT * 0.75f - pausedTextSize.y / 2);

        // Get textures
        gameOverTexture = Core.getInstance().getResourcesManager().getResource("UI/GameOver",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

        secondChanceTimerTexture = new TextureRegion((TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/PlayButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        // Create the timer sprite
        timerSprite = new Sprite(secondChanceTimerTexture);
        // Create sounds
        gameStartSound = new GameSound("GameStart");
        gameOverSound = new GameSound("GameOver");
        flapSound = new GameSound("Flap");
        bounceSound = new GameSound("Bounce");
        swishSound = new GameSound("Swish");
        hoopPassSound = new GameSound("HoopPassed");
        maxSwishSound = new GameSound("MaxSwish");

        // Setup the game logic
        gameLogic.setup();
        // Reset games counter
        gamesPlayedSinceStart = 0;
        // Reset flags
        forceSkippableVideo = false;
    }

    @Override
    public void activate()
    {
        // Hide UI elements
        scoreText.toggle(false);
        pauseButton.toggle(false);
        pauseText.toggle(false);
        secondsText.toggle(false);
        swishText.toggle(true);
        secondChanceButton.toggle(false);
        // Fade out
        ScreenEffects.getInstance().fadeOut();
        // Setup the game logic
        gameLogic.initialize();
        // Restore objects rendering
        Terrain.getInstance().setRenderObjects(true);
        // Resize the player
        Player.getInstance().resize(Core.getInstance().getModelManager().getModelNode("Player").size.x / 2f);
        // Restore its rotation
        Player.getInstance().setRotationInDegrees(0f);
    }

    @Override
    public void update()
    {
        // Check for the pause button click
        if(pauseButton.isClicked())
            pause();
        // Skip input
        else if(pauseButton.isClicking())
            Player.getInstance().setSkipInput();

        // Don't update if we're paused
        if(paused)
        {
            // Are we resuming ?
            if(resuming)
            {
                // Should we change the second tick
                if(TimeUtils.millis() - secondTick >= 1000)
                {
                    // Decrease the seconds
                    seconds--;
                    // Set text color
                    if(seconds == 2)
                        secondsText.setColor(new Color(0.6470f, 0.2235f, 0.0156f, 1f));
                    else
                        secondsText.setColor(new Color(1f, 0f, 0f, 1f));

                    // Reset the second tick
                    secondTick = TimeUtils.millis();
                }
                // Should we resume ?
                if(seconds <= 0)
                {
                    // Reset resuming flag
                    resuming = false;
                    // Reset paused flag
                    paused = false;
                    // Show UI elements
                    pauseButton.toggle(true);
                    scoreText.toggle(true);
                    secondsText.toggle(false);
                }
                else
                {
                    // Get the text size
                    Vector2 size = secondsText.getSize(Integer.toString(seconds));
                    // Draw the seconds
                    secondsText.drawText(Integer.toString(seconds), tmpVector.set(
                            Core.getInstance().getGraphicsManager().WIDTH / 2 - size.x / 2,
                            Core.getInstance().getGraphicsManager().HEIGHT / 2 + size.y / 2));
                }
            }
            else
            {
                // Draw the paused text
                pauseText.drawText("PAUSED", pausedTextPosition);
                // Check if we tapped the screen
                if(InputHandler.getInstance().isScreenTouched())
                    resume();
            }
            return;
        }
        // Process second chance
        else if(gameLogic.isSecondChance())
        {
            // Update the timer
            secondChanceTimer += Core.getInstance().getGraphicsManager().DELTA_TIME * 1000;
            // Did we finish ?
            if(secondChanceTimer >= SECOND_CHANCE_DURATION)
            {
                // Hide second chance UI
                secondChanceButton.toggle(false);
                pauseText.toggle(false);
                // Reset second chance flag
                gameLogic.resetSecondChance();
                // Restore texture height
                secondChanceTimerTexture.setRegionHeight(initialTimerHeight);
            }
            else
            {
                // Is button clicked ?
                if(secondChanceButton.isClicking() || secondChanceButton.isClicked())
                {
                    // Show video ad
                    Core.getInstance().getOSUtility().showVideoAd(new OSUtility.VideoListener() {
                        @Override
                        public void onFinished()
                        {
                            // Set the finish flag
                            rewardedAdFinished = true;
                        }

                        @Override
                        public void onClosed()
                        {
                            // Only if we didn't finish
                            if(!rewardedAdFinished)
                            {
                                // Enable gravity
                                GameWorld.getInstance().toggleGravity(true);
                                // Kill the player if he didn't finish watching the video
                                gameLogic.onLostGame();
                            }
                            // Reset flag
                            rewardedAdFinished = false;
                        }
                    });
                    // Hide second chance UI
                    secondChanceButton.toggle(false);
                    pauseText.toggle(false);
                    // Reset for second chance
                    gameLogic.resetForSecondChance();
                    // Restore texture height
                    secondChanceTimerTexture.setRegionHeight(initialTimerHeight);
                }

                // Draw second chance text
                Vector2 size = pauseText.getSize("Second");
                pauseText.drawText("Second", Core.getInstance().getGraphicsManager().WIDTH / 2f - size.x / 2f,
                        Core.getInstance().getGraphicsManager().HEIGHT / 2f + size.y * 1.5f);

                size = pauseText.getSize("Chance");
                pauseText.drawText("Chance", Core.getInstance().getGraphicsManager().WIDTH / 2f - size.x / 2f,
                        Core.getInstance().getGraphicsManager().HEIGHT / 2f - size.y * 0.5f);

                // Reduce the texture's region
                float factor = (float)(SECOND_CHANCE_DURATION - secondChanceTimer) / (float)SECOND_CHANCE_DURATION;
                timerSprite.setRegionHeight((int)(initialTimerHeight * factor));
                // Reduce the texture's size
                timerSprite.setSize(secondChanceButton.getSize().y, secondChanceButton.getSize().x * factor);
                tmpVector.set(secondChanceButton.getPosition());
                timerSprite.setPosition(timerSprite.getX(), tmpVector.y + secondChanceButton.getSize().x -
                        secondChanceButton.getSize().x * factor);
            }
        }
        else
        {
            // Get height and width
            float width = Core.getInstance().getGraphicsManager().WIDTH;
            float height = Core.getInstance().getGraphicsManager().HEIGHT;
            // Draw the score
            Vector2 textSize = scoreText.getSize("" + GameLogic.getInstance().getScore());
            scoreText.drawText("" + GameLogic.getInstance().getScore(), width / 2f - textSize.x / 2f, height / 2f - textSize.y / 2f);
            // Show swish text
            if(showSwishText)
            {
                // Did we finish fading ?
                if(swishTextColor.a <= 0.05f)
                {
                    swishTextColor.a = 0f;
                    // Reset flag
                    showSwishText = false;
                }
                else
                {
                    textSize = swishText.getSize("Swish!");
                    swishText.drawText("Swish!", width / 2f - textSize.x / 2f, swishTextPosition + textSize.y / 2f);
                    Vector2 multiplierSize = swishText.getSize("x" + swishMultiplier);
                    swishText.drawText("x" + swishMultiplier, width / 2f - multiplierSize.x / 2f,
                            swishTextPosition + textSize.y / 2f - multiplierSize.y * 1.1f);

                    // Did we finish moving the text .
                    if (swishTextSpeed < 1f) {
                        // Set color
                        swishTextColor.a -= 0.0175f;
                        // Change color
                        swishText.setColor(swishTextColor);
                    } else {
                        // Increase the position
                        swishTextPosition += swishTextSpeed;
                        // Decrease the speed
                        swishTextSpeed /= 1.4f;
                    }
                }
            }
        }
        // Update the physics world
        GameWorld.getInstance().process();
        // Post process the world
        GameWorld.getInstance().postProcess();
        // Update the camera
        camera.update();
        // Update the game logic
        gameLogic.process();
    }

    @Override
    public void render()
    {
        // Render the terrain
        Terrain.getInstance().render();
        // Render the player
        Player.getInstance().render();
        // Render the objects after the player
        Terrain.getInstance().drawObjectsAfterPlayer();
    }

    @Override
    public void postFadeRender()
    {
        // Draw the game over texture
        if(gameLogic.isGameOver() && !gameLogic.isSecondChance())
        {
            // Get height and width
            float w = Core.getInstance().getGraphicsManager().WIDTH;
            float h = Core.getInstance().getGraphicsManager().HEIGHT;
            // Set color
            Core.getInstance().getGraphicsManager().setColor(1f, 1f, 1f, 0.6f);
            // Draw texture
            float size = w / 9f;
            Core.getInstance().getGraphicsManager().drawTextureRegion(gameOverTexture, w / 2f - size / 2f, h / 2f - size / 2f, size, size, 270f);
            // Restore color
            Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
        }
        else if(gameLogic.isSecondChance())
            // Draw the timer
            Core.getInstance().getGraphicsManager().drawSprite(timerSprite);
    }

    @Override
    public void deactivate()
    {
        // Hide UI elements
        scoreText.toggle(false);
        pauseButton.toggle(false);
        pauseText.toggle(false);
        secondsText.toggle(false);
        swishText.toggle(false);
        secondChanceButton.toggle(false);
    }

    @Override
    public void dispose()
    {
        // Destroy the game logic
        gameLogic.destroy();
    }

    public boolean onLostGame()
    {
        // Hide UI
        scoreText.toggle(false);
        pauseButton.toggle(false);
        // Display the second chance
        if(!gameLogic.isHadSecondChance() && Core.getInstance().getOSUtility().isVideoAdLoaded())
        {
            // Reset the second chance timer
            secondChanceTimer = 0;
            // Set initial height
            initialTimerHeight = secondChanceButton.getTexture().getRegionHeight();
            // Setup sprite
            timerSprite.setPosition(secondChanceButton.getPosition().x, secondChanceButton.getPosition().y);
            timerSprite.setSize(secondChanceButton.getSize().x, secondChanceButton.getSize().y);
            timerSprite.setColor(0f, 0.7f, 0f, 0.7f);
            // Show the second chance UI
            secondChanceButton.toggle(true);
            pauseText.toggle(true);
            // Set the had second chance flag
            gameLogic.setHadSecondChance();
            return true;
        }
        // Play the game over sound
        gameOverSound.play();
        // Reset flags
        showSwishText = false;
        return false;
    }

    public void onStartGame()
    {
        // Activate UI elements
        scoreText.toggle(true);
        pauseButton.toggle(true);
        // Play the game start sound
        gameStartSound.play();
    }

    public void onSwish(int multiplier)
    {
        // Set the swish flag
        showSwishText = true;
        // Reset the position
        swishTextPosition = Core.getInstance().getGraphicsManager().HEIGHT / 3f * 2f;
        // Set the text speed
        swishTextSpeed = Core.getInstance().getGraphicsManager().HEIGHT / 50f;
        // Set multiplier
        swishMultiplier = multiplier;
        // Set color
        if(multiplier == 2)
            swishTextColor.set(0.0901f, 0.4313f, 0.0117f, 1f);
        else if(multiplier == 3)
            swishTextColor.set(0.4431f, 0.4313f, 0f, 1f);
        else if(multiplier == 4)
            swishTextColor.set(0.6313f, 0.2392f, 0.0078f, 1f);
        else
            swishTextColor.set(0.1843f, 0.2588f, 0.6823f, 1f);

        // Play the swish sound
        if(multiplier >= 5)
            maxSwishSound.play();
        else
            swishSound.play();

        // Change color
        swishText.setColor(swishTextColor);
    }

    public void pause()
    {
        // We only pause if the game has started and we're not already paused
        if(paused || !gameLogic.isGameStarted() || gameLogic.isGameOver())
            return;

        // Hide UI elements
        scoreText.toggle(false);
        pauseButton.toggle(false);
        // Set the paused flag
        paused = true;
        // Reset resuming flag
        resuming = false;
        // Show the pause text
        pauseText.toggle(true);
        // Fade in the screen
        Core.getInstance().getGraphicsManager().getScreenEffects().fadeIn(Color.WHITE, PAUSE_FADE);
    }

    public void resume()
    {
        // We only resume if the game has started and we're already paused
        if(!paused)
            return;

        // Activate UI elements
        scoreText.toggle(false);
        pauseButton.toggle(false);
        pauseText.toggle(false);
        secondsText.toggle(true);
        // Set the seconds
        seconds = 3;
        // Set the seconds tick
        secondTick = TimeUtils.millis();
        // Set the resuming flag
        resuming = true;
        // Reset text color
        secondsText.setColor(new Color(0.1960f, 0.8039f, 0.1960f, 1f));
        // Fade out the screen
        Core.getInstance().getGraphicsManager().getScreenEffects().fadeOut();
    }

    public void onLosingFinished()
    {
        // Increase the games played count
        gamesPlayedSinceStart++;
        // Always show the banner ad
        Core.getInstance().getOSUtility().showBannerAd();
        // Show skippable video
        if(forceSkippableVideo || gamesPlayedSinceStart % SKIPPABLE_VIDEO_AD_ATTEMPTS == 0)
        {
            // Is video loaded ?
            if(Core.getInstance().getOSUtility().isSkippableVideoAdLoaded())
            {
                // Show the skippable video
                Core.getInstance().getOSUtility().showSkippableVideoAd();
                // Reset the force flag
                forceSkippableVideo = false;
            }
            else
                // Set the force flag
                forceSkippableVideo = true;
        }

        // Do we need to show interstitial ad
        if(gamesPlayedSinceStart % INTERSTITIAL_AD_ATTEMPTS == 0)
            // Show interstitial ad
            Core.getInstance().getOSUtility().showInterstitialAd();

        // Do we need a rating ?
        if(!Core.getInstance().getStatsSaver().savedData.neverRate && gamesPlayedSinceStart == RATING_ATTEMPTS)
            // Show rating
            Core.getInstance().getScreensManager().getMainMenuScreen().showRatingDialog();

        // Submit highest score
        if(Core.getInstance().getOSUtility().isSignedIn())
            Core.getInstance().getOSUtility().submitScore(Core.getInstance().getStatsSaver().savedData.bestScores[gameLogic.getGameModeId()],
                    OSUtility.LeaderboardType.values()[gameLogic.getGameModeId()]);
    }

    public void playBounceSound()
    {
        bounceSound.play();
    }

    public void playHoopPassSound()
    {
        hoopPassSound.play();
    }

    public void playFlapSound()
    {
        flapSound.play();
    }



    public static GameScreen getInstance()
    {
        return instance;
    }
}
