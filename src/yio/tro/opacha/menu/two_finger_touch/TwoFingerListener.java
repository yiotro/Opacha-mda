package yio.tro.opacha.menu.two_finger_touch;

public interface TwoFingerListener {


    void onReachedTwoFingerState();


    void onExitedTwoFingerState();


    void onSecondFingerDragged();


    void onTwoFingerRotated(double deltaAngle);

}
