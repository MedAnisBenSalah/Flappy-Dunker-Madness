package com.ormisiclapps.flappydunkermadness.graphics.others;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIButton;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIText;

/**
 * Created by Anis on 11/4/2017.
 */

public class UnlockWindow
{
    private TextureRegion emptyTexture, unlockedItemTexture;
    private UIText nameText, descriptionText;
    private UIButton okButton;
    private Vector2 position, size, itemPosition, itemSize;
    private boolean showen;
    private String name, type;

    public UnlockWindow()
    {
        // Get empty texture
        emptyTexture = Core.getInstance().getResourcesManager().getResource("UI/Empty",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

        // Get width and height
        float w = Core.getInstance().getGraphicsManager().WIDTH;
        float h = Core.getInstance().getGraphicsManager().HEIGHT;
        // Create vectors
        size = new Vector2(w * 0.75f, h * 0.8f);
        position = new Vector2(w / 2f - size.x / 2f, h * 0.1f);
        // Create the ok button
        okButton = new UIButton(true, (TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/OkButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        Vector2 buttonSize = new Vector2(w / 3f, w / 12f);
        okButton.setSize(buttonSize);
        okButton.setPosition(new Vector2(w / 2f - buttonSize.y / 2f, h * 0.2f - buttonSize.x / 2f));
        // Create texts
        nameText = new UIText((int)h / 20, Color.BLACK, 0f, null);
        descriptionText = new UIText((int)h / 30, Color.BLACK, 0f, null);
        // Create vectors
        itemPosition = new Vector2();
        itemSize = new Vector2();
        // Reset flags
        showen = false;
    }

    public boolean process()
    {
        // Process OK button
        okButton.process();
        // Draw texts
        Vector2 textSize = nameText.getSize(name);
        nameText.drawText(name, position.x + size.x / 2f - textSize.x / 2f, position.y + size.y * 0.625f - textSize.y);

        textSize = descriptionText.getSize("New " + type + " unlocked !");
        descriptionText.drawText("New " + type + " unlocked !", position.x + size.x / 2f - textSize.x / 2f,
                position.y + size.y * 0.375f - textSize.y);

        // Update texts
        nameText.process();
        descriptionText.process();
        // Did we press ok ?
        if(okButton.isClicked())
            // Hide
            showen = false;

        return okButton.isClicked();
    }

    public void render()
    {
        // Draw container
        float containerSize = size.x * 0.02f;
        Core.getInstance().getGraphicsManager().setColor(Color.BLACK);
        Core.getInstance().getGraphicsManager().drawTextureRegion(emptyTexture, position.x - containerSize, position.y - containerSize,
                size.x + containerSize * 2f, size.y + containerSize * 2f, 0f);

        Core.getInstance().getGraphicsManager().setColor(Color.WHITE);
        Core.getInstance().getGraphicsManager().drawTextureRegion(emptyTexture, position, size, 0f);
        // Draw OK button
        okButton.render();
        // Draw texts
        nameText.render();
        descriptionText.render();
        // Draw item texture
        Core.getInstance().getGraphicsManager().drawTextureRegion(unlockedItemTexture, itemPosition, itemSize, 270f);
    }

    public void show(TextureRegion texture, Vector2 size, String name, String type)
    {
        // Set the position and size
        itemSize.set(this.size.x * size.x, this.size.x * size.y);
        itemPosition.set(this.position.x + this.size.x / 2f - itemSize.x / 2f, this.position.y + this.size.y * 0.75f - itemSize.y / 2f);
        // Set the texture
        unlockedItemTexture = texture;
        // Set name and type
        this.name = name;
        this.type = type;
        // Set showen flag
        showen = true;
    }

    public boolean isShowen() {
        return showen;
    }
}
