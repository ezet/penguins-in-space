package no.ntnu.tdt4240.asteroids.game.effect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import no.ntnu.tdt4240.asteroids.game.shothandler.MissileShotHandler;
import no.ntnu.tdt4240.asteroids.game.shothandler.MultiShotHandler;
import no.ntnu.tdt4240.asteroids.service.Assets;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

public class MissileShotEffect extends BaseShotEffect {

    public static final int DEFAULT_DURATION = 10;


    public MissileShotEffect() {
        super(new MissileShotHandler());
    }

    @Override
    protected float getDuration() {
        return DEFAULT_DURATION;
    }

    @Override
    protected TextureRegion getEffectTexture() {
        return null;
    }

    @Override
    public TextureRegion getPowerupTexture() {
        Texture texture = ServiceLocator.getAppComponent().getAssetLoader().getTexture(Assets.TextureAsset.POWERUP_MISSILE);
        return new TextureRegion(texture);
    }
}
