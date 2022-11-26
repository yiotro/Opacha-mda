package yio.tro.opacha.menu.elements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.LanguagesManager;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.RectangleYio;

public class NotificationElement extends InterfaceElement<NotificationElement> {

    public static final int AUTO_HIDE_DELAY = 1500;

    boolean autoHide;
    public PointYio textPosition, textDelta;
    public String message;
    public BitmapFont font;
    private long timeToHide;
    float textOffset;
    public RectangleYio shadowPosition;


    public NotificationElement(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);

        autoHide = false;
        message = "";
        timeToHide = 0;
        font = Fonts.smallFont;
        textOffset = 0.03f * GraphicsYio.width;
        textPosition = new PointYio();
        textDelta = new PointYio();
        shadowPosition = new RectangleYio();

        setAnimation(AnimationYio.up);
    }


    @Override
    protected NotificationElement getThis() {
        return this;
    }


    @Override
    public void move() {
        updateViewPosition();
        updateTextPosition();
        updateShadowPosition();

        checkToDie();
    }


    private void updateShadowPosition() {
        if (!factorMoved) return;

        shadowPosition.setBy(viewPosition);
        shadowPosition.x -= 0.1f * GraphicsYio.width;
        shadowPosition.width += 0.2f * GraphicsYio.width;
        shadowPosition.height = GraphicsYio.height / 2;
    }


    @Override
    protected void updateViewPosition() {
        if (!factorMoved) return;

        viewPosition.setBy(position);

        viewPosition.y += (1 - appearFactor.get()) * 1.5f * position.height;
    }


    private void checkToDie() {
        if (autoHide && System.currentTimeMillis() > timeToHide) {
            destroy();
        }
    }


    private void updateTextPosition() {
        if (!factorMoved) return;

        textPosition.x = viewPosition.x + textDelta.x;
        textPosition.y = viewPosition.y + textDelta.y;
    }


    @Override
    public void onDestroy() {
        appearFactor.destroy(1, 3);

        autoHide = false;
    }


    @Override
    public void onAppear() {
        appearFactor.appear(3, 1);
    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    public void enableAutoHide() {
        autoHide = true;

        timeToHide = System.currentTimeMillis() + AUTO_HIDE_DELAY;
    }


    public void setMessage(String message) {
        this.message = LanguagesManager.getInstance().getString(message);

        updateTextDelta();
    }


    private void updateTextDelta() {
        textDelta.x = textOffset;
        float textHeight = GraphicsYio.getTextHeight(font, message);
        textDelta.y = position.height / 2 + textHeight / 2;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderNotification;
    }
}
