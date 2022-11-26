package yio.tro.opacha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class OneTimeInfo {

    private static OneTimeInfo instance;

    public boolean syncComplete;
    public boolean aboutSandboxTasks;
    public boolean quickVelocityTutorial;
    public boolean kladomRelease;
    public boolean myGames;


    public static OneTimeInfo getInstance() {
        if (instance == null) {
            instance = new OneTimeInfo();
            instance.load();
        }

        return instance;
    }


    public static void initialize() {
        instance = null;
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences("vitamin.oneTimeInfo");
    }


    public void load() {
        Preferences preferences = getPreferences();
        syncComplete = preferences.getBoolean("sync_complete", false);
        aboutSandboxTasks = preferences.getBoolean("about_sandbox_tasks", false);
        quickVelocityTutorial = preferences.getBoolean("quick_velocity_tutorial", false);
        kladomRelease = preferences.getBoolean("kladom_release", false);
        myGames = preferences.getBoolean("my_games", false);
    }


    public void save() {
        Preferences preferences = getPreferences();
        preferences.putBoolean("sync_complete", syncComplete);
        preferences.putBoolean("about_sandbox_tasks", aboutSandboxTasks);
        preferences.putBoolean("quick_velocity_tutorial", quickVelocityTutorial);
        preferences.putBoolean("kladom_release", kladomRelease);
        preferences.putBoolean("my_games", myGames);
        preferences.flush();
    }
}
