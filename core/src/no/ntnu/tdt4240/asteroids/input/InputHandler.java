package no.ntnu.tdt4240.asteroids.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.asteroids.entity.IDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;

public class InputHandler {

    // TODO: read config from settings
    private static final int BULLET_SPEED = 800;
    private static final int ACCELERATION_SCALAR = 500;
    private final Entity player;
    private final PooledEngine engine;
    private final IDrawableComponentFactory drawableComponentFactory;
    private final List<InputListener> listeners = new ArrayList<InputListener>();

    public InputHandler(Entity player, PooledEngine engine, IDrawableComponentFactory drawableComponentFactory) {
        this.player = player;
        this.engine = engine;
        this.drawableComponentFactory = drawableComponentFactory;
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
        bulletPosition.position.set(playerPosition.position);

        MovementComponent bulletMovement = engine.createComponent(MovementComponent.class);
        bulletMovement.velocity.set(playerPosition.rotation).setLength(BULLET_SPEED);

        Entity bullet = engine.createEntity();
        bullet.add(bulletPosition);
        bullet.add(bulletMovement);
        bullet.add(drawableComponentFactory.getBullet());
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
