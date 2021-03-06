package no.ntnu.tdt4240.asteroids.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.tdt4240.asteroids.game.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.TransformComponent;

import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.movementMapper;
import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.transformMapper;


public class MovementSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(TransformComponent.class, MovementComponent.class).get();
    //    private static final int MAX_VELOCITY = 400;
    @SuppressWarnings("unused")
    private static final String TAG = MovementSystem.class.getSimpleName();
    private Vector2 temp = new Vector2();

    public MovementSystem() {
        super(FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent position = transformMapper.get(entity);
        MovementComponent movement = movementMapper.get(entity);

        temp.set(movement.acceleration).scl(deltaTime);
        movement.velocity.add(temp);
//        movement.velocity.add(temp).clamp(0, MAX_VELOCITY);

        temp.set(movement.velocity).scl(deltaTime);
        position.position.add(temp);
    }
}
