package com.ormisiclapps.flappydunkermadness.graphics.others;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIButton;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIText;

/**
 * Created by OrMisicL on 11/18/2017.
 */

public class RatingDialog
{
    private TextureRegion emptyTexture;
    private UIText descriptionText, titleText;
    private UIButton rateButton, cancelButton;
    private Vector2 position, size;
    private boolean showen;

    public RatingDialog()
    {
        // Get empty texture
        emptyTexture = Core.getInstance().getResourcesManager().getResource("UI/Empty",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION);

        // Get width and height
        float w = Core.getInstance().getGraphicsManager().WIDTH;
        float h = Core.getInstance().getGraphicsManager().HEIGHT;
        // Create vectors
        size = new Vector2(w * 0.75f, h * 0.4f);
        position = new Vector2(w / 2f - size.x / 2f, h / 2f - size.y / 2f);
        // Create buttons
        rateButton = new UIButton(true, (TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/ConfirmRateButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        cancelButton = new UIButton(true, (TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/CancelButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        Vector2 buttonSize = new Vector2(w / 3f, w / 12f);
        rateButton.setSize(buttonSize);
        rateButton.setPosition(new Vector2(w * 0.7f - buttonSize.y / 2f, position.y + size.y * 0.2f - buttonSize.x / 2f));

        cancelButton.setSize(buttonSize);
        cancelButton.setPosition(new Vector2(w * 0.3f - buttonSize.y / 2f, position.y + size.y * 0.2f - buttonSize.x / 2f));
        // Create texts
        descriptionText = new UIText((int)h / 55, Color.BLACK, 0f, null);
        titleText = new UIText((int)h / 40, Color.BLACK, 0f, null);
        // Reset flags
        showen = false;
    }

    public boolean process()
    {
        // Process buttons
        rateButton.process();
        cancelButton.process();
        // Draw texts
        Vector2 textSize = descriptionText.getSize("If you enjoy \"Flappy Dunker Madness\" \n please take a moment and rate it.");
        descriptionText.drawText("If you enjoy \"Flappy Dunker Madness\" \n please take a moment and rate it.",
                position.x + size.x / 2f - textSize.x / 2f, position.y + size.y * 0.6f - textSize.y);

        textSize = titleText.getSize("Rate the game");
        titleText.drawText("Rate the game",
                position.x + size.x / 2f - textSize.x / 2f, position.y + size.y * 0.8f - textSize.y);

        // Update texts
        descriptionText.process();
        titleText.process();
        // Did we press rate ?
        if(rateButton.isClicked())
        {
            // Hide
            showen = false;
            // Open the market
            Core.getInstance().getOSUtility().rateGame();
            // Save the choice
            //Core.getInstance().getStatsSaver().savedData.neverRate = true;
            //Core.getInstance().getStatsSaver().save();
        }
        else if(cancelButton.isClicked())
            // Hide
            showen = false;

        return rateButton.isClicked() || cancelButton.isClicked();
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
        // Draw buttons
        rateButton.render();
        cancelButton.render();
        // Draw texts
        descriptionText.render();
        titleText.render();
    }

    public void show()
    {
        // Set showen flag
        showen = true;
    }

    public boolean isShowen() {
        return showen;
    }
}
