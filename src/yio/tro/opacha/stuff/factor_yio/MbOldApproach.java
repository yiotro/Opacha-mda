package yio.tro.opacha.stuff.factor_yio;

public class MbOldApproach extends AbstractMoveBehavior {

    public MbOldApproach() {

    }


    @Override
    void onAppear(FactorYio f) {
        super.onAppear(f);
        f.speedMultiplier /= 0.3;
    }


    @Override
    void move(FactorYio f) {
        if (!f.needsToMove) return;

        if (f.inAppearState) {
            f.value += Math.max(f.speedMultiplier * 0.15 * (1 - f.value), 0.01);
            if (f.value > 0.99) {
                f.value = 1;
            }
            return;
        }

        f.value += Math.min(f.speedMultiplier * 0.15 * (0 - f.value), -0.01);
        if (f.value < 0.01) {
            f.value = 0;
        }
    }
}
