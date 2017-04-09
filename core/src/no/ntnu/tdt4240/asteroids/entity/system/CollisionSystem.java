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
        if (thisCollision == null || bounds == null) return;
        for (Entity other : getEntities()) {
            if (thisCollision.handledCollisions.contains(other)) continue;
            if (entity == other) continue;
            if (other.isScheduledForRemoval()) continue;
            if (thisCollision.ignoredEntities != null
                    && thisCollision.ignoredEntities.matches(other)) continue;
            BoundsComponent otherBounds = boundsMapper.get(other);
            CollisionComponent otherCollision = collisionMapper.get(other);
            if (otherBounds == null || otherCollision == null) continue;
            otherCollision.handledCollisions.add(entity);
            if (bounds.overlaps(otherBounds)) {
                boolean validEvent = true;
                if (thisCollision.collisionHandler != null) {
                    validEvent = thisCollision.collisionHandler.onCollision((PooledEngine) getEngine(), entity, other);
                }
                if (validEvent && otherCollision.collisionHandler != null)
                    validEvent = otherCollision.collisionHandler.onCollision((PooledEngine) getEngine(), other, entity);

                if (validEvent) {
                    notifyListeners(entity, other);
                    notifyListeners(other, entity);
                }
            }
        }
        thisCollision.handledCollisions.clear();
        bounds.getCenter(thisCollision.preCollisionPosition);
    }

    private void doCollision(Entity entity, Entity other) {
        CollisionComponent thisCollision = collisionMapper.get(entity);
        MovementComponent entityMovement = movementMapper.get(entity);
        CollisionComponent otherCollision = collisionMapper.get(other);
        MovementComponent otherMovement = movementMapper.get(other);
        Vector2 thisVelocityTmp = new Vector2(entityMovement.velocity);
        Vector2 otherVelocity = otherMovement.velocity;

        entityMovement.velocity.x = entityMovement.velocity.x * (thisCollision.mass - otherCollision.mass) + (2 * otherCollision.mass * otherVelocity.x) / (thisCollision.mass + otherCollision.mass);
        entityMovement.velocity.y = entityMovement.velocity.y * (thisCollision.mass - otherCollision.mass) + (2 * otherCollision.mass * otherVelocity.y) / (thisCollision.mass + otherCollision.mass);
        otherMovement.velocity.x = otherMovement.velocity.x * (otherCollision.mass - thisCollision.mass) + (2 * thisCollision.mass * thisVelocityTmp.x) / (otherCollision.mass + thisCollision.mass);
        otherMovement.velocity.y = otherMovement.velocity.y * (otherCollision.mass - thisCollision.mass) + (2 * thisCollision.mass * thisVelocityTmp.y) / (otherCollision.mass + thisCollision.mass);
        revertPosition(entity);
        revertPosition(other);
    }

    //         More realistic algorithm
    private void doCollision2(Entity entity, Entity other) {
        CollisionComponent thisCollision = collisionMapper.get(entity);
        MovementComponent entityMovement = movementMapper.get(entity);
        CollisionComponent otherCollision = collisionMapper.get(other);
        MovementComponent otherMovement = movementMapper.get(other);
        Vector2 thisVelocityTmp = new Vector2(entityMovement.velocity);
        Vector2 otherVelocity = otherMovement.velocity;
        BoundsComponent bounds = boundsMapper.get(entity);
        BoundsComponent otherBounds = boundsMapper.get(other);


        Circle thisCircle = (Circle) bounds.getBounds();
        Circle otherCircle = (Circle) otherBounds.getBounds();
        double distance = Math.sqrt(Math.pow((thisCircle.x) - (otherCircle.x), 2)
                + Math.pow((thisCircle.y) - (otherCircle.y), 2));
        double normalizedX = (otherCircle.x - thisCircle.x) / distance;
        double normalizedY = (otherCircle.y - thisCircle.y) / distance;
        double p = 2 * (thisVelocityTmp.x * normalizedX + thisVelocityTmp.y * normalizedY - otherVelocity.x * normalizedX - otherVelocity.y * normalizedY) /
                (thisCollision.mass + otherCollision.mass);
        entityMovement.velocity.x = (float) (entityMovement.velocity.x - p * thisCollision.mass * normalizedX);
        entityMovement.velocity.y = (float) (entityMovement.velocity.y - p * thisCollision.mass * normalizedY);

        otherMovement.velocity.x = (float) (otherMovement.velocity.x - p * otherCollision.mass * normalizedX);
        otherMovement.velocity.y = (float) (otherMovement.velocity.y - p * otherCollision.mass * normalizedY);

        revertPosition(entity);
        revertPosition(other);
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
            transformComponent.position.set(collisionComponent.preCollisionPosition);
        }
    }

    public interface ICollisionHandler {
        boolean onCollision(PooledEngine engine, Entity source, Entity target);
    }
}
