package no.ntnu.tdt4240.asteroids.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;
import java.util.Vector;

import no.ntnu.tdt4240.asteroids.entity.component.AnimationComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ObstacleComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;
import no.ntnu.tdt4240.asteroids.entity.system.BoundsSystem;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;
import no.ntnu.tdt4240.asteroids.entity.system.GravitySystem;
import no.ntnu.tdt4240.asteroids.entity.system.MovementSystem;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.bulletMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.obstacleMapper;

public class GameModel {

    public static final int EVENT_SCORE = 0;
    public static final int EVENT_GAME_OVER = 1;
    public static final int EVENT_LEVEL_COMPLETE = 2;

    public static final int STATE_READY = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_LEVEL_END = 3;
    public static final int STATE_GAME_OVER = 4;
    // TODO: add config
    private static final double SPAWN_CHANCE = 0.2;
    // TODO: add config
    private static final int MAX_OBSTACLES = 8;
    // TODO: add config
    private static final int MIN_OBSTACLES = 3;
    public final Vector<IGameListener> listeners = new Vector<>();
    // TODO: add config
    private final PooledEngine engine;
    private final CollisionSystem.ICollisionHandler playerCollisionHandler = new PlayerCollisionHandler();
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
    private CollisionSystem.ICollisionHandler obstacleCollisionHandler = new CollisionSystem.ICollisionHandler() {
        @Override
        public void onCollision(Entity source, Entity target, Engine engine) {
            if (bulletMapper.has(target)) {
                AnimationComponent animation = new AnimationComponent();
                source.remove(CollisionComponent.class);
                source.remove(MovementComponent.class);
                animation.removeOnAnimationComplete = true;
                animation.frames.addAll(explosions);
                source.add(animation);
                increaseScore();
            }
            // TODO: handle other collisions
        }
    };
    // TODO: share random seed with networked clients ?
    private Random random;

    public GameModel(PooledEngine engine) {
        this.engine = engine;
        //noinspection unchecked
        engine.addEntityListener(Family.all(ObstacleComponent.class).get(), new ObstacleListener());
        player = new Entity();
        initEngineCore();
        initExplosions();
    }

    private void increaseScore() {
        score++;
        notifyListeners(EVENT_SCORE);
    }

    private void initEngineCore() {
        engine.addSystem(new GravitySystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new CollisionSystem());
    }

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
        CollisionComponent collisionComponent = player.getComponent(CollisionComponent.class);
        collisionComponent.collisionHandler = playerCollisionHandler;
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
        for (int i = 0; i < attempts; ++i)
            if (MathUtils.random() > 1 - SPAWN_CHANCE) {
                engine.addEntity(createObstacle());
            }
    }

    private Entity createObstacle() {
        Entity obstacle = EntityFactory.getInstance().createObstacle();

        PositionComponent position = obstacle.getComponent(PositionComponent.class);
        DrawableComponent drawable = obstacle.getComponent(DrawableComponent.class);
        CollisionComponent collisionComponent = obstacle.getComponent(CollisionComponent.class);
        collisionComponent.collisionHandler = obstacleCollisionHandler;

        int obstacleSide = MathUtils.random(4);
        // 0 = top-spawn, 1 = bottom-spawn, 2 = left-spawn, 3 = right-spawn
        int x, y;
        float xVec, yVec;
        int halfRegionHeight = drawable.region.getRegionHeight()/2;
        int halfRegionWidth = drawable.region.getRegionWidth()/2;
        int graphicsWidth = Gdx.graphics.getWidth();
        int graphicsHeight = Gdx.graphics.getHeight();

        // Based on spawn, position and movement (always inwards) is generated randomly.
        if (obstacleSide < 2){
            x = MathUtils.random(-halfRegionWidth, graphicsWidth + halfRegionWidth);
            xVec = MathUtils.random(-100,101);
            yVec = MathUtils.random()*200;
            if (obstacleSide == 0){
                y = -halfRegionHeight;
            } else {
                yVec *= -1;
                y = graphicsHeight + halfRegionHeight;
            }
        } else {
            y = MathUtils.random(-halfRegionHeight, graphicsHeight + halfRegionHeight);
            yVec = MathUtils.random(-100, 101);
            xVec = MathUtils.random()*200;
            if (obstacleSide == 2){
                x = -halfRegionWidth;
            } else {
                x = graphicsWidth + halfRegionWidth;
                xVec *= -1;
            }
        }
        position.position.set(x, y);
        MovementComponent movement = obstacle.getComponent(MovementComponent.class);
        movement.velocity.set(xVec, yVec);
        return obstacle;
    }

    private void gameOver() {
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
            listener.update(this, event);
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

    public interface IGameListener {

        void update(GameModel model, int event);
    }

    private class ObstacleListener implements EntityListener {

        private ImmutableArray<Entity> obstacles = engine.getEntitiesFor(Family.all(ObstacleComponent.class).get());

        @Override
        public void entityAdded(Entity entity) {

        }

        @Override
        public void entityRemoved(Entity entity) {
            spawnObstacles(obstacles.size());
        }
    }

    private class PlayerCollisionHandler implements CollisionSystem.ICollisionHandler {
        @Override
        public void onCollision(Entity source, Entity target, Engine engine) {
            if (obstacleMapper.has(target)) {
                // TODO: We hit an obstacle, HANDLE IT, eg. update listeners
                gameOver();
            }
        }
    }

}
