package no.ntnu.tdt4240.asteroids.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

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
import no.ntnu.tdt4240.asteroids.game.effect.BombShotEffect;
import no.ntnu.tdt4240.asteroids.game.effect.InvulnerabilityEffect;
import no.ntnu.tdt4240.asteroids.game.effect.MissileShotEffect;
import no.ntnu.tdt4240.asteroids.game.effect.MultishotEffect;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioService;
import no.ntnu.tdt4240.asteroids.service.settings.IGameConfig;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.drawableMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.healthMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.scoreMapper;

@SuppressWarnings("WeakerAccess")
public class World {

    public static final int STATE_READY = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_PAUSED = 2;
    public static final int EVENT_WORLD_RESET = 0;

    @SuppressWarnings("unused")
    private static final String TAG = World.class.getSimpleName();
    private static final int SPAWN_LEFT = 0;
    private static final int SPAWN_TOP = 1;
    private static final int SPAWN_RIGHT = 2;
    private static final int SPAWN_BOTTOM = 3;
    private static final Family FAMILY_PLAYERS = Family.all(PlayerClass.class).get();
    private static final Family FAMILY_OBSTACLES = Family.all(ObstacleClass.class).get();
    public final ObstacleListener obstacleListener;
    public final PlayerListener playerListener;
    public final Array<IGameListener> listeners = new Array<>();
    final PooledEngine engine;
    private final PlayerDamageHandler playerDamageHandler = new PlayerDamageHandler(this);
    private final EntityListener resetListener = new EntityListener() {
        @Override
        public void entityAdded(Entity entity) {
        }

        @Override
        public void entityRemoved(Entity entity) {
            if (engine.getEntities().size() == 0) {
                notifyListeners(EVENT_WORLD_RESET);
                engine.addEntityListener(FAMILY_OBSTACLES, obstacleListener);
                engine.addEntityListener(FAMILY_PLAYERS, playerListener);
                engine.removeEntityListener(this);
            }
        }
    };
    private final ImmutableArray<Entity> players;
    @Inject
    IGameConfig gameSettings;
    @Inject
    EntityFactory entityFactory = ServiceLocator.getEntityComponent().getEntityFactory();
    @Inject
    AudioService audioService = ServiceLocator.getAppComponent().getAudioService();
    private int state = STATE_READY;
    private int level = 0;

    public World(PooledEngine engine) {
        this.engine = engine;
        obstacleListener = new ObstacleListener(this);
        engine.addEntityListener(FAMILY_OBSTACLES, obstacleListener);
        playerListener = new PlayerListener(this);
        engine.addEntityListener(FAMILY_PLAYERS, playerListener);
        players = engine.getEntitiesFor(FAMILY_PLAYERS);
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
        ServiceLocator.getEntityComponent().getEffectFactory().registerEffect(BombShotEffect.class);
        ServiceLocator.getEntityComponent().getEffectFactory().registerEffect(MissileShotEffect.class);
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


    private void clearEngine() {
        if (engine.getEntities().size() > 0) {
            engine.removeEntityListener(playerListener);
            engine.removeEntityListener(obstacleListener);
            engine.addEntityListener(resetListener);
            engine.removeAllEntities();
            engine.clearPools();
        }
    }

    public void reset() {
        clearEngine();
    }

    public void addPlayer(Entity entity) {
        HealthComponent healthComponent = healthMapper.get(entity);
        if (healthComponent != null) {
            healthComponent.damageHandler = playerDamageHandler;
        }
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


    private Entity createObstacle() {
        Entity entity = entityFactory.createObstacle();

        do {
            positionObstacle(entity);
        } while (!isValidPosition(entity));
        return entity;
    }

    private boolean isValidPosition(Entity entity) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        positionObstacle(entity);
        for (Entity player : players) {
            Circle playerBounds = (Circle) player.getComponent(CircularBoundsComponent.class).getBounds();
            if (playerBounds.radius > 0) {
                Circle spawnCircle = new Circle(playerBounds.x, playerBounds.y, playerBounds.radius + gameSettings.getPlayerNoSpawnRadius());
                if (spawnCircle.contains(transform.position.x, transform.position.y)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Entity positionObstacle(Entity entity) {
        DrawableComponent drawable = drawableMapper.get(entity);
        float xVec = 0;
        float yVec = 0;
        int x = 0;
        int y = 0;
        int halfRegionHeight = drawable.texture.getRegionHeight() / 2;
        int halfRegionWidth = drawable.texture.getRegionWidth() / 2;
        int graphicsWidth = Asteroids.VIRTUAL_WIDTH;
        int graphicsHeight = Asteroids.VIRTUAL_HEIGHT;
        int halfSpeed = gameSettings.getObstacleMaxSpeed() / 2;
        int edge = MathUtils.random(3);
        switch (edge) {
            case SPAWN_TOP:
                x = MathUtils.random(-halfRegionWidth, graphicsWidth + halfRegionWidth);
                xVec = MathUtils.random(-halfSpeed, halfSpeed);
                yVec = MathUtils.random() * gameSettings.getObstacleMaxSpeed();
                y = -halfRegionHeight;
                break;
            case SPAWN_BOTTOM:
                x = MathUtils.random(-halfRegionWidth, graphicsWidth + halfRegionWidth);
                xVec = MathUtils.random(-halfSpeed, halfSpeed);
                yVec = -1 * MathUtils.random() * gameSettings.getObstacleMaxSpeed();
                y = graphicsHeight + halfRegionHeight;
                break;
            case SPAWN_LEFT:
                y = MathUtils.random(-halfRegionHeight, graphicsHeight + halfRegionHeight);
                yVec = MathUtils.random(-halfSpeed, halfSpeed);
                xVec = MathUtils.random() * gameSettings.getObstacleMaxSpeed();
                x = -halfRegionWidth;
                break;
            case SPAWN_RIGHT:
                y = MathUtils.random(-halfRegionHeight, graphicsHeight + halfRegionHeight);
                yVec = MathUtils.random(-halfSpeed, halfSpeed);
                xVec = -1 * MathUtils.random() * gameSettings.getObstacleMaxSpeed();
                x = graphicsWidth + halfRegionWidth;
                break;
        }
        MovementComponent movement = entity.getComponent(MovementComponent.class);
        movement.velocity.set(xVec, yVec);
        TransformComponent position = entity.getComponent(TransformComponent.class);
        position.position.set(x, y);
        return entity;
    }

    public void run() {
        state = STATE_RUNNING;
        // TODO: start engine
    }

    public void stop() {
        state = STATE_READY;
        clearEngine();
        // TODO: stop engine
    }

    public void pause() {
        state = STATE_PAUSED;
        // TODO: pause engine
    }

    void notifyListeners(int event) {
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


    public interface IGameListener {

        void handle(World model, int event);

        void notifyScoreChanged(Entity entity, int oldScore, int newScore);

        void notifyPlayerRemoved(Entity entity);

        void notifyDamageTaken(Entity entity, int damageTaken);
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
        public void onDamageTaken(Engine engine, Entity entity, Entity source, int damageTaken) {
//            Gdx.app.debug(TAG, "onDamageTaken: ");
            for (IGameListener listener : world.listeners) {
                listener.notifyDamageTaken(entity, damageTaken);
            }
        }

        @Override
        public void onEntityDestroyed(Engine engine, Entity entity, Entity source) {
            AnimationComponent animation = new AnimationComponent();
            // TODO: 31-Mar-17 Figure out why this line sometime causes a null reference
            animation.removeEntityAfterAnimation = true;
            animation.frames.addAll(ServiceLocator.getAppComponent().getAnimationFactory().getMediumExplosion());
            entity.add(animation);
            world.audioService.playSound(AssetService.SoundAsset.SOUND_EXPLOSION_WAV);
            entity.remove(CollisionComponent.class);
            entity.remove(MovementComponent.class);
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
            for (IGameListener listener : world.listeners) {
                listener.notifyScoreChanged(entity, oldScore, scoreComponent.score);
            }
        }
    }

    private static class PlayerListener implements EntityListener {
        private World world;

        public PlayerListener(World world) {
            this.world = world;
        }

        @Override
        public void entityAdded(Entity entity) {
//            Gdx.app.debug(TAG, "entityAdded: " + entity);
        }

        @Override
        public void entityRemoved(Entity entity) {
//            Gdx.app.debug(TAG, "entityRemoved: " + entity);
            for (IGameListener listener : world.listeners) {
                listener.notifyPlayerRemoved(entity);
            }
        }
    }
}
