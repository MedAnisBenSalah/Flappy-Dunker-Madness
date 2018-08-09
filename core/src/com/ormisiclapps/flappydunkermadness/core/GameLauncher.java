package com.ormisiclapps.flappydunkermadness.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.ormisiclapps.flappydunkermadness.os.OSUtility;

public class GameLauncher extends ApplicationAdapter
{
	private Core core;
	private OSUtility googlePlayServices;

	public GameLauncher(OSUtility googlePlayServices)
	{
		// Set the google play services instance
		this.googlePlayServices = googlePlayServices;
	}

	@Override
	public void create ()
	{
		// Create the core instance
		core = new Core(googlePlayServices);
		// Initialize the game core
        core.initialize();
	}

	@Override
	public void render ()
    {
		// Update the core
        core.update();
		// Render the core
        core.render();
	}

	@Override
	public void dispose()
    {
		// Terminate the core
        core.terminate();
	}

	@Override
	public void pause()
	{
		core.pause();
	}

	@Override
	public void resume()
	{
		core.resume();
	}
}
