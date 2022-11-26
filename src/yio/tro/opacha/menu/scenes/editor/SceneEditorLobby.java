package yio.tro.opacha.menu.scenes.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import yio.tro.opacha.game.LevelSize;
import yio.tro.opacha.game.loading.LoadingParameters;
import yio.tro.opacha.game.loading.LoadingType;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.elements.slider.SliderBehavior;
import yio.tro.opacha.menu.elements.slider.SliderYio;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.menu.scenes.Scenes;

public class SceneEditorLobby extends SceneYio{

    private ButtonYio label;
    private SliderYio levelSizeSlider;
    public ButtonYio creationButton;
    private String contents;


    @Override
    protected void initialize() {
        setBackground(GroundIndex.YELLOW);
        spawnBackButton(getBackReaction());

        createLabel();
        createSlider();
        createCreationButton();
        createImportButton();
        createLoadButton();
    }


    private void createImportButton() {
        uiFactory.getButton()
                .setSize(0.6, 0.06)
                .centerHorizontal()
                .alignBottom(label, 0.1)
                .applyText("import")
                .setReaction(getImportReaction());
    }


    private Reaction getImportReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                performImportFromClipboard();
            }
        };
    }


    public void performImportFromClipboard() {
        Clipboard clipboard = Gdx.app.getClipboard();
        contents = "";
        contents = clipboard.getContents();
        if (!isLevelCodeValid(contents)) return;

        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.addParameter("level_code", contents);
        yioGdxGame.loadingManager.startInstantly(LoadingType.editor_load, loadingParameters);
    }


    private boolean isLevelCodeValid(String levelCode) {
        if (levelCode == null) return false;
        if (levelCode.length() < 3) return false;
        if (!levelCode.contains("general")) return false;
        if (!levelCode.contains("planets")) return false;
        if (!levelCode.contains("links")) return false;

        return true;
    }


    private void createLoadButton() {
        uiFactory.getButton()
                .setSize(0.6, 0.06)
                .centerHorizontal()
                .alignBottom(previousElement, 0.02)
                .applyText("load")
                .setReaction(getLoadReaction());
    }


    private Reaction getLoadReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.editorLoad.create();
            }
        };
    }


    private void createCreationButton() {
        creationButton = uiFactory.getButton()
                .setParent(label)
                .setSize(0.4, 0.05)
                .centerHorizontal()
                .alignBottom(0.01)
                .applyText("create")
                .setReaction(getCreationReaction());
    }


    private Reaction getCreationReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                LoadingParameters loadingParameters = new LoadingParameters();
                loadingParameters.addParameter("map_size", levelSizeSlider.getValueIndex());
                yioGdxGame.loadingManager.startInstantly(LoadingType.editor_create, loadingParameters);
            }
        };
    }


    private void createSlider() {
        levelSizeSlider = uiFactory.getSlider()
                .setParent(label)
                .setSize(0.65)
                .alignTop(0.03)
                .setAnimation(AnimationYio.none)
                .centerHorizontal()
                .setName("level_size")
                .setValues(0, 3)
                .setSegmentsVisible(true)
                .setBehavior(getSliderBehavior());
    }


    private SliderBehavior getSliderBehavior() {
        return new SliderBehavior() {
            @Override
            public String getValueString(LanguagesManager languagesManager, SliderYio sliderYio) {
                switch (sliderYio.getValueIndex()) {
                    default:
                    case 0:
                        return languagesManager.getString("small");
                    case 1:
                        return languagesManager.getString("normal");
                    case 2:
                        return languagesManager.getString("big");
                }
            }
        };
    }


    private void createLabel() {
        label = uiFactory.getButton()
                .setSize(0.8, 0.25)
                .centerHorizontal()
                .alignTop(0.3)
                .applyText(" ")
                .setTouchable(false)
                .setAnimation(AnimationYio.none);
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                Scenes.chooseGameMode.create();
            }
        };
    }
}
