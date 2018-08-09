package com.ormisiclapps.flappydunkermadness.graphics.others;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIButton;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIText;
import com.ormisiclapps.flappydunkermadness.utility.GameMath;

/**
 * Created by Anis on 10/15/2017.
 */

public class GameModeContainer
{
    private int id;
    private String name;
    private Vector2 position, size;
    private UIButton playButton;
    private UIText scoreText, scoreNumberText, lastScoreText, newRecordText;
    private float currentScreenPosition;
    private boolean newRecord;

    public GameModeContainer(int id, String name, UIText scoreText, UIText scoreNumberText, UIText lastScoreText, UIText newRecordText)
    {
        this.id = id;
        this.name = name;
        this.scoreText = scoreText;
        this.scoreNumberText = scoreNumberText;
        this.lastScoreText = lastScoreText;
        this.newRecordText = newRecordText;
        // Create the play button
        playButton = new UIButton((TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/PlayButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        // Get height and width
        float w = Core.getInstance().getGraphicsManager().WIDTH;
        float h = Core.getInstance().getGraphicsManager().HEIGHT;
        // Set their positions
        float size = Core.getInstance().getGraphicsManager().WIDTH / 2f;
        playButton.setSize(new Vector2(size, size));
        playButton.setPosition(new Vector2(GameMath.getCenteredPosition(new Vector2(id * w + w / 2f, h / 1.75f),
                new Vector2(size, size))));

        // Create the position vector
        position = new Vector2(w * id, h * 0.25f);
        // Create the size vectors
        this.size = new Vector2(Core.getInstance().getGraphicsManager().WIDTH, Core.getInstance().getGraphicsManager().HEIGHT * 0.75f);
        // Reset flags
        newRecord = false;
    }

    public boolean update(float currentScreenPosition)
    {
        // Update the container's position if necessary
        if(this.currentScreenPosition != currentScreenPosition)
        {
            // Calculate the new position factor
            float newPositionFactor = this.currentScreenPosition - currentScreenPosition;
            // Update our position
            position.add(newPositionFactor, 0f);
            // Update widgets
            playButton.setPosition(playButton.getPosition().add(newPositionFactor, 0f));
            // Update the current screen position
            this.currentScreenPosition = currentScreenPosition;
        }
        // If it's not on screen then don't update
        if (!isOnScreen())
            return false;

        // Get height and width
        float w = Core.getInstance().getGraphicsManager().WIDTH;
        float h = Core.getInstance().getGraphicsManager().HEIGHT;
        // Draw score text
        long bestScore = Core.getInstance().getStatsSaver().savedData.bestScores[id];
        long lastScore = Core.getInstance().getStatsSaver().savedData.lastScores[id];

        Vector2 textSize = scoreText.getSize("Best: " + bestScore);
        scoreText.drawText("Best: ", position.x + w / 2f - textSize.x / 2f, h * 0.9f + textSize.y / 2f);

        scoreNumberText.drawText("             " + bestScore,  position.x + w / 2f - textSize.x / 2f, h * 0.9f + textSize.y / 2f);

        textSize = lastScoreText.getSize("Last: " + lastScore);
        lastScoreText.drawText("Last: " + lastScore, position.x + w / 2f - textSize.x / 2f, h * 0.825f + textSize.y / 2f);

        if(newRecord)
        {
            textSize = newRecordText.getSize("NEW BEST SCORE!");
            newRecordText.drawText("NEW BEST SCORE!", position.x + w / 2f - textSize.x / 2f, h * 0.775f + textSize.y / 2f);
        }
        // Draw the game mode
        textSize = lastScoreText.getSize(name + " mode");
        lastScoreText.drawText(name + " mode", position.x + w / 2f - textSize.x / 2f, h * 0.325f + textSize.y / 2f);
        // Update widgets
        playButton.process();
        // Did we click the button ?
        return playButton.isClicked();
    }

    public void render()
    {
        // Only render if we're visible on screen
        if(!isOnScreen())
            return;

        // Draw widgets
        playButton.render();
    }

    private boolean isOnScreen()
    {
        return position.x <= currentScreenPosition || position.x + size.x >= currentScreenPosition;
    }

    public void setNewRecord(boolean newRecord) {
        this.newRecord = newRecord;
    }

    public UIButton getPlayButton() { return playButton; }
}
