package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import no.ntnu.tdt4240.asteroids.game.effect.IEffect;

public class EffectFactory<F extends IEffect> {

    private static EffectFactory instance;
    private final Array<Class<F>> effects = new Array<>();

    public static EffectFactory getInstance() {
        if (instance == null) instance = new EffectFactory();
        return instance;
    }

    public void registerEffect(Class<F> effect) {
        effects.add(effect);
    }


    public IEffect getRandomEffect() {
        if (effects.size == 0) return null;
        int random = MathUtils.random(0, effects.size - 1);
        try {
            return effects.get(random).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
