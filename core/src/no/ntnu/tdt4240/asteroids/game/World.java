package no.ntnu.tdt4240.asteroids.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;

import java.util.Vector;

import javax.inject.Inject;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.entity.component.AnimationComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CircularBoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.HealthComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ObstacleClass;
import no.ntnu.tdt4240.asteroids.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.entity.component.ScoreComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.system.BoundsSystem;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;
import no.ntnu.tdt4240.asteroids.entity.system.DamageSystem;
import no.ntnu.tdt4240.asteroids.entity.system.EffectSystem;
import no.ntnu.tdt4240.asteroids.entity.system.GravitySystem;
import no.ntnu.tdt4240.asteroids.entity.system.MovementSystem;
import no.ntnu.tdt4240.asteroids.entity.system.ScoreSystem;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.game.effect.IEffect;
import no.ntnu.tdt4240.asteroids.game.effect.InvulnerabilityEffect;
import no.ntnu.tdt4240.asteroids.game.effect.MultishotEffect;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioManager;
import no.ntnu.tdt4240.asteroids.service.settings.IGameSettings;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.drawableMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.healthMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.movementMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.playerMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.scoreMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.transformMapper;

@SuppressWarnings("WeakerAccess")
public class World {


    public static final int EVENT_SCORE = 0;
    public static final int EVENT_GAME_OVER = 1;
    public static final int EVENT_LEVEL_COMPLETE = 2;
    public static final int EVENT_PLAYER_HITPOINTS = 3;
    public static final int EVENT_PLAYER_CHANGED = 5;
    public static final int STATE_READY = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_LEVEL_END = 3;
    public static final int STATE_GAME_OVER = 4;
    private static final int EVENT_WORLD_RESET = 4;
    private static final int EDGE_LEFT = 0;
    private static final int EDGE_TOP = 1;
    private static final int EDGE_RIGHT = 2;
    private static final int EDGE_BOTTOM = 3;
    private static final String TAG = World.class.getSimpleName();
    public final Vector<IGameListener> listeners = new Vector<>();
    // TODO: add config
    final PooledEngine engine;
    private final DamageSystem.IDamageHandler obstacleDamageHandler = new ObstacleDamageHandler(this);
    private final PlayerDamageHandler playerDamageHandler = new PlayerDamageHandler(this);
    private final EntityListener resetListener = new EntityListener() {
        @Override
        public void entityAdded(Entity entity) {
        }

        @Override
        public void entityRemoved(Entity entity) {
            if (engine.getEntities().size() == 0) {
                notifyListeners(EVENT_WORLD_RESET);
                engine.removeEntityListener(this);
            }
        }
    };
    @Inject
    IGameSettings gameSettings;
    @Inject
    EntityFactory entityFactory = ServiceLocator.getEntityComponent().getEntityFactory();
    @Inject
    AudioManager audioManager = ServiceLocator.getAppComponent().getAudioManager();
    private Entity player;
    private int state = STATE_READY;
    private int level = 0;

    public World(PooledEngine engine) {
        this.engine = engine;
        engine.addEntityListener(Family.all(ObstacleClass.class).get(), new ObstacleListener(this));
        setupEngineSystems();
        registerEffects();
        gameSettings = ServiceLocator.getEntityComponent().getGameSettings();
    }

    public void initialize() {
        spawnObstacles(engine.getEntities().size());
    }

    @SuppressWarnings("unchecked")
    private void registerEffects() {
        ServiceLocator.getEntityComponent().getEffectFactory().registerEffect(InvulnerabilityEffect.class);
        ServiceLocator.getEntityComponent().getEffectFactory().registerEffect(MultishotEffect.class);
    }

    private void setupEngineSystems() {
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new DamageSystem(engine.getSystem(CollisionSystem.class)));
        engine.addSystem(new EffectSystem());
        engine.addSystem(new GravitySystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new ScoreSystem(engine.getSystem(DamageSystem.class)));
        engine.getSystem(ScoreSystem.class).getListeners().add(new ScoreListener(this));
    }

    public Entity getPlayer() {
        return player;
    }

    private void clearEngine() {
        if (engine.getEntities().size() > 0) {
            engine.addEntityListener(resetListener);
            engine.clearPools();
            engine.removeAllEntities();
        }
    }

    public void reset() {
        level = 0;
        clearEngine();
    }

    public void nextLevel() {
        level += 1;
        clearEngine();
    }

    public void addPlayer(String id, String displayName, boolean multiplayer) {
        player = entityFactory.createPlayer(id, displayName, multiplayer);
        HealthComponent healthComponent = healthMapper.get(player);
        if (healthComponent != null) {
            healthComponent.damageHandler = playerDamageHandler;
        }
        engine.addEntity(player);
        notifyListeners(EVENT_PLAYER_CHANGED);
        notifyListeners(EVENT_PLAYER_HITPOINTS);
    }

    public void addMultiplayer(String participantId, String displayName) {
        Entity entity = entityFactory.createMultiPlayer(participantId, displayName);
        HealthComponent healthComponent = healthMapper.get(entity);
        healthComponent.damageHandler = new OpponentDamageHandler(this, participantId);
        engine.addEntity(entity);
    }

    private void spawnObstacles(int currentObstacles) {
        int attempts = gameSettings.getMinObstacles() - currentObstacles;
        int current = currentObstacles;
        while (attempts > 0) {
            engine.addEntity(createObstacle());
            --attempts;
            ++current;
        }
        attempts = gameSettings.getMaxObstacles() - current;
        for (int i = 0; i < attempts; ++i) {
            if (MathUtils.random() > 1 - gameSettings.getObstacleSpawnChance()) {
                engine.addEntity(createObstacle());
            }
        }
    }

    private Entity createPowerup(Entity source) {
        MovementComponent sourceMovement = movementMapper.get(source);
        TransformComponent sourceTransform = transformMapper.get(source);
        if (sourceMovement == null || sourceTransform == null) return null;
        Entity entity = entityFactory.createPowerup(getRandomEffect());
        MovementComponent movementComponent = movementMapper.get(entity);
        movementComponent.velocity.set(sourceMovement.velocity);
        TransformComponent transformComponent = transformMapper.get(entity);
        transformComponent.position.set(sourceTransform.position);
        return entity;
    }

    private IEffect getRandomEffect() {
        return ServiceLocator.getEntityComponent().getEffectFactory().getRandomEffect();
    }

    private Entity createObstacle() {
        Entity entity = entityFactory.createObstacle();
        DrawableComponent drawable = drawableMapper.get(entity);
        HealthComponent healthComponent = healthMapper.get(entity);
        healthComponent.damageHandler = obstacleDamageHandler;

        int edge = MathUtils.random(3);
        int x = 0;
        int y = 0;
        float xVec = 0;
        float yVec = 0;
        int halfRegionHeight = drawable.texture.getRegionHeight() / 2;
        int halfRegionWidth = drawable.texture.getRegionWidth() / 2;
        int graphicsWidth = Asteroids.VIRTUAL_WIDTH;
        int graphicsHeight = Asteroids.VIRTUAL_HEIGHT;
        int halfSpeed = gameSettings.getObstacleMaxSpeed() / 2;

        // Based on spawn, position and movement (always inwards) is generated randomly.
        switch (edge) {
            case EDGE_TOP:
                x = MathUtils.random(-halfRegionWidth, graphicsWidth + halfRegionWidth);
                xVec = MathUtils.random(-halfSpeed, halfSpeed);
                yVec = MathUtils.random() * gameSettings.getObstacleMaxSpeed();
                y = -halfRegionHeight;
                break;
            case EDGE_BOTTOM:
                x = MathUtils.random(-halfRegionWidth, graphicsWidth + halfRegionWidth);
                xVec = MathUtils.random(-halfSpeed, halfSpeed);
                yVec = -1 * MathUtils.random() * gameSettings.getObstacleMaxSpeed();
                y = graphicsHeight + halfRegionHeight;
                break;
            case EDGE_LEFT:
                y = MathUtils.random(-halfRegionHeight, graphicsHeight + halfRegionHeight);
                yVec = MathUtils.random(-halfSpeed, halfSpeed);
                xVec = MathUtils.random() * gameSettings.getObstacleMaxSpeed();
                x = -halfRegionWidth;
                break;
            case EDGE_RIGHT:
                y = MathUtils.random(-halfRegionHeight, graphicsHeight + halfRegionHeight);
                yVec = MathUtils.random(-halfSpeed, halfSpeed);
                xVec = -1 * MathUtils.random() * gameSettings.getObstacleMaxSpeed();
                x = graphicsWidth + halfRegionWidth;
                break;
        }

        if (player != null) {
            Circle playerBounds = (Circle) player.getComponent(CircularBoundsComponent.class).getBounds();
            if (playerBounds.radius > 0) {
                Circle spawnCircle = new Circle(playerBounds.x, playerBounds.y, playerBounds.radius + gameSettings.getPlayerNoSpawnRadius());
                if (spawnCircle.contains(x, y)) {
                    // TODO: 04-Apr-17 remove recursion
                    return createObstacle();
                }
            }
        }

        MovementComponent movement = entity.getComponent(MovementComponent.class);
        movement.velocity.set(xVec, yVec);
        TransformComponent position = entity.getComponent(TransformComponent.class);
        position.position.set(x, y);
        return entity;
    }

    void gameOver() {
        state = STATE_GAME_OVER;
        notifyListeners(EVENT_GAME_OVER);
    }

    public void run() {
        state = STATE_RUNNING;
        // TODO: start engine
    }

    public void stop() {
        state = STATE_READY;
        // TODO: stop engine
    }

    public void pause() {
        state = STATE_PAUSED;
        // TODO: pause engine
    }

    private void notifyListeners(int event) {
        for (IGameListener listener : listeners) {
            listener.handle(this, event);
        }
    }

    public void update(float delta) {
        if (state == STATE_RUNNING)
            engine.update(delta);
    }

    public int getLevel() {
        return level;
    }

    private void spawnPowerup(Entity entity) {
        if (MathUtils.random() > 1 - gameSettings.getPowerupSpawnChance()) {
            Entity powerup = createPowerup(entity);
            if (powerup != null) engine.addEntity(powerup);
        }
    }

    public interface IGameListener {

        void handle(World model, int event);

        void notifyScoreChanged(String id, int oldScore, int newScore);
    }

    private static class ObstacleListener implements EntityListener {

        private final ImmutableArray<Entity> objects;

        private World world;

        public ObstacleListener(World world) {
            this.world = world;
            objects = world.engine.getEntitiesFor(Family.one(ObstacleClass.class).get());
        }

        @Override
        public void entityAdded(Entity entity) {

        }

        @Override
        public void entityRemoved(Entity entity) {
            world.spawnObstacles(objects.size());
        }
    }

    private static class PlayerDamageHandler implements DamageSystem.IDamageHandler {

        private static final String TAG = PlayerDamageHandler.class.getSimpleName();
        private World world;

        public PlayerDamageHandler(World world) {
            this.world = world;
        }

        @Override
        public void onDamageTaken(Engine engine, Entity entity, int damageTaken) {
            Gdx.app.debug(TAG, "onDamageTaken: ");
            world.notifyListeners(EVENT_PLAYER_HITPOINTS);
        }

        @Override
        public void onEntityDestroyed(Engine engine, Entity source, Entity target) {
            world.gameOver();
        }
    }

    private static class OpponentDamageHandler implements DamageSystem.IDamageHandler {

        private static final String TAG = PlayerDamageHandler.class.getSimpleName();
        private World world;
        private String playerId;

        public OpponentDamageHandler(World world, String playerId) {
            this.world = world;
            this.playerId = playerId;
        }

        @Override
        public void onDamageTaken(Engine engine, Entity entity, int damageTaken) {
            Gdx.app.debug(TAG, "onDamageTaken: Opponent");
            // TODO: 06-Apr-17 handle opponent damage
//            world.notifyListeners(EVENT_PLAYER_HITPOINTS);
        }

        @Override
        public void onEntityDestroyed(Engine engine, Entity source, Entity target) {
            AnimationComponent animation = new AnimationComponent();
            // TODO: 31-Mar-17 Figure out why this line sometime causes a null reference
            animation.removeOnAnimationComplete = true;
            animation.frames.addAll(ServiceLocator.getAppComponent().getAnimationFactory().getObstacleDestroyedAnimation());
            target.add(animation);
            world.audioManager.playExplosion();
            target.remove(CollisionComponent.class);
            target.remove(MovementComponent.class);
            if (target.getComponent(PlayerClass.class) != null) {
                ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(PlayerClass.class, CollisionComponent.class).get());
                if (entities.size() < 2) {
                    world.notifyListeners(EVENT_GAME_OVER);
                }
            }
        }
    }

    private static class ObstacleDamageHandler implements DamageSystem.IDamageHandler {

        private World world;

        public ObstacleDamageHandler(World world) {
            this.world = world;
        }

        @Override
        public void onDamageTaken(Engine engine, Entity entity, int damageTaken) {

        }

        @Override
        public void onEntityDestroyed(Engine engine, Entity source, Entity target) {
            world.spawnPowerup(target);
            AnimationComponent animation = new AnimationComponent();
            // TODO: 31-Mar-17 Figure out why this line sometime causes a null reference
            animation.removeOnAnimationComplete = true;
            animation.frames.addAll(ServiceLocator.getAppComponent().getAnimationFactory().getObstacleDestroyedAnimation());
            target.add(animation);
            world.audioManager.playExplosion();
            target.remove(CollisionComponent.class);
            target.remove(MovementComponent.class);
        }
    }

    private static class ScoreListener implements ScoreSystem.IScoreListener {

        private World world;

        public ScoreListener(World world) {
            this.world = world;
        }

        @Override
        public void onScoreChanged(Engine engine, Entity entity, int oldScore) {
            ScoreComponent scoreComponent = scoreMapper.get(entity);
            PlayerClass playerClass = playerMapper.get(entity);
            for (IGameListener listener : world.listeners) {
                listener.notifyScoreChanged(playerClass.id, oldScore, scoreComponent.score);
            }
            world.notifyListeners(EVENT_SCORE);
        }
    }

}
