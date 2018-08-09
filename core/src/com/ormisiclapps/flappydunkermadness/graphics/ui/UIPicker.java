package com.ormisiclapps.flappydunkermadness.graphics.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.enumerations.ScreenTouchType;

import java.util.Comparator;

/**
 * Created by OrMisicL on 8/4/2017.
 */

public class UIPicker extends UIWidget
{
    private Array<TextureRegion> elements;
    private boolean[] elementsState;
    private float elementSlotSize;
    private Vector2 tmpVector, tmpVector2, tmpVector3, lockIconSize;
    private TextureRegion selectedElement, lockIconTexture;
    private boolean selectChanged;
    private float currentScreenPosition, maximumScreenPosition;
    private UIText nameText;
    private Array<UIButton> selectButtons;
    private long[] prices;

    public UIPicker(Vector2 position, Vector2 size, String textureName, String selectedElement,
                    final String[] elementsOrder, float elementSlotSize, boolean[] elementsState, long[] prices)
    {
        // Create the widget
        super(false, position, size);
        // Create the elements array
        elements = new Array<TextureRegion>();
        // Set the elements state array
        this.elementsState = elementsState;
        // Calculate the element's slot size
        this.elementSlotSize = elementSlotSize;
        // Get all the texture regions
        TextureAtlas textureAtlas = Core.getInstance().getResourcesManager().getResource(textureName,
                ResourceType.RESOURCE_TYPE_TEXTURE_ATLAS);

        // Add elements
        elements.addAll(textureAtlas.getRegions());
        // Sort elements
        elements.sort(new Comparator<TextureRegion>() {
            @Override
            public int compare(TextureRegion o1, TextureRegion o2) {
                for(String element : elementsOrder)
                {
                    if(o1.toString().equals(element))
                        return -1;
                    else if(o2.toString().equals(element))
                        return 1;
                }
                return 0;
            }
        });
        // Set selected element
        for(TextureRegion region : elements)
        {
            if(region.toString().equals(selectedElement))
            {
                this.selectedElement = region;
                break;
            }
        }

        // Set prices
        this.prices = prices;
        // Get the lock icon texture
        lockIconTexture = Core.getInstance().getResourcesManager().getResource("UI/LockIcon",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

        // Create vectors
        tmpVector = new Vector2();
        tmpVector2 = new Vector2();
        tmpVector3 = new Vector2();
        lockIconSize = new Vector2(elementSlotSize * 0.4f, elementSlotSize * 0.35f);
        // Set the current screen position
        currentScreenPosition = 0f;
        // Calculate the maximum screen position
        maximumScreenPosition = (elementSlotSize * 1.5f) * elementsOrder.length + elementSlotSize * 0.5f;

        // Create the name text
        nameText = new UIText((int)getSize().y / 20, Color.WHITE, 0f, null);
        // Create select buttons
        selectButtons = new Array<UIButton>();
        // Initialize the first position
        tmpVector.set(getPosition()).sub(0f, getSize().y / 2f - elementSlotSize * 1.25f);
        // Initialize size
        tmpVector2.set(elementSlotSize / 3f, elementSlotSize / 3f);
        // Draw texts
        for(int i = 0; i < elements.size; i++)
        {
            // Calculate the item's position
            tmpVector.add(elementSlotSize * 1.5f, 0);
            // Get texture
            TextureRegion textureRegion = Core.getInstance().getResourcesManager().getResource("UI/SelectIcon",
                    ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

            // Create button
            UIButton button = new UIButton(textureRegion);
            // Setup button
            button.setSize(tmpVector2);
            //button.setPosition(tmpVector3.set(tmpVector).add(elementSlotSize / 2f - tmpVector.x / 2f, 0f));
            // Add it to the select buttons
            selectButtons.add(button);
        }
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void process()
    {
        super.process();
        // Reset the select changed flag
        selectChanged = false;
        // If we're dragging then update the current screen position
        if(Core.getInstance().getInputHandler().getScreenTouchType() == ScreenTouchType.SCREEN_TOUCH_DRAGGED)
        {
            // Scroll to the right if possible
            if(Core.getInstance().getInputHandler().getTouchMovementPosition().x < 0f &&
                    currentScreenPosition + Core.getInstance().getGraphicsManager().WIDTH < maximumScreenPosition)
            {
                // Scroll to the right
                currentScreenPosition += -Core.getInstance().getInputHandler().getTouchMovementPosition().x * 1.75f;
                // Don't allow it to exceed the limit
                if(currentScreenPosition + Core.getInstance().getGraphicsManager().WIDTH  > maximumScreenPosition)
                    currentScreenPosition = maximumScreenPosition - Core.getInstance().getGraphicsManager().WIDTH ;
            }
            // Scroll to the right if possible
            else if(Core.getInstance().getInputHandler().getTouchMovementPosition().x > 0f && currentScreenPosition > 0f)
            {
                // Scroll to the right
                currentScreenPosition += -Core.getInstance().getInputHandler().getTouchMovementPosition().x * 1.75f;
                // Don't allow it to exceed the limit
                if(currentScreenPosition < 0f)
                    currentScreenPosition = 0f;
            }
        }
        // Initialize the first position
        tmpVector.set(getPosition()).sub(elementSlotSize, getSize().y / 2f - elementSlotSize * 1.25f);
        // Initialize size
        tmpVector2.set(elementSlotSize, elementSlotSize);
        // Draw texts
        for(int i = 0; i < elements.size; i++)
        {
            TextureRegion region = elements.get(i);
            // Calculate the item's position
            tmpVector.add(elementSlotSize * 1.5f, 0);
            // Should we render this item ?
            if(tmpVector.x + elementSlotSize < currentScreenPosition || tmpVector.x > currentScreenPosition + getSize().x)
                continue;

            // Calculate screen position
            tmpVector3.set(tmpVector).sub(currentScreenPosition, 0f);
            // Draw name
            Vector2 textSize = nameText.getSize(region.toString());
            nameText.drawText(region.toString(), tmpVector3.x + elementSlotSize / 2f - textSize.x / 2f, tmpVector3.y);
            // Draw XP
            if(!elementsState[i])
            {
                textSize = nameText.getSize(prices[i] + " XP");
                nameText.drawText(prices[i] + " XP", tmpVector3.x + elementSlotSize / 2f - textSize.x / 2f,
                        tmpVector3.y - elementSlotSize * 1.5f + textSize.y / 2f);
            }
        }

        // Initialize the first position
        tmpVector.set(getPosition()).sub(elementSlotSize, getSize().y / 2f + selectButtons.first().getSize().y * 1.25f);
        // Update buttons
        for(int i = 0; i < selectButtons.size; i++)
        {
            // Get the button
            UIButton button = selectButtons.get(i);
            // Calculate the item's position
            tmpVector.add(elementSlotSize * 1.5f, 0);
            // Should we render this item ?
            if(tmpVector.x + elementSlotSize < currentScreenPosition || tmpVector.x > currentScreenPosition + getSize().x)
            {
                button.toggle(false);
                continue;
            }
            button.toggle(elementsState[i]);
            // Calculate screen position
            tmpVector3.set(tmpVector).sub(currentScreenPosition - elementSlotSize / 2f + button.getSize().x / 2f, 0f);
            // Is button enabled ?
            if(button.isVisible())
            {
                // Update button's position
                button.setPosition(tmpVector3);
                // Update button
                button.process();
                // Is it clicked ?
                if (button.isClicked()) {
                    // Set selected item
                    selectedElement = elements.get(i);
                    selectChanged = true;
                }
            }
        }
        // Update texts
        nameText.process();
    }

    @Override
    public void render()
    {
        // Initialize the first position
        tmpVector.set(getPosition()).sub(elementSlotSize, getSize().y / 2f);
        // Initialize size
        tmpVector2.set(elementSlotSize, elementSlotSize);
        // Draw the regions
        for(int i = 0; i < elements.size; i++)
        {
            TextureRegion region = elements.get(i);
            // Calculate the item's position
            tmpVector.add(elementSlotSize * 1.5f, 0);
            // Should we render this item ?
            if(tmpVector.x + elementSlotSize < currentScreenPosition || tmpVector.x > currentScreenPosition + getSize().x)
                continue;

            // Calculate screen position
            tmpVector3.set(tmpVector).sub(currentScreenPosition, 0f);
            // Set alpha
            if(!elementsState[i])
                // Set color
                Core.getInstance().getGraphicsManager().setColor(1f, 1f, 1f, 0.3f);

            // Draw the item
            Core.getInstance().getGraphicsManager().drawTextureRegion(region, tmpVector3, tmpVector2, 270f);
            // Restore color
            Core.getInstance().getGraphicsManager().setColor(1f, 1f, 1f, 1f);
            if(!elementsState[i])
                // Draw locked icon
                Core.getInstance().getGraphicsManager().drawTextureRegion(lockIconTexture,
                        tmpVector3.x + elementSlotSize / 2f - lockIconSize.x / 2f, tmpVector3.y + elementSlotSize / 2f - lockIconSize.y / 2f,
                        lockIconSize.x, lockIconSize.y, 270f);

        }
        // Draw buttons
        for(UIButton button : selectButtons)
        {
            if(button.isVisible())
                button.render();
        }
        // Draw texts
        nameText.render();
    }

    public TextureRegion getSelectedElement() {
        return selectedElement;
    }

    public boolean isSelectChanged() {
        return selectChanged;
    }

    public void setSelectedElement(String name) {
        this.selectedElement = selectedElement;
    }

}
