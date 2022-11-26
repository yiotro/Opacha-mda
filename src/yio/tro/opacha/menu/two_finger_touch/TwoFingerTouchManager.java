package yio.tro.opacha.menu.two_finger_touch;

import yio.tro.opacha.stuff.TouchableYio;
import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class TwoFingerTouchManager implements TouchableYio{

    public PointYio touchPoint;
    public ArrayList<TfPoint> tfPoints;
    double angle, previousAngle;
    public int counter, previousCounter;
    TwoFingerListener listener;
    ObjectPoolYio<TfPoint> poolPoints;
    int currentFrameTouchCount;


    public TwoFingerTouchManager() {
        tfPoints = new ArrayList<>();
        touchPoint = null;
        listener = null;

        angle = 0;
        previousAngle = 0;
        counter = 0;
        previousCounter = 0;
        currentFrameTouchCount = 0;

        initPool();
    }


    private void initPool() {
        poolPoints = new ObjectPoolYio<TfPoint>() {
            @Override
            public TfPoint makeNewObject() {
                return new TfPoint();
            }
        };
    }


    public void move() {
        currentFrameTouchCount = 0;
    }


    @Override
    public boolean onTouchDown(PointYio touchPoint) {
        this.touchPoint = touchPoint;

        increaseCounter();
        addPoint();

        currentFrameTouchCount++;
        updatePoints();

        if (previousCounter == 1 && counter == 2) {
            onReachedTwoFingerState();
        }

        return true;
    }


    private void addPoint() {
        TfPoint next = poolPoints.getNext();

        next.position.setBy(touchPoint);
        next.id = counter;

        tfPoints.add(next);
    }


    private void removeRedundantPoints() {
        for (int i = tfPoints.size() - 1; i >= 0; i--) {
            TfPoint tfPoint = tfPoints.get(i);

            if (tfPoint.id <= counter) continue;

            tfPoints.remove(i);
            poolPoints.add(tfPoint);
        }
    }


    private void increaseCounter() {
        previousCounter = counter;
        counter++;
    }


    private void updatePoints() {
        for (TfPoint tfPoint : tfPoints) {
            if (tfPoint.id == currentFrameTouchCount) {
                tfPoint.position.setBy(touchPoint);
            }
        }
    }


    void onReachedTwoFingerState() {
        updateAngle();

        previousAngle = angle;

        if (listener != null) {
            listener.onReachedTwoFingerState();
        }
    }


    void onExitedTwoFingerState() {
        if (listener != null) {
            listener.onExitedTwoFingerState();
        }
    }


    void onSecondFingerDragged() {
        updateAngle();
        onRotated();

        if (listener != null) {
            listener.onSecondFingerDragged();
        }
    }


    public void resetCounter() {
        counter = 0;
    }


    public int getCounter() {
        return counter;
    }


    private void onRotated() {
        double deltaAngle = angle - previousAngle;

        if (listener != null) {
            listener.onTwoFingerRotated(deltaAngle);
        }
    }


    private void updateAngle() {
        previousAngle = angle;

        TfPoint one = getPoint(1);
        TfPoint two = getPoint(2);
        angle = one.position.angleTo(two.position);
    }


    public TfPoint getPoint(int id) {
        for (TfPoint tfPoint : tfPoints) {
            if (tfPoint.id == id) {
                return tfPoint;
            }
        }

        return null;
    }


    @Override
    public boolean onTouchDrag(PointYio touchPoint) {
        this.touchPoint = touchPoint;

        currentFrameTouchCount++;
        updatePoints();

        if (counter == 2) {
            onSecondFingerDragged();
        }

        return true;
    }


    @Override
    public boolean onTouchUp(PointYio touchPoint) {
        this.touchPoint = touchPoint;

        decreaseCounter();
        removeRedundantPoints();

        if (previousCounter == 2 && counter == 1) {
            onExitedTwoFingerState();
        }

        return true;
    }


    public void setListener(TwoFingerListener listener) {
        this.listener = listener;
    }


    private void decreaseCounter() {
        previousCounter = counter;
        counter--;
    }
}
