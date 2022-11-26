package yio.tro.opacha.menu.elements.tutorial;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.VisualTextContainer;

public class GlobalMessageElement extends InterfaceElement<GlobalMessageElement> {

    public RectangleYio backgroundPosition;
    boolean touched;
    boolean readyToDie;
    public RectangleYio blackoutPosition;
    public float internalOffset;
    public VisualTextContainer visualTextContainer;


    public GlobalMessageElement(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);
        backgroundPosition = new RectangleYio();
        blackoutPosition = new RectangleYio();
        visualTextContainer = new VisualTextContainer();
        internalOffset = 0.02f * GraphicsYio.width;
    }


    @Override
    protected GlobalMessageElement getThis() {
        return this;
    }


    public void setText(String key) {
        visualTextContainer.position.width = 0.9f * GraphicsYio.width;
        visualTextContainer.position.x = GraphicsYio.width / 2 - visualTextContainer.position.width / 2;
        String string = LanguagesManager.getInstance().getString(key);
        visualTextContainer.applyManyTextLines(Fonts.smallFont, string);
        visualTextContainer.suppressEmptyLinesInTheEnd();
        visualTextContainer.updateHeightToMatchText(2 * internalOffset);

        backgroundPosition.height = visualTextContainer.position.height + 4 * internalOffset;
    }


    @Override
    public void move() {
        updateViewPosition();
        updateBackgroundPosition();
        updateBlackoutPosition();
        moveContainer();
    }


    private void moveContainer() {
        visualTextContainer.position.y = backgroundPosition.y + 2 * internalOffset;
        visualTextContainer.move(viewPosition);
    }


    private void updateBlackoutPosition() {
        blackoutPosition.setBy(backgroundPosition);
        blackoutPosition.y = backgroundPosition.y + backgroundPosition.height;
        blackoutPosition.height = GraphicsYio.height - blackoutPosition.y;
    }


    private void updateBackgroundPosition() {
        backgroundPosition.x = 0;
        backgroundPosition.width = GraphicsYio.width;
        backgroundPosition.y = (appearFactor.get() - 1) * backgroundPosition.height;
    }


    @Override
    public void onDestroy() {
        touched = false;
    }


    @Override
    public void onAppear() {
        touched = false;
        readyToDie = false;
    }


    @Override
    public boolean checkToPerformAction() {
        if (readyToDie) {
            readyToDie = false;
            Scenes.globalMessage.destroy();
            return true;
        }
        return false;
    }


    @Override
    public boolean touchDown() {
        touched = appearFactor.isInAppearState();
        if (touched) {
            readyToDie = true;
            return true;
        }
        return false;
    }


    @Override
    public boolean touchDrag() {
        return touched;
    }


    @Override
    public boolean touchUp() {
        if (touched) return true;
        touched = false;
        return false;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderGlobalMessageElement;
    }

}
