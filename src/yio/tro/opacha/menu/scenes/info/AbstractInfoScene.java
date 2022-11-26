package yio.tro.opacha.menu.scenes.info;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.CircleButtonYio;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.stuff.RectangleYio;

public abstract class AbstractInfoScene extends SceneYio {


    protected ButtonYio infoPanel;
    protected CircleButtonYio backButton;
    protected RectangleYio infoLabelPosition;


    protected void initInfoLabelPosition() {
        infoLabelPosition = new RectangleYio(0.05, 0.1, 0.9, 0.7);
    }


    protected void createInfoMenu(String key, Reaction backButtonBehavior, int lines, int background_id) {
        setBackground(background_id);

        backButton = spawnBackButton(backButtonBehavior);

        initInfoLabelPosition();
        infoPanel = uiFactory.getButton()
                .setPosition(infoLabelPosition.x, infoLabelPosition.y, infoLabelPosition.width, infoLabelPosition.height)
                .setTouchable(false)
                .setAnimation(AnimationYio.none)
                .setFont(Fonts.miniFont)
                .clearText()
                .applyFixedAmountOfLines(key, lines);
    }
}
