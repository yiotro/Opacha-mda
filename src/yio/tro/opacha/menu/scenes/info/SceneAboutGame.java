package yio.tro.opacha.menu.scenes.info;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.PlatformType;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.scenes.Scenes;

public class SceneAboutGame extends AbstractInfoScene {


    private ButtonYio helpButton;
    private ButtonYio specialThanksButton;


    @Override
    protected void initInfoLabelPosition() {
        super.initInfoLabelPosition();
        infoLabelPosition.y = 0.05f;
        infoLabelPosition.height = 0.8f;
    }


    @Override
    public void initialize() {
        createInfoMenu("about_game", getBackReaction(), 16, 1);

//        createHelpButton();
        createSpecialThanksButton();
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                if (YioGdxGame.platformType == PlatformType.ios) {
                    Scenes.mainMenu.create();
                    return;
                }

                yioGdxGame.setGamePaused(true);
                Scenes.settingsMenu.create();
            }
        };
    }


    private void createSpecialThanksButton() {
        specialThanksButton = uiFactory.getButton()
                .setSize(0.45, 0.05)
                .setParent(infoPanel)
                .centerHorizontal()
                .alignBottom(0.01)
                .setFont(Fonts.miniFont)
                .applyText("special_thanks_title")
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        Scenes.specialThanks.create();
                    }
                });
    }


    private void createHelpButton() {
        helpButton = uiFactory.getButton()
                .setPosition(0.55, 0.9, 0.4, 0.07)
                .applyText("help")
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {

                    }
                })
                .setAnimation(AnimationYio.up);
    }
}
