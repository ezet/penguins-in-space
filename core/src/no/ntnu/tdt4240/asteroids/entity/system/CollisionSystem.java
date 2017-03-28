package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;

import no.ntnu.tdt4240.asteroids.entity.component.BoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CircularBoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.RectangularBoundsComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.boundsMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.collisionMapper;

public class CollisionSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(CollisionComponent.class).one(RectangularBoundsComponent.class, CircularBoundsComponent.class).get();
    Array<ICollisionHandler> listeners = new Array<>();

    public CollisionSystem() {
        super(FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (entity.isScheduledForRemoval()) return;
        CollisionComponent collisionComponent = collisionMapper.get(entity);
        BoundsComponent bounds = boundsMapper.get(entity);
        if (collisionComponent == null || bounds == null) return;
        for (Entity other : getEntities()) {
            if (other.isScheduledForRemoval()) continue;
            if (collisionComponent.ignoredEntities != null
                    && collisionComponent.ignoredEntities.matches(other)) continue;
            if (entity == other) continue;
            BoundsComponent otherBounds = boundsMapper.get(other);
            if (bounds.overlaps(otherBounds)) {
                if (collisionComponent.collisionHandler != null) {
                    collisionComponent.collisionHandler.onCollision((PooledEngine) getEngine(), entity, other);
                }
                notifyListeners(entity, other);
            }
        }
    }

    private void notifyListeners(Entity source, Entity target) {
        for (ICollisionHandler listener : listeners) {
            listener.onCollision((PooledEngine) getEngine(), source, target);
        }
    }


    public interface ICollisionHandler {
        void onCollision(PooledEngine engine, Entity source, Entity target);
    }
}
