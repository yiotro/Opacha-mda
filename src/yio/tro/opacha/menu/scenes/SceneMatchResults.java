package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.game.gameplay.model.MatchResults;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.stuff.factor_yio.FactorYio;

import java.util.ArrayList;

public class SceneMatchResults extends ModalSceneYio{

    MatchResults matchResults;
    private ArrayList<String> lines;
    FactorYio waitFactor;


    @Override
    protected void initialize() {
        lines = new ArrayList<>();
        matchResults = null;
        waitFactor = new FactorYio();
        createCloseButton();
        updateBackReaction();
        createDefaultLabel(0.8, 0.3);
    }


    private void updateDefaultLabelText() {
        lines.clear();
        lines.add(LanguagesManager.getInstance().getString(getTitleKey()));
        lines.add(getWinnerColorLine());
        lines.add(" ");

        defaultLabel.applyText(lines);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        waitFactor.reset();
        waitFactor.appear(0, 0.18);
    }


    @Override
    public void move() {
        super.move();
        moveWaitFactor();
    }


    private void moveWaitFactor() {
        if (!waitFactor.move()) return;
        if (waitFactor.get() < 1) return;
        getBackReaction().performReactActions(menuControllerYio);
    }


    private String getWinnerColorLine() {
        String colorString = LanguagesManager.getInstance().getString(matchResults.winner + "");
        return LanguagesManager.getInstance().getString("winner") + ": " + colorString;
    }


    private String getTitleKey() {
        if (matchResults.humanWon) {
            return "player_won";
        } else {
            return "ai_won";
        }
    }


    private void updateBackReaction() {
        closeButton.setReaction(null);
    }


    private Reaction getBackReaction() {
        if (matchResults != null && matchResults.campaignMode) {
            return new Reaction() {
                @Override
                protected void reaction() {
                    yioGdxGame.setGamePaused(true);
                    menuControllerYio.destroyGameView();
                    Scenes.onCampaignLevelCompleted.create();
                    Scenes.onCampaignLevelCompleted.setCurrentLevelIndex(matchResults.levelIndex);
                }
            };
        }

        return new Reaction() {
            @Override
            protected void reaction() {
                yioGdxGame.setGamePaused(true);
                menuControllerYio.destroyGameView();
                Scenes.chooseGameMode.create();
            }
        };
    }


    public void setMatchResults(MatchResults matchResults) {
        this.matchResults = matchResults;
        updateDefaultLabelText();
        updateBackReaction();
    }

}
