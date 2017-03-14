package no.ntnu.tdt4240.asteroids.entity;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;

public class DefaultDrawableComponentFactory implements IDrawableComponentFactory {

    private final PooledEngine engine;

    public DefaultDrawableComponentFactory(PooledEngine engine) {
        this.engine = engine;
    }

    @Override
    public DrawableComponent getPlayer() {
        Texture texture = new Texture("slide_1.png");
        DrawableComponent component = engine.createComponent(DrawableComponent.class);
        component.setRegion(new TextureRegion(texture));
        return component;
    }

    @Override
    public DrawableComponent getBullet() {
        Texture texture = new Texture("slide_1.png");
        DrawableComponent component = engine.createComponent(DrawableComponent.class);
        component.setRegion(new TextureRegion(texture));
        return component;
    }
}
