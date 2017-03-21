package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class EffectTextureFactory {

    public static TextureRegion getInvulnerability() {
        return new TextureRegion(new Texture("invuln.png"));
    }
}
