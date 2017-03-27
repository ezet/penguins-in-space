package no.ntnu.tdt4240.asteroids.game.collisionhandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.entity.component.EffectComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PowerupClass;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.playerMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.powerupMapper;

public class PowerupCollisionHandler implements CollisionSystem.ICollisionHandler {
    @Override
    public void onCollision(PooledEngine engine, Entity source, Entity target) {
        if (playerMapper.has(target)) {
            PowerupClass powerup = powerupMapper.get(source);
            EffectComponent effectComponent = engine.createComponent(EffectComponent.class);
            effectComponent.addEffect(powerup.effect);
            target.add(effectComponent);
            // TODO: display pickup animation
            engine.removeEntity(source);
        }
    }
}
