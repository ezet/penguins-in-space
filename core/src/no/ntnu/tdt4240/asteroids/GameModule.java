package no.ntnu.tdt4240.asteroids;

import com.badlogic.gdx.graphics.g2d.Batch;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.ntnu.tdt4240.asteroids.controller.GameController;
import no.ntnu.tdt4240.asteroids.input.ControllerInputHandler;
import no.ntnu.tdt4240.asteroids.view.GameView;

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

}
