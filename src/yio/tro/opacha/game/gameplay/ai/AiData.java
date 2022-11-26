package yio.tro.opacha.game.gameplay.ai;

import yio.tro.opacha.stuff.object_pool.ReusableYio;

public class AiData implements ReusableYio{

    public int numberOfAdjoinedEnemies;
    public boolean safe;
    public int requestPriority;
    public int threat;


    @Override
    public void reset() {
        numberOfAdjoinedEnemies = 0;
        safe = true;
        requestPriority = 0;
        threat = 0;
    }
}
