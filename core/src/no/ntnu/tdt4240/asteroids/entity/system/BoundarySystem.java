package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;

import no.ntnu.tdt4240.asteroids.entity.component.BoundaryComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.boundaryMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.drawableMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.positionMapper;

public class BoundarySystem extends IteratingSystem {

    @SuppressWarnings("unused")
    private static final String TAG = BoundarySystem.class.getSimpleName();
    private static final Family family = Family.all(PositionComponent.class, MovementComponent.class, DrawableComponent.class).get();
    private final int width;
    private final int height;
    private final Rectangle boundary;

    public BoundarySystem(int width, int height) {
        //noinspection unchecked
        super(family);
        boundary = new Rectangle(0, 0, width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = positionMapper.get(entity);
        DrawableComponent drawable = drawableMapper.get(entity);
        boolean free = false;
        BoundaryComponent boundaryComponent = boundaryMapper.get(entity);
        if (boundaryComponent != null && boundaryComponent.boundaryMode == BoundaryComponent.MODE_FREE) {
            free = true;
        }
        if (pos.position.x + drawable.region.getRegionHeight() / 2 < 0) {
            if (free) pos.position.x = width;
            else deleteEntity(entity);
        } else if (pos.position.x - drawable.region.getRegionWidth() / 2 > width) {
            if (free) pos.position.x = 0;
            else deleteEntity(entity);
        } else if (pos.position.y + drawable.region.getRegionHeight() / 2 < 0) {
            if (free) pos.position.y = height;
            else deleteEntity(entity);
        } else if (pos.position.y - drawable.region.getRegionHeight() / 2 > height) {
            if (free) pos.position.y = 0;
            else deleteEntity(entity);
        }
    }

    private void deleteEntity(Entity entity) {
        getEngine().removeEntity(entity);
    }
}
