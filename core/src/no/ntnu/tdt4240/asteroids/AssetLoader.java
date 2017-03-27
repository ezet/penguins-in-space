package no.ntnu.tdt4240.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class AssetLoader {
    // TODO: Implement AssetLoader helper class using AssetManager, possibly make AssetLoader a singleton

    //public AssetManager assetManager;

    public static Music backgroundMusic;
    public static Sound explosion, shot, die, powerup;

    void loadAssets() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/music.mp3"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("Sounds/explosion.wav"));
        shot = Gdx.audio.newSound(Gdx.files.internal("Sounds/shoot.wav"));
        powerup = Gdx.audio.newSound(Gdx.files.internal("Sounds/powerup.wav"));
        //die = Gdx.audio.newMusic(Gdx.files.internal("Sounds/music.mp3"));
    }

    void dispose(){
        backgroundMusic.dispose();

    }

}
