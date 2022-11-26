package yio.tro.opacha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import yio.tro.opacha.game.debug.DebugFlags;

public class SoundManager {

    public static SoundThreadYio soundThreadYio;
    public static Sound button;
    public static Sound backButton;
    public static Sound score;
    public static Sound fall;
    public static Sound testEnded;
    public static Sound kbPress;


    public static void loadSounds() {
        startThread();

        button = loadSound("button");
        backButton = loadSound("back");
        score = loadSound("score");
        fall = loadSound("fall1");
        testEnded = loadSound("test_ended");
        kbPress = loadSound("kb_press");
    }


    private static void startThread() {
        soundThreadYio = new SoundThreadYio();
        soundThreadYio.start();
        soundThreadYio.setPriority(1);
    }


    private static Sound loadSound(String name) {
        return Gdx.audio.newSound(Gdx.files.internal("sound/" + name + getExtension()));
    }


    private static String getExtension() {
        if (YioGdxGame.platformType == PlatformType.ios) {
            return ".mp3";
        }
        return ".ogg";
    }


    public static void playSound(Sound sound) {
        if (!SettingsManager.getInstance().soundEnabled) return;
        soundThreadYio.playSound(sound);
    }


    public static void playSoundDirectly(Sound sound) {
        if (!SettingsManager.getInstance().soundEnabled) return;

        sound.play();
    }
}
