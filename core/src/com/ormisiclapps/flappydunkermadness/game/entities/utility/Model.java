package com.ormisiclapps.flappydunkermadness.game.entities.utility;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.game.nodes.entity.ModelNode;

/**
 * Created by OrMisicL on 9/29/2015.
 * Will handle the model's texture for each entity
 */
public class Model
{
    private ModelNode node;
    private TextureRegion texture;
    private String name;

    public Model(String name)
    {
        // Save the name
        this.name = name;
        // Reset instances
        texture = null;
        node = null;
    }

    public void load()
    {
        // Load the model
        node = Core.getInstance().getModelManager().loadModel(name);
        // Get the texture
        texture = Core.getInstance().getResourcesManager().getResource(name, ResourceType.RESOURCE_TYPE_MODEL);
        // If the model has no texture then use the default empty one
        if(texture == null)
            texture = Core.getInstance().getResourcesManager().getResource("Empty", ResourceType.RESOURCE_TYPE_MODEL);
    }

    public String getName() { return name; }
    public ModelNode getNode() { return node; }
    public TextureRegion getTexture() { return texture; }
}
