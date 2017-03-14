package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import no.ntnu.tdt4240.asteroids.entity.component.BoundaryComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.boundaryMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.drawableMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.positionMapper;

public class BoundarySystem extends IteratingSystem {

    private static final String TAG = BoundarySystem.class.getSimpleName();
    private final int width;
    private final int height;

    public BoundarySystem(int width, int height) {
        //noinspection unchecked
        super(Family.all(PositionComponent.class, MovementComponent.class, DrawableComponent.class).get());
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
        if (pos.position.x + drawable.getRegion().getRegionWidth() < 0) {
            if (free) {
                pos.position.x = width;
            } else {
                deleteEntity(entity);
                return;
            }
        }
        if (pos.position.x > width) {
            if (free) {
                pos.position.x = 0 - drawable.getRegion().getRegionWidth();
            } else {
                deleteEntity(entity);
                return;
            }
        }
        if (pos.position.y + drawable.getRegion().getRegionHeight() < 0) {
            if (free) {
                pos.position.y = height;
            } else {
                deleteEntity(entity);
                return;
            }
        }
        if (pos.position.y > height) {
            if (free) {
                pos.position.y = 0 - drawable.getRegion().getRegionHeight();
            } else {
                deleteEntity(entity);
                //noinspection UnnecessaryReturnStatement
                return;
            }
        }
    }

    private void deleteEntity(Entity entity) {
        getEngine().removeEntity(entity);
    }
}
