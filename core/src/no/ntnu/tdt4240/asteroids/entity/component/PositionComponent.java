package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class PositionComponent implements Component, Pool.Poolable {

    public Vector2 position;

    public Vector2 rotation;

    public PositionComponent() {
        position = new Vector2();
        rotation = new Vector2();
    }

    public PositionComponent(int x, int y) {
        this();
        position.set(x, y);
    }

    @Override
    public void reset() {
        position.setZero();
        rotation.setZero();
    }

}
