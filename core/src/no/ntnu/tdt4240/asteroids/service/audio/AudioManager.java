package no.ntnu.tdt4240.asteroids.service.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import no.ntnu.tdt4240.asteroids.ISettingsService;
import no.ntnu.tdt4240.asteroids.service.Assets;

public class AudioManager {

    private Assets assets;
    private ISettingsService settingsService;
    private Music backgroundMusic;
    private Sound explosion;
    private Sound powerup;
    private Sound shoot;
    private float volume = 1;


    public AudioManager(Assets assets, ISettingsService settingsService) {
        this.assets = assets;
        this.settingsService = settingsService;
    }

    public void setMusicVolume(int newVolumePercentage) {
        volume = newVolumePercentage / 100f;
        backgroundMusic.setVolume(volume);
    }

    public void playBackgroundMusic() {
        if (backgroundMusic == null) {
            backgroundMusic = assets.getMusic(Assets.MusicAsset.SOUND_MUSIC_MP3);
            backgroundMusic.setLooping(true);
        }
        setMusicVolume(settingsService.getInt(ISettingsService.MUSIC_VOLUME));
        if (!backgroundMusic.isPlaying())
            this.backgroundMusic.play();
    }

    public void playShoot() {
        if (shoot == null) shoot = assets.getSound(Assets.SoundAsset.SOUND_SHOOT_WAV);
        shoot.play(volume);
    }

    public void playExplosion() {
        if (explosion == null) explosion = assets.getSound(Assets.SoundAsset.SOUND_EXPLOSION_WAV);
        explosion.play(volume);
    }

    public void playPowerup() {
        if (powerup == null) powerup = assets.getSound(Assets.SoundAsset.SOUND_POWERUP_WAV);
        powerup.play(volume);
    }

    public void stopMusic() {
        backgroundMusic.stop();
    }

}
