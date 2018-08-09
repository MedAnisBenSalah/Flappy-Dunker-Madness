package com.ormisiclapps.flappydunkermadness;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.view.View;
import android.widget.RelativeLayout;

import com.appodeal.gdx.GdxAppodeal;
import com.appodeal.gdx.callbacks.RewardedVideoCallback;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ormisiclapps.flappydunkermadness.core.GameLauncher;
import com.ormisiclapps.flappydunkermadness.os.OSUtility;

public class AndroidLauncher extends AndroidApplication implements OSUtility
{
	// BaseGameUtils
	private GameHelper gameHelper;
	private final static int requestCode = 1;

    private GameHelper.GameHelperListener gameHelperListener;

    // Main layout that's used to contain all views (game and ads)
    private RelativeLayout mainLayout;

    // Views
    private View gameView;

    // Listeners
    private VideoListener videoListener;

    // Firebase
    private FirebaseAnalytics firebaseAnalytics;

	@Override
	protected void onCreate (Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
        // Create the game helper instance
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        // Disable debug log
		gameHelper.enableDebugLog(false);
        // Set the game helper listener
		gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInFailed()
            {

            }

			@Override
			public void onSignInSucceeded()
            {

            }
		};
		// Disable connect on start
        gameHelper.setConnectOnStart(false);
        // Setup game helper
		gameHelper.setup(gameHelperListener);

        // Reset listeners
        videoListener = null;
        // Initialize FireBase analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        // Create the main layout
        mainLayout = new RelativeLayout(this);
        // Create a layout params instance
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        // Set the main layout's params
        mainLayout.setLayoutParams(params);
        // Setup views
        createGameView();
        // Add views to the main layout
        mainLayout.addView(gameView);
        // Set it to use this view
        setContentView(mainLayout);

        // Initialize the Apoodeal SDK
        GdxAppodeal.confirm(GdxAppodeal.SKIPPABLE_VIDEO);
        GdxAppodeal.initialize(getString(R.string.appodeal_apikey), GdxAppodeal.INTERSTITIAL | GdxAppodeal.SKIPPABLE_VIDEO | GdxAppodeal.BANNER |
                GdxAppodeal.REWARDED_VIDEO);

        // Auto-cache video ads
        GdxAppodeal.setAutoCache(GdxAppodeal.REWARDED_VIDEO, true);
        GdxAppodeal.setAutoCache(GdxAppodeal.SKIPPABLE_VIDEO, true);
        // Set rewarded video callback
        GdxAppodeal.setRewardedVideoCallbacks(new RewardedVideoCallback() {
            @Override
            public void onRewardedVideoLoaded() {

            }

            @Override
            public void onRewardedVideoFailedToLoad() {

            }

            @Override
            public void onRewardedVideoShown() {

            }

            @Override
            public void onRewardedVideoFinished(int amount, String name) {
                // Invoke the finished method
                if(videoListener != null)
                    videoListener.onFinished();
            }

            @Override
            public void onRewardedVideoClosed() {
                // Invoke the closed method
                if(videoListener != null)
                    videoListener.onClosed();
            }
        });
	}

    @Override
    protected void attachBaseContext(Context context)
    {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

	/*
	    Create and setup the libgdx's view
	*/
	private void createGameView()
    {
        // Create the application config instance
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // 2 is a good value for average devices (Anti-aliasing)
        config.numSamples = 2;
        // Create the game's view
        gameView = initializeForView(new GameLauncher(this), config);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        gameHelper.onStart(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        gameHelper.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        gameHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void signIn()
    {
        try
        {
            // Setup game helper
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        }
        catch (Exception e)
        {
            //Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void signOut()
    {
        try
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    gameHelper.signOut();
                }
            });
        }
        catch (Exception e)
        {
            //Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void rateGame()
    {
        // Open the play store page
        String str = "market://details?id=com.ormisiclapps.flappydunkermadness";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }

    @Override
    public void submitScore(long highScore, LeaderboardType leaderboard)
    {
        // Only submit score if we're signed in
        if(isSignedIn())
        {
            // Get the appropriate leaderboard key
            String leaderboardKey = "";
            switch (leaderboard)
            {
                case LEADERBOARD_FLAPPY_MODE:
                    leaderboardKey = getString(R.string.leaderboard_flappy_mode);
                    break;

                case LEADERBOARD_FALLING_MODE:
                    leaderboardKey = getString(R.string.leaderboard_falling_mode);
                    break;
            }
            // Send score
            Games.Leaderboards.submitScore(gameHelper.getApiClient(), leaderboardKey, highScore);
        }
    }

    @Override
    public void showLeaderboards()
    {
        // Make sure we're signed in already
        if(isSignedIn())
            // Set the leaderboards view
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(gameHelper.getApiClient()), requestCode);
        else
            // NOTE: We force sign in since a player who wants to view the scoreboard must approve of Google Play services
            signIn();
    }

    @Override
    public void showAchievements()
    {
        // Make sure we're signed in already
        if(isSignedIn())
            // Set the leaderboards view
            startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), requestCode);
        else
            // NOTE: We force sign in since a player who wants to view the achievements must approve of Google Play services
            signIn();
    }

    @Override
    public void unlockAchievement(Achievement achievement)
    {
        // Check sign in state
        if(!isSignedIn())
            return;

        String achievementKey = "";
        switch (achievement)
        {
            case ACHIEVEMENT_FIRST_GAME:
                achievementKey = getString(R.string.achievement_just_starting);
                break;

            case ACHIEVEMENT_FLAPPY_MODE_10_SCORE:
                achievementKey = getString(R.string.achievement_basic_flapper);
                break;

            case ACHIEVEMENT_FLAPPY_MODE_30_SCORE:
                achievementKey = getString(R.string.achievement_try_hard_flapper);
                break;

            case ACHIEVEMENT_FLAPPY_MODE_50_SCORE:
                achievementKey = getString(R.string.achievement_flappy_flapper);
                break;

            case ACHIEVEMENT_FLAPPY_MODE_100_SCORE:
                achievementKey = getString(R.string.achievement_professional_flapper);
                break;

            case ACHIEVEMENT_FLAPPY_MODE_150_SCORE:
                achievementKey = getString(R.string.achievement_master_flapper);
                break;

            case ACHIEVEMENT_FLAPPY_MODE_300_SCORE:
                achievementKey = getString(R.string.achievement_elite_flapper);
                break;

            case ACHIEVEMENT_FLAPPY_MODE_500_SCORE:
                achievementKey = getString(R.string.achievement_flappy_god);
                break;

            case ACHIEVEMENT_FALLING_MODE_10_SCORE:
                achievementKey = getString(R.string.achievement_basic_faller);
                break;

            case ACHIEVEMENT_FALLING_MODE_30_SCORE:
                achievementKey = getString(R.string.achievement_try_hard_faller);
                break;

            case ACHIEVEMENT_FALLING_MODE_50_SCORE:
                achievementKey = getString(R.string.achievement_falling_faller);
                break;

            case ACHIEVEMENT_FALLING_MODE_100_SCORE:
                achievementKey = getString(R.string.achievement_professional_faller);
                break;

            case ACHIEVEMENT_FALLING_MODE_150_SCORE:
                achievementKey = getString(R.string.achievement_master_faller);
                break;

            case ACHIEVEMENT_FALLING_MODE_300_SCORE:
                achievementKey = getString(R.string.achievement_elite_faller);
                break;

            case ACHIEVEMENT_FALLING_MODE_500_SCORE:
                achievementKey = getString(R.string.achievement_falling_god);
                break;

            case ACHIEVEMENT_SWISHES_5:
                achievementKey = getString(R.string.achievement_swished);
                break;

            case ACHIEVEMENT_SWISHES_10:
                achievementKey = getString(R.string.achievement_swishing_freak);
                break;

            case ACHIEVEMENT_SWISHES_20:
                achievementKey = getString(R.string.achievement_the_swisher);
                break;
        }
        // Unlock achievement
        Games.Achievements.unlock(gameHelper.getApiClient(), achievementKey);
    }

    @Override
    public boolean isSignedIn()
    {
        return isNetworkConnected() && gameHelper.isSignedIn();
    }

    @Override
    public void showBannerAd()
    {
        GdxAppodeal.show(GdxAppodeal.BANNER);
    }

    @Override
    public void hideBannerAd()
    {
        GdxAppodeal.hide(GdxAppodeal.BANNER);
    }

    @Override
    public boolean showInterstitialAd()
    {
        boolean success = GdxAppodeal.isLoaded(GdxAppodeal.INTERSTITIAL);
        if(success)
            GdxAppodeal.show(GdxAppodeal.INTERSTITIAL);

        return success;
    }

    @Override
    public void showSkippableVideoAd()
    {
        if(GdxAppodeal.isLoaded(GdxAppodeal.SKIPPABLE_VIDEO))
            GdxAppodeal.show(GdxAppodeal.SKIPPABLE_VIDEO);
    }

    @Override
    public void showVideoAd(VideoListener listener)
    {
        // Set the listener
        videoListener = listener;
        // Show rewarded video
        if(GdxAppodeal.isLoaded(GdxAppodeal.REWARDED_VIDEO))
            GdxAppodeal.show(GdxAppodeal.REWARDED_VIDEO);
    }

    @Override
    public boolean isVideoAdLoaded()
    {
        return GdxAppodeal.isLoaded(GdxAppodeal.REWARDED_VIDEO);
    }

    @Override
    public boolean isSkippableVideoAdLoaded()
    {
        return GdxAppodeal.isLoaded(GdxAppodeal.SKIPPABLE_VIDEO);
    }

    @Override
    public boolean isNetworkConnected()
    {
        // Check the device's connectivity
        return ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    @Override
    public void log(String eventName, String text)
    {
        Bundle params = new Bundle();
        params.putString("description", text);
        firebaseAnalytics.logEvent(eventName, params);
    }
}
