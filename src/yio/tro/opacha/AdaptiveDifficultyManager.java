package yio.tro.opacha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.opacha.game.gameplay.model.MatchResults;

public class AdaptiveDifficultyManager {

    private static AdaptiveDifficultyManager instance;
    public float power;


    public static AdaptiveDifficultyManager getInstance() {
        if (instance == null) {
            instance = new AdaptiveDifficultyManager();
            instance.load();
        }
        return instance;
    }


    public static void initialize() {
        instance = null;
    }


    public void applyMatchResults(MatchResults matchResults) {
        if (!SettingsManager.getInstance().adaptiveDifficulty) return;
        if (!matchResults.campaignMode) return;

        if (matchResults.humanWon) {
            increase();
        } else {
            decrease();
        }
    }


    public void decrease() {
        power -= 0.2f;
        applyLimits();
        save();
    }


    public void increase() {
        power += 0.15f;
        applyLimits();
        save();
    }


    public float getPower() {
        return power;
    }


    private void applyLimits() {
        if (power < 0) {
            power = 0;
        }
        if (power > 1) {
            power = 1;
        }
    }


    void save() {
        Preferences preferences = getPreferences();
        preferences.putFloat("value", power);
        preferences.flush();
    }


    void load() {
        Preferences preferences = getPreferences();
        power = preferences.getFloat("value", 1);
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences("opacha.ad");
    }
}
