package no.ntnu.tdt4240.asteroids.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

public abstract class BaseComponent implements Component, Pool.Poolable {

    public static <T extends Component> T getInstance(Engine engine, Class<T> cls) {
        if (engine instanceof PooledEngine) {
            return ((PooledEngine) engine).createComponent(cls);
        }
        throw new RuntimeException("Engine must be PooledEngine");
    }
}
