package yio.tro.opacha.stuff;


import yio.tro.opacha.RefreshRateDetector;
import yio.tro.opacha.YioGdxGame;

public abstract class RepeatYio<ProviderType> {

    protected ProviderType parent;
    int frequency, countDown;
    boolean ignoreRefreshRate;


    public RepeatYio(ProviderType parent, int frequency) {
        this(parent, frequency, YioGdxGame.random.nextInt(Math.max(1, frequency)));
    }


    public RepeatYio(ProviderType parent, int frequency, int defCount) {
        this.parent = parent;
        ignoreRefreshRate = false;
        setFrequency(frequency);
        countDown = applyRefreshRate(defCount);
    }


    public void move(int quantity) {
        for (int i = 0; i < quantity; i++) {
            move();
        }
    }


    public void move() {
        if (countDown == 0) {
            resetCountDown();
            performAction();
        } else countDown--;
    }


    public void resetCountDown() {
        countDown = applyRefreshRate(frequency);
    }


    public abstract void performAction();


    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }


    private int applyRefreshRate(int value) {
        if (ignoreRefreshRate) return value;
        if (value <= 1) return value;
        return (int) (value / RefreshRateDetector.getInstance().multiplier);
    }


    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }


    public void randomizeCountDown() {
        setCountDown(YioGdxGame.random.nextInt(Math.max(1, frequency)));
    }


    public void setIgnoreRefreshRate(boolean ignoreRefreshRate) {
        this.ignoreRefreshRate = ignoreRefreshRate;
    }
}
