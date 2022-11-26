package yio.tro.opacha.stuff;

public class DebugDataContainer {

    public static String text = "Debug data";


    public static void addLine(String line) {
        text = text + "#" + line;
    }
}
