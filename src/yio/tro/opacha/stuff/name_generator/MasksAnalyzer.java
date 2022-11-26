package yio.tro.opacha.stuff.name_generator;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MasksAnalyzer {

    ArrayList<String> masks;
    String source;


    public MasksAnalyzer() {
        masks = new ArrayList<String>();
    }


    public ArrayList<String> getMasks() {
        masks.clear();

        StringTokenizer tokenizer = new StringTokenizer(source, "\n");
        while (tokenizer.hasMoreTokens()) {
            masks.add(tokenizer.nextToken());
        }

        return masks;
    }


    public void setSource(String source) {
        this.source = source;
    }
}
