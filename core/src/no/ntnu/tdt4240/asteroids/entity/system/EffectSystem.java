package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;

import no.ntnu.tdt4240.asteroids.entity.component.EffectComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.effectMapper;


public class EffectSystem extends IteratingSystem {

    private static final Family family = Family.all(EffectComponent.class).get();


    public EffectSystem() {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EffectComponent effectComponent = effectMapper.get(entity);
        effectComponent.tick((PooledEngine) getEngine(), entity, deltaTime);
    }
}
