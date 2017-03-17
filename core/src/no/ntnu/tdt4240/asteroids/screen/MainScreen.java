package no.ntnu.tdt4240.asteroids.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.stage.MainScreenStage;

public class MainScreen extends ScreenAdapter {

    private static final String TAG = MainScreen.class.getSimpleName();
    private final Asteroids game;
    private final OrthographicCamera guiCam;
    private final SpriteBatch batch;
    private final MainScreenStage stage;


    public MainScreen(final Asteroids game) {
        this.game = game;
        batch = game.getBatch();
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        stage = new MainScreenStage(batch);
        Gdx.input.setInputProcessor(stage);

        stage.play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.debug(TAG, "ClickListener: clicked play:");
                game.setScreen(new GameScreen(game));
            }
        });
        stage.exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.debug(TAG, "ClickListener: clicked: exit");
                Gdx.app.exit();
            }
        });



        // TODO: set touch points
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void update(float delta) {
        stage.act(delta);
            // TODO: handle input and process events
        }

    private void draw() {
        stage.draw();
    }
}
