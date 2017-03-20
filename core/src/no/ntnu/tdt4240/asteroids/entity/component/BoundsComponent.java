package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

// TODO: consider merging BoundsComponent and System into Collision
public class BoundsComponent implements Component, Pool.Poolable {

    public final Rectangle rectangularBounds = new Rectangle();

    @Override
    public void reset() {
        rectangularBounds.set(0, 0, 0, 0);
    }
}
