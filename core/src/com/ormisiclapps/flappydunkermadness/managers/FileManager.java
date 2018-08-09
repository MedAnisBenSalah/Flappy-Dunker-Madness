package com.ormisiclapps.flappydunkermadness.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.XmlReader;

import java.io.InputStream;

/**
 * Created by OrMisicL on 6/1/2016.
 */
public class FileManager
{
    private XmlReader xmlReader;

    public FileManager()
    {
        // Create xmd reader instance
        xmlReader = new XmlReader();
    }

    public XmlReader.Element parseConfigurationFile(String file)
    {
        // Parse the xml file
        try
        {
            // Load the file data
            InputStream inputStream = openFile(file);
            // Read the encoded XML data
            byte[] encodedData = new byte[(int)getFileLength(file)];
            inputStream.read(encodedData);
            // Decode the file
            String decodedXML = new String(Base64Coder.decode(new String(encodedData)));
            return xmlReader.parse(decodedXML);
        } catch(java.io.IOException exception) {
            return null;
        }
    }

    public XmlReader.Element parseModelFile(String modelName)
    {
        return parseConfigurationFile("Models/" + modelName + "/Settings.oac");
    }

    public XmlReader.Element parseLevelFile(String modelName)
    {
        return parseConfigurationFile("Levels/" + modelName + ".oac");
    }

    public XmlReader.Element parseLevelsFile()
    {
        return parseConfigurationFile("Levels/LevelsSettings.oac");
    }

    public XmlReader.Element parseMovementFile(String modelName, int movementId)
    {
        return parseConfigurationFile("Models/" + modelName + "/Movement-" + movementId + ".oac");
    }

    public XmlReader.Element parsePartsFile(String modelName)
    {
        return parseConfigurationFile("Models/" + modelName + "/PartedSettings.oac");
    }

    public InputStream openFile(String file)
    {
        // Get the file handle
        FileHandle fileHandle = Gdx.files.internal(file);
        // Read the file
        return fileHandle.read();
    }

    public long getFileLength(String file)
    {
        // Get the file handle
        FileHandle fileHandle = Gdx.files.internal(file);
        // Read the file
        return fileHandle.length();
    }

    public InputStream openBodyFile(String model, String body)
    {
        return openFile("Models/"+model+"/"+body+".oab");
    }
}
