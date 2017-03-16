package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;

public class CollisionComponent implements Component, Pool.Poolable {

    public CollisionSystem.ICollisionHandler collisionHandler;

//    public void onCollision(Entity source, Entity target, Engine engine) {
//        collisionHandler.onCollision(source, target, engine);
//    }

    @Override
    public void reset() {
        collisionHandler = null;

    }
}
