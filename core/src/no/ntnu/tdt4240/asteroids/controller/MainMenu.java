package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.view.IView;
import no.ntnu.tdt4240.asteroids.view.MainView;

public class MainMenu extends ScreenAdapter implements IMainMenu {

    @SuppressWarnings("unused")
    private static final String TAG = MainMenu.class.getSimpleName();
    private final Asteroids game;
    private final IMainView view;


    public MainMenu(final Asteroids game) {
        this.game = game;
        view = new MainView(game.getBatch(), this);
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
    }

    private void draw() {
        view.draw();
    }

    @Override
    public void onPlay() {
        game.setScreen(new SingleplayerGame(game, this));
    }

    @Override
    public void onMultiplayer() {
        game.setScreen(new MultiplayerMenu(game));
    }

    @Override
    public void onQuit() {
        Gdx.app.exit();
    }

    @Override
    public void onTutorial() {
        game.setScreen(new TutorialController(game, this));
    }


    public interface IMainView extends IView {
        void resize(int width, int height);
    }
}
