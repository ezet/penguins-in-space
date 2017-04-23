package no.ntnu.tdt4240.asteroids.game.collisionhandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import no.ntnu.tdt4240.asteroids.game.entity.component.AnimationComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.EffectComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.PowerupClass;
import no.ntnu.tdt4240.asteroids.game.entity.system.CollisionSystem;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.effectMapper;
import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.powerupMapper;

public class PowerupCollisionHandler implements CollisionSystem.ICollisionHandler {
    @Override
    public boolean onCollision(PooledEngine engine, Entity source, Entity target) {
        EffectComponent effectComponent = effectMapper.get(target);
        if (effectComponent == null) {
            return false;
        }
        PowerupClass powerup = powerupMapper.get(source);
        effectComponent.addEffect(engine, target, powerup.effect);
        target.add(effectComponent);
        AnimationComponent pickupAnimation = createAnimationComponent();
        target.add(pickupAnimation);
        engine.removeEntity(source);
        return true;
    }

    private AnimationComponent createAnimationComponent(){
        AnimationComponent animationComponent = new AnimationComponent();
        Array<TextureRegion> textureRegions = ServiceLocator.getAppComponent().getAnimationFactory().getPowerupPickup();
        animationComponent.frames.addAll(textureRegions);
        return animationComponent;
    }
}
