package no.ntnu.tdt4240.asteroids;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.asteroids.controller.MainController;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

public class Asteroids extends Game {

    public static final int VIRTUAL_WIDTH = 1920;
    public static final int VIRTUAL_HEIGHT = 1080;
    public static final int GUI_VIRTUAL_WIDTH = 640;
    public static final int GUI_VIRTUAL_HEIGHT = 360;
    private SpriteBatch batch;
    private AssetLoader assetLoader;

    Asteroids(INetworkService networkService) {
        ServiceLocator.initializeGameComponent(networkService);
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        assetLoader = new AssetLoader();
        assetLoader.loadAudio();

        batch = new SpriteBatch();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        setScreen(new MainController(this));
    }

    @Override
    public void render() {
        // TODO: fix audio loading, acting weird atm
        boolean loaded = assetLoader.update();
        if (loaded) {
            ServiceLocator.gameComponent.provideAudioManager().playBackgroundMusic();
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void dispose() {
        super.dispose();
        assetLoader.dispose();
    }
}
