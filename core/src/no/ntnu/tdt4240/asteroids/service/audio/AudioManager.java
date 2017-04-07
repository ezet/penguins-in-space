package no.ntnu.tdt4240.asteroids.service.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import no.ntnu.tdt4240.asteroids.service.Assets;

public class AudioManager {

    private Assets assets;
    private Music backgroundMusic;
    private Sound explosion;
    private Sound powerup;
    private Sound shoot;
    private float volume = 1;


    public void update(int newVolumePercentage){
        volume = (float)newVolumePercentage/100;
        backgroundMusic.setVolume(volume);
    }

    public AudioManager(Assets assets) {
        this.assets = assets;
    }

    public void playBackgroundMusic() {
        if (backgroundMusic == null) {
            backgroundMusic = assets.getBackgroundMusic();
            backgroundMusic.setLooping(true);
        }
        if (!backgroundMusic.isPlaying())
            this.backgroundMusic.play();
    }

    public void playShoot() {
        if (shoot == null) shoot = assets.getShoot();
        shoot.play(volume);
    }

    public void playExplosion() {
        if (explosion == null) explosion = assets.getExplosion();
        explosion.play(volume);
    }

    public void playPowerup() {
        if (powerup == null) powerup = assets.getPowerup();
        powerup.play(volume);
    }
}
