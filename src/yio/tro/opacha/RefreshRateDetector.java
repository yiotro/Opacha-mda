package yio.tro.opacha;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.Arrays;

public class RefreshRateDetector {

    private static RefreshRateDetector instance;
    public float multiplier;
    private ArrayList<Integer> fpsList;
    private long updateDelta;
    private long memoryCapacity;
    private int quantity;
    private long timeToUpdate;
    public int refreshRate;
    private int[] knownRates;
    private long timeToAnalyze;
    private long analyzeDelta;
    private ArrayList<Integer> validList;


    public RefreshRateDetector() {
        load();
        updateMultiplier();
        fpsList = new ArrayList<>();
        updateDelta = 400;
        memoryCapacity = 4000;
        quantity = (int) (memoryCapacity / updateDelta);
        timeToUpdate = 0;
        knownRates = new int[]{60, 90, 120, 144, 240};
        timeToAnalyze = 0;
        analyzeDelta = 1000;
        validList = new ArrayList<>();
    }


    public static void initialize() {
        instance = null;
    }


    public static RefreshRateDetector getInstance() {
        if (instance == null) {
            instance = new RefreshRateDetector();
        }
        return instance;
    }


    public void move() {
        checkToUpdate();
        checkToAnalyze();
    }


    private void checkToAnalyze() {
        long currentTime = System.currentTimeMillis();
        if (currentTime < timeToAnalyze) return;
        timeToAnalyze = currentTime + analyzeDelta;
        analyze();
        checkToSlowDown();
    }


    private void checkToSlowDown() {
        if (analyzeDelta == 5000) return;
        if (fpsList.size() != quantity) return;
        if (validList.size() < fpsList.size()) return;
        analyzeDelta = 5000; // slow down
    }


    private void analyze() {
        if (fpsList.size() < 3) return;
        updateValidList();
        if (validList.size() < fpsList.size() / 2) return;
        int averageValue = calculateAverageValidValue();
        if (averageValue == 0) return;
        int calculatedRefreshRate = findClosestKnownRefreshRate(averageValue);
        if (refreshRate == calculatedRefreshRate) return;
        if (calculatedRefreshRate < refreshRate && validList.size() < quantity) return;
        refreshRate = calculatedRefreshRate;
        updateMultiplier();
        save();
    }


    private void save() {
        Preferences preferences = getPreferences();
        if (preferences == null) return;
        preferences.putInteger("value", refreshRate);
        preferences.flush();
    }


    private void load() {
        Preferences preferences = getPreferences();
        if (preferences == null) {
            refreshRate = 60;
            return;
        }
        refreshRate = preferences.getInteger("value", 60);
    }


    private void updateMultiplier() {
        multiplier = 60f / (float) refreshRate;
        if (multiplier > 1) {
            multiplier = 1;
        }
    }


    private int findClosestKnownRefreshRate(int sourceValue) {
        int result = 0;
        int minDistance = 0;
        for (int rate : knownRates) {
            int currentDistance = Math.abs(rate - sourceValue);
            if (result == 0 || currentDistance < minDistance) {
                result = rate;
                minDistance = currentDistance;
            }
        }
        return result;
    }


    private int calculateAverageValidValue() {
        int sum = 0;
        for (int value : validList) {
            sum += value;
        }
        return sum / validList.size();
    }


    private void updateValidList() {
        validList.clear();
        for (int value : fpsList) {
            if (value < 30) continue;
            validList.add(value);
        }
    }


    private void checkToUpdate() {
        long currentTime = System.currentTimeMillis();
        if (currentTime < timeToUpdate) return;
        timeToUpdate = currentTime + updateDelta;
        updateFpsList();
    }


    private void updateFpsList() {
        int currentFps = Gdx.graphics.getFramesPerSecond();
        fpsList.add(currentFps);
        if (fpsList.size() <= quantity) return;
        fpsList.remove(0);
    }


    private Preferences getPreferences() {
        Application app = Gdx.app;
        if (app == null) return null;
        return app.getPreferences("yio.tro.opacha.refresh_rate");
    }


    public void showInConsole() {
        System.out.println();
        System.out.println("RefreshRateDetector.showInConsole");
        System.out.println("fpsList = " + Arrays.toString(fpsList.toArray()));
        System.out.println("refreshRate = " + refreshRate);
        System.out.println("multiplier = " + multiplier);
    }
}
