package no.ntnu.tdt4240.asteroids.game.effect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import no.ntnu.tdt4240.asteroids.game.shothandler.MultiShotHandler;
import no.ntnu.tdt4240.asteroids.service.Assets;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

public class MultishotEffect extends BaseShotEffect {

    public static final int DEFAULT_DURATION = 10;

    public MultishotEffect() {
        super(new MultiShotHandler(MultiShotHandler.DEFAULT_BULLET_SPEED, 3, MultiShotHandler.DEFAULT_BULLET_SPREAD));
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
        Texture texture = ServiceLocator.getAppComponent().getAssetLoader().getTexture(Assets.TextureAsset.POWERUP_MULTISHOT);
        return new TextureRegion(texture);
    }
}
