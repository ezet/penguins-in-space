package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;

import no.ntnu.tdt4240.asteroids.entity.component.BoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.boundsMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.collisionMapper;

public class CollisionSystem extends IteratingSystem {

    public Array<ICollisionHandler> listeners = new Array<>();

    public CollisionSystem() {
        //noinspection unchecked
        super(Family.all(CollisionComponent.class, BoundsComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (entity.isScheduledForRemoval()) return;
        CollisionComponent collisionComponent = collisionMapper.get(entity);
        BoundsComponent bounds = boundsMapper.get(entity);
        if (collisionComponent == null || bounds == null) return;
        for (Entity other : getEntities()) {
            if (other.isScheduledForRemoval()) continue;
            if (collisionComponent.ignoreComponents != null
                    && collisionComponent.ignoreComponents.matches(other)) continue;
            if (entity == other) continue;
            BoundsComponent otherBounds = boundsMapper.get(other);
            if (bounds.rectangularBounds.overlaps(otherBounds.rectangularBounds)) {
                if (collisionComponent.collisionHandler != null) {
                    collisionComponent.collisionHandler.onCollision(entity, other, getEngine());
                }
                notifyListeners(entity, other);
            }
        }
    }

    private void notifyListeners(Entity source, Entity target) {
        for (ICollisionHandler listener : listeners) {
            listener.onCollision(source, target, getEngine());
        }
    }


    public interface ICollisionHandler {
        void onCollision(Entity source, Entity target, Engine engine);
    }
}
