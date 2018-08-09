package com.ormisiclapps.flappydunkermadness.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.GameMode;
import com.ormisiclapps.flappydunkermadness.game.entities.Camera;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.Player;
import com.ormisiclapps.flappydunkermadness.game.nodes.save.SaveNode;
import com.ormisiclapps.flappydunkermadness.game.screens.GameScreen;
import com.ormisiclapps.flappydunkermadness.game.screens.ShopScreen;
import com.ormisiclapps.flappydunkermadness.game.world.GameWorld;
import com.ormisiclapps.flappydunkermadness.game.world.Terrain;
import com.ormisiclapps.flappydunkermadness.input.InputHandler;
import com.ormisiclapps.flappydunkermadness.os.OSUtility;
import com.ormisiclapps.flappydunkermadness.utility.Configuration;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;

/**
 * Created by OrMisicL on 6/1/2016.
 */
public class GameLogic
{
    private GameIntelligence gameIntelligence;
    private GameWorld gameWorld;
    private Player player;
    private Terrain terrain;
    private boolean initialized;
    private boolean gameStarted;
    private boolean gameOver;
    private long score;
    private float worldHeight;
    private int swishes, swishesStreak;
    private GameMode gameMode;
    private boolean secondChance, hadSecondChance;
    private boolean newRecord;

    private static GameLogic instance;

    public GameLogic()
    {
        // Reset values
        score = 0;
        worldHeight = 0f;
        swishes = 1;
        // Reset flags
        initialized = false;
        gameStarted = false;
        gameOver = false;
        secondChance = false;
        hadSecondChance = false;
        newRecord = false;
        // Reset game mode
        gameMode = GameMode.GAME_MODE_FLAPPY;
        // Set instance
        instance = this;
    }

    public void setup()
    {
        // Calculate the world's height
        worldHeight = GameMath.metersPerPixels(Core.getInstance().getGraphicsManager().HEIGHT);
        // Create instances
        gameIntelligence = new GameIntelligence();
        gameWorld = new GameWorld();
        terrain = new Terrain();
        player = new Player();
        // Create the player
        player.create();
        // Initialize the terrain
        terrain.initialize();
        // Initialize game intelligence
        gameIntelligence.initialize();
        // Create the saved data if it doesn't exist
        if(Core.getInstance().getStatsSaver().savedData == null)
            Core.getInstance().getStatsSaver().savedData = new SaveNode();
    }

    public void initialize()
    {
        // If we're already initialized then don't initialize
        if(initialized)
            return;

        // Setup the player
        player.setup();
        // Initialize game intelligence
        gameIntelligence.initialize();
        // Setup environment
        if(isHorizontal())
        {
            float width = Configuration.WORLD_WIDTH;
            // Reset player's position
            player.setPosition(new Vector2(width / 4f, worldHeight / 2f));
            // Reset camera's position
            Camera.getInstance().setPosition(new Vector2(width / 2f, worldHeight / 2f));
        }
        else
        {
            // Reset player's position
            player.setPosition(new Vector2(Configuration.WORLD_WIDTH / 2f, -2f));
            // Reset camera's position
            Camera.getInstance().setPosition(new Vector2(Configuration.WORLD_WIDTH / 2f, -GameLogic.getInstance().getWorldHeight() / 2f));
        }
        // Setup the terrain
        terrain.setup();
        // Reset score
        score = 0;
        swishes = 1;
        // Reset flags
        gameStarted = false;
        gameOver = false;
        secondChance = false;
        hadSecondChance = false;
        newRecord = false;
        // Mark as initialized
        initialized = true;
    }

    public void process()
    {
        // Ensure we're initialized
        if(!initialized)
            return;

        // Did we pass the objects ?
        if(gameIntelligence.isObjectPassed())
        {
            // Is it a swish ?
            if(gameIntelligence.isSwished())
            {
                //if(swishes < 5)
                swishes++;
                // Increase swishes count
                Core.getInstance().getStatsSaver().savedData.swishes++;
                // Increase swishes streak
                swishesStreak++;
                // Unlock swishes streak achievements
                if(swishesStreak >= 5)
                    Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_SWISHES_5);
                if(swishesStreak >= 10)
                    Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_SWISHES_10);
                if(swishesStreak >= 20)
                    Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_SWISHES_20);

                // Show text
                GameScreen.getInstance().onSwish(swishes);
                // Vibrate
                if(Core.getInstance().getStatsSaver().savedData.vibrationState)
                    Gdx.input.vibrate(500);
            }
            else
                swishes = 1;

            // Play hoop passed sound
            GameScreen.getInstance().playHoopPassSound();
            // Notify the terrain
            Terrain.getInstance().onObjectPassed();
            // Add score
            score += swishes;
            // Check the best score
            if(score > Core.getInstance().getStatsSaver().savedData.bestScores[getGameModeId()])
            {
                // Set the new best score
                Core.getInstance().getStatsSaver().savedData.bestScores[getGameModeId()] = score;
                // Set as new record
                newRecord = true;
            }
            // Move to next object
            gameIntelligence.detectNextObject();
        }
        // Did we pass die ?
        if(gameIntelligence.isPlayerDead() && !gameOver)
            // On lost game
            onLostGame();

        // Process the game intelligence
        gameIntelligence.process();
        // Process the terrain
        terrain.process();
        // Start the game
        if(!gameStarted && InputHandler.getInstance().isTapped())
            startGame();

        // Process the player
        player.process();
    }

    private void startGame()
    {
        // Set the started flag
        gameStarted = true;
        // Turn the gravity on
        gameWorld.toggleGravity(true);
        // Skip this frame's player input
        if(!isHorizontal())
        {
            player.setSkipInput();
            // Fall
            player.setVelocity(new Vector2(0f, -1f));
        }
        // Start game on game screen
        GameScreen.getInstance().onStartGame();
    }

    public void destroy()
    {
        // Destroy all game instances
        player.destroy();
        terrain.destroy();
        // One more process before destroying
        gameWorld.postProcess();
        // Destroy the game world
        gameWorld.destroy();
    }

    public void onLostGame()
    {
        // Set game started flag
        gameStarted = true;
        // Save the last score
        Core.getInstance().getStatsSaver().savedData.lastScores[getGameModeId()] = score;
        // Set the best swishes streak
        if(swishesStreak > Core.getInstance().getStatsSaver().savedData.bestSwishesStreak)
            Core.getInstance().getStatsSaver().savedData.bestSwishesStreak = swishesStreak;

        // Notify the game lost on the game screen
        secondChance = GameScreen.getInstance().onLostGame();
        // Set the game over flag
        gameOver = true;
    }

    public void onLosingFinished()
    {
        // Reset the terrain
        terrain.reset();
        // Reset player
        player.reset();
        // Turn the gravity off
        gameWorld.toggleGravity(false);
        // Reset initialized flag
        initialized = false;
        // Increase XP
        Core.getInstance().getStatsSaver().savedData.xp += score;
        // Save
        Core.getInstance().getStatsSaver().save();
        // Reset score
        score = 0;
        // Unlock first game achievement
        Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FIRST_GAME);
        // Unlock flappy mode best score achievements
        if(gameMode == GameMode.GAME_MODE_FLAPPY)
        {
            long bestScore = Core.getInstance().getStatsSaver().savedData.bestScores[0];
            if(bestScore >= 10)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FLAPPY_MODE_10_SCORE);
            if(bestScore >= 30)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FLAPPY_MODE_30_SCORE);
            if(bestScore >= 50)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FLAPPY_MODE_50_SCORE);
            if(bestScore >= 100)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FLAPPY_MODE_100_SCORE);
            if(bestScore >= 150)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FLAPPY_MODE_150_SCORE);
            if(bestScore >= 300)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FLAPPY_MODE_300_SCORE);
            if(bestScore >= 500)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FLAPPY_MODE_500_SCORE);
        }
        else if(gameMode == GameMode.GAME_MODE_FALLING)
        {
            long bestScore = Core.getInstance().getStatsSaver().savedData.bestScores[1];
            if(bestScore >= 10)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FALLING_MODE_10_SCORE);
            if(bestScore >= 30)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FALLING_MODE_30_SCORE);
            if(bestScore >= 50)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FALLING_MODE_50_SCORE);
            if(bestScore >= 100)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FALLING_MODE_100_SCORE);
            if(bestScore >= 150)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FALLING_MODE_150_SCORE);
            if(bestScore >= 300)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FALLING_MODE_300_SCORE);
            if(bestScore >= 500)
                Core.getInstance().getOSUtility().unlockAchievement(OSUtility.Achievement.ACHIEVEMENT_FALLING_MODE_500_SCORE);
        }
        // Notify game losing on game screen
        GameScreen.getInstance().onLosingFinished();
        // Set the main menu screen
        Core.getInstance().getScreensManager().setMainMenuScreen();
    }

    public void resetForSecondChance()
    {
        // Restore the player
        player.setup();
        player.setPosition(Terrain.getInstance().getNextObject().getPosition());
        // Update camera once
        Camera.getInstance().update();
        // Pass the object
        terrain.destroyNextObject();
        gameIntelligence.detectNextObject();
        // Disable gravity
        gameWorld.toggleGravity(false);
        // Reset flags
        gameOver = false;
        gameStarted = false;
        secondChance = false;
    }

    public void setGameMode(GameMode gameMode) { this.gameMode = gameMode; }

    public long getScore() { return score; }
    public float getWorldHeight() { return worldHeight; }
    public boolean isGameStarted() { return gameStarted; }
    public boolean isGameOver() { return gameOver; }
    public GameMode getGameMode() { return gameMode; }
    public int getGameModeId() { return gameMode.ordinal(); }
    public boolean isHorizontal() { return gameMode == GameMode.GAME_MODE_FLAPPY|| gameMode == GameMode.GAME_MODE_FLAPPY_REVERSED; }
    public boolean isSecondChance() { return secondChance; }
    public void resetSecondChance() { secondChance = false; }

    public boolean isHadSecondChance() {
        return hadSecondChance;
    }

    public boolean isNewRecord() {
        return newRecord;
    }

    public void setHadSecondChance() {
        this.hadSecondChance = true;
    }

    public static GameLogic getInstance()
    {
        return instance;
    }

}
