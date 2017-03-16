package no.ntnu.tdt4240.asteroids.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class GameScreenStage extends Stage {

    private static Viewport guiViewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private Actor inputController;
    private Actor gui;

    public GameScreenStage(Batch batch) {
        super(guiViewport, batch);
    }

    public Actor getInputController() {
        return inputController;
    }

    public void setInputController(Actor inputController) {
        getActors().removeValue(this.gui, true);
        this.inputController = inputController;
        addActor(inputController);
    }

    public Actor getGui() {
        return gui;
    }

    public void setGui(Actor gui) {
        getActors().removeValue(this.gui, true);
        this.gui = gui;
        addActor(gui);
    }
}
