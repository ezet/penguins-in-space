package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.entity.collisionhandler.BulletCollisionHandler;
import no.ntnu.tdt4240.asteroids.entity.component.BoundaryComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BulletClass;
import no.ntnu.tdt4240.asteroids.entity.component.CircularBoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DamageComponent;
import no.ntnu.tdt4240.asteroids.entity.component.EffectComponent;
import no.ntnu.tdt4240.asteroids.entity.component.GravityComponent;
import no.ntnu.tdt4240.asteroids.entity.component.HealthComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ObstacleClass;
import no.ntnu.tdt4240.asteroids.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.entity.component.PowerupClass;
import no.ntnu.tdt4240.asteroids.entity.component.ShootComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;
import no.ntnu.tdt4240.asteroids.game.effect.IEffect;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.playerMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.powerupMapper;

public class EntityFactory {


    // TODO: add config
    private static final float GRAVITY = 0.01f;
    private static EntityFactory instance;
    // TODO: find a better place for this
    private static CollisionSystem.ICollisionHandler bulletCollisionHandler = new BulletCollisionHandler();
    private PooledEngine engine;
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
        player.add(new TransformComponent(Asteroids.VIRTUAL_WIDTH/ 2, Asteroids.VIRTUAL_HEIGHT / 2, 1, 0));
        player.add(new MovementComponent());
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
        collisionComponent.ignoreComponents = Family.one(BulletClass.class, PlayerClass.class).get();
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
        collisionComponent.ignoreComponents = Family.one(ObstacleClass.class).get();
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
        collisionComponent.ignoreComponents = Family.one(ObstacleClass.class, BulletClass.class).get();
        collisionComponent.collisionHandler = new CollisionSystem.ICollisionHandler() {
            @Override
            public void onCollision(PooledEngine engine, Entity source, Entity target) {
                if (playerMapper.has(target)) {
                    PowerupClass powerup = powerupMapper.get(source);
                    EffectComponent effectComponent = engine.createComponent(EffectComponent.class);
                    effectComponent.addEffect(powerup.effect);
                    target.add(effectComponent);
                    // TODO: display pickup animation
                    engine.removeEntity(source);
                }
            }
        };
        entity.add(collisionComponent);

        entity.add(engine.createComponent(MovementComponent.class));
        entity.add(engine.createComponent(TransformComponent.class));
        return entity;
    }

}
