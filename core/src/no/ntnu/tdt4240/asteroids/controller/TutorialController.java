package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.view.TutorialView;

public class TutorialController extends BaseController {
    private static final String TAG = TutorialController.class.getSimpleName();
    private final Asteroids game;
    private final IView view;
    private Screen parent;

    public TutorialController(final Asteroids game, final Screen parent) {
        this.parent = parent;
        this.game = game;
        this.view = new TutorialView(game.getBatch(), new ViewHandler());
    }

    private void update(float delta) {
        view.update(delta);
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
    public no.ntnu.tdt4240.asteroids.view.IView getView() {
        return view;
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

    public interface IView extends no.ntnu.tdt4240.asteroids.view.IView {
    }

    public class ViewHandler {
        public void onBack() {
            game.setScreen(parent);
            dispose();
        }

    }
}
