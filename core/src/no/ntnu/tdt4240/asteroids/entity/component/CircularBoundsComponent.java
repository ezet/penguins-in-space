package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

public class CircularBoundsComponent extends BoundsComponent {

    private final Circle bounds = new Circle();

    @Override
    public void reset() {
        bounds.set(0, 0, 0);
    }

    @Override
    public Shape2D getBounds() {
        return bounds;
    }

    @Override
    public void setCenter(Vector2 vector) {
        bounds.setPosition(vector);
    }

    @Override
    public void setSize(int width, int height) {
        bounds.setRadius(Math.min(width / 2, height / 2));
    }
}
