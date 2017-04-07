package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import no.ntnu.tdt4240.asteroids.Asteroids;

abstract class BaseView extends Stage implements IView {

    private static Viewport guiViewport = new FitViewport(Asteroids.GUI_VIRTUAL_WIDTH, Asteroids.GUI_VIRTUAL_HEIGHT);

    BaseView(Batch batch) {
        super(guiViewport, batch);
    }

    @Override
    public void update(float delta) {
        act(delta);
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public final InputProcessor getInputProcessor() {
        return this;
    }

    @Override
    public final void resize(int width, int height) {
        getViewport().update(width, height);
    }
}
