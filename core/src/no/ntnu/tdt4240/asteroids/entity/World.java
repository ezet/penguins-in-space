package no.ntnu.tdt4240.asteroids.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ObstacleComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.obstacleMapper;

public class World {

    // TODO: add config
    private static final double SPAWN_CHANCE = 0.2;
    // TODO: add config
    private static final int MAX_OBSTACLES = 8;
    // TODO: add config
    private static final int MIN_OBSTACLES = 3;
    // TODO: add config
    private static final float GRAVITY = 0.01f;
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
    // TODO: share random seed with networked clients ?
    private Random random;

    public World(PooledEngine engine) {
        this.engine = engine;
        //noinspection unchecked
        engine.addEntityListener(Family.all(ObstacleComponent.class).get(), new ObstacleListener());
        player = new Entity();
    }

    public Entity getPlayer() {
        return player;
    }

    public void create() {
        initPlayer();
        spawnObstacles(0);
    }

    private void reset() {
        engine.addEntityListener(resetListener);
        engine.clearPools();
        engine.removeAllEntities();
    }

    public void addOpponentPlayer() {

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
                // TODO: We hit an obstacle, HANDLE IT, eg. notify listeners
                reset();
            }
        }
    }

}
