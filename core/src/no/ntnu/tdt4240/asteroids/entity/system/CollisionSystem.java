package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import no.ntnu.tdt4240.asteroids.entity.component.BoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CircularBoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.RectangularBoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.boundsMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.collisionMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.movementMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.transformMapper;

public class CollisionSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(CollisionComponent.class).one(RectangularBoundsComponent.class, CircularBoundsComponent.class).get();
    Array<ICollisionHandler> listeners = new Array<>();

    public CollisionSystem() {
        super(FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (entity.isScheduledForRemoval()) return;
        CollisionComponent thisCollision = collisionMapper.get(entity);
        BoundsComponent bounds = boundsMapper.get(entity);
        MovementComponent entityMovement = movementMapper.get(entity);
        if (thisCollision == null || bounds == null) return;
//        thisCollision.preCollisionVelocity = null;
        for (Entity other : getEntities()) {
            if (other.isScheduledForRemoval()) continue;
            if (thisCollision.ignoredEntities != null
                    && thisCollision.ignoredEntities.matches(other)) continue;
            if (entity == other) continue;
            BoundsComponent otherBounds = boundsMapper.get(other);
            if (bounds.overlaps(otherBounds)) {
                CollisionComponent otherCollision = collisionMapper.get(other);
                MovementComponent otherMovement = movementMapper.get(other);
                Vector2 thisVelocityTmp = new Vector2(entityMovement.velocity);
                Vector2 otherVelocity = otherMovement.velocity;


                // More realistic algorithm
//                Circle thisCircle = (Circle) bounds.getBounds();
//                Circle otherCircle = (Circle) otherBounds.getBounds();
//                double distance = Math.sqrt(Math.pow((thisCircle.x) - (otherCircle.x), 2)
//                        + Math.pow((thisCircle.y) - (otherCircle.y), 2));
//                double normalizedX = (otherCircle.x - thisCircle.x) / distance;
//                double normalizedY = (otherCircle.y - thisCircle.y) / distance;
//                double p = 2 * (thisVelocity.x * normalizedX + thisVelocity.y * normalizedY - otherVelocity.x * normalizedX - otherVelocity.y * normalizedY) /
//                        (thisCollision.mass + otherCollision.mass);
//                entityMovement.velocity.x = (float) (entityMovement.velocity.x - p * thisCollision.mass * normalizedX);
//                entityMovement.velocity.y = (float) (entityMovement.velocity.y - p * thisCollision.mass * normalizedY);
//
//                otherMovement.velocity.x = (float) (otherMovement.velocity.x - p * otherCollision.mass * normalizedX);
//                otherMovement.velocity.y = (float) (otherMovement.velocity.y - p * otherCollision.mass * normalizedY);

                // Simple algorithm
                entityMovement.velocity.x = entityMovement.velocity.x * (thisCollision.mass - otherCollision.mass) + (2 * otherCollision.mass * otherVelocity.x) / (thisCollision.mass + otherCollision.mass);
                entityMovement.velocity.y = entityMovement.velocity.y * (thisCollision.mass - otherCollision.mass) + (2 * otherCollision.mass * otherVelocity.y) / (thisCollision.mass + otherCollision.mass);
                otherMovement.velocity.x = otherMovement.velocity.x * (otherCollision.mass - thisCollision.mass) + (2 * thisCollision.mass * thisVelocityTmp.x) / (otherCollision.mass + thisCollision.mass);
                otherMovement.velocity.y = otherMovement.velocity.y * (otherCollision.mass - thisCollision.mass) + (2 * thisCollision.mass * thisVelocityTmp.y) / (otherCollision.mass + thisCollision.mass);

//                while (bounds.overlaps(otherBounds)) {
//                    getEngine().getSystem(MovementSystem.class).processEntity(entity, deltaTime);
//                    getEngine().getSystem(BoundsSystem.class).processEntity(entity, deltaTime);
//                    getEngine().getSystem(MovementSystem.class).processEntity(other, deltaTime);
//                    getEngine().getSystem(BoundsSystem.class).processEntity(other, deltaTime);
//                }
                revertPosition(entity);
                revertPosition(other);

                if (thisCollision.collisionHandler != null) {
                    thisCollision.collisionHandler.onCollision((PooledEngine) getEngine(), entity, other);
                }
                notifyListeners(entity, other);
                if (otherCollision.collisionHandler != null) {
                    otherCollision.collisionHandler.onCollision((PooledEngine) getEngine(), other, entity);
                }
                notifyListeners(other, entity);
            }
        }
        bounds.getCenter(thisCollision.preCollisionPosition);
    }

    private void notifyListeners(Entity source, Entity target) {
        for (ICollisionHandler listener : listeners) {
            listener.onCollision((PooledEngine) getEngine(), source, target);
        }
    }

    private void revertPosition(Entity entity) {
        TransformComponent transformComponent = transformMapper.get(entity);
        CollisionComponent collisionComponent = collisionMapper.get(entity);
        if (collisionComponent.preCollisionPosition.isZero()) {
            getEngine().removeEntity(entity);
        } else {
            transformComponent.position = collisionComponent.preCollisionPosition;
        }
    }

    public interface ICollisionHandler {
        void onCollision(PooledEngine engine, Entity source, Entity target);
    }
}
