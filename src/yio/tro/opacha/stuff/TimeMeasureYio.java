package yio.tro.opacha.stuff;

import yio.tro.opacha.Yio;

public class TimeMeasureYio {

    private static long time1;


    public static void begin() {
        time1 = System.currentTimeMillis();
    }


    public static long apply(String message) {
        long resultTime = System.currentTimeMillis() - time1;
        Yio.safeSay(message + ": " + resultTime);
        begin();
        return resultTime;
    }


    public static long apply() {
        return apply("Time taken");
    }

}
