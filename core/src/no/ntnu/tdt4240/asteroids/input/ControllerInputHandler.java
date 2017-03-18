package no.ntnu.tdt4240.asteroids.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;

public class ControllerInputHandler {

    // TODO: read config from settings
    private static final int BULLET_SPEED = 800;
    private static final int ACCELERATION_SCALAR = 500;
    private final PooledEngine engine;
    private final List<InputListener> listeners = new ArrayList<>();
    private Entity controlledEntity;

    public ControllerInputHandler(PooledEngine engine) {
        this.engine = engine;
    }

    public void setControlledEntity(Entity player) {
        this.controlledEntity = player;
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

    public void accelerate(float inputX, float inputY) {
        MovementComponent movement = ComponentMappers.movementMapper.get(controlledEntity);
        movement.acceleration.set(inputX, inputY).scl(ACCELERATION_SCALAR);
        if (!movement.acceleration.isZero()) {
            PositionComponent position = ComponentMappers.positionMapper.get(controlledEntity);
            position.rotation.set(movement.acceleration);
        }
        for (InputListener listener : listeners) {
            listener.onMove();
        }
    }

    public void fire() {
        EntityFactory factory = EntityFactory.getInstance();
        Entity bullet = factory.createBullet();
        PositionComponent playerPosition = ComponentMappers.positionMapper.get(controlledEntity);
        PositionComponent bulletPosition = bullet.getComponent(PositionComponent.class);
        DrawableComponent playerDrawable = ComponentMappers.drawableMapper.get(controlledEntity);
        bulletPosition.position.set(playerPosition.position);
//        bulletPosition.position.x += (playerDrawable.region.getRegionWidth() / 2);
//        bulletPosition.position.y += (playerDrawable.region.getRegionHeight() / 2);
        MovementComponent bulletMovement = bullet.getComponent(MovementComponent.class);
        bulletMovement.velocity.set(playerPosition.rotation).setLength(BULLET_SPEED);

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
