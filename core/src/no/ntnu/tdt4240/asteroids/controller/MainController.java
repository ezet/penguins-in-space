package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.view.IView;
import no.ntnu.tdt4240.asteroids.view.MainView;

public class MainController extends ScreenAdapter implements IMainController {

    @SuppressWarnings("unused")
    private static final String TAG = MainController.class.getSimpleName();
    private final Asteroids game;
    private final IMainView view;


    public MainController(final Asteroids game) {
        this.game = game;
        view = new MainView(game.getBatch(), this);
        Gdx.input.setInputProcessor(view.getInputProcessor());


        // TODO: set touch points
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        view.resize(width, height);
    }

    private void update(float delta) {
        view.update(delta);
        // TODO: handle input and process events
    }

    private void draw() {
        view.draw();
    }

    @Override
    public void onPlay() {
        game.setScreen(new GameController(game));

    }

    @Override
    public void onQuit() {
        Gdx.app.exit();
    }


    public interface IMainView extends IView {
        void resize(int width, int height);
    }
}
