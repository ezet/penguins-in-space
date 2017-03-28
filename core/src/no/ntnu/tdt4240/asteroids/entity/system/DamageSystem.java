package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Array;

import no.ntnu.tdt4240.asteroids.entity.component.DamageComponent;
import no.ntnu.tdt4240.asteroids.entity.component.HealthComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.damageMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.healthMapper;


public class DamageSystem extends EntitySystem implements CollisionSystem.ICollisionHandler {

    private Array<IDamageTakenListener> damageListeners = new Array<>();
    private Array<IEntityDestroyedListener> destroyedListeners = new Array<>();

    public DamageSystem(CollisionSystem collisionSystem) {
        collisionSystem.listeners.add(this);
    }

    @Override
    public void onCollision(PooledEngine engine, Entity source, Entity target) {
        DamageComponent damageComponent = damageMapper.get(source);
        HealthComponent healthComponent = healthMapper.get(target);
        if (healthComponent == null || damageComponent == null) return;
        if (damageComponent.ignoredEntities != null && damageComponent.ignoredEntities.matches(target))
            return;
        if (healthComponent.ignoredEntities != null && healthComponent.ignoredEntities.matches(source))
            return;


        healthComponent.hitPoints -= damageComponent.damage;
        if (healthComponent.damageTakenHandler != null)
            healthComponent.damageTakenHandler.onDamageTaken(getEngine(), source, damageComponent.damage);
        notifyDamageListeners(target, healthComponent.hitPoints);
        if (healthComponent.hitPoints <= 0) {
            if (healthComponent.entityDestroyedHandler != null)
            healthComponent.entityDestroyedHandler.onEntityDestroyed(getEngine(), source, target);
            notifyDestroyedListeners(source, target);
        }
    }

    private void notifyDamageListeners(Entity entity, int hitpoints) {
        for (IDamageTakenListener listener : damageListeners) {
            listener.onDamageTaken(getEngine(), entity, hitpoints);
        }
    }

    private void notifyDestroyedListeners(Entity source, Entity target) {
        for (IEntityDestroyedListener destroyedListener : destroyedListeners) {
            destroyedListener.onEntityDestroyed(getEngine(), source, target);
        }
    }

    public interface IDamageTakenListener {
        void onDamageTaken(Engine engine, Entity entity, int damageTaken);
    }

    public interface IEntityDestroyedListener {
        void onEntityDestroyed(Engine engine, Entity source, Entity target);
    }
}
