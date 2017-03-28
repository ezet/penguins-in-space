package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.inject.Inject;

import no.ntnu.tdt4240.asteroids.Assets;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;

public class DefaultDrawableComponentFactory implements IDrawableComponentFactory {

    private static final int PLAYER = 0;
    private static final int PROJECTILE = 1;
    private static final int OBSTACLE = 2;
    private static final int POWERUP = 3;

    // TODO: use assets and atlas
    private final PooledEngine engine;
    private Assets assets;
    private TextureRegion[] map = new TextureRegion[4];

    @Inject
    public DefaultDrawableComponentFactory(PooledEngine engine, Assets assets) {
        this.engine = engine;
        this.assets = assets;
    }

    @Override
    public DrawableComponent getPlayer() {
        return getDrawable(PLAYER);
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

    private DrawableComponent getDrawable(int entity) {
        DrawableComponent component = engine.createComponent(DrawableComponent.class);
        TextureRegion textureRegion = map[entity];
        if (textureRegion == null) {
            textureRegion = getTextureRegion(entity);
            map[entity] = textureRegion;
        }
        component.texture = textureRegion;
        return component;
    }

    private TextureRegion getTextureRegion(int entity) {
        switch (entity) {
            case PLAYER:
                return new TextureRegion(assets.getPlayer());
            case OBSTACLE:
                return new TextureRegion(assets.getObstacle());
            case PROJECTILE:
                return new TextureRegion(assets.getProjectile());
            case POWERUP:
                return new TextureRegion(assets.getEffect());
        }
        return null;
    }
}
