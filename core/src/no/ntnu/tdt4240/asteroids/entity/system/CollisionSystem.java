package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import no.ntnu.tdt4240.asteroids.entity.component.BoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.boundsMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.collisionMapper;

public class CollisionSystem extends IteratingSystem {

    public CollisionSystem() {
        //noinspection unchecked
        super(Family.all(CollisionComponent.class, BoundsComponent.class).get());

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CollisionComponent collisionComponent = collisionMapper.get(entity);
        BoundsComponent bounds = boundsMapper.get(entity);
        for (Entity other : getEntities()) {
            BoundsComponent otherBounds = boundsMapper.get(other);
            if (bounds.bounds.overlaps(otherBounds.bounds)) {
                collisionComponent.collisionHandler.onCollision(entity, other, getEngine());
            }
        }
    }

    public interface ICollisionHandler {
        void onCollision(Entity source, Entity target, Engine engine);
    }
}
