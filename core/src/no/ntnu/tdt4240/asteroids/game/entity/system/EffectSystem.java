package no.ntnu.tdt4240.asteroids.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;

import no.ntnu.tdt4240.asteroids.game.entity.component.EffectComponent;

import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.effectMapper;


public class EffectSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(EffectComponent.class).get();


    public EffectSystem() {
        super(FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EffectComponent effectComponent = effectMapper.get(entity);
        effectComponent.tick((PooledEngine) getEngine(), entity, deltaTime);
    }
}
