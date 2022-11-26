package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.menu.elements.NotificationElement;

public class SceneNotification extends SceneYio {


    public NotificationElement notificationElement;


    @Override
    public void initialize() {
        double h = 0.05;
        notificationElement = uiFactory.getNotificationElement()
                .setPosition(0, 1 - h, 1, h)
                .setOnTopOfGameView(true);
    }


    public void show(String message) {
        show(message, true);
    }


    public void show(String message, boolean autoHide) {
        appear();
        forceElementToTop(notificationElement);

        notificationElement.setMessage(message);

        if (autoHide) {
            notificationElement.enableAutoHide();
        }
    }
}
