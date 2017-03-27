package no.ntnu.tdt4240.asteroids.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.Vector;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.entity.component.AnimationComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.HealthComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ObstacleClass;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.system.BoundsSystem;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;
import no.ntnu.tdt4240.asteroids.entity.system.DamageSystem;
import no.ntnu.tdt4240.asteroids.entity.system.EffectSystem;
import no.ntnu.tdt4240.asteroids.entity.system.GravitySystem;
import no.ntnu.tdt4240.asteroids.entity.system.MovementSystem;
import no.ntnu.tdt4240.asteroids.entity.util.EffectFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.game.effect.IEffect;
import no.ntnu.tdt4240.asteroids.game.effect.InvulnerabilityEffect;
import no.ntnu.tdt4240.asteroids.game.effect.MultishotEffect;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.drawableMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.healthMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.movementMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.transformMapper;

@SuppressWarnings("WeakerAccess")
public class World {

    public static final int EVENT_SCORE = 0;
    public static final int EVENT_GAME_OVER = 1;
    public static final int EVENT_LEVEL_COMPLETE = 2;

    private final static int EDGE_LEFT = 0;
    private static final int EDGE_TOP = 1;
    private static final int EDGE_RIGHT = 2;
    private static final int EDGE_BOTTOM = 3;

    public static final int STATE_READY = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_LEVEL_END = 3;
    public static final int STATE_GAME_OVER = 4;
    public static final double POWERUP_SPAWN_CHANCE = 0.2;
    // TODO: add config
    private static final double OBSTACLE_SPAWN_CHANCE = 0.3;
    // TODO: add config
    private static final int MAX_OBSTACLES = 8;
    // TODO: add config
    private static final int MIN_OBSTACLES = 3;
    private static final int PRIMARY_OBSTACLE_SPAWN_SPEED = 200;
    private static final int SECONDARY_OBSTACLE_SPAWN_SPEED = 100;

    public final Vector<IGameListener> listeners = new Vector<>();
    // TODO: add config
    final PooledEngine engine;
    private final DamageSystem.IEntityDestroyedListener playerDestroyedHandler = new PlayerDestroyedHandler();
    private final Entity player;
    private final EntityListener resetListener = new EntityListener() {
        @Override
        public void entityAdded(Entity entity) {
        }

        @Override
        public void entityRemoved(Entity entity) {
            if (engine.getEntities().size() == 0) {
                initPlayer();
                engine.removeEntityListener(this);
            }
        }
    };
    private int state = STATE_READY;
    private int score = 0;
    private int level = 0;
    // TODO: use texture packer and atlas
    private Array<TextureRegion> explosions = new Array<>();

    private final DamageSystem.IEntityDestroyedListener obstacleDestroyedHandler = new ObstacleDestroyedHandler();


    public World(PooledEngine engine) {
        this.engine = engine;
        //noinspection unchecked
        engine.addEntityListener(Family.all(ObstacleClass.class).get(), new ObstacleListener());
        player = new Entity();
        setupEngineSystems();
        initExplosions();
        EffectFactory.getInstance().registerEffect(InvulnerabilityEffect.class);
        EffectFactory.getInstance().registerEffect(MultishotEffect.class);
    }

    private void increaseScore() {
        score++;
        notifyListeners(EVENT_SCORE);
    }

    private void setupEngineSystems() {
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new DamageSystem(engine.getSystem(CollisionSystem.class)));
        engine.addSystem(new EffectSystem());
        engine.addSystem(new GravitySystem());
        engine.addSystem(new MovementSystem());
    }

    // TODO: move this
    private void initExplosions() {
        Texture texture = new Texture("explosion.png");
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                explosions.add(new TextureRegion(texture, j * 64, i * 64, 64, 64));
            }
        }
    }

    public Entity getPlayer() {
        return player;
    }

    public void initialize() {
        score = 0;
        level = 0;
        if (engine.getEntities().size() > 0) {
            engine.addEntityListener(resetListener);
            engine.clearPools();
            engine.removeAllEntities();
        } else {
            initPlayer();
        }
        spawnObstacles(0);
    }

    private void initPlayer() {
        EntityFactory.getInstance().initPlayer(player);
        healthMapper.get(player).entityDestroyedHandler = playerDestroyedHandler;
        player.getComponent(HealthComponent.class).entityDestroyedHandler = playerDestroyedHandler;
        engine.addEntity(player);
    }

    private void spawnObstacles(int currentObstacles) {
        int attempts = MIN_OBSTACLES - currentObstacles;
        int current = currentObstacles;
        while (attempts > 0) {
            engine.addEntity(createObstacle());
            --attempts;
            ++current;
        }
        attempts = MAX_OBSTACLES - current;
        for (int i = 0; i < attempts; ++i) {
            if (MathUtils.random() > 1 - OBSTACLE_SPAWN_CHANCE) {
                engine.addEntity(createObstacle());
            }
        }
    }

    // TODO: improve spawn position and direction

    private Entity createPowerup(Entity source) {
        MovementComponent sourceMovement = movementMapper.get(source);
        TransformComponent sourceTransform = transformMapper.get(source);
        Entity entity = EntityFactory.getInstance().createPowerup(getEffect());
        TransformComponent transformComponent = transformMapper.get(entity);
        transformComponent.position.set(sourceTransform.position);
        MovementComponent movementComponent = movementMapper.get(entity);
        movementComponent.velocity.set(sourceMovement.velocity);
        return entity;
    }

    private IEffect getEffect() {
        return EffectFactory.getInstance().getRandomEffect();
    }

    private Entity createObstacle() {
        Entity entity = EntityFactory.getInstance().createObstacle();
        DrawableComponent drawable = drawableMapper.get(entity);
        HealthComponent healthComponent = healthMapper.get(entity);
        healthComponent.entityDestroyedHandler = obstacleDestroyedHandler;

        // TODO: consider not spawning obstacles close to the player

        int edge = MathUtils.random(3);
        int x = 0;
        int y = 0;
        float xVec = 0;
        float yVec = 0;
        int halfRegionHeight = drawable.texture.getRegionHeight() / 2;
        int halfRegionWidth = drawable.texture.getRegionWidth() / 2;
        int graphicsWidth = Asteroids.VIRTUAL_WIDTH;
        int graphicsHeight = Asteroids.VIRTUAL_HEIGHT;

        // Based on spawn, position and movement (always inwards) is generated randomly.
        switch (edge){
            case EDGE_TOP:
                x = MathUtils.random(-halfRegionWidth, graphicsWidth + halfRegionWidth);
                xVec = MathUtils.random(-SECONDARY_OBSTACLE_SPAWN_SPEED, SECONDARY_OBSTACLE_SPAWN_SPEED);
                yVec = MathUtils.random() * PRIMARY_OBSTACLE_SPAWN_SPEED;
                y = -halfRegionHeight;
                break;
            case EDGE_BOTTOM:
                x = MathUtils.random(-halfRegionWidth, graphicsWidth + halfRegionWidth);
                xVec = MathUtils.random(-SECONDARY_OBSTACLE_SPAWN_SPEED, SECONDARY_OBSTACLE_SPAWN_SPEED);
                yVec = -1 * MathUtils.random() * PRIMARY_OBSTACLE_SPAWN_SPEED;
                y = graphicsHeight + halfRegionHeight;
                break;
            case EDGE_LEFT:
                y = MathUtils.random(-halfRegionHeight, graphicsHeight + halfRegionHeight);
                yVec = MathUtils.random(-SECONDARY_OBSTACLE_SPAWN_SPEED, SECONDARY_OBSTACLE_SPAWN_SPEED);
                xVec = MathUtils.random() * PRIMARY_OBSTACLE_SPAWN_SPEED;
                x = -halfRegionWidth;
                break;
            case EDGE_RIGHT:
                y = MathUtils.random(-halfRegionHeight, graphicsHeight + halfRegionHeight);
                yVec = MathUtils.random(-SECONDARY_OBSTACLE_SPAWN_SPEED, SECONDARY_OBSTACLE_SPAWN_SPEED);
                xVec = -1 * MathUtils.random() * PRIMARY_OBSTACLE_SPAWN_SPEED;
                x = graphicsWidth + halfRegionWidth;
                break;

        }
        TransformComponent position = entity.getComponent(TransformComponent.class);
        position.position.set(x, y);
        MovementComponent movement = entity.getComponent(MovementComponent.class);
        movement.velocity.set(xVec, yVec);
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

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    private void spawnPowerup(Entity entity) {
        if (MathUtils.random() > 1 - POWERUP_SPAWN_CHANCE) {
            engine.addEntity(createPowerup(entity));
        }
    }

    public interface IGameListener {

        void handle(World model, int event);
    }

    private class ObstacleListener implements EntityListener {

        private final ImmutableArray<Entity> objects = engine.getEntitiesFor(Family.one(ObstacleClass.class).get());

        @Override
        public void entityAdded(Entity entity) {

        }

        @Override
        public void entityRemoved(Entity entity) {
            spawnObstacles(objects.size());
        }
    }

    private class PlayerDestroyedHandler implements DamageSystem.IEntityDestroyedListener {

        @Override
        public void onEntityDestroyed(Engine engine, Entity source, Entity target) {
            gameOver();
        }
    }

    private class ObstacleDestroyedHandler implements DamageSystem.IEntityDestroyedListener {

        @Override
        public void onEntityDestroyed(Engine engine, Entity source, Entity target) {
            spawnPowerup(target);
            AnimationComponent animation = new AnimationComponent();
            target.remove(CollisionComponent.class);
            animation.removeOnAnimationComplete = true;
            animation.frames.addAll(explosions);
            target.add(animation);
            increaseScore();
        }
    }
}
