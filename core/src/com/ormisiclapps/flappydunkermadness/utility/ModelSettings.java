package com.ormisiclapps.flappydunkermadness.utility;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.ormisiclapps.flappydunkermadness.core.Core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by OrMisicL on 7/31/2016.
 */
public class ModelSettings {
    private Map<String, ObjectMap<String, String>> modelSettings;

    public ModelSettings()
    {
        // Create the model settings array
        modelSettings = new HashMap<String, ObjectMap<String, String>>();
    }

    public boolean load()
    {
        // Parse the settings file
        XmlReader.Element rootElement = Core.getInstance().getFileManager().parseConfigurationFile("Models/ModelsSettings.oac");
        if(rootElement == null)
            return false;

        // Loop through the models
        for(int i = 0; i < rootElement.getChildCount(); i++)
            // Add the child's attributes to the model settings
            modelSettings.put(rootElement.getChild(i).getName(), rootElement.getChild(i).getAttributes());

        return true;
    }

    public String getModelAttribute(String model, String attribute)
    {
        // Get the model attributes
        ObjectMap<String, String> attributes = modelSettings.get(model);
        if(attributes != null)
        {
            // Find the target attribute
            String result = attributes.get(attribute);
            if(result != null)
                return result;
        }
        return null;
    }

    public float getModelAttributeAsFloat(String model, String attribute)
    {
        String result = getModelAttribute(model, attribute);
        return result == null ? 0 : Float.parseFloat(result);
    }

    public int getModelAttributeAsInt(String model, String attribute)
    {
        String result = getModelAttribute(model, attribute);
        return result == null ? 0 : Integer.parseInt(result);
    }

    public boolean getModelAttributeAsBoolean(String model, String attribute)
    {
        String result = getModelAttribute(model, attribute);
        return result != null && Boolean.parseBoolean(result);
    }

    public boolean doesModelAttributeExist(String model, String attribute)
    {
        return getModelAttribute(model, attribute) != null;
    }

    public String getModel(int id)
    {
        return (String)modelSettings.keySet().toArray()[id];
    }

    public int getModelsCount()
    {
        return modelSettings.size();
    }
}
