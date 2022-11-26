package yio.tro.opacha;

public interface TouchListenerYio {

    boolean touchDown(float touchX, float touchY);


    boolean touchDrag(float touchX, float touchY);


    boolean touchUp(float touchX, float touchY);


    boolean isActive();
}
