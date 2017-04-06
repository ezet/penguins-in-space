package no.ntnu.tdt4240.asteroids.game.collisionhandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import no.ntnu.tdt4240.asteroids.entity.component.AnimationComponent;
import no.ntnu.tdt4240.asteroids.entity.component.EffectComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PowerupClass;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.playerMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.powerupMapper;

public class PowerupCollisionHandler implements CollisionSystem.ICollisionHandler {
    @Override
    public boolean onCollision(PooledEngine engine, Entity source, Entity target) {
        if (playerMapper.has(target)) {
            PowerupClass powerup = powerupMapper.get(source);
            EffectComponent effectComponent = engine.createComponent(EffectComponent.class);
            effectComponent.addEffect(powerup.effect);
            target.add(effectComponent);
            AnimationComponent pickupAnimation = createAnimationComponent();
            target.add(pickupAnimation);
            engine.removeEntity(source);
        }
        return true;
    }

    private AnimationComponent createAnimationComponent(){
        AnimationComponent animationComponent = new AnimationComponent();
        Array<TextureRegion> textureRegions = ServiceLocator.getAppComponent().getAnimationFactory().getPowerupPickupAnimation();
        animationComponent.frames.addAll(textureRegions);
        return animationComponent;
    }
}
