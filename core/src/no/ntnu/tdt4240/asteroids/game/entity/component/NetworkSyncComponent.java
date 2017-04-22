package no.ntnu.tdt4240.asteroids.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class NetworkSyncComponent implements Component, Pool.Poolable {

    public NetworkSyncComponent() {
    }

    @Override
    public void reset() {
    }
}
