package no.ntnu.tdt4240.asteroids;

import javax.inject.Singleton;

import dagger.Component;
import no.ntnu.tdt4240.asteroids.service.audio.AudioManager;

@Component(modules = GameModule.class)
@Singleton
public interface GameComponent {
    AudioManager provideAudioManager();

    GameSettings provideGameSettings();

    AssetLoader provideAssetLoader();

}
