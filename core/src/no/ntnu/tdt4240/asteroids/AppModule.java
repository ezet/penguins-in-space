package no.ntnu.tdt4240.asteroids;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.ntnu.tdt4240.asteroids.game.AnimationFactory;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

@Module
public class AppModule {

    private INetworkService networkService;

    public AppModule(INetworkService networkService) {
        this.networkService = networkService;
    }

    @Provides
    @Singleton
    INetworkService networkService() {
        return networkService;
    }

    @Provides
    @Singleton
    IAppSettings provideAppSettings() {
        return new AppSettings();
    }


    @Provides
    @Singleton
    no.ntnu.tdt4240.asteroids.service.Assets provideAssetLoader() {
        return new no.ntnu.tdt4240.asteroids.service.Assets();
    }

    @Provides
    @Singleton
    no.ntnu.tdt4240.asteroids.service.audio.AudioManager provideAudioManager(no.ntnu.tdt4240.asteroids.service.Assets assets) {
        return new no.ntnu.tdt4240.asteroids.service.audio.AudioManager(assets);
    }

    @Provides
    @Singleton
    AnimationFactory provideAnimationFactory(no.ntnu.tdt4240.asteroids.service.Assets assets) {
        return new AnimationFactory(assets);
    }

}
