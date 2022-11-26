package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.AdaptiveDifficultyManager;
import yio.tro.opacha.Fonts;
import yio.tro.opacha.SettingsManager;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;

import java.util.ArrayList;

public class SceneConfirmRestart extends SceneYio{


    private ButtonYio label;


    @Override
    protected void initialize() {
        setBackground(GroundIndex.MAGENTA);

        ArrayList<String> lines = new ArrayList<>();
        lines.add(LanguagesManager.getInstance().getString("confirm_restart"));
        lines.add(" ");

        label = uiFactory.getButton()
                .setSize(0.8, 0.2)
                .centerHorizontal()
                .centerVertical()
                .applyText(lines)
                .setTouchable(false);

        uiFactory.getButton()
                .setParent(label)
                .setSize(0.35, 0.06)
                .alignBottom()
                .alignLeft(0.05)
                .setBorder(false)
                .setBackgroundEnabled(false)
                .applyText("no")
                .setFont(Fonts.miniFont)
                .tagAsBackButton()
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        Scenes.pauseMenu.create();
                    }
                });

        uiFactory.getButton()
                .clone(previousElement)
                .alignRight(0.05)
                .applyText("yes")
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        applyRestart();
                    }
                });
    }


    private void applyRestart() {
        yioGdxGame.loadingManager.startInstantly(GameRules.initialLoadingType, GameRules.initialParameters);

        if (SettingsManager.getInstance().adaptiveDifficulty) {
            AdaptiveDifficultyManager.getInstance().decrease();
        }
    }
}
