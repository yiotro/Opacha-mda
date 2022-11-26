package yio.tro.opacha.menu.scenes.gameplay;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.opacha.SettingsManager;
import yio.tro.opacha.game.gameplay.ObjectsLayer;
import yio.tro.opacha.game.gameplay.model.Planet;
import yio.tro.opacha.game.gameplay.model.PlanetType;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.scenes.ModalSceneYio;
import yio.tro.opacha.stuff.GraphicsYio;

public class SceneSelectionOverlay extends ModalSceneYio{


    private TextureRegion selectionTexture;
    public ButtonYio bDefensive;
    public ButtonYio bEconomic;
    Planet selectedPlanet;
    private ButtonYio labelDefensive;
    private ButtonYio labelEconomic;
    private ButtonYio halvedModeButton;


    @Override
    protected void initialize() {
        loadTextures();
        createDefensiveButton();
        createEconomicButton();
        createPriceLabels();
        createSendHalfButton();
    }


    private void createSendHalfButton() {
        double touchOffset = 0.04;;
        halvedModeButton = uiFactory.getButton()
                .setSize(GraphicsYio.convertToWidth(0.05))
                .alignLeft(touchOffset / 2)
                .alignBottom(GraphicsYio.convertToHeight(touchOffset / 2) + getBottomOffset())
                .setTouchOffset(touchOffset)
                .loadTexture("menu/gameplay/b_halved_mode.png")
                .setReaction(getHalvedModeReaction())
                .setAnimation(AnimationYio.down)
                .setSelectionTexture(selectionTexture);
    }


    private Reaction getHalvedModeReaction() {
        return new Reaction() {
            @Override
            protected void reaction() {
                ObjectsLayer objectsLayer = gameController.objectsLayer;
                objectsLayer.planetsManager.onLongTappedPlanet(selectedPlanet);
            }
        };
    }


    private void createPriceLabels() {
        labelDefensive = uiFactory.getButton()
                .setSize(getSize() / 2)
                .alignLeft(getOffset() + getSize() / 4)
                .alignBottom(-0.01 + GraphicsYio.convertToHeight(getSize()) + getBottomOffset())
                .setTouchable(false)
                .loadTexture("menu/gameplay/price.png")
                .setAnimation(AnimationYio.down);

        labelEconomic = uiFactory.getButton()
                .setSize(getSize() / 2)
                .alignRight(getOffset() + getSize() / 4)
                .alignBottom(-0.01 + GraphicsYio.convertToHeight(getSize()) + getBottomOffset())
                .setTouchable(false)
                .loadTexture("menu/gameplay/price.png")
                .setAnimation(AnimationYio.down);
    }


    @Override
    protected void appearElement(InterfaceElement interfaceElement) {
        if (selectedPlanet.type == PlanetType.economic && interfaceElement == bEconomic) return;
        if (selectedPlanet.type == PlanetType.defensive && interfaceElement == bDefensive) return;
        if (selectedPlanet.type == PlanetType.economic && interfaceElement == labelEconomic) return;
        if (selectedPlanet.type == PlanetType.defensive && interfaceElement == labelDefensive) return;
        if (interfaceElement == halvedModeButton && !SettingsManager.getInstance().halvedModeButtonEnabled) return;
        super.appearElement(interfaceElement);
    }


    private void createEconomicButton() {
        bEconomic = uiFactory.getButton()
                .setSize(getSize())
                .alignRight(getOffset())
                .alignBottom(getBottomOffset())
                .setTouchOffset(0.06)
                .setIgnoreResumePause(true)
                .loadTexture("menu/gameplay/b_economic.png")
                .setKey("economic_upgrade")
                .setAnimation(AnimationYio.down)
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        gameController.objectsLayer.planetsManager.onEconomicUpgradeButtonClicked();
                    }
                })
                .setSelectionTexture(selectionTexture);
    }


    private double getOffset() {
        return 0.3;
    }


    private double getSize() {
        return 0.125;
    }


    private double getBottomOffset() {
        if (SettingsManager.getInstance().thinBezels) return 0.04;
        return 0;
    }


    private void createDefensiveButton() {
        bDefensive = uiFactory.getButton()
                .setSize(getSize())
                .alignLeft(getOffset())
                .alignBottom(getBottomOffset())
                .setTouchOffset(0.06)
                .setIgnoreResumePause(true)
                .loadTexture("menu/gameplay/b_defensive.png")
                .setAnimation(AnimationYio.down)
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        gameController.objectsLayer.planetsManager.onDefensiveUpgradeButtonClicked();
                    }
                })
                .setSelectionTexture(selectionTexture);
    }


    private void loadTextures() {
        selectionTexture = GraphicsYio.loadTextureRegion("menu/selection.png", true);
    }


    public void setSelectedPlanet(Planet selectedPlanet) {
        this.selectedPlanet = selectedPlanet;
    }
}
