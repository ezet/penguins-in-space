package no.ntnu.tdt4240.asteroids.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.asteroids.entity.IDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.entity.component.BoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BulletComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;

public class InputHandler {

    // TODO: read config from settings
    private static final int BULLET_SPEED = 800;
    private static final int ACCELERATION_SCALAR = 500;
    private final PooledEngine engine;
    private final IDrawableComponentFactory drawableComponentFactory;
    private final List<InputListener> listeners = new ArrayList<InputListener>();
    private Entity player;

    public InputHandler(PooledEngine engine, IDrawableComponentFactory drawableComponentFactory) {
        this.engine = engine;
        this.drawableComponentFactory = drawableComponentFactory;
    }

    public void setControlledEntity(Entity player) {
        this.player = player;
    }

    public void addListener(InputListener listener) {
        listeners.add(listener);
    }

    public void removeListener(InputListener listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    void move(float inputX, float inputY) {
        MovementComponent movement = ComponentMappers.movementMapper.get(player);
        movement.acceleration.set(inputX, inputY).scl(ACCELERATION_SCALAR);
        if (!movement.acceleration.isZero()) {
            PositionComponent position = ComponentMappers.positionMapper.get(player);
            position.rotation.set(movement.acceleration);
        }
        for (InputListener listener : listeners) {
            listener.onMove();
        }
    }

    void fire() {
        PositionComponent playerPosition = ComponentMappers.positionMapper.get(player);
        PositionComponent bulletPosition = engine.createComponent(PositionComponent.class);
        DrawableComponent playerDrawable = ComponentMappers.drawableMapper.get(player);
        bulletPosition.position.set(playerPosition.position);
        bulletPosition.position.x += (playerDrawable.getRegion().getRegionWidth() / 2);
        bulletPosition.position.y += (playerDrawable.getRegion().getRegionHeight() / 2);

        MovementComponent bulletMovement = engine.createComponent(MovementComponent.class);
        bulletMovement.velocity.set(playerPosition.rotation).setLength(BULLET_SPEED);

        Entity bullet = engine.createEntity();
        bullet.add(bulletPosition);
        bullet.add(bulletMovement);
        DrawableComponent bulletDrawable = drawableComponentFactory.getBullet();
        bullet.add(new BulletComponent());
        bullet.add(bulletDrawable);
        bullet.add(new BoundsComponent());

        bullet.add(new CollisionComponent(new CollisionComponent.ICollisionHandler() {
            @Override
            public void onCollision(Entity source, Entity target, Engine engine) {
                if (ComponentMappers.obstacleMapper.has(target)) {
                    // TODO: we hit an obstacle, MISSION ACCOMPLISHED!
                    engine.removeEntity(source);
                }
            }
        }));
        engine.addEntity(bullet);
        for (InputListener listener : listeners) {
            listener.onFire();
        }
    }


    // TODO: implement listener for audio
    public interface InputListener {

        void onMove();

        void onFire();
    }
}
