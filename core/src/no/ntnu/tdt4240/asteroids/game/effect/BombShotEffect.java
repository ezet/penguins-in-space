package no.ntnu.tdt4240.asteroids.game.effect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import no.ntnu.tdt4240.asteroids.game.shothandler.BombShotHandler;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

public class BombShotEffect extends BaseShotEffect {

    public static final int DEFAULT_DURATION = 10;

    public BombShotEffect() {
        super(new BombShotHandler());
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
        Texture texture = ServiceLocator.getAppComponent().getAssetService().getTexture(AssetService.TextureAsset.POWERUP_BOMB);
        return new TextureRegion(texture);
    }
}
