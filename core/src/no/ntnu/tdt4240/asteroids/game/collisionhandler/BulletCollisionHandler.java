package no.ntnu.tdt4240.asteroids.game.collisionhandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.game.entity.component.AnimationComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.IdComponent;
import no.ntnu.tdt4240.asteroids.game.entity.system.CollisionSystem;

import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.animationMapper;
import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.idMapper;
import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.obstacleMapper;
import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.playerMapper;

public class BulletCollisionHandler implements CollisionSystem.ICollisionHandler {
    @Override
    public boolean onCollision(PooledEngine engine, Entity source, Entity target) {
        if (playerMapper.has(target)) {
            IdComponent sourceId = idMapper.get(source);
            IdComponent targetId = idMapper.get(target);
            if ((sourceId.participantId.equals(targetId.participantId))) {
                return false;
            } else {
                removeBullet(engine, source);
            }
        } else if (obstacleMapper.has(target)) {
            removeBullet(engine, source);
        }
        return true;
    }

    private void removeBullet(Engine engine, Entity entity) {
        AnimationComponent animationComponent = animationMapper.get(entity);
        if (animationComponent != null) {
            animationComponent.removeEntityAfterAnimation = true;
            animationComponent.delay = 0;
        } else engine.removeEntity(entity);
    }
}
