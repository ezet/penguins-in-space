package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;

public class DefaultDrawableComponentFactory implements IDrawableComponentFactory {

    // TODO: use assets and atlas
    private final PooledEngine engine;

    public DefaultDrawableComponentFactory(PooledEngine engine) {
        this.engine = engine;
    }

    @Override
    public DrawableComponent getPlayer() {
        return getDrawable("slide_1.png");
    }

    @Override
    public DrawableComponent getBullet() {
        return getDrawable("snowball-icon.png");
    }

    @Override
    public DrawableComponent getObstacle() {
        return getDrawable("snowball.png");
    }

    private DrawableComponent getDrawable(String path) {
        Texture texture = new Texture(path);
        DrawableComponent component = engine.createComponent(DrawableComponent.class);
        component.region = new TextureRegion(texture);
        return component;
    }
}
