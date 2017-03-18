package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.view.IMainScreenView;
import no.ntnu.tdt4240.asteroids.view.MainScreenStage;

public class MainScreen extends ScreenAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = MainScreen.class.getSimpleName();
    private final Asteroids game;
    private final IMainScreenView view;


    public MainScreen(final Asteroids game) {
        this.game = game;
//        OrthographicCamera guiCam = new OrthographicCamera(320, 480);
//        guiCam.position.set(320 / 2, 480 / 2, 0);
        view = new MainScreenStage(game.getBatch(), new InputHandler(this));
        Gdx.input.setInputProcessor(view.getInputProcessor());


        // TODO: set touch points
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


    public static class InputHandler {

        private final MainScreen mainScreen;

        InputHandler(MainScreen mainScreen) {

            this.mainScreen = mainScreen;
        }

        public void onPlay() {
            mainScreen.game.setScreen(new GameScreen(mainScreen.game));

        }

        public void onQuit() {
            Gdx.app.exit();
        }

    }


}
