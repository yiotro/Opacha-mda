package yio.tro.opacha.game;

import yio.tro.opacha.game.export_import.SavableYio;
import yio.tro.opacha.game.loading.LoadingParameters;
import yio.tro.opacha.game.loading.LoadingType;

public class GameRules {

    public static LoadingParameters initialParameters;
    public static LoadingType initialLoadingType;
    public static int fastForwardSpeed;
    public static boolean aiOnlyMode;
    public static Difficulty difficulty;
    public static int levelIndex;


    public static void bootInit() {
        initialParameters = null;
        initialLoadingType = null;
        fastForwardSpeed = 3;
        defaultValues();
    }


    static void defaultValues() {
        aiOnlyMode = false;
        difficulty = Difficulty.easy;
        levelIndex = -1;
    }


    public static String saveToString() {
        return difficulty + " " + aiOnlyMode;
    }
}
