package no.ntnu.tdt4240.asteroids.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import no.ntnu.tdt4240.asteroids.game.entity.component.BoundaryComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.TransformComponent;

import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.boundaryMapper;
import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.drawableMapper;
import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.transformMapper;

public class BoundarySystem extends IteratingSystem {

    @SuppressWarnings("unused")
    private static final String TAG = BoundarySystem.class.getSimpleName();
    private static final Family FAMILY = Family.all(TransformComponent.class, MovementComponent.class, DrawableComponent.class).get();
    private final int width;
    private final int height;
    // TODO: consider using rectangle as boundary
    //private final Rectangle boundary;

    public BoundarySystem(int width, int height) {
        super(FAMILY);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent pos = transformMapper.get(entity);
        DrawableComponent drawable = drawableMapper.get(entity);
        boolean free = false;
        BoundaryComponent boundaryComponent = boundaryMapper.get(entity);
        if (boundaryComponent != null && boundaryComponent.boundaryMode == BoundaryComponent.MODE_WRAP) {
            free = true;
        }
        if (pos.position.x + drawable.texture.getRegionHeight() / 2 < 0) {
            if (free) pos.position.x = width;
            else deleteEntity(entity);
        } else if (pos.position.x - drawable.texture.getRegionWidth() / 2 > width) {
            if (free) pos.position.x = 0;
            else deleteEntity(entity);
        } else if (pos.position.y + drawable.texture.getRegionHeight() / 2 < 0) {
            if (free) pos.position.y = height;
            else deleteEntity(entity);
        } else if (pos.position.y - drawable.texture.getRegionHeight() / 2 > height) {
            if (free) pos.position.y = 0;
            else deleteEntity(entity);
        }
    }

    private void deleteEntity(Entity entity) {
        getEngine().removeEntity(entity);
    }
}
