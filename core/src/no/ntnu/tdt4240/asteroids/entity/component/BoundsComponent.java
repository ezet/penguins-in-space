package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class BoundsComponent implements Component, Pool.Poolable {

    public final Rectangle bounds = new Rectangle();

    @Override
    public void reset() {
        bounds.set(0, 0, 0, 0);
    }
}
