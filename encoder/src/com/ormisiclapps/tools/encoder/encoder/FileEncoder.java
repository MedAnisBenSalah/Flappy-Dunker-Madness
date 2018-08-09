package com.ormisiclapps.tools.encoder.encoder;

import com.badlogic.gdx.utils.Base64Coder;
import com.ormisiclapps.tools.encoder.EncoderLauncher;
import com.ormisiclapps.tools.encoder.enumerations.OutputType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by OrMisicL on 6/2/2016.
 * Will encode any given file using Base64
 */
public class FileEncoder {

    public FileEncoder()
    {

    }

    public void encodeFile(String filePath)
    {
        EncoderLauncher.getInterface().output("File encoding started:", OutputType.OUTPUT_TYPE_NORMAL);
        EncoderLauncher.getInterface().output("------------------------------------------", OutputType.OUTPUT_TYPE_NORMAL);
        EncoderLauncher.getInterface().output("", OutputType.OUTPUT_TYPE_NORMAL);
        // Create the file instance
        File file = new File(filePath);
        // Get the file name
        String fileName = file.getName();
        EncoderLauncher.getInterface().output("Reading file: " + fileName, OutputType.OUTPUT_TYPE_NORMAL);
        try
        {
            // Create the file input stream
            FileInputStream inputStream = new FileInputStream(file);
            // Create the data array
            byte[] data = new byte[(int)file.length()];
            // Read file
            inputStream.read(data);
            // Encode the file using Base64
            String encodedData = new String(Base64Coder.encode(data));
            // Close the file
            inputStream.close();
            // Create the target directory
            File targetDirectory = new File(file.getPath() + "\\" + "Encoded\\");
            if(!targetDirectory.exists())
                targetDirectory.mkdir();

            // Get the file handle
            File outputFile = new File(swapExtension(file.getAbsolutePath()));
            EncoderLauncher.getInterface().output("Writing file: " + outputFile.getName(), OutputType.OUTPUT_TYPE_NORMAL);
            // Open the file and create an output stream
            FileOutputStream outputStream;
            outputStream = new FileOutputStream(outputFile);
            outputStream.write(encodedData.getBytes());
            outputStream.close();
        }
        catch(Exception e)
        {
            // For developers
            e.printStackTrace();
            // Print an error
            EncoderLauncher.getInterface().output("File encoding failed.", OutputType.OUTPUT_TYPE_ERROR);
        }

        EncoderLauncher.getInterface().output("", OutputType.OUTPUT_TYPE_NORMAL);
        EncoderLauncher.getInterface().output("File successfully encoded.", OutputType.OUTPUT_TYPE_NORMAL);
    }

    private String swapExtension(String file)
    {
        // Remove the json extension
        String result = file.substring(0, file.lastIndexOf('.'));
        return result + ".oac";
    }
}
