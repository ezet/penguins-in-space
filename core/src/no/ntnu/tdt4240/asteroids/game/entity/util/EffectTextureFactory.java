package no.ntnu.tdt4240.asteroids.game.entity.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EffectTextureFactory {

    public TextureRegion getInvulnerability() {
        return new TextureRegion(new Texture("invuln.png"));
    }
}
