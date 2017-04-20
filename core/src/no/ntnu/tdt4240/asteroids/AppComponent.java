package no.ntnu.tdt4240.asteroids;

import javax.inject.Singleton;

import dagger.Component;
import no.ntnu.tdt4240.asteroids.game.AnimationFactory;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.audio.AudioService;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

    INetworkService getNetworkService();

    AudioService getAudioService();

    ISettingsService getSettingsService();

    AssetService getAssetService();

    AnimationFactory getAnimationFactory();

}
