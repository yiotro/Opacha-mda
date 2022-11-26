package yio.tro.opacha.game.gameplay;

public abstract class GameObject {


    public String getUniqueCode() {
        String s = super.toString();
        return s.substring(s.indexOf("@"));
    }
}
