package com.ormisiclapps.flappydunkermadness.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ormisiclapps.flappydunkermadness.game.nodes.save.SaveNode;

import java.io.*;
import java.security.MessageDigest;

/**
 * Created by Anis on 2/13/2017.
 */

public class StatsSaver
{
    private static final String SAVE_FILE = "FlappyDunker.oap";
    // Save file keys
    private static final String CHECKSUM_KEY = "Key";
    private static final String ENCRYPTED_SAVE_DATA_KEY = "Value";
    private static final String GAMES_PLAYED_KEY = "GamesPlayed";
    private static final String BEST_SCORES_KEY = "BestScores";
    private static final String LAST_SCORES_KEY = "LastScores";
    private static final String OBSTACLES_PASSED_KEY = "ObstaclesPassed";
    private static final String SWISHES_KEY = "Swishes";
    private static final String BEST_SWISHES_STREAK_KEY = "BestSwishesStreak";
    private static final String EATABLES_COLLECTED_KEY = "EatablesCollected";
    private static final String SOUND_STATE_KEY = "SoundState";
    private static final String VIBRATION_STATE_KEY = "VibrationState";
    private static final String XP_KEY = "XP";
    private static final String BALL_KEY = "Ball";
    private static final String WING_KEY = "Wing";
    private static final String HOOP_KEY = "Hoop";
    private static final String BALLS_STATES_KEY = "BallsStates";
    private static final String WINGS_STATES_KEY = "WingsStates";
    private static final String HOOPS_STATES_KEY = "HoopsStates";
    private static final String NEVER_RATE_KEY = "NeverRate";
    private static final String GPG_CONNECTED_KEY = "GPGConnected";

    private Preferences preferences;
    private XmlReader xmlReader;

    public SaveNode savedData;

    public StatsSaver()
    {
        // Reset instances
        savedData = null;
        // Create XML reader instance
        xmlReader = new XmlReader();
        // Get the preferences instance
        preferences = Gdx.app.getPreferences(SAVE_FILE);
    }

    public void load()
    {
        // Create the save node instance
        savedData = new SaveNode();
        // Do we have a saved file in the internal storage ?
        boolean isUsingInternalStorage = false;
        FileHandle saveFile = Gdx.files.local(SAVE_FILE);
        if(saveFile.exists())
            isUsingInternalStorage = true;
        // Check if we have a save file in the shared preferences
        else if(!preferences.contains(CHECKSUM_KEY))
            return;

        try
        {
            // Get the encrypted saved data
            String encryptedSaveData;
            XmlReader.Element element = null;
            if(!isUsingInternalStorage)
                encryptedSaveData = preferences.getString(ENCRYPTED_SAVE_DATA_KEY);
            else
            {
                // Read the file
                byte[] fileBytes = saveFile.readBytes();
                // Parse the XML data
                element = xmlReader.parse(new String(fileBytes, "UTF-8"));
                // Get the encrypted save data
                encryptedSaveData = element.get(ENCRYPTED_SAVE_DATA_KEY);
            }
            // Decrypt it
            byte[] decodedBytes = Base64Coder.decode(encryptedSaveData);
            String decodedSaveData = new String(decodedBytes, "US-ASCII");
            // Get the encrypted hash
            String encryptedHash;
            if(!isUsingInternalStorage)
                encryptedHash = preferences.getString(CHECKSUM_KEY);
            else
                // Get the encrypted save data key
                encryptedHash = element.get(CHECKSUM_KEY);

            // Decrypt it
            byte[] decodedHashBytes = Base64Coder.decode(encryptedHash);
            String decodedHash = new String(decodedHashBytes, "UTF-8");
            // Compare both keys
            if(!decodedHash.equals(generateMD5Hash(decodedSaveData)))
            {
                // Save a blank save (resetting data)
                save();
                return;
            }
            // Parse the XML data
            element = xmlReader.parse(decodedSaveData);
            // Load each and every key
            if(element.getChildByName(GAMES_PLAYED_KEY) != null)
                savedData.gamesPlayed = Long.parseLong(element.get(GAMES_PLAYED_KEY));

            if(element.getChildByName(BEST_SCORES_KEY) != null)
            {
                // Get the element
                XmlReader.Element bestScoresElement = element.getChildByName(BEST_SCORES_KEY);
                // Loop through the elements
                for(int i = 0; i < bestScoresElement.getInt("count"); i++)
                    savedData.bestScores[i] = Long.parseLong(bestScoresElement.get("element" + i));
            }

            if(element.getChildByName(LAST_SCORES_KEY) != null)
            {
                // Get the element
                XmlReader.Element bestScoresElement = element.getChildByName(LAST_SCORES_KEY);
                // Loop through the elements
                for(int i = 0; i < bestScoresElement.getInt("count"); i++)
                    savedData.lastScores[i] = Long.parseLong(bestScoresElement.get("element" + i));
            }

            if(element.getChildByName(OBSTACLES_PASSED_KEY) != null)
                savedData.obstaclesPassed = Long.parseLong(element.get(OBSTACLES_PASSED_KEY));

            if(element.getChildByName(SWISHES_KEY) != null)
                savedData.swishes = Long.parseLong(element.get(SWISHES_KEY));

            if(element.getChildByName(BEST_SWISHES_STREAK_KEY) != null)
                savedData.bestSwishesStreak = Long.parseLong(element.get(BEST_SWISHES_STREAK_KEY));

            if(element.getChildByName(EATABLES_COLLECTED_KEY) != null)
                savedData.eatablesCollected = Long.parseLong(element.get(EATABLES_COLLECTED_KEY));

            if(element.getChildByName(SOUND_STATE_KEY) != null)
                savedData.soundState = element.getBoolean(SOUND_STATE_KEY);

            if(element.getChildByName(VIBRATION_STATE_KEY) != null)
                savedData.vibrationState = element.getBoolean(VIBRATION_STATE_KEY);

            if(element.getChildByName(NEVER_RATE_KEY) != null)
                savedData.neverRate = element.getBoolean(NEVER_RATE_KEY);

            if(element.getChildByName(GPG_CONNECTED_KEY) != null)
                savedData.GPGConnected = element.getBoolean(GPG_CONNECTED_KEY);

            if(element.getChildByName(XP_KEY) != null)
                savedData.xp = Long.parseLong(element.get(XP_KEY));

            if(element.getChildByName(BALL_KEY) != null)
                savedData.ball = element.get(BALL_KEY);

            if(element.getChildByName(WING_KEY) != null)
                savedData.wing = element.get(WING_KEY);

            if(element.getChildByName(HOOP_KEY) != null)
                savedData.hoop = element.get(HOOP_KEY);

            if(element.getChildByName(BALLS_STATES_KEY) != null)
            {
                // Get the element
                XmlReader.Element stateElement = element.getChildByName(BALLS_STATES_KEY);
                // Loop through the elements
                for(int i = 0; i < stateElement.getInt("count"); i++)
                    savedData.ballsStates[i] = stateElement.getBoolean("element" + i);
            }

            if(element.getChildByName(WINGS_STATES_KEY) != null)
            {
                // Get the element
                XmlReader.Element stateElement = element.getChildByName(WINGS_STATES_KEY);
                // Loop through the elements
                for(int i = 0; i < stateElement.getInt("count"); i++)
                    savedData.wingsStates[i] = stateElement.getBoolean("element" + i);
            }

            if(element.getChildByName(HOOPS_STATES_KEY) != null)
            {
                // Get the element
                XmlReader.Element stateElement = element.getChildByName(HOOPS_STATES_KEY);
                // Loop through the elements
                for(int i = 0; i < stateElement.getInt("count"); i++)
                    savedData.hoopsStates[i] = stateElement.getBoolean("element" + i);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void save()
    {
        try
        {
            // Maybe, only maybe we'll find another way to clear the writer's buffer
            StringWriter stringWriter = new StringWriter();
            XmlWriter xmlWriter = new XmlWriter(stringWriter);
            // Create an xml version of the save data
            xmlWriter.element("Data")
                    .element(GAMES_PLAYED_KEY, ""+savedData.gamesPlayed);

            // Create the best scores element
            xmlWriter.element(BEST_SCORES_KEY).attribute("count", savedData.bestScores.length);
            // Loop through the color states
            for(int i = 0; i < savedData.bestScores.length; i++)
                xmlWriter.attribute("element" + i, savedData.bestScores[i]);

            // Leave the element
            xmlWriter.pop();

            // Create the best scores element
            xmlWriter.element(LAST_SCORES_KEY).attribute("count", savedData.lastScores.length);
            // Loop through the color states
            for(int i = 0; i < savedData.lastScores.length; i++)
                xmlWriter.attribute("element" + i, savedData.lastScores[i]);

            // Leave the element
            xmlWriter.pop();

            xmlWriter.element(OBSTACLES_PASSED_KEY, ""+savedData.obstaclesPassed)
                    .element(SWISHES_KEY, ""+savedData.swishes)
                    .element(BEST_SWISHES_STREAK_KEY, ""+savedData.bestSwishesStreak)
                    .element(EATABLES_COLLECTED_KEY, ""+savedData.eatablesCollected)
                    .element(SOUND_STATE_KEY, ""+savedData.soundState)
                    .element(VIBRATION_STATE_KEY, ""+savedData.vibrationState)
                    .element(NEVER_RATE_KEY, ""+savedData.neverRate)
                    .element(GPG_CONNECTED_KEY, ""+savedData.GPGConnected)
                    .element(XP_KEY, ""+savedData.xp)
                    .element(BALL_KEY, ""+savedData.ball)
                    .element(WING_KEY, ""+savedData.wing)
                    .element(HOOP_KEY, ""+savedData.hoop);

            // Create the color state element
            xmlWriter.element(BALLS_STATES_KEY).attribute("count", savedData.ballsStates.length);
            // Loop through the states
            for(int i = 0; i < savedData.ballsStates.length; i++)
                xmlWriter.attribute("element" + i, savedData.ballsStates[i]);

            // Leave the element
            xmlWriter.pop();

            // Create the color state element
            xmlWriter.element(WINGS_STATES_KEY).attribute("count", savedData.wingsStates.length);
            // Loop through the states
            for(int i = 0; i < savedData.wingsStates.length; i++)
                xmlWriter.attribute("element" + i, savedData.wingsStates[i]);

            // Leave the element
            xmlWriter.pop();

            // Create the state element
            xmlWriter.element(HOOPS_STATES_KEY).attribute("count", savedData.hoopsStates.length);
            // Loop through the color states
            for(int i = 0; i < savedData.hoopsStates.length; i++)
                xmlWriter.attribute("element" + i, savedData.hoopsStates[i]);

            // Leave the element
            xmlWriter.pop();

            // Close the XML writer
            xmlWriter.close();
            // Generate an MD5 hash for the xml
            String hash = generateMD5Hash(stringWriter.toString());
            // Encode the XML data
            String encodedSaveData = String.valueOf(Base64Coder.encode(stringWriter.toString().getBytes()));
            // Reset writers
            stringWriter = new StringWriter();
            xmlWriter = new XmlWriter(stringWriter);
            // Write it to the preferences file
            xmlWriter.element("Data");
            xmlWriter.element(CHECKSUM_KEY, String.valueOf(Base64Coder.encode(hash.getBytes())));
            xmlWriter.element(ENCRYPTED_SAVE_DATA_KEY, encodedSaveData);
            xmlWriter.pop();
            // Close the XML writer
            xmlWriter.close();
            // Open internal storage
            FileHandle saveFile = Gdx.files.local(SAVE_FILE);
            saveFile.writeBytes(stringWriter.toString().getBytes(), false);
        }
        catch(Exception exception)
        {
            // Save file failed somehow
            exception.printStackTrace();
        }
    }

    private String generateMD5Hash(String data)
    {
        try
        {
            // Get message digest instance
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // Hash the string
            byte[] hashedBytes = messageDigest.digest(data.getBytes("UTF-8"));
            // Get the result as a string
            return new String(hashedBytes);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //Gdx.app.exit();
        }
        return "";
    }
}
