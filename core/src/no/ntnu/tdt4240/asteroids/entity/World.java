package no.ntnu.tdt4240.asteroids.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

public class World {

    private final PooledEngine engine;

    private Entity player;

    public World(PooledEngine engine) {

        this.engine = engine;
    }

    public void create() {

    }

    public void reset() {

    }

}
