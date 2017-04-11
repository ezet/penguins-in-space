package no.ntnu.tdt4240.asteroids;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.asteroids.controller.MainMenu;
import no.ntnu.tdt4240.asteroids.controller.MultiplayerGame;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

public class Asteroids extends Game implements INetworkService.IGameListener {

    public static final int VIRTUAL_WIDTH = 1920;
    public static final int VIRTUAL_HEIGHT = 1080;
    public static final int GUI_VIRTUAL_WIDTH = 640;
    public static final int GUI_VIRTUAL_HEIGHT = 360;
    private static final String TAG = Asteroids.class.getSimpleName();
    private SpriteBatch batch;
    private no.ntnu.tdt4240.asteroids.service.Assets assets;
    private INetworkService networkService;
    private ISettingsService settingsService;
    private boolean loaded = false;

    Asteroids(INetworkService networkService, ISettingsService settingsService) {
        this.networkService = networkService;
        this.settingsService = settingsService;
    }

    @Override
    public void create() {
        ServiceLocator.initializeAppComponent(networkService, settingsService);
        ServiceLocator.getAppComponent().getNetworkService().setGameListener(this);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        assets = ServiceLocator.getAppComponent().getAssetLoader();

        assets.loadAudio();
        assets.loadTextures();

//        assets.getAssetManager().finishLoading();

        batch = new SpriteBatch();
        Gdx.gl.glClearColor(0, 0, 0, 1);
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
    public void render() {
        if (!this.loaded) {
            if (assets.update()) {
                if (settingsService.getBoolean(ISettingsService.MUSIC_ENABLED)) {
                    ServiceLocator.getAppComponent().getAudioManager().playBackgroundMusic();
                }
                setScreen(new MainMenu(this));
                this.loaded = true;
            }
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void onMultiplayerGameStarting() {
        Gdx.app.debug(TAG, "onMultiplayerGameStarting: ");
        ServiceLocator.getAppComponent().getAssetLoader().getAssetManager().finishLoading();
        setScreen(new MultiplayerGame(this, new MainMenu(this)));
    }
}
