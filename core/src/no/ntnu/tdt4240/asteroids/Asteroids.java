package no.ntnu.tdt4240.asteroids;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.asteroids.controller.MainMenu;
import no.ntnu.tdt4240.asteroids.controller.MultiplayerGame;
import no.ntnu.tdt4240.asteroids.service.*;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

public class Asteroids extends Game implements INetworkService.IGameListener {

    public static final int VIRTUAL_WIDTH = 1920;
    public static final int VIRTUAL_HEIGHT = 1080;
    public static final int GUI_VIRTUAL_WIDTH = 640;
    public static final int GUI_VIRTUAL_HEIGHT = 360;
    private SpriteBatch batch;
    private no.ntnu.tdt4240.asteroids.service.Assets assets;
    private static final String TAG = Asteroids.class.getSimpleName();
    private INetworkService networkService;

    Asteroids(INetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void create() {
        ServiceLocator.initializeAppComponent(networkService);
        ServiceLocator.getAppComponent().getNetworkService().setGameListener(this);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        assets = ServiceLocator.getAppComponent().getAssetLoader();

        assets.loadAudio();
        assets.loadTextures();
        assets.getAssetManager().finishLoading();

        batch = new SpriteBatch();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        boolean loaded = assets.update();
        if (loaded) {
            ServiceLocator.getAppComponent().getAudioManager().playBackgroundMusic();
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
        assets.dispose();
    }

    @Override
    public void onMultiplayerGameStarting() {
        Gdx.app.debug(TAG, "onMultiplayerGameStarting: ");
        ServiceLocator.getAppComponent().getAssetLoader().getAssetManager().finishLoading();
        setScreen(new MultiplayerGame(this, new MainMenu(this)));
    }
}
