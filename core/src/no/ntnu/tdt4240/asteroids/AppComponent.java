package no.ntnu.tdt4240.asteroids;

import javax.inject.Singleton;

import dagger.Component;
import no.ntnu.tdt4240.asteroids.game.AnimationFactory;
import no.ntnu.tdt4240.asteroids.service.audio.AudioManager;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

    INetworkService getNetworkService();

    AudioManager getAudioManager();

    GameSettings getGameSettings();

    Assets getAssetLoader();

    AnimationFactory getAnimationFactory();

}
