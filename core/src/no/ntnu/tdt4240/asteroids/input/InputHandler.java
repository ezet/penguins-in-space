package no.ntnu.tdt4240.asteroids.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.entity.IDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;

public class InputHandler {

    public static final int MAX_VELOCITY = 500;
    private final Entity player;
    private final PooledEngine engine;
    private final IDrawableComponentFactory drawableComponentFactory;

    public InputHandler(Entity player, PooledEngine engine, IDrawableComponentFactory drawableComponentFactory) {
        this.player = player;
        this.engine = engine;
        this.drawableComponentFactory = drawableComponentFactory;
    }

    public void move(float inputX, float inputY) {
        // TODO: implement proper player velocity and acceleration
        MovementComponent movementComponent = ComponentMappers.movementMapper.get(player);
        movementComponent.acceleration.set(inputX, inputY).scl(500);
//        movementComponent.velocity.set(inputX * MAX_VELOCITY, inputY * MAX_VELOCITY);
    }

    public void fire() {
        // TODO: implement proper bullet speed and direction
        PositionComponent playerPosition = ComponentMappers.positionMapper.get(player);
        PositionComponent bulletPosition = engine.createComponent(PositionComponent.class);
        bulletPosition.position.set(playerPosition.position);

        MovementComponent playerMovement = ComponentMappers.movementMapper.get(player);
        MovementComponent bulletMovement = engine.createComponent(MovementComponent.class);
        bulletMovement.velocity.set(playerMovement.velocity.x * 10, playerMovement.velocity.y * 10);

        Entity bullet = engine.createEntity();
        bullet.add(bulletPosition);
        bullet.add(bulletMovement);
        bullet.add(drawableComponentFactory.getBullet());
        engine.addEntity(bullet);
    }
}
