package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;

import no.ntnu.tdt4240.asteroids.entity.component.BoundaryComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BulletComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.GravityComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ObstacleComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.bulletMapper;

public class EntityFactory {

    private static EntityFactory instance;
    private PooledEngine engine;
    private IDrawableComponentFactory drawableComponentFactory;
    private CollisionSystem.ICollisionHandler bulletCollisionHandler = new CollisionSystem.ICollisionHandler() {
        @Override
        public void onCollision(Entity source, Entity target, Engine engine) {
            if (ComponentMappers.obstacleMapper.has(target)) {
                // TODO: we hit an obstacle, MISSION ACCOMPLISHED!
                engine.removeEntity(source);
            }
        }
    };
    private CollisionSystem.ICollisionHandler obstacleCollisionHandler = new CollisionSystem.ICollisionHandler() {
        @Override
        public void onCollision(Entity source, Entity target, Engine engine) {
            if (bulletMapper.has(target)) {
                // TODO: oh noes we dies, explosions commence
                engine.removeEntity(source);
            }
            // TODO: handle other collisions
        }
    };

    private EntityFactory(PooledEngine engine, IDrawableComponentFactory drawableComponentFactory) {
        this.engine = engine;
        this.drawableComponentFactory = drawableComponentFactory;
    }

    public static void initialize(PooledEngine engine, IDrawableComponentFactory factory) {
        instance = new EntityFactory(engine, factory);
    }

    public static EntityFactory getInstance() {
        return instance;
    }

    public Entity createBullet() {
        Entity entity = new Entity();
        entity.add(engine.createComponent(BulletComponent.class));
        entity.add(engine.createComponent(PositionComponent.class));
        entity.add(engine.createComponent(MovementComponent.class));
        entity.add(engine.createComponent(BoundsComponent.class));
        entity.add(drawableComponentFactory.getBullet());
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        collisionComponent.collisionHandler = bulletCollisionHandler;
        entity.add(collisionComponent);
        return entity;
    }

    public Entity createObstacle() {
        Entity entity = new Entity();
        entity.add(engine.createComponent(ObstacleComponent.class));
        entity.add(engine.createComponent(PositionComponent.class));
        entity.add(engine.createComponent(MovementComponent.class));
        entity.add(engine.createComponent(BoundsComponent.class));
        entity.add(drawableComponentFactory.getObstacle());
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        collisionComponent.collisionHandler = obstacleCollisionHandler;
        entity.add(collisionComponent);
        return entity;
    }

    public Entity initPlayer(Entity player) {
        player.add(new PositionComponent(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 1, 0));
        player.add(new MovementComponent());
        player.add(new GravityComponent(0.01f));
        player.add(new BoundsComponent());
        player.add(new BoundaryComponent(BoundaryComponent.MODE_FREE));
        player.add(drawableComponentFactory.getPlayer());
        player.add(new CollisionComponent());
        return player;
    }
}
