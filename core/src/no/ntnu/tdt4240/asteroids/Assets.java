package no.ntnu.tdt4240.asteroids;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

public class Assets {

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

    public Array<TextureRegion> getExplosions() {
        Array<TextureRegion> explosions = new Array<>();
        Texture texture = new Texture("explosion.png");
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                explosions.add(new TextureRegion(texture, j * 64, i * 64, 64, 64));
            }
        }
        return explosions;
    }

    public Array<TextureRegion> getPowerupPickupAnimationSequence(){
        Array<TextureRegion> animationSequence = new Array<>();
        for (int i = 0; i < 8; i++) {
            Texture texture;
            if (i < 2){
                texture = assetManager.get("playerRed.png");
            } else if (i < 4){
                texture = assetManager.get("playerGreen.png");
            } else if (i < 6){
                texture = assetManager.get("playerBlue.png");
            } else {
                texture = assetManager.get("playerYellow.png");
            }
            animationSequence.add(new TextureRegion(texture));
        }
        return animationSequence;
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
