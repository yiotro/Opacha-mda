package yio.tro.opacha.menu.scenes;

import yio.tro.opacha.menu.elements.LoadingScreenView;

public class SceneLoadingScreen extends SceneYio {


    public LoadingScreenView loadingScreenView;


    @Override
    protected void initialize() {
        loadingScreenView = uiFactory.getLoadingScreen()
                .setSize(1, 1)
                .setAppearParameters(6, 1.5)
                .setDestroyParameters(6, 2)
                .setOnTopOfGameView(false);
    }


    @Override
    protected void prepareGround() {
        // no ground
    }


    @Override
    protected void onAppear() {
        super.onAppear();

        forceElementsToTop();
    }
}
