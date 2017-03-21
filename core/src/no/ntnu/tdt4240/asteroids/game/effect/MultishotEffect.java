package no.ntnu.tdt4240.asteroids.game.effect;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import no.ntnu.tdt4240.asteroids.entity.component.EffectComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ShootComponent;
import no.ntnu.tdt4240.asteroids.game.shothandler.IShotHandler;
import no.ntnu.tdt4240.asteroids.game.shothandler.MultiShotHandler;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.shootMapper;

public class MultishotEffect extends BaseEffect {

    public static final int DEFAULT_DURATION = 10;

    private IShotHandler oldHandler;
    private ShootComponent shootComponent;

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
        shootComponent.handler = new MultiShotHandler(800, 3, 5);
    }

    @Override
    protected void removeEffect(PooledEngine engine, Entity entity, EffectComponent effectComponent) {
        shootComponent.handler = oldHandler;
    }
}
