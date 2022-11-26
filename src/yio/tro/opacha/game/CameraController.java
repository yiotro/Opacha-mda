package yio.tro.opacha.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import yio.tro.opacha.SettingsManager;
import yio.tro.opacha.Yio;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.stuff.*;

public class CameraController implements TouchableYio {


    public static final int DOUBLE_TAP_DELAY = 300;
    public static final double ZOOM_CUT = 0.001;
    YioGdxGame yioGdxGame;
    GameController gameController;
    public OrthographicCamera orthoCam;
    boolean blockDragMovement, kineticsEnabled, boundsEnabled;
    int w, h;
    float boundWidth, boundHeight, zoomMinimum, zoomMaximum;
    float camDx, camDy, camDz, targetZoomLevel;
    public float viewZoomLevel, comfortableZoomLevel;
    long touchDownTime, lastTapTime;
    RectangleYio field; // bounds of level
    RectangleYio frame; // what is visible
    RectangleYio lastMultiTouch, currentMultiTouch;
    double zoomValues[][], kineticsSpeed;
    PointYio currentTouch, position, viewPosition, defaultDragBounds, backVisBounds;
    PointYio delta, kinetics, actualDragBounds, lastTapPoint;
    boolean locked, slowModeEnabled;
    private float bottomSpecialOffset;


    public CameraController(GameController gameController) {
        this.gameController = gameController;
        yioGdxGame = gameController.yioGdxGame;
        w = gameController.w;
        h = gameController.h;

        currentTouch = new PointYio();
        position = new PointYio();
        viewPosition = new PointYio();
        field = new RectangleYio();
        frame = new RectangleYio();
        lastTapPoint = new PointYio();
        currentMultiTouch = new RectangleYio();
        lastMultiTouch = new RectangleYio();
        defaultDragBounds = new PointYio();
        backVisBounds = new PointYio();
        delta = new PointYio();
        kinetics = new PointYio();
        actualDragBounds = new PointYio();
        locked = false;
        slowModeEnabled = false;

        zoomMinimum = 0.3f;
        kineticsSpeed = 0.01 * w;
        comfortableZoomLevel = 0.55f;
        bottomSpecialOffset = 0.15f * GraphicsYio.width;
        kineticsEnabled = false;
        setBoundsEnabled(true);
    }


    public void initLevels() {
        double[][] lowGraphicsZooms, normalGraphicsZooms, highGraphicsZooms;

        lowGraphicsZooms = new double[][]{
                {0.35, 0.5, 1.1},
                {0.35, 0.5, 1.7},
                {0.35, 0.5, 2.1},
                {0.35, 0.5, 6.5}
        };

        normalGraphicsZooms = new double[][]{
                {0.85, 1.7, 1.1},
                {0.85, 1.7, 1.7},
                {0.85, 1.7, 2.1},
                {0.85, 1.7, 6.5}
        };

        highGraphicsZooms = new double[][]{
                {1.3, 2.6, 1.1},
                {1.3, 2.6, 1.7},
                {1.3, 2.6, 2.1},
                {1.3, 2.6, 6.5}
        };

        switch (SettingsManager.getInstance().graphicsQuality) {
            case GraphicsYio.QUALITY_LOW:
                zoomValues = lowGraphicsZooms;
                break;
            case GraphicsYio.QUALITY_NORMAL:
                zoomValues = normalGraphicsZooms;
                break;
            case GraphicsYio.QUALITY_HIGH:
                zoomValues = highGraphicsZooms;
                break;
        }

        updateUpperZoomLimit(gameController.initialLevelSize);
    }


    public void updateUpperZoomLimit(LevelSize levelSize) {
        int lsIndex = levelSize.ordinal();

        if (lsIndex >= zoomValues.length) {
            lsIndex = zoomValues.length - 1;
        }

        setZoomMaximum(zoomValues[lsIndex][2]);
    }


    public void onEndCreation() {
        initLevels();
        checkToFlyUp();
        forceToTargetPosition();
    }


    private void checkToFlyUp() {
        if (gameController.initialLevelSize != LevelSize.normal) return;
        setTargetZoomLevel(2);
    }


    public void forceToTargetPosition() {
        setBoundsEnabled(false);
        for (int i = 0; i < 30; i++) {
            move();
        }

        setBoundsEnabled(true);
        for (int i = 0; i < 20; i++) {
            move();
        }
    }


    private void updateCurrentMultiTouch() {
        currentMultiTouch.set(0, 0, Gdx.input.getX(1) - Gdx.input.getX(0), Gdx.input.getY(1) - Gdx.input.getY(0));
    }


    private void updateLastMultiTouch() {
        lastMultiTouch.setBy(currentMultiTouch);
    }


    @Override
    public boolean onTouchDown(PointYio touchPoint) {
        if (locked) return false;

        // initial touch with one finger
        if (gameController.currentTouchCount == 1) {
            touchDownTime = gameController.currentTime;
            blockDragMovement = false;
            currentTouch.setBy(touchPoint);
            delta.set(0, 0);
        }

        // multi touch
        if (gameController.currentTouchCount >= 2) {
            blockDragMovement = true;
            updateCurrentMultiTouch();
            updateLastMultiTouch();
        }

        return false;
    }


    @Override
    public boolean onTouchDrag(PointYio touchPoint) {
        if (locked) return false;

        delta.x = 1.4f * (currentTouch.x - touchPoint.x) * viewZoomLevel;
        delta.y = 1.4f * (currentTouch.y - touchPoint.y) * viewZoomLevel;

        if (!blockDragMovement) {
            position.x += delta.x;
            position.y += delta.y;

            applyBoundsToPosition();
        }

        currentTouch.setBy(touchPoint);

        // pinch to zoom
        if (gameController.currentTouchCount == 2) {
            updateCurrentMultiTouch();

            double currentDistance = Yio.distance(0, 0, currentMultiTouch.width, currentMultiTouch.height);
            double lastDistance = Yio.distance(0, 0, lastMultiTouch.width, lastMultiTouch.height);
            double zoomDelta = 0.004 * (lastDistance - currentDistance);

            changeZoomLevel(zoomDelta);

            updateLastMultiTouch();
        }

        return false;
    }


    public void setTargetZoomLevel(float targetZoomLevel) {
        this.targetZoomLevel = targetZoomLevel;
    }


    public void changeZoomLevel(double zoomDelta) {
        targetZoomLevel += zoomDelta;
        if (targetZoomLevel < zoomMinimum) targetZoomLevel = zoomMinimum;
        if (targetZoomLevel > zoomMaximum) targetZoomLevel = zoomMaximum;
    }


    private void applyBoundsToPosition() {
        if (!boundsEnabled) return;

        if (position.x > actualDragBounds.x) {
            position.x = actualDragBounds.x;
        }

        if (position.x < -actualDragBounds.x) {
            position.x = -actualDragBounds.x;
        }

        if (position.y > actualDragBounds.y) {
            position.y = actualDragBounds.y;
        }

        if (position.y < -actualDragBounds.y - bottomSpecialOffset) {
            position.y = -actualDragBounds.y - bottomSpecialOffset;
        }
    }


    @Override
    public boolean onTouchUp(PointYio touchPoint) {
        if (locked) return false;

        double speed = Yio.distance(0, 0, delta.x, delta.y);

        if (!blockDragMovement && (speed > kineticsSpeed || touchWasQuick())) {
            kineticsEnabled = true;
            kinetics.x = delta.x;
            kinetics.y = delta.y;
        }

        currentTouch.setBy(touchPoint);

        if (touchWasQuick() && gameController.currentTouchCount == 0 && speed < kineticsSpeed) {
            tapReaction(touchPoint);
        }

        return false;
    }


    public void onMouseWheelScrolled(int amount) {
        if (locked) return;

        if (amount == 1) {
            changeZoomLevel(0.5);
        } else if (amount == -1) {
            changeZoomLevel(-0.6);
        }
    }


    private void tapReaction(PointYio touchPoint) {
        long currentTapTime = System.currentTimeMillis();

        if (doubleTapConditions(currentTapTime)) {
            doubleTapReaction(touchPoint);
            forgetAboutLastTap();
        } else {
            lastTapTime = currentTapTime;
        }

        lastTapPoint.setBy(currentTouch);
    }


    private boolean doubleTapConditions(long currentTapTime) {
        if (currentTapTime - lastTapTime > DOUBLE_TAP_DELAY) return false;
        if (currentTouch.distanceTo(lastTapPoint) > 0.1f * GraphicsYio.width) return false;

        return true;
    }


    private void doubleTapReaction(PointYio touchPoint) {
        if (gameController.touchMode != null && gameController.touchMode.isDoubleClickDisabled()) return;

        if (targetZoomLevel == 0.55f) {
            setTargetZoomLevel(1.92f);

            if (targetZoomLevel > zoomMaximum) {
                targetZoomLevel = zoomMaximum;
            }

            return;
        }

        setTargetZoomLevel(0.55f);
    }


    private boolean touchWasQuick() {
        return System.currentTimeMillis() - touchDownTime < 200;
    }


    public void move() {
        updateDragBounds();
        updateField();

        moveKinetics();
        moveDrag();
        moveZoom();

        updateFrame();
        updateBackgroundVisibility();
    }


    private void updateDragBounds() {
        actualDragBounds.setBy(defaultDragBounds);
        actualDragBounds.x -= 0.4 * w * viewZoomLevel;
        actualDragBounds.y -= 0.45 * h * viewZoomLevel;

        if (actualDragBounds.x < 0) {
            actualDragBounds.x = 0;
        }

        if (actualDragBounds.y < 0) {
            actualDragBounds.y = 0;
        }
    }


    private void moveKinetics() {
        if (!kineticsEnabled) return;

        if (Yio.distance(0, 0, kinetics.x, kinetics.y) < 0.5 * kineticsSpeed) {
            kineticsEnabled = false;
        }

        position.x += kinetics.x;
        position.y += kinetics.y;

        applyBoundsToPosition();

        kinetics.x *= 0.85;
        kinetics.y *= 0.85;
    }


    private void updateBackgroundVisibility() {
        backVisBounds.setBy(defaultDragBounds);
        backVisBounds.x -= 0.5 * w * viewZoomLevel;
        backVisBounds.y -= 0.5 * h * viewZoomLevel;

        if (Math.abs(position.x) > backVisBounds.x || Math.abs(position.y) > backVisBounds.y) {
            gameController.setBackgroundVisible(true);
        } else {
            gameController.setBackgroundVisible(false);
        }
    }


    private void updateField() {
        field.x = 0.5f * w - orthoCam.position.x / orthoCam.zoom;
        field.y = 0.5f * h - orthoCam.position.y / orthoCam.zoom;
        field.width = boundWidth / orthoCam.zoom;
        field.height = boundHeight / orthoCam.zoom;
    }


    private boolean moveZoom() {
        camDz = 0.2f * (targetZoomLevel - viewZoomLevel);
        if (Math.abs(camDz) < ZOOM_CUT) return false;

        yioGdxGame.gameView.orthoCam.zoom += camDz;
        viewZoomLevel += camDz;
        yioGdxGame.gameView.updateCam();
        applyBoundsToPosition(); // bounds may change on zoom

        return true;
    }


    public boolean isCircleInViewFrame(CircleYio circleYio) {
        return isPointInViewFrame(circleYio.center, circleYio.radius);
    }


    public boolean isCircleInViewFrame(CircleYio circleYio, double offset) {
        return isPointInViewFrame(circleYio.center, (float) (circleYio.radius + offset));
    }


    public boolean isPointInViewFrame(PointYio pos, float offset) {
        if (pos.x < frame.x - offset) return false;
        if (pos.x > frame.x + frame.width + offset) return false;
        if (pos.y < frame.y - offset) return false;
        if (pos.y > frame.y + frame.height + offset) return false;

        return true;
    }


    public boolean isRectangleInViewFrame(RectangleYio pos, float offset) {
        if (pos.x + pos.width < frame.x - offset) return false;
        if (pos.x > frame.x + frame.width + offset) return false;
        if (pos.y + pos.height < frame.y - offset) return false;
        if (pos.y > frame.y + frame.height + offset) return false;

        return true;
    }


    void moveDrag() {
        if (slowModeEnabled) {
            moveCamInSlowMode();
        } else {
            camDx = 0.5f * (position.x - viewPosition.x);
            camDy = 0.5f * (position.y - viewPosition.y);
        }

        viewPosition.x += camDx;
        viewPosition.y += camDy;

        yioGdxGame.gameView.orthoCam.translate(camDx, camDy);
        yioGdxGame.gameView.updateCam();
    }


    private void moveCamInSlowMode() {
        camDx = 0.1f * (position.x - viewPosition.x);
        camDy = 0.1f * (position.y - viewPosition.y);

        if (Math.abs(camDx) + Math.abs(camDy) < 0.1) {
            slowModeEnabled = false;
        }
    }


    public void setBounds(float width, float height) {
        boundWidth = width;
        boundHeight = height;
        defaultDragBounds.set(boundWidth / 2, boundHeight / 2);
    }


    public void setZoomMaximum(double zoomMaximum) {
        this.zoomMaximum = (float) zoomMaximum;
    }


    void createCamera() {
        orthoCam = yioGdxGame.gameView.orthoCam;
        orthoCam.translate((boundWidth - w) / 2, (boundHeight - h) / 2); // focus camera of center
        targetZoomLevel = orthoCam.zoom;
        setLocked(false);
        updateFrame();
    }


    void defaultValues() {
        viewZoomLevel = 1;
        position.set(0, 0);
        viewPosition.setBy(position);
    }


    void updateFrame() {
        frame.x = (0 - 0.5f * w) * orthoCam.zoom + orthoCam.position.x;
        frame.y = (0 - 0.5f * h) * orthoCam.zoom + orthoCam.position.y;
        frame.width = w * orthoCam.zoom;
        frame.height = h * orthoCam.zoom;
    }


    boolean cameraSpeedIsSlow() {
        return Math.abs(camDx) < 0.01 * w && Math.abs(camDy) < 0.01 * w;
    }


    public double[][] getZoomValues() {
        return zoomValues;
    }


    public void forgetAboutLastTap() {
        lastTapTime = 0;
    }


    public float getTargetZoomLevel() {
        return targetZoomLevel;
    }


    public void focusOnPoint(PointYio position) {
        focusOnPoint(position.x, position.y);
    }


    public void focusOnPoint(double x, double y) {
        position.x = (float) (x - gameController.boundWidth / 2);
        position.y = (float) (y - gameController.boundHeight / 2);
    }


    public void applyMaxZoom() {
        focusOnPoint(gameController.boundWidth / 2, gameController.boundHeight / 2);
        setTargetZoomLevel(zoomMaximum);
    }


    public void goToOptimalPosition() {
        PointYio sumPoint = new PointYio();
        int n = 0;

        //
        sumPoint.set(boundWidth / 2, boundHeight / 2);
        n = 1;

        PointYio optimalPosition = new PointYio();
        optimalPosition.x = sumPoint.x / n;
        optimalPosition.y = sumPoint.y / n;

        focusOnPoint(optimalPosition);

        forceToTargetPosition();
    }


    public void checkToZoomInIfNeeded() {
        if (targetZoomLevel > comfortableZoomLevel) {
            setTargetZoomLevel(comfortableZoomLevel);
        }
    }


    public void setBoundsEnabled(boolean boundsEnabled) {
        this.boundsEnabled = boundsEnabled;
    }


    public boolean isLocked() {
        return locked;
    }


    public void setLocked(boolean locked) {
        this.locked = locked;

        if (locked) {
            gameController.currentTouchCount = 0;
        }
    }


    public float getZoomMinimum() {
        return zoomMinimum;
    }


    public void enableSlowMode() {
        slowModeEnabled = true;
    }


    @Override
    public String toString() {
        return "[CameraController: " +
                getValuesAsString() +
                "]";
    }


    public void loadFromString(String src) {
        String[] split = src.split(" ");
        float x = Float.valueOf(split[0]);
        float y = Float.valueOf(split[1]);
        float z = Float.valueOf(split[2]);
        x *= GraphicsYio.width;
        x -= boundWidth / 2;
        y *= GraphicsYio.width;
        y -= boundHeight / 2;

        setTargetZoomLevel(z);
        for (int i = 0; i < 100; i++) {
            move();
        }
        position.set(x, y);
        for (int i = 0; i < 100; i++) {
            move();
        }
    }


    public String getValuesAsString() {
        return Yio.roundUp((position.x + boundWidth / 2) / GraphicsYio.width, 2) + " " +
                Yio.roundUp((position.y + boundHeight / 2) / GraphicsYio.width, 2) + " " +
                Yio.roundUp(viewZoomLevel, 2);
    }


    public RectangleYio getFrame() {
        return frame;
    }

}
