package no.ntnu.tdt4240.asteroids.game.effect;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import no.ntnu.tdt4240.asteroids.game.entity.component.EffectComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.ShootComponent;
import no.ntnu.tdt4240.asteroids.game.shothandler.IShotHandler;

import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.shootMapper;

abstract class BaseShotEffect extends BaseEffect {
    private static final int DEFAULT_DURATION = 10;

    private static IShotHandler oldHandler;
    private ShootComponent shootComponent;
    private IShotHandler shotHandler;

    BaseShotEffect(IShotHandler shotHandler) {
        this.shotHandler = shotHandler;
    }

    @Override
    protected float getDuration() {
        return DEFAULT_DURATION;
    }
    
    @Override
    protected TextureRegion getEffectTexture() {
        return null;
    }

    @Override
    protected void applyEffect(PooledEngine engine, Entity entity, EffectComponent effectComponent) {
        shootComponent = shootMapper.get(entity);
        oldHandler = shootComponent.handler;
        shootComponent.handler = shotHandler;
    }

    @Override
    protected void removeEffect(PooledEngine engine, Entity entity, EffectComponent effectComponent) {
        shootComponent.handler = oldHandler;
    }

    @Override
    public Class<? extends Component> getEffectClass() {
        return ShootComponent.class;
    }
}
