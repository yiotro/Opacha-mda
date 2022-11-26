package yio.tro.opacha.menu.elements.customizable_list;

import yio.tro.opacha.SoundManager;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.LongTapDetector;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.scroll_engine.ScrollEngineYio;

import java.util.ArrayList;

public class CustomizableListYio extends InterfaceElement<CustomizableListYio> {

    public ArrayList<AbstractCustomListItem> items;
    public boolean touched;
    float hook;
    ScrollEngineYio scrollEngineYio;
    LongTapDetector longTapDetector;
    AbstractCustomListItem targetItem;
    boolean readyToProcessItemClick;
    boolean readyToProcessItemLongTap;
    public RectangleYio maskPosition;
    public float cornerRadius;
    public boolean alphaEnabled;


    public CustomizableListYio(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);

        items = new ArrayList<>();
        touched = false;
        hook = 0;
        scrollEngineYio = new ScrollEngineYio();
        scrollEngineYio.setFriction(0.05);
        targetItem = null;
        readyToProcessItemClick = false;
        readyToProcessItemLongTap = false;
        maskPosition = new RectangleYio();
        alphaEnabled = true;
        updateCornerRadius();

        initLongTapDetector();
    }


    private void initLongTapDetector() {
        longTapDetector = new LongTapDetector() {
            @Override
            public void onLongTapDetected() {
                CustomizableListYio.this.onLongTapDetected();
            }
        };
    }


    private void updateCornerRadius() {
        cornerRadius = 0.05f * GraphicsYio.width;
        if (cornerRadius > 0.05f * GraphicsYio.height) {
            cornerRadius = 0.05f * GraphicsYio.height;
        }
    }


    public void addItem(AbstractCustomListItem newItem) {
        newItem.setCustomizableListYio(this);
        items.add(newItem);
        updateItemDeltas();
    }


    private void updateItemDeltas() {
        updateViewPosition();
        updateMaskPosition();
        float currentY = maskPosition.height;
        for (AbstractCustomListItem item : items) {
            item.viewPosition.width = (float) item.getWidth();
            item.viewPosition.height = (float) item.getHeight();
            item.positionDelta.set(
                    (maskPosition.width - item.getWidth()) / 2,
                    currentY - item.getHeight()
            );
            item.onPositionChanged();
            currentY -= item.getHeight();
        }

        updateScrollLimit();
    }


    public CustomizableListYio clearItems() {
        items.clear();
        return this;
    }


    private void onLongTapDetected() {
        for (AbstractCustomListItem item : items) {
            if (!item.isTouched(currentTouch)) continue;

            targetItem = item;
            readyToProcessItemLongTap = true;
            break;
        }
    }


    @Override
    protected CustomizableListYio getThis() {
        return this;
    }


    @Override
    public void move() {
        updateViewPosition();
        scrollEngineYio.move();
        updateHook();
        longTapDetector.move();
    }


    private void updateHook() {
        hook = (float) scrollEngineYio.getSlider().a;
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();
        updateItemDeltas();

        scrollEngineYio.setSlider(0, maskPosition.height);
        scrollEngineYio.setSoftLimitOffset(0.05 * maskPosition.height);

        updateScrollLimit();
    }


    private void updateScrollLimit() {
        double sum = 0;
        for (AbstractCustomListItem item : items) {
            sum += item.getHeight();
        }
        sum = Math.max(sum, maskPosition.height);

        scrollEngineYio.setLimits(0, sum);
    }


    @Override
    protected void onPositionChanged() {
        super.onPositionChanged();
        updateItemDeltas();
    }


    @Override
    protected void onApplyParent() {
        super.onApplyParent();
        updateMaskPosition();
        moveItems();
    }


    private void updateMaskPosition() {
        maskPosition.setBy(viewPosition);
        maskPosition.increase(-0.04 * position.width);
    }


    private void moveItems() {
        for (AbstractCustomListItem item : items) {
            item.moveItem();

            if (!touched) {
                item.selectionEngineYio.move();
            }
        }
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        readyToProcessItemClick = false;
        readyToProcessItemLongTap = false;
        resetScroll();
    }


    public void resetScroll() {
        scrollEngineYio.resetToBottom();
    }


    public void scrollToItem(AbstractCustomListItem item) {
        updateMaskPosition();
        while (scrollEngineYio.getSlider().b  < scrollEngineYio.getLimits().b - 1) {
            updateHook();
            item.moveItem();
            if (isItemInGoodEnoughPosition(item)) break;
            scrollEngineYio.relocate(0.1f * GraphicsYio.height);
            scrollEngineYio.hardCorrection();
        }
    }


    private boolean isItemInGoodEnoughPosition(AbstractCustomListItem item) {
        if (item.viewPosition.y < maskPosition.y + 0.3f * maskPosition.height) return false;
        if (item.viewPosition.y > maskPosition.y + maskPosition.height) return false;
        return true;
    }


    @Override
    public boolean checkToPerformAction() {
        if (readyToProcessItemClick) {
            readyToProcessItemClick = false;

            targetItem.onClicked();

            return true;
        }

        if (readyToProcessItemLongTap) {
            readyToProcessItemLongTap = false;

            targetItem.onLongTapped();

            return true;
        }

        return false;
    }


    @Override
    public boolean touchDown() {
        touched = isTouched(currentTouch);
        if (!touched) return false;

        scrollEngineYio.onTouchDown();
        checkToSelectItem();
        longTapDetector.onTouchDown(currentTouch);

        return true;
    }


    private void checkToSelectItem() {
        for (AbstractCustomListItem item : items) {
            if (!item.isTouched(currentTouch)) continue;

            item.selectionEngineYio.select();
            item.onTouchDown(currentTouch);
            break;
        }
    }


    @Override
    public boolean touchDrag() {
        if (!touched) return false;

        scrollEngineYio.setSpeed(1.5 * (currentTouch.y - lastTouch.y));
        longTapDetector.onTouchDrag(currentTouch);

        return true;
    }


    @Override
    public boolean touchUp() {
        if (!touched) return false;

        scrollEngineYio.onTouchUp();
        longTapDetector.onTouchUp(currentTouch);

        if (isClicked()) {
            onClick();
        }

        touched = false;
        return true;
    }


    private void onClick() {
        for (AbstractCustomListItem item : items) {
            if (!item.isTouched(currentTouch)) continue;

            targetItem = item;
            readyToProcessItemClick = true;
            SoundManager.playSound(SoundManager.button);
            break;
        }
    }


    @Override
    public boolean onMouseWheelScrolled(int amount) {
        if (amount == 1) {
            scrollEngineYio.giveImpulse(0.03 * GraphicsYio.width);
        } else if (amount == -1) {
            scrollEngineYio.giveImpulse(-0.03 * GraphicsYio.width);
        }
        scrollEngineYio.hardCorrection();
        return true;
    }


    public CustomizableListYio setCornerRadius(double r) {
        cornerRadius = (float) (r * GraphicsYio.width);
        return this;
    }


    @Override
    public float getAlpha() {
        if (!alphaEnabled) {
            return 1;
        }

        return super.getAlpha();
    }


    public boolean isAlphaEnabled() {
        return alphaEnabled;
    }


    public CustomizableListYio setAlphaEnabled(boolean alphaEnabled) {
        this.alphaEnabled = alphaEnabled;
        return this;
    }


    public ScrollEngineYio getScrollEngineYio() {
        return scrollEngineYio;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderCustomizableList;
    }
}
