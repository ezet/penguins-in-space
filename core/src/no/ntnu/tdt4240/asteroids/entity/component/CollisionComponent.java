package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

public class CollisionComponent implements Component {

    private final ICollisionHandler collisionHandler;

    public CollisionComponent(ICollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
    }


    public void onCollision(Entity source, Entity target, Engine engine) {
        collisionHandler.onCollision(source, target, engine);
    }

    public interface ICollisionHandler {
        void onCollision(Entity source, Entity target, Engine engine);
    }
}
