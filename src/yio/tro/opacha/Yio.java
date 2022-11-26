package yio.tro.opacha;

import yio.tro.opacha.stuff.RepeatYio;

import java.util.ArrayList;
import java.util.ListIterator;

public class Yio {

    private static String slowSayMessage = null;
    private static RepeatYio<Yio> repeatCheckSlowSay = new RepeatYio<Yio>(null, 120, 1) {
        @Override
        public void performAction() {
            checkToSlowSay();
        }
    };


    public static double angle(double x1, double y1, double x2, double y2) {
        if (x1 == x2) {
            if (y2 > y1) return 0.5 * Math.PI;
            if (y2 < y1) return 1.5 * Math.PI;
            return 0;
        }

        if (x2 >= x1) {
            return Math.atan((y2 - y1) / (x2 - x1));
        } else {
            return Math.PI + Math.atan((y2 - y1) / (x2 - x1));
        }
    }


    public static double roundUp(double value, int length) {
        double d = Math.pow(10, length);
        value = value * d;
        int i = (int) (value + 0.45);
        return (double) i / d;
    }


    public static boolean removeByIterator(ArrayList<?> list, Object object) {
        ListIterator iterator = list.listIterator();

        while (iterator.hasNext()) {
            if (iterator.next() == object) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }


    public static void addByIterator(ArrayList<?> list, Object object) {
        ListIterator iterator = list.listIterator();
        iterator.add(object);
    }


    public static void addToEndByIterator(ArrayList<?> list, Object object) {
        ListIterator iterator = list.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        iterator.add(object);
    }


    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }


    public static double distanceBetweenAngles(double a1, double a2) {
        double result = a2 - a1;

        while (result > Math.PI) {
            result -= 2 * Math.PI;
        }

        while (result < -Math.PI) {
            result += 2 * Math.PI;
        }

        return Math.abs(result);
    }


    public static float radianToDegree(double angle) {
        return (float) (180 / Math.PI * angle);
    }


    public static void syncSay(String message) {
        synchronized (System.out) {
            safeSay(message);
        }
    }


    public static void timeSay(String message) {
        long millis = System.currentTimeMillis() - YioGdxGame.appLaunchTime;
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        String time = String.format("%02d:%02d:%03d", minute, second, millis % 1000);
        safeSay(time + ": " + message);
    }


    public static void slowSay(String message) {
        slowSayMessage = message;
        repeatCheckSlowSay.move();
    }


    private static void checkToSlowSay() {
        safeSay(slowSayMessage);
    }


    public static void safeSay(String message) {
        System.out.println(message);
    }


    public static void printStackTrace() {
        try {
            forceException();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static StackTraceElement[] getStackTrace() {
        try {
            forceException();
        } catch (Exception e) {
            return e.getStackTrace();
        }

        return null;
    }


    public static void forceException() throws Exception {
        throw new Exception();
    }


    public static String getUniqueCode(Object object) {
        String s = object.toString();
        return s.substring(s.indexOf("@"));
    }


    public static double getRandomAngle() {
        return 2d * Math.PI * YioGdxGame.random.nextDouble();
    }


    public static String getCapitalizedString(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }


    public static String convertTime(long time) {
        long currentCountDown = time;
        currentCountDown /= 60; // seconds
        int min = 0;
        while (currentCountDown >= 60) {
            min++;
            currentCountDown -= 60;
        }
        String zero = "";
        if (currentCountDown < 10) zero = "0";
        return min + ":" + zero + currentCountDown;
    }


    public static int convertMillisIntoFrames(long millis) {
        return (int) (60 * (millis / 1000));
    }


    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
