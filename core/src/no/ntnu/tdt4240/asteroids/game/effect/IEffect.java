package no.ntnu.tdt4240.asteroids.game.effect;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import no.ntnu.tdt4240.asteroids.game.entity.component.EffectComponent;

public interface IEffect {
    void refresh(IEffect effect);

    boolean tick(PooledEngine engine, Entity entity, EffectComponent component, float deltaTime);

    TextureRegion getPowerupTexture();

}
