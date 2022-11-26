package yio.tro.opacha.menu.scenes.options;

import yio.tro.opacha.PlatformType;
import yio.tro.opacha.SettingsManager;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.elements.slider.SliderBehavior;
import yio.tro.opacha.menu.elements.slider.SliderYio;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.CheckButtonYio;
import yio.tro.opacha.menu.elements.ScrollableAreaYio;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.GraphicsYio;

public class SceneSettingsMenu extends SceneYio {

    ButtonYio mainLabel;
    public CheckButtonYio chkSound;
    private ButtonYio langButton;
    private ButtonYio infoButton;
    private ScrollableAreaYio scrollableAreaYio;
    public CheckButtonYio chkFullScreen;
    private SliderYio graphicsSlider;
    private CheckButtonYio chkHalvedMode;
    private CheckButtonYio chkAdaptiveDifficulty;
    private CheckButtonYio chkSpectateAfterVictory;
    private CheckButtonYio chkLockButton;
    private CheckButtonYio chkVelocitySlider;
    ButtonYio otherLabel;
    private CheckButtonYio chkColorblindMode;
    private CheckButtonYio chkThinBezels;


    @Override
    public void initialize() {
        setBackground(GroundIndex.CYAN);

        createInternals();

        createBackButton();
        createInfoButton();
    }


    private void createBackButton() {
        spawnBackButton(new Reaction() {
            @Override
            protected void reaction() {
                applyValues();
                SettingsManager.getInstance().saveValues();
                Scenes.mainMenu.create();

                if (SettingsManager.getInstance().requestRestartApp) {
                    Scenes.notification.show("restart_app");
                }
            }
        });
    }


    private void createInternals() {
        scrollableAreaYio = uiFactory.getScrollableAreaYio()
                .setSize(1, 0.85);

        createMainLabel();
        createOtherLabel();

        createGraphicsSlider();
        createCheckButtons();
        createLangButton();
    }


    @Override
    protected void onAppear() {
        super.onAppear();

        loadValues();
    }


    private void createOtherLabel() {
        otherLabel = uiFactory.getButton()
                .setParent(scrollableAreaYio)
                .setSize(0.9, 0.55)
                .alignBottom(mainLabel, 0.06)
                .centerHorizontal()
                .applyText(" ")
                .setTouchable(false)
                .setAnimation(AnimationYio.none);
    }


    private void createGraphicsSlider() {
        graphicsSlider = uiFactory.getSlider()
                .setSize(0.7, 0)
                .setParent(mainLabel)
                .centerHorizontal()
                .alignTop(0.08)
                .setSegmentsVisible(true)
                .setName("graphics")
                .setValues(0.5, 3)
                .setBehavior(getGraphicsSliderBehavior());
    }


    private SliderBehavior getGraphicsSliderBehavior() {
        return new SliderBehavior() {
            @Override
            public String getValueString(LanguagesManager languagesManager, SliderYio sliderYio) {
                switch (sliderYio.getValueIndex()) {
                    default:
                        return "error";
                    case 0:
                        return LanguagesManager.getInstance().getString("low");
                    case 1:
                        return LanguagesManager.getInstance().getString("medium");
                    case 2:
                        return LanguagesManager.getInstance().getString("high");
                }
            }
        };
    }


    private void createInfoButton() {
        if (YioGdxGame.platformType == PlatformType.ios) return;
        uiFactory.getCircleButton()
                .setSize(GraphicsYio.convertToWidth(0.09))
                .alignRight(0.04)
                .alignTop(0.02)
                .setTouchOffset(0.05)
                .loadTexture("menu/info_icon.png")
                .setAnimation(AnimationYio.none)
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        Scenes.aboutMenu.create();
                    }
                });
    }


    private void createLangButton() {
        langButton = uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.4, 0.05)
                .centerHorizontal()
                .alignBottom(0.01)
                .applyText("lang")
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        Scenes.languages.create();
                    }
                });
    }


    private void createMainLabel() {
        mainLabel = uiFactory.getButton()
                .setParent(scrollableAreaYio)
                .setSize(0.9, 0.44)
                .alignTop()
                .centerHorizontal()
                .applyText("settings", 10)
                .makeFirstLineCentered()
                .setTouchable(false)
                .setAnimation(AnimationYio.none);
    }


    private void createCheckButtons() {
        chkSound = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .setName("sound")
                .alignBottom(previousElement, 0.05)
                .setReaction(getCheckSoundReaction());

        chkFullScreen = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .setName("full_screen")
                .alignBottom(previousElement);

        chkHalvedMode = uiFactory.getCheckButton()
                .setParent(otherLabel)
                .setName("halved_mode_button")
                .alignTop(0.03);

        chkAdaptiveDifficulty = uiFactory.getCheckButton()
                .setParent(otherLabel)
                .setName("adaptive_difficulty")
                .alignBottom(previousElement);

        chkSpectateAfterVictory = uiFactory.getCheckButton()
                .setParent(otherLabel)
                .setName("spectate_after_victory")
                .alignBottom(previousElement);

        chkLockButton = uiFactory.getCheckButton()
                .setParent(otherLabel)
                .setName("lock_button")
                .alignBottom(previousElement);

        chkVelocitySlider = uiFactory.getCheckButton()
                .setParent(otherLabel)
                .setName("velocity_slider")
                .alignBottom(previousElement);

        chkColorblindMode = uiFactory.getCheckButton()
                .setParent(otherLabel)
                .setName("colorblind_mode")
                .alignBottom(previousElement);

        chkThinBezels = uiFactory.getCheckButton()
                .setParent(otherLabel)
                .setName("thin_bezels")
                .alignBottom(previousElement);
    }


    private Reaction getCheckSoundReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                SettingsManager.getInstance().soundEnabled = chkSound.isChecked();
            }
        };
    }


    private void applyValues() {
        SettingsManager instance = SettingsManager.getInstance();
        instance.requestRestartApp = false;

        instance.soundEnabled = chkSound.isChecked();
        instance.fullScreenMode = chkFullScreen.isChecked();
        instance.graphicsQuality = graphicsSlider.getValueIndex();
        instance.halvedModeButtonEnabled = chkHalvedMode.isChecked();
        instance.adaptiveDifficulty = chkAdaptiveDifficulty.isChecked();
        instance.spectateAfterVictory = chkSpectateAfterVictory.isChecked();
        instance.lockButtonEnabled = chkLockButton.isChecked();
        instance.velocitySliderEnabled = chkVelocitySlider.isChecked();
        instance.colorblindMode = chkColorblindMode.isChecked();
        instance.thinBezels = chkThinBezels.isChecked();

        instance.onValuesChanged();
    }


    private void loadValues() {
        SettingsManager instance = SettingsManager.getInstance();

        chkSound.setChecked(instance.soundEnabled);
        chkFullScreen.setChecked(instance.fullScreenMode);
        graphicsSlider.setValueIndex(instance.graphicsQuality);
        chkHalvedMode.setChecked(instance.halvedModeButtonEnabled);
        chkAdaptiveDifficulty.setChecked(instance.adaptiveDifficulty);
        chkSpectateAfterVictory.setChecked(instance.spectateAfterVictory);
        chkLockButton.setChecked(instance.lockButtonEnabled);
        chkVelocitySlider.setChecked(instance.velocitySliderEnabled);
        chkColorblindMode.setChecked(instance.colorblindMode);
        chkThinBezels.setChecked(instance.thinBezels);
    }

}
