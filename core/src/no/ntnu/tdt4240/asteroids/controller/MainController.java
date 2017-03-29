package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.view.IMainView;
import no.ntnu.tdt4240.asteroids.view.MainView;
import sun.rmi.runtime.Log;

public class MainController extends ScreenAdapter implements IMainController {

    @SuppressWarnings("unused")
    private static final String TAG = MainController.class.getSimpleName();
    private final Asteroids game;
    private final IMainView view;


    public MainController(final Asteroids game) {
        this.game = game;
        view = new MainView(game.getBatch(), this);


        // TODO: set touch points
    }
    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(view.getInputProcessor());
        Gdx.app.debug(TAG, "Show");


    }

    @Override
    public void hide() {
        super.hide();
        Gdx.input.setInputProcessor(null);
        Gdx.app.debug(TAG, "HIDE");


    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
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
        game.setScreen(new GameController(game, this));

    }

    @Override
    public void onQuit() {
        Gdx.app.exit();
    }

    @Override
    public void onTutorial() {
        game.setScreen(new TutorialController(game,this));


    }


}
