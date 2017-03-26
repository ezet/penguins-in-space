package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.AssetLoader;
import no.ntnu.tdt4240.asteroids.game.shothandler.IShotHandler;
import no.ntnu.tdt4240.asteroids.game.shothandler.StandardShotHandler;

public class ShootComponent implements Component {

    public IShotHandler handler;

    public void fire(PooledEngine engine, Entity controlledEntity) {
        if (handler == null) handler = new StandardShotHandler();
        handler.fire(engine, controlledEntity);
        //TODO move sound to somewhere else?
        AssetLoader.shot.play();
    }


}
