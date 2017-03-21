package no.ntnu.tdt4240.asteroids.game.effect;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.entity.component.EffectComponent;

public interface IEffect {
    void tick(PooledEngine engine, Entity entity, EffectComponent component, float deltaTime);
}
