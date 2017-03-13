package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class VelocityComponent implements Component, Pool.Poolable {

    private float x;

    private float y;

    public VelocityComponent() {
    }

    public VelocityComponent(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public void reset() {
        setX(0);
        setY(0);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
