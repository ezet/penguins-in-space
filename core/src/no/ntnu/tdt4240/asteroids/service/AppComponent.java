package no.ntnu.tdt4240.asteroids.service;

import javax.inject.Singleton;

import dagger.Component;
import no.ntnu.tdt4240.asteroids.game.AnimationFactory;
import no.ntnu.tdt4240.asteroids.service.audio.AudioService;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

    INetworkService getNetworkService();

    AudioService getAudioService();

    no.ntnu.tdt4240.asteroids.service.ISettingsService getSettingsService();

    AssetService getAssetService();

    AnimationFactory getAnimationFactory();

}
