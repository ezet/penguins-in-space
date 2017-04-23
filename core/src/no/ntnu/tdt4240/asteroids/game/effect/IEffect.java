package no.ntnu.tdt4240.asteroids.game.effect;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import no.ntnu.tdt4240.asteroids.game.entity.component.EffectComponent;

public interface IEffect {

    int GUN = 0;
    int HP = 1;

    void refresh(PooledEngine engine, Entity target, EffectComponent effectComponent, IEffect effect);

    boolean tick(PooledEngine engine, Entity entity, EffectComponent component, float deltaTime);

    TextureRegion getPowerupTexture();

    Class<? extends Component> getEffectClass();

    void replace(PooledEngine engine, Entity target, EffectComponent effectComponent, IEffect newEffect);
}
