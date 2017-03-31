package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.view.TutorialView;

/**
 * Created by morte on 3/28/2017.
 */

public class TutorialController  extends ScreenAdapter implements ITutorialController {
    private static final String TAG = MainController.class.getSimpleName();
    private final Asteroids game;
    private final TutorialView view;
    private Screen parent;
    public TutorialController(final Asteroids game,final Screen parent) {
        this.parent = parent;
        this.game = game;
        this.view = new TutorialView(game.getBatch(),this);
    }

    private void update(float delta) {
        view.update(delta);
        // TODO: handle input and process events
    }

    private void draw() {
        view.draw();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void onQuitLevel() {
        game.setScreen(this.parent);
        dispose();
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(view.getInputProcessor());

    }

    @Override
    public void hide() {
        super.hide();
        Gdx.input.setInputProcessor(null);

    }
}
