package no.ntnu.tdt4240.asteroids.game.collisionhandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.entity.component.BulletClass;
import no.ntnu.tdt4240.asteroids.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.bulletMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.obstacleMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.playerMapper;

public class BulletCollisionHandler implements CollisionSystem.ICollisionHandler {
    @Override
    public boolean onCollision(PooledEngine engine, Entity source, Entity target) {
        if (playerMapper.has(target)) {
            BulletClass sourceClass = bulletMapper.get(source);
            PlayerClass targetClass = playerMapper.get(target);
            if ((sourceClass.id.equals(targetClass.id))) {
                return false;
            } else {
                engine.removeEntity(source);
            }
        } else if (obstacleMapper.has(target)) {
            engine.removeEntity(source);
        }
        return true;
    }
}
