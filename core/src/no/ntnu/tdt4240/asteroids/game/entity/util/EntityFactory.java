package no.ntnu.tdt4240.asteroids.game.entity.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.MathUtils;

import javax.inject.Inject;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.game.AnimationFactory;
import no.ntnu.tdt4240.asteroids.game.collisionhandler.BulletCollisionHandler;
import no.ntnu.tdt4240.asteroids.game.collisionhandler.PowerupCollisionHandler;
import no.ntnu.tdt4240.asteroids.game.damagehandler.ObstacleDamageHandler;
import no.ntnu.tdt4240.asteroids.game.effect.IEffect;
import no.ntnu.tdt4240.asteroids.game.entity.component.AchievementComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.AnimationComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.BoundaryComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.BulletClass;
import no.ntnu.tdt4240.asteroids.game.entity.component.CircularBoundsComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.DamageComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.EffectComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.GravityComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.HealthComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.IdComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.NetworkAddComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.NetworkSyncComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.ObstacleClass;
import no.ntnu.tdt4240.asteroids.game.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.game.entity.component.PowerupClass;
import no.ntnu.tdt4240.asteroids.game.entity.component.ScoreComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.ShootComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.game.entity.system.CollisionSystem;
import no.ntnu.tdt4240.asteroids.game.entity.system.DamageSystem;
import no.ntnu.tdt4240.asteroids.game.shothandler.MultiShotHandler;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.config.IGameConfig;

import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.healthMapper;

public class EntityFactory {

    private static final DamageSystem.IDamageHandler OBSTACLE_DAMAGE_HANDLER = new ObstacleDamageHandler();
    private static final Family POWERUP_COLLISION_IGNORE = Family.one(ObstacleClass.class, BulletClass.class).get();
    private static final Family BULLET_COLLISION_IGNORE = Family.one(BulletClass.class).get();
    private static final Family OBSTACLE_COLLISION_IGNORE = Family.one(ObstacleClass.class).get();
    private static final CollisionSystem.ICollisionHandler bulletCollisionHandler = new BulletCollisionHandler();
    private static final PowerupCollisionHandler POWERUP_COLLISION_HANDLER = new PowerupCollisionHandler();
    private final PooledEngine engine;
    private final IDrawableComponentFactory drawableComponentFactory;
    private final IGameConfig gameSettings;
    private AnimationFactory animationFactory;

    @Inject
    public EntityFactory(PooledEngine engine, IDrawableComponentFactory drawableComponentFactory,
                         IGameConfig gameSettings, AnimationFactory animationFactory) {
        this.engine = engine;
        this.drawableComponentFactory = drawableComponentFactory;
        this.gameSettings = gameSettings;
        this.animationFactory = animationFactory;
    }

    public Entity createPlayer(String id, String displayName, boolean multiplayer) {
        Entity entity = new Entity();
        PlayerClass playerClass = new PlayerClass(displayName);
        entity.add(new IdComponent(id));
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
            entity.add(new HealthComponent(3));
            entity.add(drawableComponentFactory.getPlayer(false));
        } else {
            entity.add(new HealthComponent(1));
            entity.add(new EffectComponent());
            entity.add(drawableComponentFactory.getPlayer(true));
        }
        entity.add(new TransformComponent(positionX, positionY, rotationX, rotationY));
        MovementComponent movementComponent = new MovementComponent();
        movementComponent.accelerationScalar = gameSettings.getAccelerationScalar();
        entity.add(movementComponent);
        entity.add(new GravityComponent(gameSettings.getPlayerGravity()));
        entity.add(new CircularBoundsComponent());
        entity.add(new ShootComponent(new MultiShotHandler()));
        entity.add(new ScoreComponent());
        entity.add(new AchievementComponent());
        entity.add(new BoundaryComponent(BoundaryComponent.MODE_WRAP));
        CollisionComponent collisionComponent = new CollisionComponent();
        entity.add(collisionComponent);
        return entity;
    }

    public Entity createOpponent(String participantId, String displayName) {
        Entity entity = new Entity();
        entity.add(new PlayerClass(displayName));
        entity.add(new IdComponent(participantId));
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
        entity.add(engine.createComponent(BulletClass.class));
        IdComponent idComponent = engine.createComponent(IdComponent.class);
        idComponent.participantId = playerId;
        entity.add(idComponent);
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

    public Entity createMissile(String playerId) {
        Entity entity = engine.createEntity();
        entity.add(engine.createComponent(BulletClass.class));
        IdComponent idComponent = engine.createComponent(IdComponent.class);
        idComponent.participantId = playerId;
        entity.add(idComponent);
        entity.add(engine.createComponent(TransformComponent.class));
        entity.add(engine.createComponent(MovementComponent.class));
        entity.add(engine.createComponent(CircularBoundsComponent.class));
        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(engine.createComponent(NetworkAddComponent.class));
        entity.add(drawableComponentFactory.getMissile());
        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        entity.add(animation);
        animation.delay = gameSettings.getMissileDelay();
        animation.scale.set(1.5f, 1.5f);
        animation.removeDuringAnimation.add(MovementComponent.class);
        animation.removeEntityAfterAnimation = true;
        animation.soundOnStart = AssetService.SoundAsset.SOUND_EXPLOSION_WAV;
        animation.frames.addAll(animationFactory.getShortExplosion());
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        collisionComponent.collisionHandler = bulletCollisionHandler;
        collisionComponent.ignoredEntities = BULLET_COLLISION_IGNORE;
        entity.add(collisionComponent);
        return entity;
    }

    public Entity createBomb(String playerId) {
        Entity entity = engine.createEntity();
        entity.add(engine.createComponent(BulletClass.class));
        IdComponent idComponent = engine.createComponent(IdComponent.class);
        idComponent.participantId = playerId;
        entity.add(idComponent);
        entity.add(engine.createComponent(TransformComponent.class));
        entity.add(engine.createComponent(CircularBoundsComponent.class));
        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(engine.createComponent(NetworkAddComponent.class));
        entity.add(drawableComponentFactory.getBomb());
        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        entity.add(animation);
        animation.soundOnStart = AssetService.SoundAsset.SOUND_EXPLOSION_WAV;
        animation.delay = gameSettings.getBombDelay();
        animation.scale.set(2.5f, 2.5f);
        animation.removeEntityAfterAnimation = true;
        animation.frames.addAll(animationFactory.getLongExplosion());
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
        HealthComponent healthComponent = healthMapper.get(entity);
        healthComponent.damageHandler = OBSTACLE_DAMAGE_HANDLER;
        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(engine.createComponent(NetworkAddComponent.class));
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
        entity.add(drawableComponentFactory.getPowerup(effect));
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        collisionComponent.ignoredEntities = POWERUP_COLLISION_IGNORE;
        collisionComponent.collisionHandler = POWERUP_COLLISION_HANDLER;
        entity.add(collisionComponent);
        entity.add(engine.createComponent(MovementComponent.class));
        entity.add(engine.createComponent(TransformComponent.class));
        return entity;
    }
}
