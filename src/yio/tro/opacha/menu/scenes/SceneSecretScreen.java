package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.Yio;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.GameRules;
import yio.tro.opacha.game.debug.DebugFlags;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.elements.slider.SliderBehavior;
import yio.tro.opacha.menu.elements.slider.SliderYio;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.stuff.DebugDataContainer;

public class SceneSecretScreen extends SceneYio{


    private ButtonYio mainLabel;
    private int[] speedValues;
    private SliderYio fastForwardSpeedSlider;


    @Override
    protected void initialize() {
        setBackground(GroundIndex.RED);

        initSpeedValues();
        createBackButton();
        createMainLabel();
        createSlider();
        createButtons();
    }


    private void createButtons() {
        uiFactory.getButton()
                .setSize(0.7, 0.06)
                .centerHorizontal()
                .alignBottom(previousElement, 0.06)
                .setBorder(true)
                .setTouchOffset(0.02)
                .applyText("Unlock levels")
                .setReaction(getUnlockLevelsReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignBottom(previousElement, 0.02)
                .centerHorizontal()
                .applyText("Show fps")
                .setReaction(getShowFpsReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignBottom(previousElement, 0.02)
                .centerHorizontal()
                .applyText("Test screen")
                .setReaction(getTestScreenReaction());

        uiFactory.getButton()
                .clone(previousElement)
                .alignBottom(previousElement, 0.02)
                .centerHorizontal()
                .applyText("Debug data")
                .setReaction(getDebugDataReaction());
    }


    private Reaction getDebugDataReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.debugData.create();
            }
        };
    }


    private Reaction getTestScreenReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                System.out.println("SceneSecretScreen.reaction");
            }
        };
    }


    private Reaction getShowFpsReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                switchShowFps();
            }
        };
    }


    private Reaction getUnlockLevelsReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                DebugFlags.unlockLevels = true;
                Scenes.campaign.create();
                Scenes.notification.show("Levels unlocked");
            }
        };
    }


    private void createSlider() {
        fastForwardSpeedSlider = uiFactory.getSlider()
                .setParent(mainLabel)
                .alignTop(0.2)
                .setName("Fast forward speed")
                .setValues(0, speedValues.length)
                .setBehavior(getSliderBehavior());
    }


    private SliderBehavior getSliderBehavior() {
        return new SliderBehavior() {
            @Override
            public String getValueString(LanguagesManager languagesManager, SliderYio sliderYio) {
                return "" + speedValues[sliderYio.getValueIndex()];
            }


            @Override
            public void onValueChanged(SliderYio sliderYio) {
                GameRules.fastForwardSpeed = speedValues[sliderYio.getValueIndex()];
            }
        };
    }


    private void loadValues() {
        fastForwardSpeedSlider.setValueIndex(convertSpeedIntoSliderIndex(GameRules.fastForwardSpeed));
    }


    @Override
    protected void onAppear() {
        loadValues();
    }


    private int convertSpeedIntoSliderIndex(int speed) {
        for (int i = 0; i < speedValues.length; i++) {
            if (speedValues[i] == speed) {
                return i;
            }
        }

        return 0;
    }


    private void createMainLabel() {
        mainLabel = uiFactory.getButton()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignTop(0.15)
                .setTouchable(false)
                .applyText(convertStringToArray("- Secret screen - #Load time: " + YioGdxGame.initialLoadingTime));
    }


    private void createBackButton() {
        spawnBackButton(new Reaction() {
            @Override
            protected void reaction() {
                Scenes.mainMenu.create();
            }
        });
    }


    private void initSpeedValues() {
        speedValues = new int[]{4, 10, 20, 30, 50, 75, 100};
    }


    private void switchShowFps() {
        if (DebugFlags.showFps) {
            DebugFlags.showFps = false;
            Scenes.notification.show("Show fps disabled");
        } else {
            DebugFlags.showFps = true;
            Scenes.notification.show("Show fps enabled");
        }
    }
}
