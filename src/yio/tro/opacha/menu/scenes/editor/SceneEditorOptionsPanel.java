package yio.tro.opacha.menu.scenes.editor;

import yio.tro.opacha.game.Difficulty;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.gameplay.EditorWorker;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.loading.LoadingParameters;
import yio.tro.opacha.game.loading.LoadingType;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.CheckButtonYio;
import yio.tro.opacha.menu.elements.LightBottomPanelElement;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.elements.slider.SliderBehavior;
import yio.tro.opacha.menu.elements.slider.SliderYio;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.ModalSceneYio;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RepeatYio;

public class SceneEditorOptionsPanel extends ModalSceneYio {


    private SliderYio difficultySlider;
    int rgConfirmCounter;
    RepeatYio<SceneEditorOptionsPanel> repeatDecreaseConfirmCounter;
    private LightBottomPanelElement lightBottomPanelElement;
    private CheckButtonYio chkAiOnly;


    public SceneEditorOptionsPanel() {
        initRepeats();
    }


    private void initRepeats() {
        repeatDecreaseConfirmCounter = new RepeatYio<SceneEditorOptionsPanel>(this, 60) {
            @Override
            public void performAction() {
                parent.decreaseConfirmCounter();
            }
        };
    }


    @Override
    protected void initialize() {
        createCloseButton();
        createLightBottomPanel();
        createInternals();
    }


    private void createLightBottomPanel() {
        lightBottomPanelElement = uiFactory.getLightBottomPanelElement()
                .setSize(1, 0.5)
                .setTitle("settings")
                .setAnimation(AnimationYio.down);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        yioGdxGame.gameController.resetTouchMode();
        rgConfirmCounter = 0;
        loadValues();
    }


    private void loadValues() {
        difficultySlider.setValueIndex(GameRules.difficulty.ordinal());
        chkAiOnly.setChecked(GameRules.aiOnlyMode);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        applyValues();
    }


    private void applyValues() {
        GameRules.difficulty = Difficulty.values()[difficultySlider.getValueIndex()];
        GameRules.aiOnlyMode = chkAiOnly.isChecked();
    }


    private void createInternals() {
        difficultySlider = uiFactory.getSlider()
                .setParent(lightBottomPanelElement)
                .setSize(0.7)
                .alignTop(0.08)
                .setAnimation(AnimationYio.none)
                .centerHorizontal()
                .setName("difficulty")
                .setValues(0, Difficulty.values().length)
                .setSegmentsVisible(true)
                .setTitleDownwordsOffset(GraphicsYio.convertToWidth(0.03))
                .setBehavior(getDifficultySliderBehavior());

        chkAiOnly = uiFactory.getCheckButton()
                .setParent(lightBottomPanelElement)
                .setWidth(0.8)
                .centerHorizontal()
                .alignBottom(previousElement, 0.05)
                .setName("ai_only");

        uiFactory.getButton()
                .setParent(lightBottomPanelElement)
                .setSize(0.7, 0.055)
                .alignBottom(0.059)
                .centerHorizontal()
                .setBackgroundEnabled(true)
                .setGroundIndex(GroundIndex.BUTTON_WHITE)
                .setAnimation(AnimationYio.none)
                .applyText("export")
                .setReaction(getExportReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignTop(previousElement, 0.01)
                .applyText("random_generation")
                .setReaction(getRandomGenerationReaction());
    }


    private Reaction getExportReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                onExportButtonPressed();
            }
        };
    }


    void onExportButtonPressed() {
        ObjectsLayer objectsLayer = yioGdxGame.gameController.objectsLayer;
        objectsLayer.exportManager.performToClipboard();
        Scenes.notification.show("exported");
        destroy();
    }


    private Reaction getRandomGenerationReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                onRandomGenerationButtonPressed();
            }
        };
    }


    @Override
    public void move() {
        super.move();
        repeatDecreaseConfirmCounter.move();
    }


    private void decreaseConfirmCounter() {
        if (rgConfirmCounter <= 0) return;
        rgConfirmCounter--;
    }


    void onRandomGenerationButtonPressed() {
        rgConfirmCounter += 3;

        if (rgConfirmCounter < 4) {
            Scenes.notification.show("tap_again_to_confirm");
            return;
        }

        EditorWorker editorWorker = yioGdxGame.gameController.objectsLayer.editorWorker;
        editorWorker.onRandomGenerationRequested();
        Scenes.notification.destroy();
        destroy();
        rgConfirmCounter = 0;
    }


    private SliderBehavior getDifficultySliderBehavior() {
        return new SliderBehavior() {
            @Override
            public String getValueString(LanguagesManager languagesManager, SliderYio sliderYio) {
                Difficulty difficulty = Difficulty.values()[sliderYio.getValueIndex()];
                return LanguagesManager.getInstance().getString("" + difficulty);
            }
        };
    }
}
