package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;

public class DefaultDrawableComponentFactory implements IDrawableComponentFactory {

    // TODO: use assets and atlas
    private final PooledEngine engine;
    private HashMap<String, TextureRegion> map = new HashMap<>();

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
        DrawableComponent component = engine.createComponent(DrawableComponent.class);
        TextureRegion textureRegion = map.get(path);
        if (textureRegion != null)
            component.texture = textureRegion;
        else {
            Texture texture = new Texture(path);
            component.texture = new TextureRegion(texture);
            map.put(path, textureRegion);
        }
        return component;
    }

    @Override
    public DrawableComponent getPowerup() {
        return getDrawable("powerup.png");
    }

}
