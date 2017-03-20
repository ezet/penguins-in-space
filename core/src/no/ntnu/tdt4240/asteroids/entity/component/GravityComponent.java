package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class GravityComponent implements Component, Pool.Poolable {

    public float gravity;

    @SuppressWarnings("unused")
    public GravityComponent() {
    }

    public GravityComponent(float gravity) {
        this.gravity = gravity;
    }

    @Override
    public void reset() {
        gravity = 0;
    }
}
