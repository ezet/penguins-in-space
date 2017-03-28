package no.ntnu.tdt4240.asteroids;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AssetLoader {

    private AssetManager assetManager;
    private boolean loaded = false;

    public AssetLoader() {
        assetManager = new AssetManager();
    }

    void loadAudio() {
        loaded = false;
        assetManager.load("sound/music.mp3", Music.class);
        assetManager.load("sound/explosion.wav", Sound.class);
        assetManager.load("sound/shoot.wav", Sound.class);
        assetManager.load("sound/powerup.wav", Sound.class);
        assetManager.finishLoading();
//        assetManager.update(500);
//        assetManager.load("sound/music.mp3", Sound.class);
    }

    public Music getBackgroundMusic() {
        if (!assetManager.isLoaded("sound/music.mp3")) {
//            assetManager.load("sound/music.mp3", Music.class);
            loadAudio();
            assetManager.finishLoading();
        }
        return assetManager.get("sound/music.mp3");
    }

    public Sound getExplosion() {
        return assetManager.get("sound/explosion.wav", Sound.class);
    }

    public Sound getShoot() {
        return assetManager.get("sound/shoot.wav", Sound.class);
    }

    public Sound getPowerup() {
        return assetManager.get("sound/powerup.wav", Sound.class);
    }

    void dispose() {
//        assetManager.clear();
    }

    public boolean update() {
        if (loaded) return loaded;
        loaded = assetManager.update();
        return loaded;
    }
}
