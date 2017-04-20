package no.ntnu.tdt4240.asteroids.game.damagehandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;

import no.ntnu.tdt4240.asteroids.entity.component.AnimationComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.system.DamageSystem;
import no.ntnu.tdt4240.asteroids.game.effect.IEffect;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.movementMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.transformMapper;

public class ObstacleDamageHandler implements DamageSystem.IDamageHandler {


    private static Entity createPowerup(Entity source) {
        MovementComponent sourceMovement = movementMapper.get(source);
        TransformComponent sourceTransform = transformMapper.get(source);
        if (sourceMovement == null || sourceTransform == null) return null;
        Entity entity = ServiceLocator.getEntityComponent().getEntityFactory().createPowerup(getRandomEffect());
        MovementComponent movementComponent = movementMapper.get(entity);
        movementComponent.velocity.set(sourceMovement.velocity);
        TransformComponent transformComponent = transformMapper.get(entity);
        transformComponent.position.set(sourceTransform.position);
        return entity;
    }

    private static IEffect getRandomEffect() {
        return ServiceLocator.getEntityComponent().getEffectFactory().getRandomEffect();
    }

    private static void spawnPowerup(Engine engine, Entity entity) {
        if (MathUtils.random() > 1 - ServiceLocator.getEntityComponent().getGameSettings().getPowerupSpawnChance()) {
            Entity powerup = createPowerup(entity);
            if (powerup != null) engine.addEntity(powerup);
        }
    }

    @Override
    public void onDamageTaken(Engine engine, Entity entity, Entity source, int damageTaken) {

    }

    @Override
    public void onEntityDestroyed(Engine engine, Entity entity, Entity source) {
        spawnPowerup(engine, entity);
        AnimationComponent animation = new AnimationComponent();
        animation.removeEntityAfterAnimation = true;
        animation.frames.addAll(ServiceLocator.getAppComponent().getAnimationFactory().getMediumExplosion());
        animation.soundOnStart = AssetService.SoundAsset.SOUND_EXPLOSION_WAV;
        animation.removeDuringAnimation.add(CollisionComponent.class);
        animation.removeDuringAnimation.add(MovementComponent.class);
        entity.add(animation);
        // TODO: 31-Mar-17 Figure out why this line sometime causes a null reference
    }
}
