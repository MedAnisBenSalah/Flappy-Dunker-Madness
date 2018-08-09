package com.ormisiclapps.tools.generator.ui;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * Created by OrMisicL on 6/2/2016.
 * This will handle the dialog of the directory chooser
 */
public class DirectoryChooser extends JFrame {
    private JFileChooser fileChooser;

    public DirectoryChooser()
    {
        // Create the directory chooser
        fileChooser = new JFileChooser();
        // Setup the directory chooser
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Choose the JSON model directory");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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

    public boolean isChoosen()
    {
        return fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION;
    }

    public String getChoosenDirectory()
    {
        return fileChooser.getSelectedFile().getAbsolutePath();
    }
}
