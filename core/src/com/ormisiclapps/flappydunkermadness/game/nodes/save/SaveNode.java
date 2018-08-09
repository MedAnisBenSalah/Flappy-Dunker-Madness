package com.ormisiclapps.flappydunkermadness.game.nodes.save;

import com.ormisiclapps.flappydunkermadness.game.screens.ShopScreen;

import java.util.Arrays;

/**
 * Created by Anis on 2/13/2017.
 */

public class SaveNode
{
    public long[] bestScores;
    public long[] lastScores;
    public long gamesPlayed;
    public long obstaclesPassed;
    public long swishes;
    public long bestSwishesStreak;
    public long eatablesCollected;
    public boolean soundState;
    public boolean vibrationState;
    public boolean neverRate;
    public long xp;
    public String ball, wing, hoop;
    public boolean[] ballsStates, wingsStates, hoopsStates;
    public boolean GPGConnected;

    public static final int GAMEMODES_COUNT = 2;

    public SaveNode()
    {
        bestScores = new long[GAMEMODES_COUNT];
        lastScores = new long[GAMEMODES_COUNT];
        gamesPlayed = 0;
        swishes = 0;
        bestSwishesStreak = 0;
        obstaclesPassed = 0;
        eatablesCollected = 0;
        soundState = true;
        vibrationState = true;
        xp = 0;
        ball = "Classic";
        wing = "Classic";
        hoop = "Classic";
        neverRate = false;
        ballsStates = new boolean[ShopScreen.ballsOrder.length];
        ballsStates[0] = true;
        for(int i = 1; i < ShopScreen.ballsOrder.length; i++)
            ballsStates[i] = false;

        wingsStates = new boolean[ShopScreen.wingsOrder.length];
        wingsStates[0] = true;
        for(int i = 1; i < ShopScreen.wingsOrder.length; i++)
            wingsStates[i] = false;

        hoopsStates = new boolean[ShopScreen.hoopsOrder.length];
        hoopsStates[0] = true;
        for(int i = 1; i < ShopScreen.hoopsOrder.length; i++)
            hoopsStates[i] = false;

        GPGConnected = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaveNode)) return false;

        SaveNode saveNode = (SaveNode) o;

        if (gamesPlayed != saveNode.gamesPlayed) return false;
        if (obstaclesPassed != saveNode.obstaclesPassed) return false;
        if (swishes != saveNode.swishes) return false;
        if (bestSwishesStreak != saveNode.bestSwishesStreak) return false;
        if (eatablesCollected != saveNode.eatablesCollected) return false;
        if (soundState != saveNode.soundState) return false;
        if (vibrationState != saveNode.vibrationState) return false;
        if (neverRate != saveNode.neverRate) return false;
        if (xp != saveNode.xp) return false;
        if (GPGConnected != saveNode.GPGConnected) return false;
        if (!Arrays.equals(bestScores, saveNode.bestScores)) return false;
        if (!Arrays.equals(lastScores, saveNode.lastScores)) return false;
        if (!ball.equals(saveNode.ball)) return false;
        if (!wing.equals(saveNode.wing)) return false;
        if (!hoop.equals(saveNode.hoop)) return false;
        if (!Arrays.equals(ballsStates, saveNode.ballsStates)) return false;
        if (!Arrays.equals(wingsStates, saveNode.wingsStates)) return false;
        return Arrays.equals(hoopsStates, saveNode.hoopsStates);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(bestScores);
        result = 31 * result + Arrays.hashCode(lastScores);
        result = 31 * result + (int) (gamesPlayed ^ (gamesPlayed >>> 32));
        result = 31 * result + (int) (obstaclesPassed ^ (obstaclesPassed >>> 32));
        result = 31 * result + (int) (swishes ^ (swishes >>> 32));
        result = 31 * result + (int) (bestSwishesStreak ^ (bestSwishesStreak >>> 32));
        result = 31 * result + (int) (eatablesCollected ^ (eatablesCollected >>> 32));
        result = 31 * result + (soundState ? 1 : 0);
        result = 31 * result + (vibrationState ? 1 : 0);
        result = 31 * result + (neverRate ? 1 : 0);
        result = 31 * result + (int) (xp ^ (xp >>> 32));
        result = 31 * result + ball.hashCode();
        result = 31 * result + wing.hashCode();
        result = 31 * result + hoop.hashCode();
        result = 31 * result + Arrays.hashCode(ballsStates);
        result = 31 * result + Arrays.hashCode(wingsStates);
        result = 31 * result + Arrays.hashCode(hoopsStates);
        result = 31 * result + (GPGConnected ? 1 : 0);
        return result;
    }
}
