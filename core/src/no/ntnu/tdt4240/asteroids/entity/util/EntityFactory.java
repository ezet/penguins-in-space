package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;

import no.ntnu.tdt4240.asteroids.entity.collisionhandler.BulletCollisionHandler;
import no.ntnu.tdt4240.asteroids.entity.component.BoundaryComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BulletClass;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DamageComponent;
import no.ntnu.tdt4240.asteroids.entity.component.EffectComponent;
import no.ntnu.tdt4240.asteroids.entity.component.GravityComponent;
import no.ntnu.tdt4240.asteroids.entity.component.HealthComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ObstacleClass;
import no.ntnu.tdt4240.asteroids.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.entity.component.ShootComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.effect.InvulnerabilityEffect;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;

public class EntityFactory {


    // TODO: add config
    public static final float GRAVITY = 0.01f;
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


    public Entity createPlayerBullet() {
        Entity entity = new Entity();
        entity.add(engine.createComponent(BulletClass.class));
        entity.add(engine.createComponent(TransformComponent.class));
        entity.add(engine.createComponent(MovementComponent.class));
        entity.add(engine.createComponent(BoundsComponent.class));
        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(drawableComponentFactory.getBullet());
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        collisionComponent.collisionHandler = bulletCollisionHandler;
        collisionComponent.ignoreComponents = Family.one(BulletClass.class, PlayerClass.class).get();
        entity.add(collisionComponent);
        return entity;
    }

    public Entity createObstacle() {
        Entity entity = new Entity();
        entity.add(engine.createComponent(ObstacleClass.class));
        entity.add(engine.createComponent(TransformComponent.class));
        entity.add(engine.createComponent(MovementComponent.class));
        entity.add(engine.createComponent(BoundsComponent.class));
        entity.add(engine.createComponent(HealthComponent.class));
        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(drawableComponentFactory.getObstacle());
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        collisionComponent.ignoreComponents = Family.one(ObstacleClass.class).get();
        entity.add(collisionComponent);
        return entity;
    }

    public Entity initPlayer(Entity player) {
//        player.add(new PlayerClass());
        player.add(new PlayerClass());
        player.add(new TransformComponent(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 1, 0));
        player.add(new MovementComponent());
        player.add(new GravityComponent(GRAVITY));
        player.add(new BoundsComponent());
        player.add(new ShootComponent());
        EffectComponent effectComponent = new EffectComponent();
        effectComponent.effect = new InvulnerabilityEffect();
        player.add(effectComponent);
        player.add(new HealthComponent());
//        DamageComponent damageComponent = new DamageComponent();
//        damageComponent.ignoreComponents = Family.one(ObstacleClass.class).get();
//        player.add(damageComponent);
        player.add(new BoundaryComponent(BoundaryComponent.MODE_WRAP));
        player.add(drawableComponentFactory.getPlayer());
        player.add(new CollisionComponent());
        return player;
    }

}
