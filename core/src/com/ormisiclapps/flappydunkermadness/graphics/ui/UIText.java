package com.ormisiclapps.flappydunkermadness.graphics.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ormisiclapps.flappydunkermadness.core.Core;

import java.util.Iterator;

/**
 * Created by OrMisicL on 9/22/2015.
 */
public class UIText extends UIWidget
{
    private BitmapFont font;
    private Array<Object> requestedTextArray;
    private GlyphLayout glyphLayout;
    private Vector2 size;
    private Vector2 tmpVector;
    private int textSize;

    public UIText(int size, Color color, float borderWidth, Color borderColor)
    {
        // Setup the widget
        super(false);
        // Create the font parameter instance
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        // Set the parameters
        parameter.color = color;
        parameter.size = size;
        parameter.borderWidth = borderWidth;
        parameter.borderColor = borderColor;
        // Generate the font
        font = UIMain.getInstance().generateFont(parameter);
        // Create the GlyphLayout instance
        glyphLayout = new GlyphLayout();
        // Set size
        textSize = size;
        // Create the requested text array
        requestedTextArray = new Array<Object>();
        // Create vector
        this.size = new Vector2();
        tmpVector = new Vector2();
    }

    public void dispose()
    {
        font.dispose();
    }

    public void drawText(String text, Vector2 position)
    {
        // Add the text and position to the requested array
        requestedTextArray.add(text);
        requestedTextArray.add(position.cpy());
    }

    public void drawText(String text, float x, float y)
    {
        drawText(text, tmpVector.set(x, y));
    }

    public void render()
    {
        // Loop through all the requested texts
        Iterator<Object> iterator = requestedTextArray.iterator();
        while(iterator.hasNext())
        {
            // Get the text
            String text = ((String)iterator.next());
            // Remove the text
            iterator.remove();
            // Get the position
            Vector2 position = ((Vector2)iterator.next());
            // Remove the position
            iterator.remove();
            // Render it
            Core.getInstance().getGraphicsManager().drawText(font, text, position);
        }
    }

    public Vector2 getSize(String text)
    {
        // Get the font bounds from the Glyph layout
        glyphLayout.setText(font, text);
        return size.set(glyphLayout.width, glyphLayout.height);
    }

    public void setAlpha(float alpha)
    {
        // Get the color
        Color color = font.getColor();
        // Set alpha
        color.a = alpha;
        // Change color
        font.setColor(color);
    }

    public void clean()
    {
        requestedTextArray.clear();
    }

    public void setColor(Color color)
    {
        font.setColor(color);
    }

}
