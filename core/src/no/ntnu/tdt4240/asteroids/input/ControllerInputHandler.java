package no.ntnu.tdt4240.asteroids.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ShootComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.shootMapper;

public class ControllerInputHandler {

    private final PooledEngine engine;


    private Entity controlledEntity;
    private static final String TAG = ControllerInputHandler.class.getSimpleName();


    public ControllerInputHandler(PooledEngine engine) {
        this.engine = engine;
    }

    public void setControlledEntity(Entity player) {
        this.controlledEntity = player;
    }

    public void accelerate(float inputX, float inputY) {
        MovementComponent movement = ComponentMappers.movementMapper.get(controlledEntity);
        if (movement == null) {
//            Gdx.app.debug(TAG, "accelerate: No movement component");
            return;
        }
        movement.acceleration.set(inputX, inputY).scl(movement.accelerationScalar);
        if (!movement.acceleration.isZero()) {
            TransformComponent position = ComponentMappers.transformMapper.get(controlledEntity);
            position.rotation.set(movement.acceleration);
        }
    }

    public void fire() {
        ShootComponent shootComponent = shootMapper.get(controlledEntity);
        if (shootComponent == null) {
//            Gdx.app.debug(TAG, "fire: No shoot component");
            return;
        }
        shootComponent.fire(engine, controlledEntity);

    }

}
