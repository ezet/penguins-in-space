package no.ntnu.tdt4240.asteroids.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Pool;

import no.ntnu.tdt4240.asteroids.game.entity.system.DamageSystem;


public class HealthComponent implements Component, Pool.Poolable {

    public int hitPoints = 0;
    public DamageSystem.IDamageHandler damageHandler;
    public Family ignoredEntities;

    public HealthComponent() {
    }


    public HealthComponent(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    @Override
    public void reset() {
        hitPoints = 0;
    }

}
