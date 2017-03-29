package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.ashley.core.PooledEngine;

import dagger.Module;

@Module
public class ViewModule {

    PooledEngine engine;

    public ViewModule(PooledEngine engine) {
        this.engine = engine;
    }
}