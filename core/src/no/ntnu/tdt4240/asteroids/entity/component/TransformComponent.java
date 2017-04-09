package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class TransformComponent implements Component, Pool.Poolable {

    public final Vector2 position;

    public final Vector2 rotation;

    public final Vector2 scale;

    @SuppressWarnings("WeakerAccess")
    public TransformComponent() {
        position = new Vector2();
        rotation = new Vector2();
        scale = new Vector2(1, 1);
    }

    public TransformComponent(int positionX, int positionY, int rotationX, int rotationY) {
        this();
        position.set(positionX, positionY);
        rotation.set(rotationX, rotationY);
    }

    @Override
    public void reset() {
        position.setZero();
        rotation.setZero();
        scale.set(1, 1);
    }

}
