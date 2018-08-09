package com.ormisiclapps.flappydunkermadness.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.game.nodes.entity.ModelNode;
import com.ormisiclapps.flappydunkermadness.game.nodes.movement.MovementNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by OrMisicL on 6/1/2016.
 */
public class ModelManager
{
    private Map<String, ModelNode> loadedModels;
    private Map<String, MovementNode[]> loadedMovements;

    public ModelManager()
    {
        // Create the maps
        loadedModels = new HashMap<String, ModelNode>();
        loadedMovements = new HashMap<String, com.ormisiclapps.flappydunkermadness.game.nodes.movement.MovementNode[]>();
    }

    /*
        Will load the model's XML file and store it into the models map
    */
    public ModelNode loadModel(String modelName)
    {
        // If the model is already loaded then simply return it
        if(loadedModels.containsKey(modelName))
            return loadedModels.get(modelName);

        // Parse the settings file
        XmlReader.Element rootElement = Core.getInstance().getFileManager().parseModelFile(modelName);
        if(rootElement == null)
            return null;

        // Get the parts count
        int partsCount = rootElement.getChildCount();
        // Validate the parts count
        if(partsCount == 0)
            return null;

        // Validate the body element
        XmlReader.Element bodyElement = rootElement.getChildByName("Body");
        if(bodyElement == null || bodyElement.getChildByName("Size") == null)
            return null;

        // Get the size
        float x = bodyElement.getChildByName("Size").getFloatAttribute("x");
        float y = bodyElement.getChildByName("Size").getFloatAttribute("y");
        Vector2 size = new Vector2(x, y);

        // Validate the physics element
        XmlReader.Element physicsElement = rootElement.getChildByName("Physics");
        if (physicsElement == null || physicsElement.getChildByName("Mass") == null || physicsElement.getChildByName("Friction") == null
                || physicsElement.getChildByName("Restitution") == null)
            return null;

        // Get the part parameters
        float mass = physicsElement.getChildByName("Mass").getFloatAttribute("value");
        float friction = physicsElement.getChildByName("Friction").getFloatAttribute("value");
        float restitution = physicsElement.getChildByName("Restitution").getFloatAttribute("value");

        // Validate the movement element
        XmlReader.Element movementElement = rootElement.getChildByName("Movements");
        if(movementElement == null)
            return null;

        // Get the simple movements count
        int movementsCount = movementElement.getIntAttribute("count");
        // Create the movements array
        MovementNode[] movementNodes = new MovementNode[movementsCount];
        // Add it to the loaded movements map
        loadedMovements.put(modelName, movementNodes);
        // Load all the movements
        for(int i = 0; i < movementsCount; i++)
            loadMovement(modelName, i);

        // Create the model node
        ModelNode modelNode = new ModelNode(rootElement.getChildCount() - 3);
        // Loop through the fixtures
        for(int i = 2; i < rootElement.getChildCount() - 1; i++)
        {
            // Validate the render element
            XmlReader.Element fixtureElement = rootElement.getChild(i);
            if (fixtureElement == null || fixtureElement.getChildByName("Position") == null ||
                    fixtureElement.getChildByName("Radius") == null)
                return null;

            // Get the position
            x = fixtureElement.getChildByName("Position").getFloatAttribute("x");
            y = fixtureElement.getChildByName("Position").getFloatAttribute("y");
            Vector2 position = new Vector2(x, y);
            // Get the radius
            float radius = fixtureElement.getChildByName("Radius").getFloatAttribute("value");

            // Add fixture
            modelNode.addFixture(i - 2, position, radius);
        }
        // Set the model node
        modelNode.setNode(size, mass, friction, restitution, movementsCount);
        // Add it to the loaded models
        loadedModels.put(modelName, modelNode);
        return modelNode;
    }

    /*public void unloadModel(String model)
    {
        // Unload the model
        if(loadedModels.containsKey(model))
            loadedModels.remove(model);
    }*/

    /*
        Will load the movement's XML file and store it into the movements map
    */
    private void loadMovement(String modelName, int movementId)
    {
        // Parse the settings file
        XmlReader.Element rootElement = Core.getInstance().getFileManager().parseMovementFile(modelName, movementId + 1);
        if(rootElement == null)
            return;

        // Create the movement node
        MovementNode movementNode = new MovementNode();
        // Get the type
        String type = rootElement.getChildByName("Type").get("value");
        // Get the points count
        int pointsCount = rootElement.getChildByName("Path").getIntAttribute("points");
        // Setup the movement node
        movementNode.setNode(type, pointsCount);
        // Load all the movement points
        for(int i = 0; i < pointsCount; i++)
        {
            // Find the x and y positions
            String x = rootElement.getChildByName("Point" + (i + 1)).get("x");
            String y = rootElement.getChildByName("Point" + (i + 1)).get("y");
            // Add the point
            movementNode.setPoint(i, x, y);
        }

        // Add it to the loaded movements
        loadedMovements.get(modelName)[movementId] = movementNode;
    }

    public MovementNode[] getMovementNodes(String modelName)
    {
        if(loadedMovements.containsKey(modelName))
            return loadedMovements.get(modelName);
        else
            return null;
    }

    public ModelNode getModelNode(String modelName)
    {
        if(loadedModels.containsKey(modelName))
            return loadedModels.get(modelName);
        else
            return null;
    }
}
