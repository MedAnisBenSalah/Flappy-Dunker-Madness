package com.ormisiclapps.flappydunkermadness.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;

/**
 * Created by OrMisicL on 5/29/2016.
 * Will automatically handle loading and disposing of game resources (assets) based on the game state.
 * Will also dispose of all of the remaining resources when leaving.
 */
public class ResourcesManager
{
    private AssetManager assetManager;

    public ResourcesManager()
    {
        // Create the asset manager instance
        assetManager = new AssetManager();
    }

    public void update()
    {
        // Update the assets manager
        if(isLoading())
            assetManager.update();
    }

    /* This will load the initial resources needed by the game in order to run */
    public void loadInitialResources()
    {
        // Load the developer logo background
        requestResource("DeveloperLogo", ResourceType.RESOURCE_TYPE_TEXTURE);
        // Load empty texture
        requestResource("Empty", ResourceType.RESOURCE_TYPE_TEXTURE);
        // Load resources now
        assetManager.finishLoading();
    }

    public void loadResources()
    {
        // Load texture atlas
        requestResource("UI", ResourceType.RESOURCE_TYPE_TEXTURE_REGION);
        requestResource("Models", ResourceType.RESOURCE_TYPE_MODEL);
        // Game resources
        loadGameResources();
    }

    private void loadGameResources()
    {
        // Load the model settings
        Core.getInstance().getModelSettings().load();
        // Load textures
        requestResource("Balls", ResourceType.RESOURCE_TYPE_TEXTURE_REGION);
        requestResource("Wings", ResourceType.RESOURCE_TYPE_TEXTURE_REGION);
        requestResource("Hoops", ResourceType.RESOURCE_TYPE_TEXTURE_REGION);
        requestResource("HoopsFronts", ResourceType.RESOURCE_TYPE_TEXTURE_REGION);
        // Load all the models
        Core.getInstance().getModelManager().loadModel("Player");
        Core.getInstance().getModelManager().loadModel("Hoop");
        // Load sounds
        requestResource("ButtonClick", ResourceType.RESOURCE_TYPE_SOUND);
        requestResource("GameStart", ResourceType.RESOURCE_TYPE_SOUND);
        requestResource("GameOver", ResourceType.RESOURCE_TYPE_SOUND);
        requestResource("Flap", ResourceType.RESOURCE_TYPE_SOUND);
        requestResource("Bounce", ResourceType.RESOURCE_TYPE_SOUND);
        requestResource("HoopPassed", ResourceType.RESOURCE_TYPE_SOUND);
        requestResource("Swish", ResourceType.RESOURCE_TYPE_SOUND);
        requestResource("MaxSwish", ResourceType.RESOURCE_TYPE_SOUND);
    }

    public void dispose()
    {
        // Get all the loaded resources
        Array<Disposable> resources = new Array<Disposable>();
        assetManager.getAll(Disposable.class, resources);
        // Loop through all the loaded resources
        for(Disposable resource : resources)
            // Dispose of the resource
            resource.dispose();

        // Dispose of the asset manager
        assetManager.dispose();
    }

    public <T> T getResource(String name, ResourceType type)
    {
        try
        {
            // Return the resource depending on its type
            switch (type)
            {
                // Texture resource
                case RESOURCE_TYPE_TEXTURE:
                    return assetManager.get("Textures/" + name + ".png");

                // Texture region resource
                case RESOURCE_TYPE_TEXTURE_REGION:
                    // Get the texture's atlas name
                    String atlasName = name.substring(0, name.indexOf("/"));
                    // Find the texture atlas
                    TextureAtlas atlas = assetManager.get("Textures/" + atlasName + ".pack");
                    // Get the specified region
                    return (T) atlas.findRegion(name.substring(name.indexOf("/") + 1));

                // Texture atlas resource
                case RESOURCE_TYPE_TEXTURE_ATLAS:
                    // Get the specified region
                    return (T) assetManager.get("Textures/" + name + ".pack");

                // Model resource
                case RESOURCE_TYPE_MODEL:
                    // Find the texture atlas
                    atlas = assetManager.get("Models/Models.pack");
                    // Get the model's region
                    return (T) atlas.findRegion(name);

                // Sound resource
                case RESOURCE_TYPE_SOUND:
                    return assetManager.get("Audio/" + name + ".ogg");

                // Music resource
                case RESOURCE_TYPE_MUSIC:
                    return assetManager.get("Audio/" + name + ".ogg");
            }
        }
        catch (Exception e)
        {
            Core.getInstance().getOSUtility().log("invalid_resource",
                    "getResource: " + name + " of type " + getResourceNameFromType(type));
        }
        return null;
    }

    public void requestResource(String name, ResourceType type)
    {
        try
        {
            // Add the resource to the loading queue
            switch (type)
            {
                case RESOURCE_TYPE_MODEL:
                    assetManager.load("Models/Models.pack", TextureAtlas.class);
                    break;

                case RESOURCE_TYPE_TEXTURE:
                    assetManager.load("Textures/" + name + ".png", Texture.class);
                    break;

                case RESOURCE_TYPE_TEXTURE_REGION:
                    assetManager.load("Textures/" + name + ".pack", TextureAtlas.class);
                    break;

                case RESOURCE_TYPE_SOUND:
                    assetManager.load("Audio/" + name + ".ogg", Sound.class);
                    break;

                case RESOURCE_TYPE_MUSIC:
                    assetManager.load("Audio/" + name + ".ogg", Music.class);
                    break;

            }
        }
        catch (Exception e)
        {
            Core.getInstance().getOSUtility().log("invalid_resource",
                    "requestResource: " + name + " of type " + getResourceNameFromType(type));
        }
    }

    public boolean isResourceLoaded(String name, ResourceType type)
    {
        // Add the resource to the loading queue
        switch(type)
        {
            case RESOURCE_TYPE_MODEL:
                return getResource(name, type) != null;

            case RESOURCE_TYPE_TEXTURE:
                return assetManager.isLoaded("Textures/" + name + ".png", Texture.class);

            case RESOURCE_TYPE_TEXTURE_REGION:
                return assetManager.isLoaded("Textures/" + name + ".pack", TextureAtlas.class);

            case RESOURCE_TYPE_SOUND:
                return assetManager.isLoaded("Audio/" + name + ".ogg", Sound.class);

            case RESOURCE_TYPE_MUSIC:
                return assetManager.isLoaded("Audio/" + name + ".ogg", Music.class);
        }
        return false;
    }

    private String getResourceNameFromType(ResourceType type)
    {
        switch (type)
        {
            case RESOURCE_TYPE_MODEL:
                return "Model";

            case RESOURCE_TYPE_TEXTURE:
                return "Texture";

            case RESOURCE_TYPE_TEXTURE_REGION:
                return "TextureRegion";

            case RESOURCE_TYPE_SOUND:
                return "Sound";

            case RESOURCE_TYPE_MUSIC:
                return "Music";

            case RESOURCE_TYPE_TEXTURE_ATLAS:
                return "TextureAtlas";
        }
        return "Unknown type";
    }

    public boolean isLoading()
    {
        return assetManager.getQueuedAssets() != 0;
    }

    public int getLoadingProgress()
    {
        return (int)(assetManager.getProgress() * 100);
    }
}
