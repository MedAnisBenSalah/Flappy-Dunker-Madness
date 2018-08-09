package com.ormisiclapps.flappydunkermadness.graphics.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ScreenTouchType;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by OrMisicL on 8/7/2017.
 */

public class UITabView extends UIWidget
{
    private Array<String> tabs;
    private Map<String, Array<UIWidget>> tabContent;
    private TextureRegion backgroundTexture;
    private Vector2 tmpVector, tmpVector2, tmpVector3;
    private Vector2 tabSize;
    private UIText text;
    private String selectedTab;
    private boolean selectChanged;

    public UITabView(Vector2 position, Vector2 size)
    {
        // Create the widget
        super(false, position, size);
        // Create the tabs array
        tabs = new Array<String>();
        // Create the tab content map
        tabContent = new HashMap<String, Array<UIWidget>>();
        // Initialize
        initialize();
        // Create vectors array
        tmpVector = new Vector2();
        tmpVector2 = new Vector2();
        tmpVector3 = new Vector2();
        // Reset the selected tab
        selectedTab = "";
        // Reset flags
        selectChanged = false;
    }

    private void initialize()
    {
        // Get the background texture
        backgroundTexture = Core.getInstance().getResourcesManager().getResource("UI/Empty",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

        backgroundTexture.setRegion(15, 15, 1, 1);
        // Calculate the tabs height
        tabSize = new Vector2(getSize().x, getSize().y / 10f);
        // Create the text instance
        text = new UIText((int)tabSize.y / 3, Color.WHITE, 0f, Color.WHITE);
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void process()
    {
        // Update the widget
        super.process();
        // Reset select changed flag
        selectChanged = false;
        // Get the touch position
        Vector2 touchPosition = Core.getInstance().getInputHandler().getScreenTouchPosition();
        // Draw the tab texts
        int i = 1;
        for(String tabText : tabs)
        {
            // Get the tab text size
            Vector2 textSize = text.getSize(tabText);
            // Draw the tab text
            tmpVector.set(getPosition());
            tmpVector2.set(getSize());
            text.drawText(tabText, tmpVector.x + tabSize.x * i - tabSize.x / 2f - textSize.x / 2f, tmpVector.y + tmpVector2.y - tabSize.y / 2f + textSize.y / 2f);
            // Check if its pressed
            if(!selectChanged && Core.getInstance().getInputHandler().isScreenTouched() &&
                    Core.getInstance().getInputHandler().getScreenTouchType() == ScreenTouchType.SCREEN_TOUCH_UP &&
                    GameMath.checkCollision(touchPosition, tmpVector3.set(1f, 1f), tmpVector.add(tabSize.x * (i - 1) + tabSize.x / 2f, tmpVector2.y - tabSize.y / 2f), tmpVector2.set(tabSize)))
            {
                // Set the selected flag
                selectChanged = true;
                // Set the new selection
                selectedTab = tabText;
            }
            // Increase position
            i++;
        }
        // Update text
        text.process();
        // Update tab widgets
        if(tabContent.get(selectedTab) != null)
        {
            for(UIWidget widget : tabContent.get(selectedTab))
                widget.process();
        }
    }

    @Override
    public void render()
    {
        // Set the background color
        Core.getInstance().getGraphicsManager().setColor(0f, 0f, 0f, 0.9f);
        // Draw the background
        Core.getInstance().getGraphicsManager().drawTextureRegion(backgroundTexture, tmpVector.set(getPosition()),
                tmpVector2.set(getSize().x, getSize().y - tabSize.y), 0f);

        // Draw the tab texts
        int i = 0;
        for(String tabText : tabs)
        {
            // Set the tabs background color
            if(tabText.equals(selectedTab))
                Core.getInstance().getGraphicsManager().setColor(0f, 0f, 0f, 0.9f);
            else
                Core.getInstance().getGraphicsManager().setColor(0.2f, 0.2f, 0.2f, 0.9f);

            // Draw tabs
            tmpVector.set(getSize());
            tmpVector.set(getPosition().x + tabSize.x * i, getPosition().y + tmpVector.y - tabSize.y);
            Core.getInstance().getGraphicsManager().drawTextureRegion(backgroundTexture, tmpVector, tabSize, 0f);
            // Increase position
            i++;
        }
        // Restore background color
        Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
        // Draw text
        text.render();
        // Draw tab widgets
        if(tabContent.get(selectedTab) != null)
        {
            for (UIWidget widget : tabContent.get(selectedTab))
                widget.render();
        }
    }

    public void addTab(String tab)
    {
        // Add the tab
        if(!tabs.contains(tab, false))
        {
            tabs.add(tab);
            // Recalculate the size
            tabSize.set(getSize().x / tabs.size, tabSize.y);
            // If its the first tab then set it as selected
            if(tabs.size == 1)
                selectedTab = tab;
        }
    }

    public void addElement(String tab, com.ormisiclapps.flappydunkermadness.graphics.ui.UIWidget widget)
    {
        // Ensure the tab exists
        if(!tabs.contains(tab, false))
            return;

        // Create the widgets array if its a new tab
        if(!tabContent.containsKey(tab))
            tabContent.put(tab, new Array<com.ormisiclapps.flappydunkermadness.graphics.ui.UIWidget>());

        // Add to the widgets array
        if(!tabContent.get(tab).contains(widget, false))
            tabContent.get(tab).add(widget);
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public boolean isSelectChanged() {
        return selectChanged;
    }
}
