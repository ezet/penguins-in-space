package no.ntnu.tdt4240.asteroids;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import javax.inject.Inject;
import javax.inject.Singleton;

import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

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
        assetManager.update();
    }


    public Texture getPlayer(){
        return assetManager.get(ServiceLocator.gameComponent.getGameSettings().playerAppearance);
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
        assetManager.finishLoading();
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


    void dispose() {
//        assetManager.clear();
    }

    public boolean update() {
        if (loaded) return loaded;
        loaded = assetManager.update();
        return loaded;
    }
}
