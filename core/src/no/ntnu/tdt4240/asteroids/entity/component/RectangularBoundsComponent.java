package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

public class RectangularBoundsComponent extends BoundsComponent {

    private final Rectangle bounds = new Rectangle();

    @Override
    public void reset() {
        bounds.set(0, 0, 0, 0);
    }

    @Override
    public Shape2D getBounds() {
        return bounds;
    }

    @Override
    public void setCenter(Vector2 vector) {
        bounds.setCenter(vector);
    }

    @Override
    public void setSize(int width, int height) {
        bounds.setSize(width, height);

    }
}
