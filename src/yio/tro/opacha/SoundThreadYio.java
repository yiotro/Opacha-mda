package yio.tro.opacha;

import com.badlogic.gdx.audio.Sound;

public class SoundThreadYio extends Thread {


    Sound currentSound;
    boolean running;
    boolean readyToPlaySound;


    public void playSound(Sound sound) {
        setCurrentSound(sound);
        readyToPlaySound = true;
    }


    public void setCurrentSound(Sound currentSound) {
        this.currentSound = currentSound;
    }


    @Override
    public void run() {
        running = true;

        while (running) {
            if (readyToPlaySound) {
                readyToPlaySound = false;
                SoundManager.playSoundDirectly(currentSound);
            }

            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {

            }
        }
    }
}
