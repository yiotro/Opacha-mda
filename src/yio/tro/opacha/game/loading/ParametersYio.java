package yio.tro.opacha.game.loading;

import java.util.HashMap;

public class ParametersYio {

    HashMap<String, Object> hashMap;


    public ParametersYio() {
        hashMap = new HashMap<>();
    }


    public Object getParameter(String key) {
        if (!hashMap.containsKey(key)) return null;
        return hashMap.get(key);
    }


    public void addParameter(String key, Object value) {
        hashMap.put(key, value);
    }
}
