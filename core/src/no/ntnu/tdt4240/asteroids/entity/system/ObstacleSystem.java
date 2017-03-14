package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import no.ntnu.tdt4240.asteroids.entity.IDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ObstacleComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;

public class ObstacleSystem extends EntitySystem {

    private static final int MAX_OBSTACLES = 8;
    private static final int MIN_OBSTACLES = 2;

    private ImmutableArray<Entity> entities;

    private IDrawableComponentFactory drawableComponentFactory;

    public ObstacleSystem(IDrawableComponentFactory drawableComponentFactory) {
        this.drawableComponentFactory = drawableComponentFactory;
    }


    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        //noinspection unchecked
        entities = engine.getEntitiesFor(Family.all(ObstacleComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        // TODO: Figure out when and where to spawn obstacles
        int remaining = MIN_OBSTACLES - entities.size();
        while (remaining > 0) {
            createObstacle();
            --remaining;
        }
        if (entities.size() < MAX_OBSTACLES && MathUtils.random() > 0.8)
            createObstacle();
    }

    private void createObstacle() {
        Entity obstacle;
        PositionComponent position;
        MovementComponent movement;

        if (getEngine() instanceof PooledEngine) {
            PooledEngine engine = (PooledEngine) getEngine();
            obstacle = engine.createEntity();
            position = engine.createComponent(PositionComponent.class);
            movement = engine.createComponent(MovementComponent.class);
        } else {
            obstacle = new Entity();
            position = new PositionComponent();
            movement = new MovementComponent();
        }

        // TODO: Implement better obstacle spawn position
        DrawableComponent obstacleDrawable = drawableComponentFactory.getObstacle();
        int x = MathUtils.random(0, Gdx.graphics.getWidth());
        int y = Gdx.graphics.getHeight();
        position.position.set(x, y);
        movement.velocity.setToRandomDirection().clamp(100, 200);

        obstacle.add(position);
        obstacle.add(movement);
        obstacle.add(obstacleDrawable);
        obstacle.add(new ObstacleComponent());
        getEngine().addEntity(obstacle);
    }
}
