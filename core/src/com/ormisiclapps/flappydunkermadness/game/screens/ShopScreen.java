package com.ormisiclapps.flappydunkermadness.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ormisiclapps.flappydunkermadness.core.Core;
import com.ormisiclapps.flappydunkermadness.enumerations.ResourceType;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.Player;
import com.ormisiclapps.flappydunkermadness.game.entities.physical.base.GameObject;
import com.ormisiclapps.flappydunkermadness.game.world.Terrain;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIButton;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIMain;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIPicker;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIProgressBar;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UITabView;
import com.ormisiclapps.flappydunkermadness.graphics.ui.UIText;

/**
 * Created by OrMisicL on 7/27/2017.
 */

public class ShopScreen implements Screen
{
    private UITabView tabView;
    private UIPicker ballsPicker, wingsPicker, hoopsPicker;
    private UIButton backButton;
    private UIProgressBar elementsProgressBar;
    private UIText elementsText;
    private int unlockedElements, elementsCount, elementsProgress;
    private Vector2 hoopPosition, hoopSize;

    public static final String[] ballsOrder = { "Classic", "Citrus", "IceBlue", "UltraViolet", "Reddish", "Raspberry", "Lime",
            "Heuron", "Sahnoun", "Beff", "Allouch", "Tosh", "Inversed", "Football", "Tennisball", "Bownlingball", "Poolball",
            "Beachball", "Baseball", "YinYang", "Blackhole", "Wheel", "Pizza", "Eye", "Earth", "Sun"};

    public static final String[] wingsOrder = { "Classic", "Citrus", "UltraViolet", "Raspberry", "Reddish", "Lime", "Mrabet",
            "Pezkz", "MrGreeN", "Mesh", "Zheni", "Bisi", "Chouchou" };

    public static final String[] hoopsOrder = { "Classic", "Citrus", "Lime", "UltraViolet", "Raspberry", "Inversed" };

    public static final long[] ballsPrices = { 0, 10, 50, 100, 200, 500, 750, 1000, 1500, 2000, 2750, 4500, 5500, 7500, 9000, 10500,
        13000, 15000, 20000, 25000, 30000, 40000, 50000, 70000, 100000, 150000};

    public static final long[] wingsPrices = { 0, 40, 100, 300, 600, 1000, 2500, 5000, 10000, 15000, 22000, 30000, 45000 };
    public static final long[] hoopsPrices = { 0, 100, 500, 750, 2000, 7500 };

    @Override
    public void initialize()
    {
        // Create the tab view
        tabView = new UITabView(new Vector2(), new Vector2(Core.getInstance().getGraphicsManager().WIDTH,
                Core.getInstance().getGraphicsManager().HEIGHT * 0.85f));

        // Add tabs
        tabView.addTab("Balls");
        tabView.addTab("Wings");
        tabView.addTab("Hoops");

        // Create the balls picker
        ballsPicker = new UIPicker(new Vector2(0f, tabView.getSize().y / 2f), new Vector2(Core.getInstance().getGraphicsManager().WIDTH, tabView.getSize().y / 2f),
                "Balls", Core.getInstance().getStatsSaver().savedData.ball, ballsOrder, tabView.getSize().y / 7f,
                Core.getInstance().getStatsSaver().savedData.ballsStates, ballsPrices);

        // Create the wings picker
        wingsPicker = new UIPicker(new Vector2(0f, tabView.getSize().y / 2f), new Vector2(Core.getInstance().getGraphicsManager().WIDTH, tabView.getSize().y / 2f),
                "Wings", Core.getInstance().getStatsSaver().savedData.wing, wingsOrder, tabView.getSize().y / 7f,
                Core.getInstance().getStatsSaver().savedData.wingsStates, wingsPrices);

        // Create the hoops picker
        hoopsPicker = new UIPicker(new Vector2(0f, tabView.getSize().y / 2f), new Vector2(Core.getInstance().getGraphicsManager().WIDTH, tabView.getSize().y / 2f),
                "Hoops", Core.getInstance().getStatsSaver().savedData.hoop, hoopsOrder, tabView.getSize().y / 7f,
                Core.getInstance().getStatsSaver().savedData.wingsStates, hoopsPrices);

        // Add tab view items
        tabView.addElement("Balls", ballsPicker);
        tabView.addElement("Wings", wingsPicker);
        tabView.addElement("Hoops", hoopsPicker);

        // Create the back button
        backButton = new UIButton((TextureRegion)Core.getInstance().getResourcesManager().getResource("UI/NextButton",
                ResourceType.RESOURCE_TYPE_TEXTURE_REGION));

        float buttonSize = Core.getInstance().getGraphicsManager().HEIGHT / 17f;
        backButton.setRotation(90f);
        backButton.setSize(new Vector2(buttonSize, buttonSize));
        backButton.setPosition(new Vector2(buttonSize * 0.25f, Core.getInstance().getGraphicsManager().HEIGHT * 0.925f - buttonSize / 2f));
        backButton.setFadeEffected(false);

        // Create the elements progress bar
        Vector2 size = new Vector2(Core.getInstance().getGraphicsManager().WIDTH / 3f,
                Core.getInstance().getGraphicsManager().HEIGHT / 30f);

        elementsProgressBar = new UIProgressBar(new Vector2(Core.getInstance().getGraphicsManager().WIDTH / 2f - size.x / 2f,
                Core.getInstance().getGraphicsManager().HEIGHT * 0.925f - size.y / 2f), size, Color.BLACK, Color.WHITE, Color.GREEN);

        elementsProgressBar.setProgress(50);
        elementsProgressBar.setFadeEffected(false);

        // Create the elements text
        elementsText = new UIText((int)size.y, Color.BLACK, 0f, null);
        elementsText.setFadeEffected(false);

        // Create hoop position and size vectors
        hoopSize = new Vector2(Core.getInstance().getGraphicsManager().HEIGHT / 18f,
                Core.getInstance().getGraphicsManager().HEIGHT / 5f);

        hoopPosition = new Vector2(Core.getInstance().getGraphicsManager().WIDTH / 2f - hoopSize.x / 2f,
                Core.getInstance().getGraphicsManager().HEIGHT / 2.3f - hoopSize.y / 2f);

        // Add elements to the main ui
        UIMain.getInstance().addWidget(backButton);
        UIMain.getInstance().addWidget(elementsProgressBar);
        UIMain.getInstance().addWidget(elementsText);
        // Update progress
        updateProgress();
    }

    @Override
    public void activate()
    {
        // Restore player's rotation
        Player.getInstance().setRotationInDegrees(0f);
        // Update progress
        updateProgress();
        // Enable UI
        backButton.toggle(true);
        elementsProgressBar.toggle(true);
        elementsText.toggle(true);
    }

    @Override
    public void deactivate()
    {
        // Disable UI
        backButton.toggle(false);
        elementsProgressBar.toggle(false);
        elementsText.toggle(false);
    }

    @Override
    public void update()
    {
        // Did we click back ?
        if(backButton.isClicked())
            Core.getInstance().getScreensManager().setMainMenuScreen();

        // Update terrain
        Terrain.getInstance().process();
        // Update tab view
        tabView.process();
        // Is select changed ?
        if(tabView.getSelectedTab().equals("Balls"))
        {
            Player.getInstance().setSkin(ballsPicker.getSelectedElement().toString());
            // Set selected element
            Core.getInstance().getStatsSaver().savedData.ball = ballsPicker.getSelectedElement().toString();
            Core.getInstance().getStatsSaver().save();
        }
        else if(tabView.getSelectedTab().equals("Wings"))
        {
            Player.getInstance().setWingSkin(wingsPicker.getSelectedElement().toString());
            // Set selected element
            Core.getInstance().getStatsSaver().savedData.wing = wingsPicker.getSelectedElement().toString();
            Core.getInstance().getStatsSaver().save();
        }
        else if(tabView.getSelectedTab().equals("Hoops"))
        {
            Terrain.getInstance().setHoopsTexture(hoopsPicker.getSelectedElement().toString());
            // Set selected element
            Core.getInstance().getStatsSaver().savedData.hoop = hoopsPicker.getSelectedElement().toString();
            Core.getInstance().getStatsSaver().save();
        }
        // Update tab if necessary
        if(tabView.isSelectChanged())
            // Update progress
            updateProgress();

        // Set progress bar
        elementsProgressBar.setProgress(elementsProgress);
        // Draw elements count
        Vector2 textSize = elementsText.getSize(unlockedElements + "/" + elementsCount);
        elementsText.drawText(unlockedElements + "/" + elementsCount, elementsProgressBar.getPosition().x + elementsProgressBar.getSize().x / 2f - textSize.x / 2f,
                elementsProgressBar.getPosition().y + elementsProgressBar.getSize().y / 2f + textSize.y / 2f);

        // Draw XP
        long xp = Core.getInstance().getStatsSaver().savedData.xp;
        textSize = elementsText.getSize(xp + " XP");
        elementsText.drawText(xp + " XP", elementsProgressBar.getPosition().x + elementsProgressBar.getSize().x / 2f - textSize.x / 2f,
                elementsProgressBar.getPosition().y - elementsProgressBar.getSize().y + textSize.y / 2f);
    }

    @Override
    public void render()
    {
        // Draw terrain
        Terrain.getInstance().render();
    }

    @Override
    public void postFadeRender()
    {
        // Render tab view
        tabView.render();
        // Draw the player
        Player.getInstance().render();
        // Draw the hoop
        Core.getInstance().getGraphicsManager().drawTextureRegion(hoopsPicker.getSelectedElement(), hoopPosition, hoopSize, 270f);
    }

    @Override
    public void dispose() {

    }

    private void updateProgress()
    {
        unlockedElements = 0;
        elementsCount = 0;
        if(tabView.getSelectedTab().equals("Balls"))
        {
            elementsCount = Core.getInstance().getStatsSaver().savedData.ballsStates.length;
            for(boolean state : Core.getInstance().getStatsSaver().savedData.ballsStates)
            {
                if(state)
                    unlockedElements++;
            }
        }
        else if(tabView.getSelectedTab().equals("Wings"))
        {
            elementsCount = Core.getInstance().getStatsSaver().savedData.wingsStates.length;
            for(boolean state : Core.getInstance().getStatsSaver().savedData.wingsStates)
            {
                if(state)
                    unlockedElements++;
            }
        }
        else
        {
            elementsCount = Core.getInstance().getStatsSaver().savedData.hoopsStates.length;
            for(boolean state : Core.getInstance().getStatsSaver().savedData.hoopsStates)
            {
                if(state)
                    unlockedElements++;
            }
        }
        elementsProgress = (int)((float)unlockedElements / (float)elementsCount * 100f);
    }
}
