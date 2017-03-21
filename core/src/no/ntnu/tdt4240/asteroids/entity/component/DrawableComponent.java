package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class DrawableComponent implements Component, Pool.Poolable {

    public TextureRegion texture;

    public DrawableComponent() {
    }

    @Override
    public void reset() {
        texture = null;
    }

}
