package no.ntnu.tdt4240.asteroids.screen;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.entity.component.NetworkSyncComponent;
import no.ntnu.tdt4240.asteroids.entity.system.NetworkSystem;

public class MultiplayerGameScreen extends GameScreen {

    MultiplayerGameScreen(Asteroids game) {
        super(game);
    }

    @Override
    void initEngine(PooledEngine engine, SpriteBatch batch) {
        super.initEngine(engine, batch);
        player.add(new NetworkSyncComponent());
        engine.addSystem(new NetworkSystem(Asteroids.getNetworkService()));
    }
}
