package com.ormisiclapps.flappydunkermadness.os;

/**
 * Created by OrMisicL on 8/24/2017.
 */

/*
    This is used to handle OS specific features (scoreboard, achievements ...)
*/
public interface OSUtility
{
    enum Achievement
    {
        ACHIEVEMENT_FIRST_GAME,

        ACHIEVEMENT_FLAPPY_MODE_10_SCORE,
        ACHIEVEMENT_FLAPPY_MODE_30_SCORE,
        ACHIEVEMENT_FLAPPY_MODE_50_SCORE,
        ACHIEVEMENT_FLAPPY_MODE_100_SCORE,
        ACHIEVEMENT_FLAPPY_MODE_150_SCORE,
        ACHIEVEMENT_FLAPPY_MODE_300_SCORE,
        ACHIEVEMENT_FLAPPY_MODE_500_SCORE,

        ACHIEVEMENT_FALLING_MODE_10_SCORE,
        ACHIEVEMENT_FALLING_MODE_30_SCORE,
        ACHIEVEMENT_FALLING_MODE_50_SCORE,
        ACHIEVEMENT_FALLING_MODE_100_SCORE,
        ACHIEVEMENT_FALLING_MODE_150_SCORE,
        ACHIEVEMENT_FALLING_MODE_300_SCORE,
        ACHIEVEMENT_FALLING_MODE_500_SCORE,

        ACHIEVEMENT_SWISHES_5,
        ACHIEVEMENT_SWISHES_10,
        ACHIEVEMENT_SWISHES_20
    }

    enum LeaderboardType
    {
        LEADERBOARD_FLAPPY_MODE,
        LEADERBOARD_FALLING_MODE
    }

    interface VideoListener
    {
        void onFinished();
        void onClosed();
    }

    // Google Play services
    void signIn();
    void signOut();
    boolean isSignedIn();

    // Google play services features
    void rateGame();
    void submitScore(long highScore, LeaderboardType leaderboard);
    void showLeaderboards();
    void showAchievements();
    void unlockAchievement(Achievement achievement);

    // Logging
    void log(String eventName, String text);

    // Ad banner
    void showBannerAd();
    void hideBannerAd();

    // Ad Interstitial
    boolean showInterstitialAd();

    // Video ad
    void showSkippableVideoAd();
    void showVideoAd(VideoListener listener);
    boolean isVideoAdLoaded();
    boolean isSkippableVideoAdLoaded();

    // Utilities
    boolean isNetworkConnected();
}
