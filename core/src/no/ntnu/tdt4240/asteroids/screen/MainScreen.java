package no.ntnu.tdt4240.asteroids.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.stage.MainScreenStage;

public class MainScreen extends ScreenAdapter {

    private final Asteroids game;
    private final OrthographicCamera guiCam;
    private final SpriteBatch batch;
    private final Stage stage;
    private Vector3 touchPoint;

    public MainScreen(Asteroids game) {
        this.game = game;
        batch = game.getBatch();
        touchPoint = new Vector3();
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        stage = new MainScreenStage();

        // TODO: set touch points
    }

    @Override
    public void render(float delta) {
        update();
        draw();
    }

    private void update() {
        game.setScreen(new GameScreen(game));
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            // TODO: handle input and process events
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        guiCam.update();
        batch.disableBlending();
        batch.begin();
        // TODO: draw background
        batch.end();
        batch.enableBlending();
        batch.begin();
        // TODO: draw ui components
        batch.end();
    }
}
