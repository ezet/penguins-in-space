package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

public class EffectComponent implements Component, Pool.Poolable {

    // TODO: array of effects
    public IEffect effect;

    private float remainingDuration;

    private boolean applied = false;

    // TODO: move to IEffect implementation
    public void tick(PooledEngine engine, Entity entity, float delta) {
        if (!applied) {
            effect.applyEffect(engine, entity, this);
            remainingDuration = effect.getDuration();
            applied = true;
        } else if (remainingDuration > 0) {
            remainingDuration -= delta;
        } else {
            entity.remove(EffectComponent.class);
            effect.removeEffect(engine, entity, this);
            applied = false;
            remainingDuration = 0;
        }
    }

    @Override
    public void reset() {
        effect = null;
        remainingDuration = 0;
        applied = false;
    }

    public interface IEffect {
        void applyEffect(PooledEngine engine, Entity entity, EffectComponent effectComponent);

        void removeEffect(PooledEngine engine, Entity entity, EffectComponent effectComponent);

        float getDuration();
    }
}
