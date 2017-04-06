package no.ntnu.tdt4240.asteroids.service;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Assets {

    public AssetManager getAssetManager() {
        return assetManager;
    }

    private AssetManager assetManager;
    private boolean loaded = false;

    public Assets() {
        assetManager = new AssetManager();
    }

    public void loadTextures() {
        assetManager.load("playerBlack.png", Texture.class);
        assetManager.load("powerup.png", Texture.class);
        assetManager.load("invuln.png", Texture.class);
        assetManager.load("obstacle.png", Texture.class);
        assetManager.load("projectile.png", Texture.class);
        assetManager.load("explosion.png", Texture.class);
        assetManager.load("playerRed.png", Texture.class);
        assetManager.load("playerBlue.png", Texture.class);
        assetManager.load("playerGreen.png", Texture.class);
        assetManager.load("playerYellow.png", Texture.class);
        assetManager.load("data/touchBackground.png", Texture.class);
        assetManager.load("data/touchKnob.png", Texture.class);
        assetManager.update();
    }

    public Texture getTouchBackground() {
        return assetManager.get("data/touchBackground.png");
    }

    public Texture getTouchKnob() {
        return assetManager.get("data/touchKnob.png");
    }


    public Texture getPlayer(){
        return assetManager.get(ServiceLocator.getAppComponent().getSettings().getPlayerAppearance());
    }

    public Texture getProjectile() {
        return assetManager.get("projectile.png");
    }

    public Texture getObstacle() {
        return assetManager.get("obstacle.png");
    }

    public Texture getEffect() {
        return assetManager.get("powerup.png");
    }

    public Array<String> getCharacters(){
        String[] pngs = {"playerBlack.png", "playerBlue.png", "playerGreen.png", "playerYellow.png", "playerRed.png"};
        return new Array(pngs);
    }

    public Texture getObstacleExplosion() {
        return assetManager.get("explosion.png", Texture.class);
    }

    public void loadAudio() {
        loaded = false;
        assetManager.load("sound/music.mp3", Music.class);
        assetManager.load("sound/explosion.wav", Sound.class);
        assetManager.load("sound/shoot.wav", Sound.class);
        assetManager.load("sound/powerup.wav", Sound.class);
        assetManager.update();
    }

    public Music getBackgroundMusic() {
        if (!assetManager.isLoaded("sound/music.mp3")) {
            assetManager.load("sound/music.mp3", Music.class);
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


    public void dispose() {
//        assetManager.clear();
    }

    public boolean update() {
        if (loaded) return loaded;
        loaded = assetManager.update();
        return loaded;
    }
}
