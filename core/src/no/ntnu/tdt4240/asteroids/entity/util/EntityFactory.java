package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.MathUtils;

import javax.inject.Inject;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.entity.component.BoundaryComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BulletClass;
import no.ntnu.tdt4240.asteroids.entity.component.CircularBoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DamageComponent;
import no.ntnu.tdt4240.asteroids.entity.component.EffectComponent;
import no.ntnu.tdt4240.asteroids.entity.component.GravityComponent;
import no.ntnu.tdt4240.asteroids.entity.component.HealthComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.NetworkAddComponent;
import no.ntnu.tdt4240.asteroids.entity.component.NetworkSyncComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ObstacleClass;
import no.ntnu.tdt4240.asteroids.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.entity.component.PowerupClass;
import no.ntnu.tdt4240.asteroids.entity.component.ScoreComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ShootComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;
import no.ntnu.tdt4240.asteroids.game.collisionhandler.BulletCollisionHandler;
import no.ntnu.tdt4240.asteroids.game.collisionhandler.PowerupCollisionHandler;
import no.ntnu.tdt4240.asteroids.game.effect.IEffect;
import no.ntnu.tdt4240.asteroids.service.settings.IGameSettings;

public class EntityFactory {

    private static final Family POWERUP_COLLISION_IGNORE = Family.one(ObstacleClass.class, BulletClass.class).get();
    private static final Family BULLET_COLLISION_IGNORE = Family.one(BulletClass.class).get();
    private static final Family OBSTACLE_COLLISION_IGNORE = Family.one(ObstacleClass.class).get();
    private static final CollisionSystem.ICollisionHandler bulletCollisionHandler = new BulletCollisionHandler();
    private static final PowerupCollisionHandler POWERUP_COLLISION_HANDLER = new PowerupCollisionHandler();
    private final PooledEngine engine;
    private final IDrawableComponentFactory drawableComponentFactory;
    private final IGameSettings gameSettings;

    @Inject
    public EntityFactory(PooledEngine engine, IDrawableComponentFactory drawableComponentFactory, IGameSettings gameSettings) {
        this.engine = engine;
        this.drawableComponentFactory = drawableComponentFactory;
        this.gameSettings = gameSettings;
    }

    public Entity createPlayer(String id, String displayName, boolean multiplayer) {
        Entity entity = new Entity();
        PlayerClass playerClass = new PlayerClass(id, displayName);
        playerClass.isSelf = true;
        entity.add(playerClass);
        int rotationX = 1;
        int rotationY = 0;
        int positionX = Asteroids.VIRTUAL_WIDTH / 2;
        int positionY = Asteroids.VIRTUAL_HEIGHT / 2;
        if (multiplayer) {
            positionX = (int) (Asteroids.VIRTUAL_WIDTH * MathUtils.random());
            positionY = (int) (Asteroids.VIRTUAL_HEIGHT * MathUtils.random());
            entity.add(new NetworkSyncComponent());
        } else {
            entity.add(new EffectComponent());
        }
        entity.add(new TransformComponent(positionX, positionY, rotationX, rotationY));
        MovementComponent movementComponent = new MovementComponent();
        movementComponent.accelerationScalar = gameSettings.getAccelerationScalar();
        entity.add(movementComponent);
        entity.add(new GravityComponent(gameSettings.getPlayerGravity()));
        entity.add(new CircularBoundsComponent());
        entity.add(new ShootComponent());
        entity.add(new HealthComponent(3));
        entity.add(new ScoreComponent());
        entity.add(new BoundaryComponent(BoundaryComponent.MODE_WRAP));
        entity.add(drawableComponentFactory.getPlayer());
        CollisionComponent collisionComponent = new CollisionComponent();
        entity.add(collisionComponent);
        return entity;
    }

    public Entity createMultiPlayer(String participantId, String displayName) {
        Entity entity = new Entity();
        entity.add(new PlayerClass(participantId, displayName));
        int rotationX = 1;
        int rotationY = 0;
        int positionX = Asteroids.VIRTUAL_WIDTH / 2;
        int positionY = Asteroids.VIRTUAL_HEIGHT / 2;
        entity.add(new TransformComponent(positionX, positionY, rotationX, rotationY));
        entity.add(new MovementComponent());
        entity.add(new BoundaryComponent());
        entity.add(new CircularBoundsComponent());
        entity.add(new HealthComponent(3));
        entity.add(new ScoreComponent());
        entity.add(drawableComponentFactory.getMultiPlayer());
        entity.add(new GravityComponent(gameSettings.getPlayerGravity()));
        entity.add(engine.createComponent(CollisionComponent.class));
        return entity;
    }

    public Entity createBullet(String playerId) {
        Entity entity = engine.createEntity();
        BulletClass bullet = engine.createComponent(BulletClass.class);
        bullet.id = playerId;
        entity.add(bullet);
        entity.add(engine.createComponent(TransformComponent.class));
        entity.add(engine.createComponent(MovementComponent.class));
        entity.add(engine.createComponent(CircularBoundsComponent.class));
        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(engine.createComponent(NetworkAddComponent.class));
        entity.add(drawableComponentFactory.getProjectile());
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        collisionComponent.collisionHandler = bulletCollisionHandler;
        collisionComponent.ignoredEntities = BULLET_COLLISION_IGNORE;
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
        collisionComponent.ignoredEntities = OBSTACLE_COLLISION_IGNORE;
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
        collisionComponent.ignoredEntities = POWERUP_COLLISION_IGNORE;
        collisionComponent.collisionHandler = POWERUP_COLLISION_HANDLER;
        entity.add(collisionComponent);
        entity.add(engine.createComponent(MovementComponent.class));
        entity.add(engine.createComponent(TransformComponent.class));
        return entity;
    }


}
