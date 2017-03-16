package no.ntnu.tdt4240.asteroids;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    // TODO: Implement Assets helper class using AssetManager, possibly make Assets a singleton

    public AssetManager assetManager;

    public Assets() {
        this.assetManager = new AssetManager();
    }

    public void loadAssets() {
        // TODO: load assets
    }

    public <T> T get(String asset) {
        assetManager.load(asset, Texture.class);
        return assetManager.get(asset);
    }

}
