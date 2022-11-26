package yio.tro.opacha.stuff.name_generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class GroupsAnalyzer {

    HashMap<String, String> groups;
    String source;

    public GroupsAnalyzer() {
        groups = new HashMap<String, String>();
    }


    public HashMap<String, String> getGroups() {
        groups.clear();

        ArrayList<String> rows = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(source, "\n");
        while (tokenizer.hasMoreTokens()) {
            rows.add(tokenizer.nextToken());
        }

        for (String row : rows) {
            int index = row.indexOf(':');
            groups.put(row.substring(0, index), row.substring(index + 1));
        }

        return groups;
    }


    public void setSource(String source) {
        this.source = source;
    }
}
