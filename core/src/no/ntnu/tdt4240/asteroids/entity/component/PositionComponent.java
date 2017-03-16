package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class PositionComponent implements Component, Pool.Poolable {

    public Vector2 position;

    public Vector2 rotation;

    @SuppressWarnings("WeakerAccess")
    public PositionComponent() {
        position = new Vector2();
        rotation = new Vector2();
    }

    public PositionComponent(int positionX, int positionY, int rotationX, int rotationY) {
        this();
        position.set(positionX, positionY);
        rotation.set(rotationX, rotationY);
    }

    @Override
    public void reset() {
        position.setZero();
        rotation.setZero();
    }

}
