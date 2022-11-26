package yio.tro.opacha.stuff;

import yio.tro.opacha.Yio;
import yio.tro.opacha.game.GameController;
import yio.tro.opacha.game.gameplay.GameObject;

import java.util.ArrayList;

public class LogCollectorYio {

    private static LogCollectorYio instance;
    private ArrayList<String> strings;
    private ArrayList<GameObject> problemObjects;
    private StringBuilder builder;


    public LogCollectorYio() {
        strings = new ArrayList<>();
        problemObjects = new ArrayList<>();
        builder = new StringBuilder();
    }


    public static void initialize() {
        instance = null;
    }


    public static LogCollectorYio getInstance() {
        if (instance == null) {
            instance = new LogCollectorYio();
        }

        return instance;
    }


    public void clear() {
        showAllProblemObjects();

        strings.clear();
        problemObjects.clear();
    }


    public void say(String message) {
        strings.add(getCurrentMoveIndexPrefix() + message);
    }


    private String getCurrentMoveIndexPrefix() {
        builder.setLength(0);

        builder.append(GameController.currentMoveIndex);

        while (builder.length() < 5) {
            builder.append(" ");
        }

        return builder.toString() + "| ";
    }


    public void addProblemObject(GameObject gameObject) {
        if (problemObjects.contains(gameObject)) return;

        problemObjects.add(gameObject);
    }


    public void showAllProblemObjects() {
        if (problemObjects.size() == 0) return;

        for (GameObject problemObject : problemObjects) {
            showFiltered(problemObject);
        }

        problemObjects.clear(); // don't show same thing twice
    }


    public boolean hasProblemObjects() {
        return problemObjects.size() > 0;
    }


    public void noticeStackTrace(String name) {
        StackTraceElement[] stackTrace = Yio.getStackTrace();
        say("--- Stack trace: " + name + " ---");
        for (int i = 0; i < stackTrace.length; i++) {
            say("--- " + stackTrace[i]);
        }
    }


    public void showFiltered(String filter) {
        System.out.println();
        System.out.println("Filtered strings (" + filter + "): ");
        for (String string : strings) {
            if (string.contains(filter) || string.contains("---")) {
                System.out.println(string);
            }
        }
        System.out.println();
    }


    public void showFiltered(GameObject gameObject) {
        showFiltered(gameObject.getUniqueCode());
    }

}
