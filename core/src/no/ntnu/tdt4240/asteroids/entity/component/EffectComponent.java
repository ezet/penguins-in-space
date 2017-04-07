package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import no.ntnu.tdt4240.asteroids.game.effect.IEffect;

public class EffectComponent extends BaseComponent {

    private static final String TAG = EffectComponent.class.getSimpleName();
    private final HashMap<Class, IEffect> effects = new HashMap<>();

    public void addEffect(IEffect effect) {
        if (effects.containsKey(effect.getClass())) {
            effects.get(effect.getClass()).refresh(effect);
        } else {
            effects.put(effect.getClass(), effect);
        }
    }

    public void tick(PooledEngine engine, Entity entity, float delta) {
        Iterator<Map.Entry<Class, IEffect>> iterator = effects.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class, IEffect> next = iterator.next();
            boolean remove = next.getValue().tick(engine, entity, this, delta);
            if (remove) iterator.remove();
        }
    }

    @Override
    public void reset() {
        effects.clear();
    }
}
