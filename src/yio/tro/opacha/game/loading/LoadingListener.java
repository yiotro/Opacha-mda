package yio.tro.opacha.game.loading;

public interface LoadingListener {


    void onLevelCreationEnd();


    void makeExpensiveLoadingStep(int step);
}
