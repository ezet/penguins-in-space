package no.ntnu.tdt4240.asteroids;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.ntnu.tdt4240.asteroids.game.AnimationFactory;
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
    no.ntnu.tdt4240.asteroids.service.Assets provideAssetLoader() {
        return new no.ntnu.tdt4240.asteroids.service.Assets();
    }

    @Provides
    @Singleton
    no.ntnu.tdt4240.asteroids.service.audio.AudioManager provideAudioManager(no.ntnu.tdt4240.asteroids.service.Assets assets, ISettingsService settingsService) {
        return new no.ntnu.tdt4240.asteroids.service.audio.AudioManager(assets, settingsService);
    }

    @Provides
    @Singleton
    AnimationFactory provideAnimationFactory(no.ntnu.tdt4240.asteroids.service.Assets assets) {
        return new AnimationFactory(assets);
    }

}
