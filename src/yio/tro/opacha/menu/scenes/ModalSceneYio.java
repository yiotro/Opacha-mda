package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.menu.elements.gameplay.FollowGameViewElement;
import yio.tro.opacha.menu.elements.ground.GroundIndex;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.elements.AnimationYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.InterfaceElement;

public abstract class ModalSceneYio extends SceneYio {


    protected ButtonYio defaultLabel;
    protected ButtonYio closeButton;
    protected FollowGameViewElement followGameViewElement;


    public ModalSceneYio() {
        super();
        defaultLabel = null;
        followGameViewElement = null;
    }


    @Override
    protected void beginCreation() {
        menuControllerYio.setCurrentScene(this);
        menuControllerYio.checkToRemoveInvisibleElements();
    }


    @Override
    protected void endInitialization() {
        super.endInitialization();

        for (InterfaceElement element : getLocalElementsList()) {
            element.setOnTopOfGameView(true);
        }
    }


    @Override
    public void addLocalElement(InterfaceElement interfaceElement) {
        super.addLocalElement(interfaceElement);

        if (followGameViewElement != null && !interfaceElement.hasParent()) {
            currentAddedElement.setParent(followGameViewElement);
        }
    }


    @Override
    protected void prepareGround() {
        // no ground

        followGameViewElement = uiFactory.getFollowGameViewElement()
                .setPosition(0, 0, 1, 1);
    }


    protected void createDefaultLabel(double width, double height) {
        defaultLabel = uiFactory.getButton()
                .setSize(width, height)
                .alignBottom(0.08)
                .centerHorizontal()
                .setAnimation(AnimationYio.down)
                .setGroundIndex(GroundIndex.BUTTON_WHITE)
                .setSilentReactionMode(true);

        if (height > 0.2) {
            defaultLabel
                    .setShadow(true)
                    .setTransparencyEnabled(false)
                    .setCornerRadius(0.07)
                    .setFakeDyingStatusEnabled(true)
                    .setSilentReactionMode(true);
        }
    }


    protected void createDownsideLabel(double height) {
        defaultLabel = uiFactory.getButton()
                .setSize(1.02, height + 0.01)
                .setPosition(0, -0.01)
                .centerHorizontal()
                .setAnimation(AnimationYio.down)
                .setGroundIndex(GroundIndex.BUTTON_WHITE)
                .setSilentReactionMode(true);
    }


    protected void createCloseButton() {
        closeButton = uiFactory.getButton()
                .setSize(1, 1)
                .centerHorizontal()
                .centerVertical()
                .setRenderable(false)
                .tagAsBackButton()
                .setDebugName("invisible close button")
                .setReaction(new Reaction() {
                    @Override
                    protected void reaction() {
                        destroy();
                    }
                });
    }

}
