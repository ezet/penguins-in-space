package no.ntnu.tdt4240.asteroids.service;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;


public class Assets {

    private AssetManager assetManager;

    public Assets() {
        assetManager = new AssetManager();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void loadTextures() {
        assetManager.load(SkinAsset.UISKIN, Skin.class);
        assetManager.load(TextureAsset.PLAYER_DEFAULT, Texture.class);
        assetManager.load(TextureAsset.POWERUP, Texture.class);
        assetManager.load(TextureAsset.PLAYER_INVULNERABLE, Texture.class);
        assetManager.load(TextureAsset.OBSTACLE, Texture.class);
        assetManager.load(TextureAsset.PROJECTILE, Texture.class);
        assetManager.load(TextureAsset.EXPLOSION, Texture.class);
        assetManager.load(TextureAsset.PLAYER_RED, Texture.class);
        assetManager.load(TextureAsset.PLAYER_BLUE, Texture.class);
        assetManager.load(TextureAsset.PLAYER_GREEN, Texture.class);
        assetManager.load(TextureAsset.PLAYER_YELLOW, Texture.class);
        assetManager.load(TextureAsset.TOUCH_BACKGROUND, Texture.class);
        assetManager.load(TextureAsset.TOUCH_KNOB, Texture.class);
        assetManager.update();
    }

    public void loadAudio() {
        assetManager.load(MusicAsset.SOUND_MUSIC_MP3, Music.class);
        assetManager.load(SoundAsset.SOUND_EXPLOSION_WAV, Sound.class);
        assetManager.load(SoundAsset.SOUND_SHOOT_WAV, Sound.class);
        assetManager.load(SoundAsset.SOUND_POWERUP_WAV, Sound.class);
        assetManager.update();
    }

    public Array<String> getCharacters() {
        String[] pngs = {TextureAsset.PLAYER_DEFAULT, TextureAsset.PLAYER_BLUE, TextureAsset.PLAYER_GREEN, TextureAsset.PLAYER_YELLOW, TextureAsset.PLAYER_RED};
        return new Array<>(pngs);
    }

    public void dispose() {
        assetManager.clear();
    }

    public Skin getSkin(String asset) {
        return get(asset, Skin.class);
    }

    public boolean update() {
        return assetManager.update();
    }

    public Music getMusic(String asset) {
        return get(asset, Music.class);
    }

    public Sound getSound(String asset) {
        return get(asset, Sound.class);
    }

    public Texture getTexture(String asset) {
        return get(asset, Texture.class);
    }

    private <T> T get(String asset, Class<T> cls) {
        // TODO: 07-Apr-17 finish loading
        return assetManager.get(asset, cls);
    }

    public static abstract class SkinAsset {
        public static final String UISKIN = "data/uiskin.json";
    }

    public static abstract class TextureAsset {
        public static final String PLAYER_DEFAULT = "playerBlack.png";
        public static final String POWERUP = "powerup.png";
        public static final String OBSTACLE = "obstacle.png";
        public static final String PROJECTILE = "projectile.png";
        public static final String PLAYER_RED = "playerRed.png";
        public static final String PLAYER_BLUE = "playerBlue.png";
        public static final String PLAYER_GREEN = "playerGreen.png";
        public static final String PLAYER_YELLOW = "playerYellow.png";
        public static final String PLAYER_INVULNERABLE = "invuln.png";
        public static final String EXPLOSION = "explosion.png";
        public static final String TOUCH_BACKGROUND = "data/touchBackground.png";
        public static final String TOUCH_KNOB = "data/touchKnob.png";
    }

    public static abstract class SoundAsset {

        public static final String SOUND_EXPLOSION_WAV = "sound/explosion.wav";
        public static final String SOUND_SHOOT_WAV = "sound/shoot.wav";
        public static final String SOUND_POWERUP_WAV = "sound/powerup.wav";
    }

    public static abstract class MusicAsset {
        public static final String SOUND_MUSIC_MP3 = "sound/music.mp3";


    }
}
