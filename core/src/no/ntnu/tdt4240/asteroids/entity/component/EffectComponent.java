package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Array;

import no.ntnu.tdt4240.asteroids.game.effect.IEffect;

public class EffectComponent extends BaseComponent {

    private final Array<IEffect> effects = new Array<>();

    public void addEffect(IEffect effect) {
        effects.add(effect);
    }

    public void tick(PooledEngine engine, Entity entity, float delta) {
        for (IEffect effect : effects) {
            effect.tick(engine, entity, this, delta);
        }
    }

    @Override
    public void reset() {
        effects.clear();
    }

}
