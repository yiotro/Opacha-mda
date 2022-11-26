package yio.tro.opacha.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class VelocityManager {

    private static VelocityManager instance;
    public static final String PREFS = "yio.tro.opacha.velocity";
    public float value;


    public VelocityManager() {
        loadValues();
    }


    private void loadValues() {
        Preferences preferences = getPreferences();
        value = preferences.getFloat("value", 0.5f);
    }


    public static void initialize() {
        instance = null;
    }


    public static VelocityManager getInstance() {
        if (instance == null) {
            instance = new VelocityManager();
        }

        return instance;
    }


    public void saveValues() {
        Preferences preferences = getPreferences();
        preferences.putFloat("value", value);
        preferences.flush();
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences(PREFS);
    }


    public void setValue(double value) {
        this.value = (float) value;
    }
}
