package yio.tro.opacha.menu.elements;

import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.reactions.Reaction;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.RenderableTextYio;
import yio.tro.opacha.stuff.factor_yio.FactorYio;

public class CheckButtonYio extends InterfaceElement<CheckButtonYio> {

    RectangleYio activeSquare;
    float internalOffset;
    boolean checked, touched;
    double defaultHeight;
    public FactorYio activeFactor, selectionFactor;
    Reaction reaction;
    PointYio textOffset;
    public RenderableTextYio renderableText;


    public CheckButtonYio(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);

        activeSquare = new RectangleYio();
        activeFactor = new FactorYio();
        selectionFactor = new FactorYio();
        renderableText = new RenderableTextYio();
        renderableText.setFont(Fonts.smallFont);
        textOffset = new PointYio();
        reaction = null;

        // defaults
        internalOffset = 0;
        defaultHeight = 0.07;
        setChecked(false);
        setName("[Check button]");
        setAnimation(AnimationYio.none);
    }


    @Override
    protected CheckButtonYio getThis() {
        return this;
    }


    @Override
    public void move() {
        updateViewPosition();
        activeFactor.move();
        moveSelection();
    }


    private void moveSelection() {
        if (touched) return;

        selectionFactor.move();
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();

        internalOffset = 0.3f * position.height;
    }


    @Override
    protected void onApplyParent() {
        super.onApplyParent();

        updateActiveSquare();
        updateTextPosition();
    }


    private void updateTextPosition() {
        renderableText.position.x = viewPosition.x + textOffset.x;
        renderableText.position.y = viewPosition.y + viewPosition.height - textOffset.y;
        renderableText.updateBounds();
    }


    @Override
    public CheckButtonYio setParent(InterfaceElement parent) {
        setSize(parent.position.width / GraphicsYio.width, defaultHeight);
        return super.setParent(parent);
    }


    public CheckButtonYio setWidth(double width) {
        return setSize(width, position.height / GraphicsYio.height);
    }


    public CheckButtonYio setHeight(double height) {
        return setSize(position.width / GraphicsYio.width, height);
    }


    public CheckButtonYio setInternalOffset(double offset) {
        internalOffset = (float) (offset * GraphicsYio.width);
        updateActiveSquare();
        return this;
    }


    private void updateActiveSquare() {
        activeSquare.height = viewPosition.height - 2 * internalOffset;
        activeSquare.width = activeSquare.height;
        activeSquare.x = viewPosition.x + viewPosition.width - internalOffset - activeSquare.width;
        activeSquare.y = viewPosition.y + internalOffset;
    }


    public RectangleYio getActiveSquare() {
        return activeSquare;
    }


    @Override
    public void onDestroy() {
        selectionFactor.destroy(3, 2);
    }


    @Override
    public void onAppear() {

    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        if (isTouchInsideRectangle(currentTouch, viewPosition)) {
            touched = true;
            select();
            return true;
        }

        return false;
    }


    public void press() {
        setChecked(!checked);
    }


    private void select() {
        selectionFactor.setValues(1, 0);
        selectionFactor.destroy(1, 3);
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        if (touched) {
            touched = false;
            if (isClicked()) {
                onClick();
            }
            return true;
        }

        return false;
    }


    private void onClick() {
        press();

        if (reaction != null) {
            reaction.performReactActions(menuControllerYio);
        }
    }


    public CheckButtonYio setReaction(Reaction reaction) {
        this.reaction = reaction;
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderCheckButton;
    }


    public boolean isChecked() {
        return checked;
    }


    public CheckButtonYio setName(String key) {
        renderableText.setString(LanguagesManager.getInstance().getString(key));
        updateTextMetrics();

        return this;
    }


    private void updateTextMetrics() {
        renderableText.updateMetrics();

        textOffset.x = internalOffset;
        textOffset.y = (position.height - renderableText.height) / 2;
    }


    public CheckButtonYio setChecked(boolean checked) {
        if (this.checked == checked) return this;

        this.checked = checked;

        if (!checked) { // uncheck
            activeFactor.setValues(1, 0);
            activeFactor.destroy(1, 3);
        } else { // check
            activeFactor.setValues(0, 0);
            activeFactor.appear(1, 2);
        }

        return this;
    }


    @Override
    public String toString() {
        return "[CheckButton: " +
                renderableText.string +
                "]";
    }
}
