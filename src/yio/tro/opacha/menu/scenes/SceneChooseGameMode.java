package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;

public class SceneChooseGameMode extends SceneYio {


    public ButtonYio skirmishButton;


    @Override
    public void initialize() {
        setBackground(GroundIndex.CYAN);

        skirmishButton = uiFactory.getButton()
                .setSize(0.8, 0.08)
                .setPosition(0.1, 0.57)
                .applyText("skirmish")
                .setReaction(getSkirmishReaction())
                .setAnimation(AnimationYio.none);

        uiFactory.getButton()
                .clone(previousElement)
                .alignBottom(previousElement, 0.02)
                .applyText("editor")
                .setReaction(getEditorReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignBottom(previousElement, 0.02)
                .applyText("calendar")
                .setReaction(getCalendarReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignBottom(previousElement, 0.02)
                .applyText("campaign")
                .setReaction(getCampaignReaction());

        spawnBackButton(getBackReaction());
    }


    private Reaction getEditorReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.editorLobby.create();
            }
        };
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                yioGdxGame.setGamePaused(true);
                Scenes.mainMenu.create();
            }
        };
    }


    private Reaction getCampaignReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.campaign.create();
            }
        };
    }


    private Reaction getCalendarReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.calendar.create();
            }
        };
    }


    private Reaction getSkirmishReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.skirmishMenu.create();
            }
        };
    }


}
