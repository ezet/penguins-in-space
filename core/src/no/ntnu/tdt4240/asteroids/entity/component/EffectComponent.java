package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import java.util.HashMap;

import no.ntnu.tdt4240.asteroids.game.effect.IEffect;

public class EffectComponent extends BaseComponent {

    private final HashMap<Class, IEffect> effects = new HashMap<>();


    public void removeEffect(IEffect effect) {
        effects.remove(effect.getClass());
    }

    public void addEffect(IEffect effect) {
        // TODO: 28-Mar-17 Improve data structure
        if (effects.containsKey(effect.getClass())) {
            effects.get(effect.getClass()).refresh(effect);
        } else {
            effects.put(effect.getClass(), effect);
        }

    }

    public void tick(PooledEngine engine, Entity entity, float delta) {
        for (IEffect effect : effects.values()) {
            effect.tick(engine, entity, this, delta);
        }
    }

    @Override
    public void reset() {
        effects.clear();
    }

}
