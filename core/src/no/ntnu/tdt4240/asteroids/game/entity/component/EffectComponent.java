package no.ntnu.tdt4240.asteroids.game.entity.component;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import no.ntnu.tdt4240.asteroids.game.effect.IEffect;

public class EffectComponent extends BaseComponent {

    @SuppressWarnings("unused")
    private static final String TAG = EffectComponent.class.getSimpleName();
    private final HashMap<Class, IEffect> effects = new HashMap<>();

    public void addEffect(PooledEngine engine, Entity target, IEffect newEffect) {
        if (effects.containsKey(newEffect.getEffectClass())) {
            IEffect currentEffect = effects.get(newEffect.getEffectClass());

            if (newEffect.getClass().equals(currentEffect.getClass())) {
                currentEffect.refresh(engine, target, this, newEffect);
            } else {
                currentEffect.replace(engine, target, this, newEffect);
                effects.put(newEffect.getEffectClass(), newEffect);
            }
        } else {
            effects.put(newEffect.getEffectClass(), newEffect);
        }
    }

    public void tick(PooledEngine engine, Entity entity, float delta) {
        Iterator<Map.Entry<Class, IEffect>> iterator = effects.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class, IEffect> next = iterator.next();
            boolean expired = next.getValue().tick(engine, entity, this, delta);
            if (expired) iterator.remove();
        }
    }

    @Override
    public void reset() {
        effects.clear();
    }
}
