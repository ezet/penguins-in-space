package no.ntnu.tdt4240.asteroids;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.ntnu.tdt4240.asteroids.game.AnimationFactory;

@Module
public class GameModule {

    @Provides
    @Singleton
    GameSettings provideGameSettings() {
        return new GameSettings();
    }

    @Provides
    @Singleton
    Assets provideAssetLoader() {
        return new Assets();
    }

    @Provides
    @Singleton
    no.ntnu.tdt4240.asteroids.service.audio.AudioManager provideAudioManager(Assets assets) {
        return new no.ntnu.tdt4240.asteroids.service.audio.AudioManager(assets);
    }

    @Provides
    @Singleton
    AnimationFactory provideAnimationFactory(Assets assets) {
        return new AnimationFactory(assets);
    }

}
