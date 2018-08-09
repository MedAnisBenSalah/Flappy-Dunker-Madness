package com.ormisiclapps.flappydunkermadness.audio;

import com.badlogic.gdx.audio.Sound;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;

/**
 * Created by Anis on 1/22/2017.
 */

public class GameSound
{
    private Sound sound;

    public GameSound(String name)
    {
        // Get the sound
        sound = Core.getInstance().getResourcesManager().getResource(name, ResourceType.RESOURCE_TYPE_SOUND);
    }

    public void dispose()
    {
        // Disposing happens automatically within resources manager
        //sound.dispose();
    }

    public void play()
    {
        // Play the sound
        if(Core.getInstance().getStatsSaver().savedData.soundState)
            sound.play();
    }
}
