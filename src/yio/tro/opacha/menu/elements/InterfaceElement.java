package yio.tro.opacha.menu.elements;

import yio.tro.opacha.game.GameController;
import yio.tro.opacha.menu.ClickDetector;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.elements.gameplay.FollowGameViewElement;
import yio.tro.opacha.menu.elements.ground.GroundElement;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.stuff.GraphicsYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.RectangleYio;
import yio.tro.opacha.stuff.factor_yio.FactorYio;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;

public abstract class InterfaceElement<T extends InterfaceElement<T>> {

    public MenuControllerYio menuControllerYio;
    public int id;
    protected RectangleYio position, viewPosition;
    protected FactorYio appearFactor;
    protected boolean visible, touchable, factorMoved, shouldApplyParent;
    protected boolean captureTouch;
    private float hor, ver, cx, cy;
    protected AnimationYio animType;
    float animDistance;
    protected int spawnType, destroyType;
    protected double spawnSpeed, destroySpeed;
    protected static InterfaceElement screen = null;
    protected long touchDownTime;
    protected InterfaceElement parent;
    protected SceneYio sceneOwner;
    protected PointYio initialTouch, currentTouch, lastTouch;
    protected PointYio tempPoint;
    boolean onTopOfGameView;
    ClickDetector clickDetector;
    private String key;
    public boolean reverseAnimMode;
    boolean fakeDyingStatus;


    public InterfaceElement(MenuControllerYio menuControllerYio, int id) {
        this.menuControllerYio = menuControllerYio;
        this.id = id;

        appearFactor = new FactorYio();
        position = new RectangleYio();
        viewPosition = new RectangleYio(-1, -1, -1, -1); // it has to be not zero
        initialTouch = new PointYio();
        currentTouch = new PointYio();
        lastTouch = new PointYio();
        tempPoint = new PointYio();
        clickDetector = new ClickDetector();
        visible = false;
        touchable = true;
        shouldApplyParent = true;
        parent = null;
        sceneOwner = null;
        onTopOfGameView = false;
        captureTouch = true;
        key = null;
        reverseAnimMode = false;
        fakeDyingStatus = false;

        spawnType = 6;
        spawnSpeed = 2;
        destroyType = 6;
        destroySpeed = 2;

        setParent(screen);
        setAnimation(AnimationYio.none); // after setParent()
    }


    public static void initScreenElement(MenuControllerYio menuControllerYio) {
        screen = new ButtonYio(menuControllerYio, -1);
        screen.getFactor().setValues(1, 0);
        screen.setPosition(0, 0, 1, 1);
    }


    public void moveElement() {
        if (!visible) return;

        factorMoved = appearFactor.move();
        checkToCancelVisibility();

        move();

        applyParentViewPosition();
    }


    public float getAlpha() {
        if (appearFactor.isInAppearState() && appearFactor.get() < 0.35) return 0;

        return appearFactor.get();
    }


    private void limitByParent() {
        if (!factorMoved) return;
        if (parent == screen) return;
        if (parent instanceof GroundElement) return;
        if (parent instanceof FollowGameViewElement) return;

        parent.limitChild(this);
    }


    protected void limitChild(InterfaceElement child) {
        RectangleYio pos = viewPosition;

        if (child.viewPosition.x + child.viewPosition.width > pos.x + pos.width) {
            child.viewPosition.x = pos.x + pos.width - child.viewPosition.width;
        }

        if (child.viewPosition.x < pos.x) {
            child.viewPosition.x = pos.x;
            child.viewPosition.width = pos.width;
        }

        if (child.viewPosition.y + child.viewPosition.height > pos.y + pos.height) {
            child.viewPosition.y = pos.y + pos.height - child.viewPosition.height;
        }

        if (child.viewPosition.y < pos.y) {
            child.viewPosition.y = pos.y;
            child.viewPosition.height = pos.height;
        }
    }


    private void applyParentViewPosition() {
        if (parent == screen || !shouldApplyParent) {
            onApplyParent();
            return;
        }

        viewPosition.x += parent.viewPosition.x;
        viewPosition.y += parent.viewPosition.y;

        limitByParent();
        onApplyParent();
    }


    protected void onApplyParent() {
        // nothing by default
    }


    private void checkToCancelVisibility() {
        if (!factorMoved) return;
        if (!appearFactor.isInDestroyState()) return;
        if (!isCompletelyDestroyed()) return;

        visible = false;
        onVisibilityCanceled();
    }


    private boolean isCompletelyDestroyed() {
        if (appearFactor.get() == 0) return true;
        if (hasParent() && parent.isCompletelyDestroyed()) return true;
        return false;
    }


    protected void onVisibilityCanceled() {
        //
    }


    protected abstract T getThis();


    public abstract void move();


    protected void updateViewPosition() {
        switch (animType) {
            case none:
                viewPosition.setBy(position);
                break;
            case def:
                animDefault();
                break;
            case up:
                animUp();
                break;
            case down:
                animDown();
                break;
            case center:
                animFromCenter();
                break;
            case down_short:
                animDownShort();
                break;
            case right:
                animRight();
                break;
            case vertical_directed:
                animVerticalDirected();
                break;
            case left:
                animLeft();
                break;
            case horizontal_directed:
                animHorizontalDirected();
                break;
        }
    }


    private void animLeft() {
        viewPosition.setBy(position);
        viewPosition.x -= (1 - appearFactor.get()) * 1.2f * position.width;
    }


    private void animHorizontalDirected() {
        viewPosition.setBy(position);

        if (!reverseAnimMode) {
            if (appearFactor.isInAppearState()) {
                viewPosition.x += (1 - appearFactor.get()) * 0.5f * position.width;
            } else {
                viewPosition.x -= (1 - appearFactor.get()) * position.width;
            }
        } else {
            if (appearFactor.isInAppearState()) {
                viewPosition.x -= (1 - appearFactor.get()) * position.width;
            } else {
                viewPosition.x += (1 - appearFactor.get()) * 0.5f * position.width;
            }
        }
    }


    private void animVerticalDirected() {
        viewPosition.setBy(position);

        if (reverseAnimMode) {
            if (appearFactor.isInAppearState()) {
                viewPosition.y += (1 - appearFactor.get()) * 0.5f * position.height;
            } else {
                viewPosition.y -= (1 - appearFactor.get()) * position.height;
            }
        } else {
            if (appearFactor.isInAppearState()) {
                viewPosition.y -= (1 - appearFactor.get()) * position.height;
            } else {
                viewPosition.y += (1 - appearFactor.get()) * 0.5f * position.height;
            }
        }
    }


    private void animRight() {
        viewPosition.setBy(position);
        viewPosition.x += (1 - appearFactor.get()) * (GraphicsYio.width - position.x);
    }


    protected void animDownShort() {
        if (animDistance == -1) {
            initAnimDistance();
        }

        viewPosition.setBy(position);
        viewPosition.y -= (1 - appearFactor.get()) * animDistance;
    }


    protected void initAnimDistance() {
        animDistance = 2 * position.height;
        if (animDistance > 0.3f * GraphicsYio.width) {
            animDistance = 0.3f * GraphicsYio.width;
        }
        if (animDistance < 0.1f * GraphicsYio.width) {
            animDistance = 0.1f * GraphicsYio.width;
        }
    }


    protected void animFromCenter() {
        hor = (float) (0.5 * appearFactor.get() * position.width);
        ver = (float) (0.5 * appearFactor.get() * position.height);
        cx = position.x + 0.5f * position.width;
        cy = position.y + 0.5f * position.height;
        cx -= (1 - appearFactor.get()) * (cx - 0.5f * GraphicsYio.width);
        cy -= (1 - appearFactor.get()) * (cy - 0.5f * GraphicsYio.height);
        viewPosition.set(cx - hor, cy - ver, 2 * hor, 2 * ver);
    }


    protected void animDown() {
        viewPosition.setBy(position);
        viewPosition.y -= (1 - appearFactor.get()) * (position.y + position.height);
    }


    protected void animUp() {
        viewPosition.setBy(position);
        viewPosition.y += ((1 - appearFactor.get()) * (GraphicsYio.height - position.y));
    }


    protected void animDefault() {
        hor = 0.5f * appearFactor.get() * position.width;
        ver = 0.5f * appearFactor.get() * position.height;
        cx = position.x + 0.5f * position.width;
        cy = position.y + 0.5f * position.height;
        viewPosition.set(cx - hor, cy - ver, 2 * hor, 2 * ver);
    }


    public T setAnimation(AnimationYio animType) {
        this.animType = animType;

        animDistance = -1;
        return getThis();
    }


    public FactorYio getFactor() {
        return appearFactor;
    }


    public RectangleYio getPosition() {
        return position;
    }


    public RectangleYio getViewPosition() {
        return viewPosition;
    }


    public boolean isVisible() {
        return visible && viewPosition.height != 0 && viewPosition.width != 0;
    }


    public boolean isViewPositionNotUpdatedYet() {
        return hasParent() && getFactor().get() < 0.001;
    }


    public boolean isTouchable() {
        return touchable;
    }


    protected void onPositionChanged() {
        if (parent != null) {
            parent.onChildPositionChanged(this);
        }
    }


    public void relocate(float horizontal, float vertical) {
        position.x += horizontal;
        position.y += vertical;
        onPositionChanged();
    }


    protected void onChildPositionChanged(InterfaceElement child) {
        // nothing by default
    }


    protected void onSizeChanged() {
        // nothing for now
    }


    public T setPosition(double x, double y) {
        position.x = (float) (x * GraphicsYio.width);
        position.y = (float) (y * GraphicsYio.height);
        onPositionChanged();
        return getThis();
    }


    public T setPosition(double x, double y, double width, double height) {
        setPosition(x, y);
        return setSize(width, height);
    }


    public T setSize(InterfaceElement src) {
        return setSize(src.position.width / GraphicsYio.width, src.position.height / GraphicsYio.height);
    }


    public T setSize(double width, double height) {
        position.width = (float) (width * GraphicsYio.width);
        position.height = (float) (height * GraphicsYio.height);
        onSizeChanged();
        return getThis();
    }


    public T setSize(double size) {
        return setSize(size, GraphicsYio.convertToHeight(size));
    }


    public T setPosition(double x, double y, double size) {
        setPosition(x, y);
        return setSize(size);
    }


    public T setTouchable(boolean touchable) {
        this.touchable = touchable;
        return getThis();
    }


    public T setKey(String key) {
        this.key = key;
        return getThis();
    }


    public T setFakeDyingStatusEnabled(boolean value) {
        fakeDyingStatus = value;
        return getThis();
    }


    public T setAppearParameters(int spawnType, double spawnSpeed) {
        this.spawnType = spawnType;
        this.spawnSpeed = spawnSpeed;
        return getThis();
    }


    public T setDestroyParameters(int destroyType, double destroySpeed) {
        this.destroyType = destroyType;
        this.destroySpeed = destroySpeed;
        return getThis();
    }


    protected float getParentCompensationX(InterfaceElement anotherElement) {
        return getParentCompensationX(anotherElement.parent == parent);
    }


    protected float getParentCompensationX(boolean sameParent) {
        if (sameParent) {
            return 0;
        }

        return parent.position.x;
    }


    protected float getParentCompensationY(InterfaceElement anotherElement) {
        return getParentCompensationY(anotherElement.parent == parent);
    }


    protected float getParentCompensationY(boolean sameParent) {
        if (sameParent) {
            return 0;
        }

        return parent.position.y;
    }


    public T clone(InterfaceElement src) {
        setParent(src.parent);
        RectangleYio position = src.position;
        setPosition(
                position.x / GraphicsYio.width,
                position.y / GraphicsYio.height,
                position.width / GraphicsYio.width,
                position.height / GraphicsYio.height);

        return getThis();
    }


    public T alignTop() {
        return alignTop(0);
    }


    public T alignTop(double offset) {
        return alignTop(parent, offset);
    }


    public T alignTop(InterfaceElement element) {
        return alignTop(element, 0);
    }


    public T alignTop(InterfaceElement element, double offset) {
        if (element == parent) {
            return alignTop(element, offset, true);
        }

        return alignTop(element, offset, false);
    }


    private T alignTop(InterfaceElement element, double offset, boolean inside) {
        if (inside) {
            position.y = (float) (element.position.y
                    + element.position.height
                    - position.height
                    - offset * GraphicsYio.height
                    - getParentCompensationY(element));
        } else {
            position.y = (float) (element.position.y
                    + element.position.height
                    + offset * GraphicsYio.height
                    - getParentCompensationY(element));
        }

        onPositionChanged();
        return getThis();
    }


    public T alignBottom() {
        return alignBottom(0);
    }


    public T alignBottom(double offset) {
        return alignBottom(parent, offset);
    }


    public T alignBottom(InterfaceElement element) {
        return alignBottom(element, 0);
    }


    public T alignBottom(InterfaceElement element, double offset) {
        if (element == parent) {
            return alignBottom(element, offset, true);
        }

        return alignBottom(element, offset, false);
    }


    private T alignBottom(InterfaceElement element, double offset, boolean inside) {
        if (inside) {
            position.y = (float) (element.position.y
                    + offset * GraphicsYio.height
                    - getParentCompensationY(element));
        } else {
            position.y = (float) (element.position.y
                    - position.height
                    - offset * GraphicsYio.height
                    - getParentCompensationY(element));
        }

        onPositionChanged();
        return getThis();
    }


    public T alignRight() {
        return alignRight(0);
    }


    public T alignRight(double offset) {
        return alignRight(parent, offset);
    }


    public T alignRight(InterfaceElement element) {
        return alignRight(element, 0);
    }


    public T alignRight(InterfaceElement element, double offset) {
        if (element == parent) {
            return alignRight(element, offset, true);
        }

        return alignRight(element, offset, false);
    }


    private T alignRight(InterfaceElement element, double offset, boolean inside) {
        if (inside) {
            position.x = (float) (element.position.x
                    + element.position.width
                    - position.width
                    - offset * GraphicsYio.width
                    - getParentCompensationX(element));
        } else {
            position.x = (float) (element.position.x
                    + element.position.width
                    + offset * GraphicsYio.width
                    - getParentCompensationX(element));
        }

        onPositionChanged();
        return getThis();
    }


    public T alignLeft() {
        return alignLeft(0);
    }


    public T alignLeft(double offset) {
        return alignLeft(parent, offset);
    }


    public T alignLeft(InterfaceElement element) {
        return alignLeft(element, 0);
    }


    public T alignLeft(InterfaceElement element, double offset) {
        if (element == parent) {
            return alignLeft(element, offset, true);
        }

        return alignLeft(element, offset, false);
    }


    private T alignLeft(InterfaceElement element, double offset, boolean inside) {
        if (inside) {
            position.x = (float) (element.position.x
                    + offset * GraphicsYio.width
                    - getParentCompensationX(element));
        } else {
            position.x = (float) (element.position.x
                    - position.width
                    - offset * GraphicsYio.width
                    - getParentCompensationX(element));
        }

        onPositionChanged();
        return getThis();
    }


    public T centerHorizontal() {
        centerHorizontal(parent);
        position.x -= getParentCompensationX(false);
        return getThis();
    }


    public T centerHorizontal(InterfaceElement element) {
        position.x = element.position.x + (element.position.width - position.width) / 2;
        onPositionChanged();
        return getThis();
    }


    public T centerVertical() {
        centerVertical(parent);
        position.y -= getParentCompensationY(false);
        return getThis();
    }


    public T centerVertical(InterfaceElement element) {
        position.y = element.position.y + (element.position.height - position.height) / 2;
        onPositionChanged();
        return getThis();
    }


    public T setParent(InterfaceElement parent) {
        this.parent = parent;
        setAnimation(AnimationYio.none);

        if (parent != null) {
            parent.onChildAdded(this);
        }

        return getThis();
    }


    public InterfaceElement getParent() {
        return parent;
    }


    protected void onChildAdded(InterfaceElement child) {
        // nothing by default
    }


    public void destroy() {
        appearFactor.destroy(destroyType, destroySpeed);
        reverseAnimMode = false;
        onDestroy();
    }


    public boolean getDyingStatus() {
        if (fakeDyingStatus) {
            return false;
        }

        if (hasParent() && parent.fakeDyingStatus) {
            return false;
        }

        if (reverseAnimMode) {
            return !getFactor().isInDestroyState();
        }

        return getFactor().isInDestroyState();
    }


    public abstract void onDestroy();


    public void appear() {
        visible = true;
        appearFactor.appear(spawnType, spawnSpeed);
        reverseAnimMode = false;
        onAppear();
    }


    public abstract void onAppear();


    public abstract boolean checkToPerformAction();


    private void updateCurrentTouch(int screenX, int screenY) {
         currentTouch.x = screenX;
         currentTouch.y = screenY;
    }


    protected boolean isClicked() {
        return clickDetector.isClicked();
    }


    public boolean isTouched(PointYio touchPoint) {
        return isTouchInsideRectangle(touchPoint, viewPosition);
    }


    public abstract boolean touchDown();


    public boolean touchDownElement(int screenX, int screenY, int pointer, int button) {
        if (appearFactor.getGravity() < 0) return false;

        updateCurrentTouch(screenX, screenY);

        lastTouch.setBy(currentTouch);
        initialTouch.setBy(currentTouch);
        touchDownTime = System.currentTimeMillis();
        clickDetector.onTouchDown(currentTouch);
        return touchDown();
    }


    public abstract boolean touchDrag();


    public boolean touchDragElement(int screenX, int screenY, int pointer) {
        updateCurrentTouch(screenX, screenY);

        boolean b = touchDrag();
        lastTouch.setBy(currentTouch);
        clickDetector.onTouchDrag(currentTouch);
        return b;
    }


    public abstract boolean touchUp();


    public boolean touchUpElement(int screenX, int screenY, int pointer, int button) {
        if (appearFactor.getGravity() < 0) return false;
        updateCurrentTouch(screenX, screenY);
        clickDetector.onTouchUp(currentTouch);

        return touchUp();
    }


    public T stretchVertically(InterfaceElement src, double offset) {
        position.height = (float) (src.position.height + offset * GraphicsYio.height);
        return getThis();
    }


    public abstract RenderInterfaceElement getRenderSystem();


    protected boolean isTouchInsideRectangle() {
        return isTouchInsideRectangle(currentTouch, viewPosition);
    }


    public static boolean isTouchInsideRectangle(PointYio touchPoint, RectangleYio touchRectangle) {
        return isTouchInsideRectangle(touchPoint.x, touchPoint.y, touchRectangle);
    }


    public static boolean isTouchInsideRectangle(float touchX, float touchY, RectangleYio rectangleYio) {
        return isTouchInsideRectangle(touchX, touchY, rectangleYio.x, rectangleYio.y, rectangleYio.width, rectangleYio.height);
    }


    public static boolean isTouchInsideRectangle(float touchX, float touchY, RectangleYio rectangleYio, float horOffset, float verOffset) {
        return isTouchInsideRectangle(touchX, touchY, rectangleYio.x, rectangleYio.y, rectangleYio.width, rectangleYio.height, horOffset, verOffset);
    }


    public static boolean isTouchInsideRectangle(float touchX, float touchY, float x, float y, float width, float height) {
        return isTouchInsideRectangle(touchX, touchY, x, y, width, height, 0, 0);
    }


    public static boolean isTouchInsideRectangle(float touchX, float touchY, float x, float y, float width, float height, float horOffset, float verOffset) {
        if (touchX < x - horOffset) return false;
        if (touchX > x + width + horOffset) return false;
        if (touchY < y - verOffset) return false;
        if (touchY > y + height + verOffset) return false;
        return true;
    }


    public boolean onMouseWheelScrolled(int amount) {
        return false;
    }


    public void pressArtificially() {
        // nothing by default
    }


    public void forceDestroyToEnd() {
        // nothing by default
    }


    public void onSceneEndCreation() {
        // nothing by default
    }


    public boolean onKeyDown(int keycode) {
        // nothing by default
        return false;
    }


    public T setOnTopOfGameView(boolean onTopOfGameView) {
        this.onTopOfGameView = onTopOfGameView;
        return getThis();
    }


    public boolean compareGvStatus(boolean onTopOfGameView) {
        return this.onTopOfGameView == onTopOfGameView;
    }


    public void onAppPause() {

    }


    public void onAppResume() {

    }


    public void setSceneOwner(SceneYio sceneOwner) {
        this.sceneOwner = sceneOwner;
    }


    public SceneYio getSceneOwner() {
        return sceneOwner;
    }


    public boolean isCaptureTouch() {
        return captureTouch;
    }


    public boolean isTouchedBy(PointYio touchPoint) {
        return isTouchInsideRectangle(touchPoint, viewPosition);
    }


    public void setCaptureTouch(boolean captureTouch) {
        this.captureTouch = captureTouch;
    }


    public GameController getGameController() {
        return menuControllerYio.yioGdxGame.gameController;
    }


    public PointYio getTagPosition(String argument) {
        // default, probably shouldn't be used
        tempPoint.set(
                viewPosition.x + viewPosition.width / 2,
                viewPosition.y + viewPosition.height / 2
        );

        return tempPoint;
    }


    public boolean isTagTouched(String argument, PointYio touchPoint) {
        return getTagPosition(argument).distanceTo(touchPoint) < 0.1f * GraphicsYio.width;
    }


    public String getKey() {
        return key;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }


    public boolean hasParent() {
        return parent != null && parent != screen && !(parent instanceof GroundElement);
    }


    public void enableReverseAnimMode() {
        reverseAnimMode = true;
    }
}
