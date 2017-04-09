package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;

import dagger.Module;
import dagger.Provides;
import no.ntnu.tdt4240.asteroids.view.MainView;

@Module
public class ViewModule {

    private Game game;

    PooledEngine engine;

    public ViewModule(PooledEngine engine) {
        this.engine = engine;
    }
}