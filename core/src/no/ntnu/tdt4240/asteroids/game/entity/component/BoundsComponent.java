package no.ntnu.tdt4240.asteroids.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

// TODO: consider merging BoundsComponent and System into Collision
public abstract class BoundsComponent implements Component, Pool.Poolable {

    @Override
    public abstract void reset();

    public boolean overlaps(BoundsComponent other) {
        if (getBounds() instanceof Rectangle && other.getBounds() instanceof Rectangle) {
            return Intersector.overlaps((Rectangle) getBounds(), (Rectangle) other.getBounds());
        } else if (getBounds() instanceof Circle && other.getBounds() instanceof Circle) {
            return Intersector.overlaps((Circle) getBounds(), (Circle) other.getBounds());
        } else if (getBounds() instanceof Circle && other.getBounds() instanceof Rectangle) {
            return Intersector.overlaps((Circle) getBounds(), (Rectangle) other.getBounds());
        } else if (getBounds() instanceof Rectangle && other.getBounds() instanceof Circle) {
            return Intersector.overlaps((Circle) other.getBounds(), (Rectangle) getBounds());
        }
        throw new RuntimeException("Cannot compare " + this.getBounds() + " and " + other.getBounds());
    }

    public abstract Shape2D getBounds();

    public abstract void setCenter(Vector2 vector);

    public abstract void setSize(float width, float height);

    public abstract Vector2 getCenter(Vector2 result);
}