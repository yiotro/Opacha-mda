package yio.tro.opacha.menu.elements.scrollable_list;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.opacha.Fonts;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.LongTapDetector;
import yio.tro.opacha.stuff.scroll_engine.ScrollEngineYio;

import java.util.ArrayList;

public class ScrollableListElement extends InterfaceElement<ScrollableListElement> {

    public ArrayList<ScrItem> items;
    double itemHeight, itemOffset;
    boolean touched;
    public BitmapFont titleFont, descFont;
    public float cornerRadius;
    float hook;
    ScrollEngineYio scrollEngineYio;
    LongTapDetector longTapDetector;
    ScrItem targetItem;
    boolean readyToProcessItemClick;
    ScrBehavior behavior;


    public ScrollableListElement(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);

        items = new ArrayList<>();
        itemHeight = 0.2 * GraphicsYio.width;
        itemOffset = 0.05f * GraphicsYio.width;
        touched = false;
        titleFont = Fonts.smallFont;
        descFont = Fonts.miniFont;
        updateCornerRadius();
        hook = 0;
        scrollEngineYio = new ScrollEngineYio();
        scrollEngineYio.setFriction(0.05);
        targetItem = null;
        readyToProcessItemClick = false;
        behavior = null;

        initLongTapDetector();
    }


    private void initLongTapDetector() {
        longTapDetector = new LongTapDetector() {
            @Override
            public void onLongTapDetected() {
                ScrollableListElement.this.onLongTapDetected();
            }
        };
    }


    void onLongTapDetected() {

    }


    public void addListItem(String name, String description, String key) {
        ScrItem newItem = new ScrItem(this);

        newItem.set(name, description, key);

        items.add(newItem);
        updateItemDeltas();
    }


    public ScrollableListElement clearItems() {
        items.clear();
        return this;
    }


    private void updateCornerRadius() {
        cornerRadius = (float) (itemHeight / 2 - 1);
        if (cornerRadius > 0.05f * GraphicsYio.height) {
            cornerRadius = 0.05f * GraphicsYio.height;
        }
    }


    @Override
    protected ScrollableListElement getThis() {
        return this;
    }


    @Override
    public void move() {
        updateViewPosition();
        moveItemSelections();
        scrollEngineYio.move();
        updateHook();
        longTapDetector.move();
    }


    private void updateHook() {
        hook = (float) scrollEngineYio.getSlider().a;
    }


    private void moveItemSelections() {
        if (touched) return;

        for (ScrItem item : items) {
            item.moveSelection();
        }
    }


    @Override
    protected void onSizeChanged() {
        super.onSizeChanged();
        updateItemDeltas();

        scrollEngineYio.setSlider(position.y, position.y + position.height);
        scrollEngineYio.setSoftLimitOffset(0.05 * position.height);

        updateScrollLimit();
    }


    private void updateScrollLimit() {
        if (items.size() == 0) {
            scrollEngineYio.setLimits(position.y, position.y + position.height);
            return;
        }

        ScrItem lowestItem = items.get(items.size() - 1);
        scrollEngineYio.setLimits(position.y + lowestItem.positionDelta.y - itemHeight / 2, position.y + position.height);
    }


    @Override
    protected void onPositionChanged() {
        super.onPositionChanged();
        updateItemDeltas();
    }


    private void updateItemDeltas() {
        float currentY = (float) (position.height - itemHeight);
        for (ScrItem item : items) {
            item.viewPosition.width = position.width;
            item.viewPosition.height = (float) itemHeight;
            item.positionDelta.x = 0;
            item.positionDelta.y = currentY;
            item.updateDeltas();
            currentY -= itemHeight + itemOffset;
        }

        updateScrollLimit();
    }


    @Override
    protected void onApplyParent() {
        moveItemsPositions();
    }


    private void moveItemsPositions() {
        for (ScrItem item : items) {
            item.updatePositions();
        }
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {

    }


    @Override
    public boolean checkToPerformAction() {
        if (readyToProcessItemClick && behavior != null) {
            readyToProcessItemClick = false;

            behavior.onClickItem(targetItem);
            return true;
        }

        return false;
    }


    @Override
    public boolean touchDown() {
        touched = true;
        scrollEngineYio.onTouchDown();
        checkToSelectItem();
        longTapDetector.onTouchDown(currentTouch);

        return true;
    }


    private void checkToSelectItem() {
        for (ScrItem item : items) {
            if (!item.isTouched(currentTouch)) continue;

            item.select();
            break;
        }
    }


    @Override
    public boolean touchDrag() {
        if (!touched) return false;

        scrollEngineYio.setSpeed(-1.5 * (currentTouch.y - lastTouch.y));
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
        for (ScrItem item : items) {
            if (!item.isTouched(currentTouch)) continue;

            targetItem = item;
            readyToProcessItemClick = true;
            break;
        }
    }


    @Override
    public boolean onMouseWheelScrolled(int amount) {
        if (amount == 1) {
            scrollEngineYio.giveImpulse(-0.03 * GraphicsYio.width);
        } else if (amount == -1) {
            scrollEngineYio.giveImpulse(0.03 * GraphicsYio.width);
        }
        scrollEngineYio.hardCorrection();
        return true;
    }


    public ScrollableListElement setBehavior(ScrBehavior behavior) {
        this.behavior = behavior;
        return this;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderScrollableList;
    }
}
