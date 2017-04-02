package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;

public class CollisionComponent implements Component, Pool.Poolable {

    public CollisionSystem.ICollisionHandler collisionHandler;

    public Family ignoredEntities;

    public Vector2 preCollisionVelocity;

    public final Vector2 preCollisionPosition = new Vector2();

    public float mass = 1;

    @Override
    public void reset() {
        collisionHandler = null;
        mass = 1;
        ignoredEntities = null;
        preCollisionVelocity = null;
        preCollisionPosition.setZero();
    }
}
