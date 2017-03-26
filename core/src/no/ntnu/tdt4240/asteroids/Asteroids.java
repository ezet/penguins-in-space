package no.ntnu.tdt4240.asteroids;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.asteroids.controller.MainScreen;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

public class Asteroids extends Game {

    private static INetworkService networkService;
    private SpriteBatch batch;
    public static final int VIRTUAL_WIDTH = 1920;
    public static final int VIRTUAL_HEIGHT = 1080;
    public static final int GUI_VIRTUAL_WIDTH = 1920/3;
    public static final int GUI_VIRTUAL_HEIGHT = 1080/3;
    private Music music;

    Asteroids(INetworkService networkService) {
        Asteroids.networkService = networkService;
    }

    public static INetworkService getNetworkService() {
        return networkService;
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        // TODO: load assets
        music = Gdx.audio.newMusic(Gdx.files.internal("Sounds/music.mp3"));
        music.setLooping(true);                // will repeat playback until music.stop() is called
        music.play();                          // resumes the playback

        // TODO: load settings
        batch = new SpriteBatch();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        setScreen(new MainScreen(this));
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void dispose() {
        super.dispose();
        music.dispose();

    }
}
