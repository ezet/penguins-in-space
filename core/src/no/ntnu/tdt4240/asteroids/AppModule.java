package no.ntnu.tdt4240.asteroids;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.ntnu.tdt4240.asteroids.game.AnimationFactory;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.audio.AudioService;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

@Module
public class AppModule {

    private INetworkService networkService;
    private ISettingsService settingsService;

    public AppModule(INetworkService networkService, ISettingsService settingsService) {
        this.networkService = networkService;
        this.settingsService = settingsService;
    }

    @Provides
    @Singleton
    INetworkService networkService() {
        return networkService;
    }

    @Provides
    @Singleton
    ISettingsService provideSettingsService() {
        return settingsService;
    }

    @Provides
    @Singleton
    AssetService provideAssetService() {
        return new AssetService();
    }

    @Provides
    @Singleton
    AudioService provideAudioManager(AssetService assetService, ISettingsService settingsService) {
        return new AudioService(assetService, settingsService);
    }

    @Provides
    @Singleton
    AnimationFactory provideAnimationFactory(AssetService assetService) {
        return new AnimationFactory(assetService);
    }

}
