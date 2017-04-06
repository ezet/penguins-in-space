package no.ntnu.tdt4240.asteroids.service;


import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.AppComponent;
import no.ntnu.tdt4240.asteroids.AppModule;
import no.ntnu.tdt4240.asteroids.DaggerAppComponent;
import no.ntnu.tdt4240.asteroids.entity.DaggerEntityComponent;
import no.ntnu.tdt4240.asteroids.entity.EntityComponent;
import no.ntnu.tdt4240.asteroids.entity.EntityModule;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

public abstract class ServiceLocator {

    private static EntityComponent entityComponent;
    private static AppComponent appComponent;

    public static void initializeSinglePlayerEntityComponent(PooledEngine engine) {
        entityComponent = DaggerEntityComponent.builder().entityModule(new EntityModule(engine, false)).appComponent(getAppComponent()).build();
    }

    public static void initializeMultiPlayerEntityComponent(PooledEngine engine) {
        entityComponent = DaggerEntityComponent.builder().entityModule(new EntityModule(engine, true)).appComponent(getAppComponent()).build();
    }

    public static void initializeAppComponent(INetworkService networkService) {
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(networkService)).build();
    }

    public static EntityComponent getEntityComponent() {
        return entityComponent;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
