package no.ntnu.tdt4240.asteroids.presenter;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;

import dagger.Module;

@Module
public class ViewModule {

    PooledEngine engine;
    private Game game;

    public ViewModule(PooledEngine engine) {
        this.engine = engine;
    }
}