package no.ntnu.tdt4240.asteroids.service;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class Assets {

    public static final String DATA_UISKIN_JSON = "data/uiskin.json";
    public static final String PLAYER_DEFAULT = "playerBlack.png";
    public static final String POWERUP = "powerup.png";
    public static final String OBSTACLE = "obstacle.png";
    public static final String PROJECTILE = "projectile.png";
    public static final String PLAYER_RED_PNG = "playerRed.png";
    public static final String PLAYER_BLUE_PNG = "playerBlue.png";
    public static final String PLAYER_GREEN_PNG = "playerGreen.png";
    public static final String PLAYER_YELLOW_PNG = "playerYellow.png";

    public AssetManager getAssetManager() {
        return assetManager;
    }

    private AssetManager assetManager;
    private boolean loaded = false;

    public Assets() {
        assetManager = new AssetManager();
    }

    public void loadTextures() {
        assetManager.load(DATA_UISKIN_JSON, Skin.class);
        assetManager.load(PLAYER_DEFAULT, Texture.class);
        assetManager.load(POWERUP, Texture.class);
        assetManager.load("invuln.png", Texture.class);
        assetManager.load(OBSTACLE, Texture.class);
        assetManager.load(PROJECTILE, Texture.class);
        assetManager.load("explosion.png", Texture.class);
        assetManager.load(PLAYER_RED_PNG, Texture.class);
        assetManager.load(PLAYER_BLUE_PNG, Texture.class);
        assetManager.load(PLAYER_GREEN_PNG, Texture.class);
        assetManager.load(PLAYER_YELLOW_PNG, Texture.class);
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

    public Texture getEffect() {
        return assetManager.get(POWERUP);
    }

    public Array<String> getCharacters(){
        String[] pngs = {PLAYER_DEFAULT, PLAYER_BLUE_PNG, PLAYER_GREEN_PNG, PLAYER_YELLOW_PNG, PLAYER_RED_PNG};
        return new Array<>(pngs);
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

    public Skin getUiSkin() {
        return assetManager.get(DATA_UISKIN_JSON, Skin.class);
    }

    public boolean update() {
        if (loaded) return loaded;
        loaded = assetManager.update();
        return loaded;
    }

    public Texture getTexture(String asset) {
        return get(asset, Texture.class);
    }

    private <T> T get(String asset, Class<T> cls) {
        // TODO: 07-Apr-17 finish loading
        return assetManager.get(asset, cls);
    }
}
