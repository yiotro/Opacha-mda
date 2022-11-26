package yio.tro.opacha.game.view.game_renders;

import yio.tro.opacha.game.view.GameView;
import yio.tro.opacha.game.view.game_renders.tm_renders.*;

import java.util.ArrayList;

public class GameRendersList {

    private static GameRendersList instance;
    ArrayList<GameRender> gameRenders = new ArrayList<>();

    public RenderTmDefault renderTmDefault;
    public RenderStars renderStars;
    public RenderPlanets renderPlanets;
    public RenderLinks renderLinks;
    public RenderParticles renderParticles;
    public RenderUnits renderUnits;
    public RenderFillers renderFillers;
    public RenderTmAddition renderTmAddition;
    public RenderTmDeletion renderTmDeletion;
    public RenderTmEditor renderTmEditor;
    // initialize them lower


    public GameRendersList() {
        //
    }


    public static GameRendersList getInstance() {
        if (instance == null) {
            instance = new GameRendersList();
            instance.createAllRenders();
        }

        return instance;
    }


    public static void initialize() {
        instance = null;
    }


    public void updateGameRenders(GameView gameView) {
        for (GameRender gameRender : gameRenders) {
            gameRender.update(gameView);
        }
    }


    private void createAllRenders() {
        renderTmDefault = new RenderTmDefault();
        renderStars = new RenderStars();
        renderPlanets = new RenderPlanets();
        renderLinks = new RenderLinks();
        renderParticles = new RenderParticles();
        renderUnits = new RenderUnits();
        renderFillers = new RenderFillers();
        renderTmAddition = new RenderTmAddition();
        renderTmDeletion = new RenderTmDeletion();
        renderTmEditor = new RenderTmEditor();
    }
}
