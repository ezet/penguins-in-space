package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class MovementComponent implements Component, Pool.Poolable {

    public Vector2 acceleration;

    public Vector2 velocity;

    public MovementComponent() {
        acceleration = new Vector2();
        velocity = new Vector2();
    }

    @Override
    public void reset() {
        acceleration.setZero();
        velocity.setZero();

    }
}
