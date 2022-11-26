package yio.tro.opacha.game.tutorial;

import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.GameMode;
import yio.tro.opacha.game.gameplay.IGameplayManager;
import yio.tro.opacha.stuff.RepeatYio;

import java.util.ArrayList;
import java.util.Arrays;

public class ScriptManager implements IGameplayManager{

    GameController gameController;
    ArrayList<ScriptYio> scripts;
    RepeatYio<ScriptManager> repeatPerform;


    public ScriptManager(GameController gameController) {
        this.gameController = gameController;
        scripts = new ArrayList<>();
        initRepeats();
    }


    private void initRepeats() {
        repeatPerform = new RepeatYio<ScriptManager>(this, 6) {
            @Override
            public void performAction() {
                parent.perform();
            }
        };
    }


    @Override
    public void defaultValues() {
        scripts.clear();
    }


    @Override
    public void onEndCreation() {
        if (gameController.gameMode != GameMode.tutorial) return;
        TutorialManager tutorialManager = gameController.yioGdxGame.tutorialManager;
        ScriptYio[] scriptsArray = tutorialManager.getScriptsArray();
        scripts.addAll(Arrays.asList(scriptsArray));
    }


    void perform() {
        if (scripts.size() == 0) return;
        ScriptYio firstScript = scripts.get(0);
        if (!firstScript.applied) {
            checkToApply(firstScript);
        } else {
            checkToRemove(firstScript);
        }
    }


    private void checkToRemove(ScriptYio firstScript) {
        if (firstScript.isActive()) return;
        scripts.remove(firstScript);
    }


    private void checkToApply(ScriptYio firstScript) {
        if (!firstScript.isReady()) return;
        firstScript.applied = true;
        firstScript.setYioGdxGame(gameController.yioGdxGame);
        firstScript.apply();
    }


    @Override
    public void moveActually() {

    }


    @Override
    public void moveVisually() {
        repeatPerform.move();
    }
}
