package no.ntnu.tdt4240.asteroids;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.asteroids.presenter.MainMenuPresenter;
import no.ntnu.tdt4240.asteroids.presenter.MpGamePresenter;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

public class Asteroids extends Game implements INetworkService.IGameListener {

    public static final int VIRTUAL_WIDTH = 1920;
    public static final int VIRTUAL_HEIGHT = 1080;
    public static final int GUI_VIRTUAL_WIDTH = 640;
    public static final int GUI_VIRTUAL_HEIGHT = 360;
    private static final String TAG = Asteroids.class.getSimpleName();
    private SpriteBatch batch;
    private AssetService assetService;
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
        assetService = ServiceLocator.getAppComponent().getAssetService();

        assetService.loadAudio();
        assetService.loadTextures();

//        assetService.getAssetManager().finishLoading();

        batch = new SpriteBatch();
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void dispose() {
        super.dispose();
        assetService.dispose();
    }

    @Override
    public void render() {
        if (!this.loaded) {
            if (assetService.update()) {
                if (settingsService.getBoolean(ISettingsService.MUSIC_ENABLED)) {
                    ServiceLocator.getAppComponent().getAudioService().startMusic();
                }
                setScreen(new MainMenuPresenter(this));
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
        ServiceLocator.getAppComponent().getAssetService().getAssetManager().finishLoading();
        setScreen(new MpGamePresenter(this, new MainMenuPresenter(this)));
    }
}
