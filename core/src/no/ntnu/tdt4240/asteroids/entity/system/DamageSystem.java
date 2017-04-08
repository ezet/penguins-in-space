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

    public Array<IDamageHandler> getDamageListeners() {
        return damageListeners;
    }

    private final Array<IDamageHandler> damageListeners = new Array<>();
    private final CollisionSystem collisionSystem;

    public DamageSystem(CollisionSystem collisionSystem) {
        this.collisionSystem = collisionSystem;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        collisionSystem.listeners.add(this);
    }


    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        collisionSystem.listeners.removeValue(this, true);
    }

    @Override
    public boolean onCollision(PooledEngine engine, Entity source, Entity target) {
        if (!checkProcessing()) return false;
        HealthComponent healthComponent = healthMapper.get(target);
        DamageComponent damageComponent = damageMapper.get(source);
        if (healthComponent == null || damageComponent == null) return false;
        if (damageComponent.ignoredEntities != null && damageComponent.ignoredEntities.matches(target))
            return false;
        if (healthComponent.ignoredEntities != null && healthComponent.ignoredEntities.matches(source))
            return false;
        healthComponent.hitPoints -= damageComponent.damage;
        if (healthComponent.damageHandler != null) {
            healthComponent.damageHandler.onDamageTaken(getEngine(), target, source, damageComponent.damage);
        }
        notifyDamageListeners(target, source, damageComponent.damage);

        if (healthComponent.hitPoints <= 0) {
            if (healthComponent.damageHandler != null)
                healthComponent.damageHandler.onEntityDestroyed(getEngine(), target, source);
        }
        notifyDestroyedListeners(target, source);
        return true;
    }

    private void notifyDamageListeners(Entity entity, Entity source, int damageDone) {
        for (IDamageHandler listener : damageListeners) {
            listener.onDamageTaken(getEngine(), entity, source, damageDone);
        }
    }

    private void notifyDestroyedListeners(Entity entity, Entity source) {
        for (IDamageHandler listener : damageListeners) {
            listener.onEntityDestroyed(getEngine(), entity, source);
        }
    }

    public interface IDamageHandler {
        void onDamageTaken(Engine engine, Entity entity, Entity source, int damageTaken);

        void onEntityDestroyed(Engine engine, Entity entity, Entity source);
    }

}
