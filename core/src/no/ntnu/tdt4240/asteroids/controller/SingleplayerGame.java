package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.game.World;
import no.ntnu.tdt4240.asteroids.model.PlayerData;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

class SingleplayerGame extends BaseGameController implements World.IGameListener, IGameController {

    @SuppressWarnings("unused")
    protected static final String TAG = SingleplayerGame.class.getSimpleName();

    SingleplayerGame(Asteroids game, Screen parent) {
        super(game, parent);
    }

    protected void initializeEntityComponent(PooledEngine engine) {
        ServiceLocator.initializeSinglePlayerEntityComponent(engine);
    }

    @Override
    protected void setupWorld() {
        super.setupWorld();
        String displayName = ServiceLocator.getAppComponent().getNetworkService().getDisplayName();
        PlayerData data = new PlayerData("", displayName);
        world.addPlayer(data);
    }
}
