package com.ormisiclapps.tools.generator.ui;

import com.ormisiclapps.tools.generator.GeneratorLauncher;
import com.ormisiclapps.tools.generator.enumerations.OutputType;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * Created by OrMisicL on 6/2/2016.
 */
public class GeneratorInterface extends JFrame implements ActionListener{
    private DirectoryChooser directoryChooser;
    // JFrame components
    private JButton generateButton;
    private JButton browseButton;
    private JLabel fileChooseLabel;
    private JLabel logoPanel;
    private JScrollPane scrollPane;
    private JTextPane outputTextArea;
    private JTextField filePathTextField;
    
    public GeneratorInterface()
    {
        // Create the directory chooser instance
        directoryChooser = new DirectoryChooser();
        // Create the logo image
        Image logoImage = new ImageIcon("Textures/GeneratorLogo.png").getImage();
        // Resize the image
        logoImage = resizeImage(logoImage, 300, 300);
        // Create components
        filePathTextField = new JTextField();
        fileChooseLabel = new JLabel();
        generateButton = new JButton();
        logoPanel = new JLabel(new ImageIcon(logoImage));
        scrollPane = new JScrollPane();
        outputTextArea = new JTextPane();
        browseButton = new JButton();
        // Set the window size and position
        setBounds(new java.awt.Rectangle(200, 100, 500, 400));
        // Set the file chooser label's text
        fileChooseLabel.setText("Specify the model directory (containing JSON files):");
        // Setup the generate button
        generateButton.setText("Generate");
        generateButton.addActionListener(this);
        // Setup the logo pane layout
        GroupLayout logoPanelLayout = new GroupLayout(logoPanel);
        logoPanel.setLayout(logoPanelLayout);
        logoPanelLayout.setHorizontalGroup(
                logoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 271, Short.MAX_VALUE)
        );
        logoPanelLayout.setVerticalGroup(
                logoPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 107, Short.MAX_VALUE)
        );

        //outputTextArea.setColumns(20);
        //outputTextArea.setRows(5);
        scrollPane.setViewportView(outputTextArea);

        browseButton.setText("Browse ...");
        browseButton.addActionListener(this);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 429, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(30, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(48, 48, 48)
                                                .addComponent(fileChooseLabel, GroupLayout.PREFERRED_SIZE, 344, GroupLayout.PREFERRED_SIZE)
                                                .addGap(40, 40, 40))
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(filePathTextField, GroupLayout.PREFERRED_SIZE, 344, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(browseButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGap(84, 84, 84)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                .addComponent(generateButton, GroupLayout.PREFERRED_SIZE, 259, GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGap(81, 81, 81)
                                                        .addComponent(logoPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                                .addGap(17, 17, 17))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(logoPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fileChooseLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(filePathTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(browseButton))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(generateButton)
                                .addGap(18, 18, 18)
                                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        // Set the background color
        getContentPane().setBackground(new Color(197, 197, 197));
        // Set title
        setTitle("OrMisicL Apps model generator");
        // Pack
        pack();
        // Set visible
        setVisible(true);
        // Set unresizable
        setResizable(false);
        // Set the default close operation
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private Image resizeImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Is it browse button
        if(e.getSource() == browseButton)
        {
            // Show the directory chooser
            int returnedValue = directoryChooser.showDialog(this);
            // If Open button
            if (returnedValue == JFileChooser.APPROVE_OPTION) {
                // Set the file path's text field
                filePathTextField.setText(directoryChooser.getChoosenDirectory());
            } else {

            }
        }
        else if(e.getSource() == generateButton)
        {
            // Start generating
            GeneratorLauncher.getModelGenerator().generateModel(filePathTextField.getText());
        }
    }

    public void output(String text, OutputType type)
    {
        // Append text according to its type
        if(type == OutputType.OUTPUT_TYPE_ERROR)
            appendToPane(outputTextArea, text, new Color(200, 0, 0));
        else if(type == OutputType.OUTPUT_TYPE_SUCCESS)
            appendToPane(outputTextArea, text, new Color(0, 150, 0));
        else
            appendToPane(outputTextArea, text, new Color(0, 0, 0));
    }

    private void appendToPane(JTextPane textPane, String text, Color color)
    {
        // Get the default style context
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        // Generate the attribute set
        AttributeSet attributeSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
        // Add font and alignment attributes
        attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.FontFamily, "Arial");
        attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        // Get the text pane's length
        int length = textPane.getDocument().getLength();
        // Set the caret to the last position
        textPane.setCaretPosition(length);
        // Set the text to use the new attributes
        textPane.setCharacterAttributes(attributeSet, false);
        // Add text
        textPane.replaceSelection(text + "\n");
    }
}
