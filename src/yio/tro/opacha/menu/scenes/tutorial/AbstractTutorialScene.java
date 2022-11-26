package yio.tro.opacha.menu.scenes.tutorial;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.CircleButtonYio;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.GraphicsYio;

public abstract class AbstractTutorialScene extends SceneYio{


    public ButtonYio label;
    public CircleButtonYio nextButton;


    @Override
    protected void initialize() {
        setBackground(getGroundIndex());

        createBackButton();
        createLabel();
        createNextButton();
    }


    private void createNextButton() {
        nextButton = uiFactory.getCircleButton()
                .setParent(label)
                .setSize(GraphicsYio.convertToWidth(0.09))
                .alignRight(0.04)
                .alignBottom(0.02)
                .setTouchOffset(0.05)
                .loadTexture("menu/forward_icon.png")
                .setAnimation(AnimationYio.none)
                .setReaction(getNextSceneReaction());
    }


    private void createLabel() {
        label = uiFactory.getButton()
                .setSize(0.9, 0.45)
                .centerHorizontal()
                .alignBottom(0.22)
                .setFont(Fonts.smallFont)
                .setTouchable(false)
                .applyManyLines(getHintKey());
    }


    private void createBackButton() {
        spawnBackButton(new Reaction() {
            @Override
            protected void reaction() {
                Scenes.chooseGameMode.create();
            }
        });
    }


    protected abstract String getHintKey();


    protected abstract int getGroundIndex();


    protected abstract Reaction getNextSceneReaction();
}
