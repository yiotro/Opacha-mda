package yio.tro.opacha.menu;

import yio.tro.opacha.*;
import yio.tro.opacha.game.campaign.CampaignManager;
import yio.tro.opacha.menu.elements.InterfaceElement;
import yio.tro.opacha.menu.scenes.SceneYio;
import yio.tro.opacha.menu.scenes.Scenes;

import java.util.ArrayList;
import java.util.ListIterator;

public class MenuControllerYio {

    public YioGdxGame yioGdxGame;
    ArrayList<InterfaceElement> interfaceElements, visibleElements;
    public LanguagesManager languagesManager;
    private SceneYio currentScene;


    public MenuControllerYio(YioGdxGame yioGdxGame) {
        this.yioGdxGame = yioGdxGame;
        interfaceElements = new ArrayList<>();
        languagesManager = LanguagesManager.getInstance();
        visibleElements = new ArrayList<>();
        InterfaceElement.initScreenElement(this);
        currentScene = null;

        initRepeats();

        createAllScenes();

        createInitialScene();
    }


    public static void createInitialScene() {
        Scenes.notification.initialize();
        Scenes.campaign.create();
        Scenes.mainMenu.create();
        checkToShowAttractionScene();
        checkToShowMyGamesButton();
    }


    private static void checkToShowMyGamesButton() {
        if (OneTimeInfo.getInstance().myGames) return;
        if (!Scenes.mainMenu.isCurrentlyVisible()) return;
        if (CampaignManager.getInstance().getNumberOfCompletedLevels() < 25) return;
        Scenes.myGamesButton.create();
    }


    private static void checkToShowAttractionScene() {
        if (OneTimeInfo.getInstance().kladomRelease) return;
        if (CampaignManager.getInstance().getNumberOfCompletedLevels() < 25) return;
        OneTimeInfo.getInstance().kladomRelease = true;
        OneTimeInfo.getInstance().save();
        Scenes.attraction.create();
    }


    private void createAllScenes() {
        Scenes.createAllScenes(); // init scenes
        SceneYio.updateAllScenes(this); // update scenes
    }


    private void initRepeats() {

    }


    public void move() {
        moveScene();

        for (InterfaceElement visibleElement : visibleElements) {
            visibleElement.moveElement();
        }

        for (int i = visibleElements.size() - 1; i >= 0; i--) {
            if (visibleElements.get(i).checkToPerformAction()) {
                break;
            }
        }
    }


    public void checkToRemoveInvisibleElements() {
        for (int i = visibleElements.size() - 1; i >= 0; i--) {
            InterfaceElement element = visibleElements.get(i);
            if (element != null && !element.isVisible()) {
                removeVisibleElement(element);
            }
        }
    }


    public void removeVisibleElement(InterfaceElement element) {
        Yio.removeByIterator(visibleElements, element);
    }


    private void moveScene() {
        if (currentScene == null) return;

        currentScene.move();
    }


    public void onReturningBackButtonPressed() {
        for (InterfaceElement interfaceElement : interfaceElements) {
            interfaceElement.enableReverseAnimMode();
        }
    }


    public void setCurrentScene(SceneYio currentScene) {
        this.currentScene = currentScene;
    }


    public void addElement(InterfaceElement interfaceElement) {
        // considered that menu block is not in array at this moment
        ListIterator iterator = interfaceElements.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        iterator.add(interfaceElement);
    }


    public void addVisibleElement(InterfaceElement interfaceElement) {
        if (visibleElements.contains(interfaceElement)) return;

        Yio.addToEndByIterator(visibleElements, interfaceElement);
    }


    public void removeElementFromScene(InterfaceElement interfaceElement) {
        ListIterator iterator = interfaceElements.listIterator();
        InterfaceElement currentElement;
        while (iterator.hasNext()) {
            currentElement = (InterfaceElement) iterator.next();
            if (currentElement == interfaceElement) {
                iterator.remove();
                return;
            }
        }
    }


    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for (int i = visibleElements.size() - 1; i >= 0; i--) {
            InterfaceElement interfaceElement = visibleElements.get(i);
            if (!interfaceElement.isVisible()) continue;
            if (!interfaceElement.isTouchable()) continue;
            if (interfaceElement.touchDownElement(screenX, screenY, pointer, button) && interfaceElement.isCaptureTouch()) {
//                System.out.println("interfaceElement = " + interfaceElement);
                return true;
            }
        }
        return false;
    }


    public void touchDragged(int screenX, int screenY, int pointer) {
        for (InterfaceElement interfaceElement : visibleElements) {
            interfaceElement.touchDragElement(screenX, screenY, pointer);
        }
    }


    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (int i = visibleElements.size() - 1; i >= 0; i--) {
            InterfaceElement element = visibleElements.get(i);
            if (element.touchUpElement(screenX, screenY, pointer, button) && element.isCaptureTouch()) return true;
        }

        return false;
    }


    public boolean onMouseWheelScrolled(int amount) {
        for (InterfaceElement interfaceElement : interfaceElements) {
            if (!interfaceElement.isVisible()) continue;
            if (interfaceElement.onMouseWheelScrolled(amount)) {
                return true;
            }
        }

        return false;
    }


    public void onPause() {
        for (InterfaceElement interfaceElement : interfaceElements) {
            interfaceElement.onAppPause();
        }
    }


    public void onResume() {
        for (InterfaceElement interfaceElement : interfaceElements) {
            interfaceElement.onAppResume();
        }
    }


    public void forceDyingElementsToEnd() {
        for (InterfaceElement interfaceElement : interfaceElements) {
            if (interfaceElement.getFactor().getGravity() >= 0) continue;
            interfaceElement.forceDestroyToEnd();
        }
    }


    public void destroyGameView() {
        if (yioGdxGame.gameView == null) return;

        yioGdxGame.gameView.destroy();
    }


    public void showVisibleElementsInConsole() {
        System.out.println();
        System.out.println("Visible elements: ");
        for (InterfaceElement visibleElement : visibleElements) {
            if (visibleElement.getFactor().get() == 0) {
                System.out.println("- " + visibleElement);
            } else {
                System.out.println("+ " + visibleElement);
            }
        }
        System.out.println();
    }


    public void clear() {
        interfaceElements.clear();
        visibleElements.clear();

        // reload all scenes
        SceneYio.sceneList.clear();
        createAllScenes();
        yioGdxGame.loadingManager.createListeners();
    }


    public ArrayList<InterfaceElement> getInterfaceElements() {
        return interfaceElements;
    }


    public InterfaceElement getElement(String key) {
        for (InterfaceElement interfaceElement : interfaceElements) {
            if (key.equals(interfaceElement.getKey())) {
                return interfaceElement;
            }
        }

        return null;
    }


    public ArrayList<InterfaceElement> getVisibleElements() {
        return visibleElements;
    }
}
