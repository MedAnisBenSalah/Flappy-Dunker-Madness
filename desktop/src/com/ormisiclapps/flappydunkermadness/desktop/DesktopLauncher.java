package com.ormisiclapps.flappydunkermadness.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ormisiclapps.flappydunkermadness.core.GameLauncher;
import com.ormisiclapps.flappydunkermadness.os.OSUtility;

public class DesktopLauncher implements OSUtility
{
	public static void main (String[] arg)
	{
        // Create the configuration instance
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// Its okay to have a higher samples value on desktop
		config.samples = 10;
		// Edit these values if necessary
		/*config.width = 1366;
		config.height = 768;
		config.fullscreen = true;*/

		config.width = 480;
		config.height = 854;
        // Start the application
		new LwjglApplication(new GameLauncher(new DesktopLauncher()), config);
	}

	@Override
	public void signIn() {

	}

	@Override
	public void signOut() {

	}

	@Override
	public boolean isSignedIn() {
		return false;
	}

	@Override
	public void rateGame() {

	}

	@Override
	public void submitScore(long highScore, LeaderboardType leaderboard) {

	}

	@Override
	public void showLeaderboards() {

	}

	@Override
	public void showAchievements() {

	}

	@Override
	public void unlockAchievement(Achievement achievement) {

	}

	@Override
	public void showBannerAd() {

	}

	@Override
	public void hideBannerAd() {

	}

	@Override
	public boolean showInterstitialAd() {
		return false;
	}

	@Override
	public void showSkippableVideoAd() {

	}

	@Override
	public void showVideoAd(VideoListener listener) {
		if(listener != null)
			listener.onFinished();
	}

	@Override
	public boolean isVideoAdLoaded() {
		return false;
	}

	@Override
	public boolean isSkippableVideoAdLoaded() {
		return false;
	}

	@Override
	public boolean isNetworkConnected() {
		return false;
	}

	@Override
	public void log(String eventName, String text) {

	}
}
