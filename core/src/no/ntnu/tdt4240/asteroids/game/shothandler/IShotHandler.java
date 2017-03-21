package no.ntnu.tdt4240.asteroids.game.shothandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

public interface IShotHandler {

    void fire(PooledEngine engine, Entity controlledEntity);

}
