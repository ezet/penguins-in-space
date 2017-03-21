package no.ntnu.tdt4240.asteroids.entity.collisionhandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;

public class BulletCollisionHandler implements CollisionSystem.ICollisionHandler {
    @Override
    public void onCollision(PooledEngine engine, Entity source, Entity target) {
        if (ComponentMappers.obstacleMapper.has(target)) {
            // TODO: we hit an obstacle, MISSION ACCOMPLISHED!
            engine.removeEntity(source);
        }
    }
}
