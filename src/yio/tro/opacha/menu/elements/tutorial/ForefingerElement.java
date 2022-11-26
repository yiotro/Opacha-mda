package yio.tro.opacha.menu.elements.tutorial;

import com.badlogic.gdx.graphics.OrthographicCamera;
import yio.tro.opacha.game.CameraController;
import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.gameplay.model.Planet;
import yio.tro.opacha.menu.MenuControllerYio;
import yio.tro.opacha.menu.elements.ButtonYio;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.menu_renders.MenuRenders;
import yio.tro.opacha.menu.menu_renders.RenderInterfaceElement;
import yio.tro.opacha.menu.scenes.Scenes;
import yio.tro.opacha.stuff.*;
import yio.tro.opacha.stuff.factor_yio.FactorYio;

import java.util.ArrayList;
import java.util.Iterator;

public class ForefingerElement extends InterfaceElement<ForefingerElement> {

    public ForefingerModeType mode;
    public PointYio defTarget, hook;
    public float touchOffset, radius, viewRadius;
    public FactorYio selectionFactor, effectFactor, blackoutFactor;
    boolean touched, readyForEffect, readyForBlackout;
    public float effectRadius;
    RepeatYio<ForefingerElement> repeatStartEffect, repeatStartBlackout;
    public RectangleYio blackoutPosition;
    public ArrayList<RectangleYio> blackoutBorders;
    InterfaceElement uiTarget;
    String tagArgument;
    private float blackoutRadius;
    double angle, cutAngle;
    public double viewAngle;
    public PointYio viewPoint;
    Object goTarget;
    public double targetAngle;
    FactorYio swipeDelayFactor, swipeMovementFactor, swipePrepareFactor;
    LongTapDetector longTapDetector;


    public ForefingerElement(MenuControllerYio menuControllerYio, int id) {
        super(menuControllerYio, id);

        defTarget = new PointYio();
        hook = new PointYio();
        mode = ForefingerModeType.def;
        viewRadius = 0;
        selectionFactor = new FactorYio();
        touched = false;
        effectFactor = new FactorYio();
        effectRadius = 0;
        readyForEffect = false;
        blackoutFactor = new FactorYio();
        readyForBlackout = false;
        blackoutPosition = new RectangleYio();
        initBorders();
        uiTarget = null;
        tagArgument = null;
        viewAngle = 0;
        resetAngle();
        cutAngle = 0.01;
        viewPoint = new PointYio();
        goTarget = null;
        swipeDelayFactor = new FactorYio();
        swipeMovementFactor = new FactorYio();
        swipePrepareFactor = new FactorYio();

        initMetrics();
        initRepeats();
        initLongTapDetector();
    }


    private void initLongTapDetector() {
        longTapDetector = new LongTapDetector() {
            @Override
            public void onLongTapDetected() {
                ForefingerElement.this.onLongTapDetected();
            }
        };
    }


    private void onLongTapDetected() {
        if (mode != ForefingerModeType.long_tap_planet) return;
        destroy();
    }


    private void initBorders() {
        blackoutBorders = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            blackoutBorders.add(new RectangleYio());
        }
    }


    private void initRepeats() {
        repeatStartEffect = new RepeatYio<ForefingerElement>(this, -1) {
            @Override
            public void performAction() {
                parent.startEffect();
            }
        };

        repeatStartBlackout = new RepeatYio<ForefingerElement>(this, -1) {
            @Override
            public void performAction() {
                parent.startBlackout();
            }
        };
    }


    private void initMetrics() {
        radius = 0.05f * GraphicsYio.width;
        touchOffset = 2 * radius;
        blackoutRadius = 8 * radius;
    }


    @Override
    protected ForefingerElement getThis() {
        return this;
    }


    private void resetAngle() {
        setAngle(Math.PI / 2);
    }


    public void setAngle(double angle) {
        this.angle = angle;
        adjustViewAngle();
    }


    public void adjustViewAngle() {
        while (viewAngle > angle + Math.PI) {
            viewAngle -= 2 * Math.PI;
        }

        while (viewAngle < angle - Math.PI) {
            viewAngle += 2 * Math.PI;
        }
    }


    @Override
    public void move() {
        updateViewRadius();
        updateHook();
        updateViewPosition();
        updateViewPoint();
        moveSelection();
        moveEffect();
        moveBlackout();
        updateViewAngle();
        moveSwipeFactors();
        longTapDetector.move();
    }


    private void moveSwipeFactors() {
        if (mode != ForefingerModeType.swipe_from_planet) return;

        swipeDelayFactor.move();
        swipeMovementFactor.move();
        swipePrepareFactor.move();

        if (swipeDelayFactor.get() == 1 && !swipeMovementFactor.isInAppearState()) {
            swipeDelayFactor.reset();
            swipeMovementFactor.reset();
            swipeMovementFactor.appear(1, 1.5);
        }

        if (swipeMovementFactor.get() == 1 && !swipePrepareFactor.isInAppearState()) {
            swipePrepareFactor.reset();
            swipePrepareFactor.appear(1, 0.2);
        }

        if (swipePrepareFactor.get() == 1 && !swipeDelayFactor.isInAppearState()) {
            swipeMovementFactor.reset();
            swipePrepareFactor.reset();
            swipeDelayFactor.reset();
            swipeDelayFactor.appear(1, 0.25);
            appearFactor.reset();
            appearFactor.appear(6, 2);
            appearFactor.move();
            appearFactor.move();
        }
    }


    protected void updateViewAngle() {
        if (angle == viewAngle) return;

        viewAngle += 0.15f * (angle - viewAngle);

        if (Math.abs(angle - viewAngle) < cutAngle) {
            viewAngle = angle;
        }
    }


    private void moveBlackout() {
        if (readyForBlackout) {
            repeatStartBlackout.move();
            return;
        }

        blackoutFactor.move();
        updateBlackoutPosition();
        updateBlackoutBorders();
    }


    private void updateBlackoutBorders() {
        Iterator<RectangleYio> iterator = blackoutBorders.iterator();

        iterator.next().set(
                0, 0,
                GraphicsYio.width, blackoutPosition.y
        );

        iterator.next().set(
                0, blackoutPosition.y,
                blackoutPosition.x, blackoutPosition.height
        );

        iterator.next().set(
                0, blackoutPosition.y + blackoutPosition.height,
                GraphicsYio.width, GraphicsYio.height - (blackoutPosition.y + blackoutPosition.height)
        );

        iterator.next().set(
                blackoutPosition.x + blackoutPosition.width, blackoutPosition.y,
                GraphicsYio.width - (blackoutPosition.x + blackoutPosition.width), blackoutPosition.height
        );
    }


    private void updateBlackoutPosition() {
        blackoutPosition.x = hook.x - blackoutRadius;
        blackoutPosition.y = hook.y - blackoutRadius;
        blackoutPosition.width = 2 * blackoutRadius;
        blackoutPosition.height = 2 * blackoutRadius;
    }


    public void startBlackoutWithDelay() {
        blackoutFactor.setValues(0, 0);
        blackoutFactor.stop();

        readyForBlackout = true;

        repeatStartBlackout.setCountDown(10);
    }


    public void startBlackout() {
        readyForBlackout = false;

        blackoutFactor.setValues(0, 0);
        blackoutFactor.appear(3, 1);
    }


    private void moveEffect() {
        if (readyForEffect) {
            repeatStartEffect.move();
            return;
        }

        effectFactor.move();

        effectRadius = 3 * effectFactor.get() * radius;
    }


    public void startEffectWithDelay() {
        readyForEffect = true;

        repeatStartEffect.setCountDown(15);
    }


    public void startEffect() {
        readyForEffect = false;

        effectFactor.setValues(0, 0);
        effectFactor.appear(3, 1);
    }


    private void moveSelection() {
        if (touched) return;

        selectionFactor.move();
    }


    public void select() {
        selectionFactor.setValues(1, 0);
        selectionFactor.destroy(1, 3);
    }


    public boolean isSelected() {
        return selectionFactor.get() > 0;
    }


    private void updateViewRadius() {
        viewRadius = (appearFactor.get() + 0.5f * selectionFactor.get()) * radius;
    }


    private void updateViewPoint() {
        viewPoint.setBy(hook);
        viewPoint.relocateRadial(0.25f * radius, viewAngle - Math.PI / 2);
        viewPoint.relocateRadial(1.2f * radius, viewAngle + Math.PI);
    }


    @Override
    protected void updateViewPosition() {
        viewPosition.x = hook.x - viewRadius;
        viewPosition.y = hook.y - viewRadius;
        viewPosition.width = 2 * viewRadius;
        viewPosition.height = 2 * viewRadius;
    }


    private void updateHook() {
        switch (mode) {
            case def:
                updateHookDefault();
                break;
            case ui_element:
                updateHookForUiElement();
                break;
            case ui_tag:
                updateHookForUiTag();
                break;
            case planet:
                updateHookForPlanet();
                break;
            case swipe_from_planet:
                updateHookForSwipeFromPlanet();
                break;
            case long_tap_planet:
                updateHookForPlanet();
                break;
        }
    }


    private void updateHookForSwipeFromPlanet() {
        Planet planet = (Planet) goTarget;
        CameraController cameraController = getGameController().cameraController;
        OrthographicCamera orthoCam = cameraController.orthoCam;

        PointYio center = planet.viewPosition.center;
        tempPoint.setBy(center);
        tempPoint.relocateRadial(swipeMovementFactor.get() * 0.5f * GraphicsYio.width, targetAngle);
        hook.x = (tempPoint.x - orthoCam.position.x) / orthoCam.zoom + 0.5f * GraphicsYio.width;
        hook.y = (tempPoint.y - orthoCam.position.y) / orthoCam.zoom + 0.5f * GraphicsYio.height;
    }


    private void updateHookForPlanet() {
        Planet planet = (Planet) goTarget;
        CameraController cameraController = getGameController().cameraController;
        OrthographicCamera orthoCam = cameraController.orthoCam;

        PointYio center = planet.viewPosition.center;
        hook.x = (center.x - orthoCam.position.x) / orthoCam.zoom + 0.5f * GraphicsYio.width;
        hook.y = (center.y - orthoCam.position.y) / orthoCam.zoom + 0.5f * GraphicsYio.height;
    }


    private void updateHookForUiTag() {
        hook.setBy(uiTarget.getTagPosition(tagArgument));
    }


    private void updateHookForUiElement() {
        if (uiTarget == null) return;
        RectangleYio viewPosition = uiTarget.getViewPosition();
        hook.set(
                viewPosition.x + viewPosition.width / 2,
                viewPosition.y + viewPosition.height / 2
        );
    }


    private void updateHookDefault() {
        hook.setBy(defTarget);
    }


    public void setTarget(double x, double y) {
        mode = ForefingerModeType.def;
        defTarget.set(x, y);
        onTargetSet();
    }


    public void setTarget(InterfaceElement target) {
        mode = ForefingerModeType.ui_element;
        uiTarget = target;
        onTargetSet();
    }


    public void setTarget(InterfaceElement target, String tag) {
        mode = ForefingerModeType.ui_tag;
        uiTarget = target;
        tagArgument = tag;
        onTargetSet();
    }


    public void setTarget(Planet planet, boolean longTap) {
        if (longTap) {
            mode = ForefingerModeType.long_tap_planet;
        } else {
            mode = ForefingerModeType.planet;
        }
        goTarget = planet;
        onTargetSet();
    }


    public void setTarget(Planet planet, double angle) {
        mode = ForefingerModeType.swipe_from_planet;
        goTarget = planet;
        targetAngle = angle;
        swipeMovementFactor.reset();
        swipePrepareFactor.reset();
        swipeDelayFactor.reset();
        swipeDelayFactor.appear(1, 0.2);
        onTargetSet();
    }


    private void onTargetSet() {
        startEffectWithDelay();
        startBlackoutWithDelay();
        updateAngle();
        checkToShowNotification();
    }


    private void checkToShowNotification() {
        updateHook();

        if (hook.y > 0.9f * GraphicsYio.height) return;

        switch (mode) {
            default:
                break;
            case ui_element:
                if (uiTarget != null && uiTarget instanceof ButtonYio) {
                    Scenes.notification.show("press_button", false);
                }
                break;
            case swipe_from_planet:
                Scenes.notification.show("swipe_from_planet", false);
                break;
            case long_tap_planet:
                Scenes.notification.show("long_tap_planet", false);
                break;
        }
    }


    private void updateAngle() {
        viewAngle = 0.75 * Math.PI;
        resetAngle();
        checkToAdjustAngleByScreenBorders();
    }


    private void checkToAdjustAngleByScreenBorders() {
        updateHook();
        if (!isHookTooCloseToScreenBorders()) return;

        tempPoint.set(GraphicsYio.width / 2, GraphicsYio.height / 2);
        setAngle(tempPoint.angleTo(hook));
    }


    boolean isHookTooCloseToScreenBorders() {
        if (hook.x < 2 * radius) return true;
        if (hook.y < 2 * radius) return true;
        if (hook.x > GraphicsYio.width - 2 * radius) return true;
        if (hook.y > GraphicsYio.height - 2 * radius) return true;

        return false;
    }


    @Override
    public void onDestroy() {
        touched = false;

        blackoutFactor.destroy(1, 3);
        hideNotification();
    }


    private void hideNotification() {
        Scenes.notification.destroy();
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
        touched = isTouchValid();

        if (touched) {
            select();
            longTapDetector.onTouchDown(currentTouch);
            return false;
        }

        return true;
    }


    @Override
    public boolean touchDrag() {
        if (touched) {
            longTapDetector.onTouchDrag(currentTouch);
        }

        return false;
    }


    @Override
    public float getAlpha() {
        switch (mode) {
            default:
                return getFactor().get();
            case swipe_from_planet:
                return getFactor().get() * (1 - swipeMovementFactor.get());
        }
    }


    @Override
    public boolean touchUp() {
        if (touched) {
            touched = false;

            if (isClicked()) {
                onClick();
            }

            if (mode == ForefingerModeType.swipe_from_planet) {
                destroy();
            }

            longTapDetector.onTouchUp(currentTouch);

            return false;
        }

        return false;
    }


    private void onClick() {
        if (!isTouchValid()) return;
        if (!isModeClickable()) return;
        destroy();
    }


    private boolean isModeClickable() {
        switch (mode) {
            default:
                return true;
            case swipe_from_planet:
            case long_tap_planet:
                return false;
        }
    }


    private boolean isTouchValid() {
        GameController gameController = getGameController();
        gameController.updateTouchPoints((int) currentTouch.x, (int) currentTouch.y);
        switch (mode) {
            default:
                return false;
            case def:
                return currentTouch.distanceTo(hook) < radius + touchOffset;
            case ui_element:
                return uiTarget != null && uiTarget.isTouchedBy(currentTouch);
            case ui_tag:
                return uiTarget.isTagTouched(tagArgument, currentTouch);
            case planet:
            case swipe_from_planet:
            case long_tap_planet:
                return ((Planet) goTarget).isTouchedBy(gameController.currentTouchConverted, gameController.cameraController.viewZoomLevel);
        }
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderForefinger;
    }
}
