package no.ntnu.tdt4240.asteroids;

import com.badlogic.gdx.assets.AssetManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GameModule {

    @Provides
    @Singleton
    GameSettings provideGameSettings() {
        return new GameSettings();
    }

    @Provides
    @Singleton
    AssetLoader provideAssetLoader() {
        return new AssetLoader();
    }

    @Provides
    @Singleton
    no.ntnu.tdt4240.asteroids.service.audio.AudioManager provideAudioManager(AssetLoader assetLoader) {
        return new no.ntnu.tdt4240.asteroids.service.audio.AudioManager(assetLoader);
    }



}
