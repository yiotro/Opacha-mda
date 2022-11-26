package yio.tro.opacha.game.gameplay;

public interface IGameplayManager extends AcceleratableYio{

    void defaultValues();


    void onEndCreation();


    void moveActually();


    void moveVisually();

}
