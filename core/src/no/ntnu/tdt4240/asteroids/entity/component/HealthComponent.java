package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Pool;

import no.ntnu.tdt4240.asteroids.entity.system.DamageSystem;


public class HealthComponent implements Component, Pool.Poolable {

    public HealthComponent() {
    }

    public HealthComponent(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public int hitPoints = 0;

    public DamageSystem.IDamageTakenListener damageTakenHandler;

    public DamageSystem.IEntityDestroyedListener entityDestroyedHandler;

    public Family ignoredEntities;

    @Override
    public void reset() {
        hitPoints = 0;
    }

}
