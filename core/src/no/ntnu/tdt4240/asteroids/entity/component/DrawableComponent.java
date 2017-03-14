package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class DrawableComponent implements Component, Pool.Poolable {

    private TextureRegion region;

    public DrawableComponent() {
    }

    @Override
    public void reset() {
        setRegion(null);
    }

    public TextureRegion getRegion() {
        return region;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
    }
}
