package yio.tro.opacha.menu.scenes.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.loading.LoadingParameters;
import yio.tro.opacha.game.loading.LoadingType;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.elements.*;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.elements.slider.SliderBehavior;
import yio.tro.opacha.menu.elements.slider.SliderYio;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.GraphicsYio;

public class SceneSkirmishMenu extends SceneYio {


    public ButtonYio startButton;
    private ButtonYio label;
    private SliderYio levelSizeSlider;
    private SliderYio difficultySlider;
    private ButtonYio innerParent;
    private int colorValues[];
    private SliderYio colorsSlider;
    private CheckButtonYio chkAiOnly;


    @Override
    protected void initialize() {
        setBackground(GroundIndex.YELLOW);

        initColorsValues();
        createBackButton();
        createStartButton();
        createInnerParent();
        createLabel();
        createSliders();
        createChecks();

        loadValues();
    }


    private void initColorsValues() {
        colorValues = new int[]{2, 3, 4};
    }


    private void createBackButton() {
        spawnBackButton(new Reaction() {
            @Override
            protected void reaction() {
                saveValues();
                Scenes.chooseGameMode.create();
            }
        });
    }


    private void createInnerParent() {
        innerParent = uiFactory.getButton()
                .setSize(1, 0.9)
                .centerHorizontal()
                .alignBottom()
                .setAnimation(AnimationYio.none)
                .setRenderable(false)
                .setTouchable(false);
    }


    private void createLabel() {
        label = uiFactory.getButton()
                .setParent(innerParent)
                .setSize(0.8, 0.55)
                .centerHorizontal()
                .centerVertical()
                .applyText(" ")
                .setTouchable(false)
                .setAnimation(AnimationYio.none);
    }


    private void createSliders() {
        levelSizeSlider = uiFactory.getSlider()
                .setParent(label)
                .setSize(0.65)
                .alignTop(0.03)
                .setAnimation(AnimationYio.none)
                .centerHorizontal()
                .setName("level_size")
                .setValues(0, 5)
                .setSegmentsVisible(true)
                .setBehavior(new SliderBehavior() {
                    @Override
                    public String getValueString(LanguagesManager languagesManager, SliderYio sliderYio) {
                        return "" + convertSliderIndexIntoLevelSize(sliderYio);
                    }
                });

        difficultySlider = uiFactory.getSlider()
                .clone(previousElement)
                .alignBottom(previousElement, 0.05)
                .setName("difficulty")
                .setAnimation(AnimationYio.none)
                .setValues(0.25, 3)
                .setBehavior(new SliderBehavior() {
                    @Override
                    public String getValueString(LanguagesManager languagesManager, SliderYio sliderYio) {
                        return convertSliderIndexIntoDifficultyString(languagesManager, sliderYio);
                    }
                })
                .setVerticalTouchOffset(0.1f * GraphicsYio.width);

        colorsSlider = uiFactory.getSlider()
                .clone(previousElement)
                .alignBottom(previousElement, 0.05)
                .setName("colors")
                .setAnimation(AnimationYio.none)
                .setValues(0, colorValues.length)
                .setBehavior(new SliderBehavior() {
                    @Override
                    public String getValueString(LanguagesManager languagesManager, SliderYio sliderYio) {
                        return "" + colorValues[sliderYio.getValueIndex()];
                    }
                })
                .setVerticalTouchOffset(0.1f * GraphicsYio.width);

    }


    private String convertSliderIndexIntoLevelSize(SliderYio sliderYio) {
        switch (sliderYio.getValueIndex()) {
            default:
            case 0:
                return languagesManager.getString("tiny");
            case 1:
                return languagesManager.getString("small");
            case 2:
                return languagesManager.getString("normal");
            case 3:
                return languagesManager.getString("big");
            case 4:
                return languagesManager.getString("giant");
        }
    }


    private String convertSliderIndexIntoDifficultyString(LanguagesManager languagesManager, SliderYio sliderYio) {
        switch (sliderYio.getValueIndex()) {
            default:
            case 0:
                return languagesManager.getString("easy");
            case 1:
                return languagesManager.getString("normal");
            case 2:
                return languagesManager.getString("hard");
            case 3:
                return languagesManager.getString("impossible");
        }
    }


    private void createChecks() {
        chkAiOnly = uiFactory.getCheckButton()
                .setParent(label)
                .setName("ai_only")
                .alignBottom(0.02)
                .setWidth(0.75)
                .centerHorizontal();
    }


    private void loadValues() {
        Preferences prefs = getPreferences();

        levelSizeSlider.setValueIndex(prefs.getInteger("level_size", 1));
        difficultySlider.setValueIndex(prefs.getInteger("difficulty", 1));
        colorsSlider.setValueIndex(prefs.getInteger("colors", 0));
        chkAiOnly.setChecked(prefs.getBoolean("ai_only", false));
    }


    private void saveValues() {
        Preferences prefs = getPreferences();

        prefs.putInteger("level_size", levelSizeSlider.getValueIndex());
        prefs.putInteger("difficulty", difficultySlider.getValueIndex());
        prefs.putInteger("colors", colorsSlider.getValueIndex());
        prefs.putBoolean("ai_only", chkAiOnly.isChecked());

        prefs.flush();
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences("vitamin.soccer_parameters");
    }


    private void createStartButton() {
        startButton = uiFactory.getButton()
                .setPosition(0.55, 0.9, 0.4, 0.07)
                .applyText("start", BackgroundYio.green)
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        saveValues();
                        LoadingParameters loadingParameters = new LoadingParameters();
                        loadingParameters.addParameter("map_size", levelSizeSlider.getValueIndex());
                        loadingParameters.addParameter("colors", colorValues[colorsSlider.getValueIndex()]);
                        loadingParameters.addParameter("ai_only", chkAiOnly.isChecked());
                        loadingParameters.addParameter("difficulty", difficultySlider.getValueIndex());
                        loadingParameters.addParameter("seed", YioGdxGame.random.nextInt());
                        yioGdxGame.loadingManager.startInstantly(LoadingType.skirmish_create, loadingParameters);
                    }
                });
    }

}
