package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.Asteroids;
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
    // TODO: add config
    private static final float GRAVITY = 0.01f;
    private static final int ACCELERATION_SCALAR = 500;
    private static final int POSITION_X = Asteroids.VIRTUAL_WIDTH / 2;
    private static final int POSITION_Y = Asteroids.VIRTUAL_HEIGHT / 2;
    private static final int ROTATION_X = 1;
    private static final int ROTATION_Y = 0;
    // TODO: find a better place for this
    private static final CollisionSystem.ICollisionHandler bulletCollisionHandler = new BulletCollisionHandler();
    private static final Family OBSTACLE_COLLISION_IGNORE = Family.one(ObstacleClass.class).get();
    private static final PowerupCollisionHandler POWERUP_COLLISION_HANDLER = new PowerupCollisionHandler();
    private static EntityFactory instance;
    private final PooledEngine engine;
    private IDrawableComponentFactory drawableComponentFactory;

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

    public Entity initPlayer(Entity player) {
        player.add(new PlayerClass());
        //// TODO: 3/23/2017  Change to constant width/height
        player.add(new TransformComponent(POSITION_X, POSITION_Y, ROTATION_X, ROTATION_Y));
        MovementComponent movementComponent = new MovementComponent();
        movementComponent.accelerationScalar = ACCELERATION_SCALAR;
        player.add(movementComponent);
        player.add(new GravityComponent(GRAVITY));
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
        entity.add(drawableComponentFactory.getBullet());
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
