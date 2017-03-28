package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;

import javax.inject.Inject;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.GameSettings;
import no.ntnu.tdt4240.asteroids.entity.component.BoundaryComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BulletClass;
import no.ntnu.tdt4240.asteroids.entity.component.CircularBoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DamageComponent;
import no.ntnu.tdt4240.asteroids.entity.component.GravityComponent;
import no.ntnu.tdt4240.asteroids.entity.component.HealthComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ObstacleClass;
import no.ntnu.tdt4240.asteroids.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.entity.component.PowerupClass;
import no.ntnu.tdt4240.asteroids.entity.component.ShootComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;
import no.ntnu.tdt4240.asteroids.game.collisionhandler.BulletCollisionHandler;
import no.ntnu.tdt4240.asteroids.game.collisionhandler.PowerupCollisionHandler;
import no.ntnu.tdt4240.asteroids.game.effect.IEffect;

public class EntityFactory {

    private static final Family POWERUP_COLLISION_IGNORE = Family.one(ObstacleClass.class, BulletClass.class).get();
    private static final Family BULLET_COLLISION_IGNORE = Family.one(BulletClass.class, PlayerClass.class).get();
    private static final Family OBSTACLE_COLLISION_IGNORE = Family.one(ObstacleClass.class).get();
    private static final CollisionSystem.ICollisionHandler bulletCollisionHandler = new BulletCollisionHandler();
    private static final PowerupCollisionHandler POWERUP_COLLISION_HANDLER = new PowerupCollisionHandler();
    private final PooledEngine engine;
    private final IDrawableComponentFactory drawableComponentFactory;
    private final GameSettings gameSettings;

    @Inject
    public EntityFactory(PooledEngine engine, IDrawableComponentFactory drawableComponentFactory, GameSettings gameSettings) {
        this.engine = engine;
        this.drawableComponentFactory = drawableComponentFactory;
        this.gameSettings = gameSettings;
    }

    public Entity initPlayer(Entity player) {
        player.add(new PlayerClass());
        int rotationX = 1;
        int rotationY = 0;
        int positionX = Asteroids.VIRTUAL_WIDTH / 2;
        int positionY = Asteroids.VIRTUAL_HEIGHT / 2;
        player.add(new TransformComponent(positionX, positionY, rotationX, rotationY));
        MovementComponent movementComponent = new MovementComponent();
        movementComponent.accelerationScalar = gameSettings.accelerationScalar;
        player.add(movementComponent);
        player.add(new GravityComponent(gameSettings.playerGravity));
        player.add(new CircularBoundsComponent());
        player.add(new ShootComponent());
        player.add(new HealthComponent());
        player.add(new BoundaryComponent(BoundaryComponent.MODE_WRAP));
        player.add(drawableComponentFactory.getPlayer());
        CollisionComponent collisionComponent = new CollisionComponent();
        collisionComponent.ignoreComponents = Family.all(BulletClass.class).get();
        player.add(collisionComponent);
        return player;
    }

    public Entity createPlayerBullet() {
        Entity entity = engine.createEntity();
        entity.add(engine.createComponent(BulletClass.class));
        entity.add(engine.createComponent(TransformComponent.class));
        entity.add(engine.createComponent(MovementComponent.class));
        entity.add(engine.createComponent(CircularBoundsComponent.class));
        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(drawableComponentFactory.getProjectile());
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        collisionComponent.collisionHandler = bulletCollisionHandler;
        collisionComponent.ignoreComponents = BULLET_COLLISION_IGNORE;
        entity.add(collisionComponent);
        return entity;
    }

    public Entity createObstacle() {
        Entity entity = engine.createEntity();
        entity.add(engine.createComponent(ObstacleClass.class));
        entity.add(engine.createComponent(TransformComponent.class));
        entity.add(engine.createComponent(MovementComponent.class));
        entity.add(engine.createComponent(CircularBoundsComponent.class));
        entity.add(engine.createComponent(HealthComponent.class));
        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(drawableComponentFactory.getObstacle());
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        collisionComponent.ignoreComponents = OBSTACLE_COLLISION_IGNORE;
        entity.add(collisionComponent);
        return entity;
    }

    public Entity createPowerup(IEffect effect) {
        Entity entity = engine.createEntity();
        PowerupClass powerup = engine.createComponent(PowerupClass.class);
        powerup.effect = effect;
        entity.add(powerup);
        entity.add(engine.createComponent(CircularBoundsComponent.class));
        entity.add(drawableComponentFactory.getPowerup());
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        collisionComponent.ignoreComponents = POWERUP_COLLISION_IGNORE;
        collisionComponent.collisionHandler = POWERUP_COLLISION_HANDLER;
        entity.add(collisionComponent);
        entity.add(engine.createComponent(MovementComponent.class));
        entity.add(engine.createComponent(TransformComponent.class));
        return entity;
    }
}
