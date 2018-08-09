package com.ormisiclapps.tools.encoder.ui;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * Created by OrMisicL on 6/2/2016.
 * This will handle the dialog of the file chooser
 */
public class FileChooser extends JFrame
{
    private JFileChooser fileChooser;

    public FileChooser()
    {
        // Create the directory chooser
        fileChooser = new JFileChooser();
        // Setup the directory chooser
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Choose the file to encode ...");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setAcceptAllFileFilterUsed(false);
        // Set this frame to use only the file chooser
        add(fileChooser);
        // Pack
        pack();
        // Set size
        setSize(500, 400);
    }

    public int showDialog(JFrame parent)
    {
        return fileChooser.showOpenDialog(parent);
    }

    public String getChoosenDirectory()
    {
        return fileChooser.getSelectedFile().getAbsolutePath();
    }
}
