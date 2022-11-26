package yio.tro.opacha.stuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.opacha.PlatformType;
import yio.tro.opacha.Yio;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.menu.scenes.Scenes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GradualAttractionManager {

    private static GradualAttractionManager instance = null;
    public static final String PREFS = "yio.tro.opacha.gam";
    private static final String START_DATE = "01.01.2021";
    private static final String END_DATE = "21.01.2021";
    private long startTime;
    private long endTime;
    private double currentProgressValue;
    private double targetProgressValue;
    private boolean firstLaunch;
    private boolean applied;


    public GradualAttractionManager() {
        startTime = convertStringToMilliseconds(START_DATE);
        endTime = convertStringToMilliseconds(END_DATE);
        currentProgressValue = (double) (System.currentTimeMillis() - startTime) / (double) (endTime - startTime);
        loadValues();
        checkForFirstLaunch();
    }


    private void checkForFirstLaunch() {
        if (!firstLaunch) return;
        firstLaunch = false;
        Random random = new Random();
        targetProgressValue = random.nextDouble();
        saveValues();
    }


    public static void initialize() {
        instance = null;
    }


    public static GradualAttractionManager getInstance() {
        if (instance == null) {
            instance = new GradualAttractionManager();
        }
        return instance;
    }


    public void checkToApply() {
        // opacha-mda can be easily rejected in GP if google decides that
        // game promoted here has something to do with 'gambling', for example
        // So I have to be extremely careful with what I'm promoting here
        if (applied) return;
        if (YioGdxGame.platformType == PlatformType.ios) return;
        if (!isReady()) return;
        apply();
    }


    private void apply() {
//        Scenes.attraction.create();
        onApplied();
    }


    private boolean isReady() {
        return currentProgressValue > targetProgressValue;
    }


    private void onApplied() {
        applied = true;
        saveValues();
    }


    private long convertStringToMilliseconds(String source) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Date date = null;
        try {
            date = simpleDateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return date.getTime();
    }


    private void loadValues() {
        Preferences preferences = getPreferences();
        firstLaunch = preferences.getBoolean("first_launch", true);
        targetProgressValue = preferences.getFloat("target_value", 0);
        applied = preferences.getBoolean("applied", false);
    }


    private void saveValues() {
        Preferences preferences = getPreferences();
        preferences.putBoolean("first_launch", firstLaunch);
        preferences.putFloat("target_value", (float) targetProgressValue);
        preferences.putBoolean("applied", applied);
        preferences.flush();
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences(PREFS);
    }


    public int calculateAttractionsPerDay(int activeInstalls) {
        long period = endTime - startTime;
        long millisInDay = 24 * 60 * 60 * 1000;
        int days = (int) (period / millisInDay);
        return activeInstalls / days;
    }
}
