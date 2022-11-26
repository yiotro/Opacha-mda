package yio.tro.opacha.game.tutorial;

public class DelayScriptYio extends ScriptYio{

    long startTime;
    long delay;


    public DelayScriptYio(long delay) {
        this.delay = delay;
    }


    @Override
    public void apply() {
        startTime = System.currentTimeMillis();
    }


    @Override
    public boolean isActive() {
        return System.currentTimeMillis() - startTime < delay;
    }
}
