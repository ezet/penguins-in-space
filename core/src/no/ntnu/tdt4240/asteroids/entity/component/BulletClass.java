package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class BulletClass implements Component, Poolable {

    public String id = "";

    public BulletClass(String id) {
        this.id = id;
    }

    public BulletClass() {
    }

    @Override
    public void reset() {
        id = "";
    }
}
