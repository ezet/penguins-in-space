package no.ntnu.tdt4240.asteroids.game.entity.util;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import javax.inject.Inject;

import no.ntnu.tdt4240.asteroids.game.effect.IEffect;
import no.ntnu.tdt4240.asteroids.game.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.ISettingsService;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.MISSILE;
import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.OBSTACLE;
import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.PLAYER_BLUE;
import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.PLAYER_DEFAULT;
import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.PLAYER_GREEN;
import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.PLAYER_RED;
import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.PLAYER_YELLOW;
import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.PROJECTILE;


public class DefaultDrawableComponentFactory implements IDrawableComponentFactory {

    // TODO: use assets and atlas
    private static final String TAG = DefaultDrawableComponentFactory.class.getSimpleName();
    private final PooledEngine engine;
    private int playerCounter = 0;
    private AssetService assetService;
    private Array<String> playerTextures = new Array<>();

    @Inject
    public DefaultDrawableComponentFactory(PooledEngine engine, AssetService assetService) {
        this.engine = engine;
        this.assetService = assetService;
        playerTextures.add(PLAYER_BLUE);
        playerTextures.add(PLAYER_RED);
        playerTextures.add(PLAYER_GREEN);
        playerTextures.add(PLAYER_YELLOW);
    }

    @Override
    public DrawableComponent getPlayer(boolean allowCustomAppearance) {
        String playerAppearance = PLAYER_DEFAULT;
        if (allowCustomAppearance) {
            playerAppearance = ServiceLocator.getAppComponent().getSettingsService()
                    .getString(ISettingsService.PLAYER_APPEARANCE, PLAYER_DEFAULT);
        }
        return getDrawable(playerAppearance);
    }

    @Override
    public DrawableComponent getProjectile() {
        return getDrawable(PROJECTILE);
    }

    @Override
    public DrawableComponent getObstacle() {
        return getDrawable(OBSTACLE);
    }

    @Override
    public DrawableComponent getPowerup(IEffect effect) {
        DrawableComponent component = engine.createComponent(DrawableComponent.class);
        component.texture = effect.getPowerupTexture();
        return component;
    }

    @Override
    public void resetOpponentCount() {
        playerCounter = 0;
    }

    @Override
    public DrawableComponent getMultiPlayer() {
        if (playerCounter == playerTextures.size) {
            Gdx.app.error(TAG, "getMultiPlayer: not enough player textures");
            playerCounter = 0;
        }
        String asset = playerTextures.get(playerCounter++);
        return getDrawable(asset);
    }

    @Override
    public Component getBomb() {
        return getDrawable(AssetService.TextureAsset.BOMB);
    }

    @Override
    public Component getMissile() {
        return getDrawable(MISSILE);
    }

    private DrawableComponent getDrawable(String asset) {
        DrawableComponent component = engine.createComponent(DrawableComponent.class);
        component.texture = getTextureRegion(asset);
        return component;
    }

    private TextureRegion getTextureRegion(String asset) {
        return new TextureRegion(assetService.getTexture(asset));
    }
}
