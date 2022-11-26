package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.PlatformType;
import yio.tro.opacha.SoundManager;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.menu.elements.ground.GpSrUp;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.CircleButtonYio;
import yio.tro.opacha.stuff.GraphicsYio;

public class SceneMainMenu extends SceneYio {

    public CircleButtonYio supportiveButton;
    public CircleButtonYio settingsButton;
    public CircleButtonYio playButton;
    private double verticalPosition;
    private double iconSize;
    private double playButtonSize;
    private ButtonYio logoButton;
    private double logoWidth;
    private double iconOffset;
    private double touchOffset;
    int secretCount;


    @Override
    public void initialize() {
        setBackground(GroundIndex.MAIN_MENU);
        getGround().setSpawnRule(new GpSrUp());

        initMetrics();

        createSupportiveButton();
        createSettingsButton();
        createPlayButton();
        createLogo();
    }


    private void createLogo() {
        double logoHeight = GraphicsYio.convertToHeight(logoWidth) / 2;
        double logoY = 0.5;

        uiFactory.getButton()
                .setSize(0.7, GraphicsYio.convertToHeight(0.005))
                .centerHorizontal()
                .alignBottom(logoY + 0.01)
                .loadTexture("menu/main_menu/black_line.png")
                .setTouchable(false)
                .setAnimation(AnimationYio.none);

        logoButton = uiFactory.getButton()
                .setSize(logoWidth, logoHeight)
                .centerHorizontal()
                .alignBottom(logoY)
                .loadTexture("menu/main_menu/mm_logo.png")
                .setSilentReactionMode(true)
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        secretCount += 120;

                        if (secretCount > 300) {
                            Scenes.secretScreen.create();
                            SoundManager.playSound(SoundManager.button);
                        }
                    }
                })
                .setAnimation(AnimationYio.none);
    }


    private void createPlayButton() {
        playButton = uiFactory.getCircleButton()
                .loadTexture("menu/main_menu/play_button.png")
                .setPosition((1 - playButtonSize) / 2, verticalPosition - GraphicsYio.convertToHeight(playButtonSize) / 2, playButtonSize)
                .setTouchOffset(touchOffset)
                .setAnimation(AnimationYio.none)
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        onPlayButtonPressed();
                    }
                });
    }


    private void createSettingsButton() {
        settingsButton = uiFactory.getCircleButton()
                .loadTexture("menu/main_menu/settings_icon.png")
                .clone(supportiveButton)
                .alignBottom(verticalPosition - GraphicsYio.convertToHeight(iconSize) / 2)
                .alignLeft(iconOffset)
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        Scenes.settingsMenu.create();
                    }
                });
    }


    private void createSupportiveButton() {
        String path = "menu/main_menu/quit_icon.png";
        if (YioGdxGame.platformType == PlatformType.ios) {
            path = "menu/main_menu/mm_info_icon.png";
        }

        supportiveButton = uiFactory.getCircleButton()
                .setSize(iconSize)
                .alignBottom(verticalPosition - GraphicsYio.convertToHeight(iconSize) / 2)
                .alignRight(iconOffset)
                .setTouchOffset(touchOffset)
                .loadTexture(path)
                .setReaction(getSupportiveReaction())
                .setAnimation(AnimationYio.none)
                .tagAsBackButton();
    }


    private Reaction getSupportiveReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                if (YioGdxGame.platformType == PlatformType.ios) {
                    Scenes.aboutMenu.create();
                    return;
                }
                yioGdxGame.exitApp();
            }
        };
    }


    private void initMetrics() {
        verticalPosition = 0.4;
        iconSize = 0.16;
        playButtonSize = 0.32;
        logoWidth = 0.5;
        iconOffset = 0.07;
        touchOffset = 0.05;
        secretCount = 0;
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        secretCount = 0;
    }


    @Override
    public void move() {
        super.move();

        if (secretCount > 0) {
            secretCount--;
        }
    }


    private void onPlayButtonPressed() {
        Scenes.chooseGameMode.create();
    }
}
