package yio.tro.opacha.game;

import yio.tro.opacha.game.loading.LoadingType;
import yio.tro.opacha.stuff.object_pool.ReusableYio;

public class GameResults implements ReusableYio{


    public LoadingType loadingType;
    String debugMessage;


    @Override
    public void reset() {
        loadingType = null;

    }


    public void setLoadingType(LoadingType loadingType) {
        this.loadingType = loadingType;
    }


    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }


    @Override
    public String toString() {
        return "[GameResults:" +
                " type=" + loadingType +
                " debugMessage=" + debugMessage +
                "]";
    }
}
