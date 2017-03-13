package no.ntnu.tdt4240.asteroids;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Asteroids extends Game {

    private SpriteBatch batch;
    @Override
    public void create() {
        // TODO: load assets
        // TODO: load settings
        batch = new SpriteBatch();
        setScreen(new MainScreen(this));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }


    public SpriteBatch getBatch() {
        return batch;
    }
}
