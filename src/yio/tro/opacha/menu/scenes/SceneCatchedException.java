package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;

public class SceneCatchedException extends SceneYio{

    Exception exception;
    private ButtonYio label;
    private ButtonYio showMoreButton;


    @Override
    protected void initialize() {
        setBackground(GroundIndex.MAGENTA);

        label = uiFactory.getButton()
                .setSize(0.95, 0.4)
                .centerHorizontal()
                .centerVertical()
                .setFont(Fonts.miniFont)
                .applyManyLines("catched_exception", 2)
                .setAnimation(AnimationYio.none)
                .setTouchable(false);

        showMoreButton = uiFactory.getButton()
                .setParent(label)
                .setSize(0.3, 0.05)
                .setTouchOffset(0.05)
                .centerHorizontal()
                .alignBottom(0.01)
                .applyText("show_more")
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        Scenes.exceptionReport.createReport(exception);
                    }
                })
                .tagAsBackButton();
    }


    public void setException(Exception exception) {
        this.exception = exception;
    }
}
