package no.ntnu.tdt4240.asteroids.service.audio;

import com.badlogic.gdx.audio.Music;

import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.ISettingsService;

public class AudioService {

    private final AssetService assetService;
    private Music backgroundMusic;
    private float soundVolume = 1;
    private float volume;
    private boolean muted = false;


    public AudioService(AssetService assetService, ISettingsService settingsService) {
        this.assetService = assetService;
        setSoundVolume(settingsService.getInt(ISettingsService.SOUND_VOLUME, 100));
        setMusicVolume(settingsService.getInt(ISettingsService.MUSIC_VOLUME, 100));
        muted = !settingsService.getBoolean(ISettingsService.MUSIC_ENABLED);

        // TODO: 19-Apr-17 implement sound volume
    }

    public void setMusicVolume(float newVolume) {
        this.volume = newVolume / 100f;
        assetService.getMusic(AssetService.MusicAsset.SOUND_MUSIC_MP3).setVolume(this.volume);
    }

    public void startMusic() {
        if (backgroundMusic == null) {
            backgroundMusic = assetService.getMusic(AssetService.MusicAsset.SOUND_MUSIC_MP3);
            backgroundMusic.setLooping(true);
        }
        if (!backgroundMusic.isPlaying())
            this.backgroundMusic.play();
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public void stopMusic() {
        backgroundMusic.stop();
    }

    public void playSound(String sound) {
        if (muted) return;
        assetService.getSound(sound).play(volume);
    }

    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume / 100f;
    }
}
