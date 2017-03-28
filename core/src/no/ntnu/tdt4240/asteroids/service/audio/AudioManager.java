package no.ntnu.tdt4240.asteroids.service.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import javax.inject.Inject;

import no.ntnu.tdt4240.asteroids.AssetLoader;

public class AudioManager {

    private AssetLoader assetLoader;
    private Music backgroundMusic;
    private Sound explosion;
    private Sound powerup;
    private Sound shoot;

    @Inject
    public AudioManager(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
    }

    public void playBackgroundMusic() {
        if (backgroundMusic == null) {
            backgroundMusic = assetLoader.getBackgroundMusic();
            backgroundMusic.setLooping(true);
        }
        if (!backgroundMusic.isPlaying())
            this.backgroundMusic.play();
    }

    public void playShoot() {
        if (shoot == null) shoot = assetLoader.getShoot();
        shoot.play();
    }

    public void playExplosion() {
        if (explosion == null) explosion = assetLoader.getExplosion();
        explosion.play();
    }

    public void playPowerup() {
        if (powerup == null) powerup = assetLoader.getPowerup();
        powerup.play();
    }
}
