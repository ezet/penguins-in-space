package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import javax.inject.Inject;

import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.service.Assets;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

import static no.ntnu.tdt4240.asteroids.service.Assets.OBSTACLE;
import static no.ntnu.tdt4240.asteroids.service.Assets.PLAYER_BLUE_PNG;
import static no.ntnu.tdt4240.asteroids.service.Assets.PLAYER_DEFAULT;
import static no.ntnu.tdt4240.asteroids.service.Assets.PLAYER_GREEN_PNG;
import static no.ntnu.tdt4240.asteroids.service.Assets.PLAYER_RED_PNG;
import static no.ntnu.tdt4240.asteroids.service.Assets.PLAYER_YELLOW_PNG;
import static no.ntnu.tdt4240.asteroids.service.Assets.POWERUP;
import static no.ntnu.tdt4240.asteroids.service.Assets.PROJECTILE;

public class DefaultDrawableComponentFactory implements IDrawableComponentFactory {

    // TODO: use assets and atlas
    private static final String TAG = DefaultDrawableComponentFactory.class.getSimpleName();
    private final PooledEngine engine;
    private int playerCounter = 0;
    private Assets assets;
    private Array<String> playerTextures = new Array<>();

    @Inject
    public DefaultDrawableComponentFactory(PooledEngine engine, Assets assets) {
        this.engine = engine;
        this.assets = assets;
        playerTextures.add(PLAYER_DEFAULT);
        playerTextures.add(PLAYER_BLUE_PNG);
        playerTextures.add(PLAYER_RED_PNG);
        playerTextures.add(PLAYER_GREEN_PNG);
        playerTextures.add(PLAYER_YELLOW_PNG);
    }

    @Override
    public DrawableComponent getPlayer() {
        playerCounter = 0;
        String playerAppearance = ServiceLocator.getAppComponent().getSettings().getPlayerAppearance();
        return getDrawable(playerAppearance);
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
    public DrawableComponent getProjectile() {
        return getDrawable(PROJECTILE);
    }

    @Override
    public DrawableComponent getObstacle() {
        return getDrawable(OBSTACLE);
    }

    @Override
    public DrawableComponent getPowerup() {
        return getDrawable(POWERUP);
    }

    private DrawableComponent getDrawable(String asset) {
        DrawableComponent component = engine.createComponent(DrawableComponent.class);
        component.texture = getTextureRegion(asset);
        return component;
    }

    private TextureRegion getTextureRegion(String asset) {
        return new TextureRegion(assets.getTexture(asset));
    }
}
