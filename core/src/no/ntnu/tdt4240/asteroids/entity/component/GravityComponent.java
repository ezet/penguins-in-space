package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;

public class GravityComponent implements Component {

    public float gravity;

    @SuppressWarnings("unused")
    public GravityComponent() {
    }

    public GravityComponent(float gravity) {
        this.gravity = gravity;
    }
}
