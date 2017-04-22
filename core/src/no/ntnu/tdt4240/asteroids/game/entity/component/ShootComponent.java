package no.ntnu.tdt4240.asteroids.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

import no.ntnu.tdt4240.asteroids.game.shothandler.IShotHandler;

public class ShootComponent implements Component, Pool.Poolable {

    public IShotHandler handler;

    public ShootComponent(IShotHandler handler) {
        this.handler = handler;
    }

    public ShootComponent() {
    }

    public void fire(PooledEngine engine, Entity controlledEntity) {
        if (handler == null) return;
        handler.fire(engine, controlledEntity);
    }

    @Override
    public void reset() {
        handler = null;
    }
}
