package no.ntnu.tdt4240.asteroids.service;


import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.entity.DaggerEntityComponent;
import no.ntnu.tdt4240.asteroids.entity.EntityComponent;
import no.ntnu.tdt4240.asteroids.entity.EntityModule;
import no.ntnu.tdt4240.asteroids.DaggerGameComponent;
import no.ntnu.tdt4240.asteroids.GameComponent;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

public abstract class ServiceLocator {

    public static EntityComponent entityComponent;
    public static GameComponent gameComponent;
    public static INetworkService networkService;

    public static void initializeEntityComponent(PooledEngine engine) {
        entityComponent = DaggerEntityComponent.builder().entityModule(new EntityModule(engine)).build();
    }

    public static void initializeGameComponent(INetworkService networkService) {
        ServiceLocator.networkService = networkService;
        gameComponent = DaggerGameComponent.create();
    }
}
