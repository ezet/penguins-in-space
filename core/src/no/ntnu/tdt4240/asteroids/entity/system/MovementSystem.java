package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.VelocityComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.positionMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.velocityMapper;


public class MovementSystem extends IteratingSystem {

    private static final String TAG = MovementSystem.class.getSimpleName();

    public MovementSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = positionMapper.get(entity);
        VelocityComponent velocity = velocityMapper.get(entity);
        position.setX(position.getX() + velocity.getX() * deltaTime);
        position.setY(position.getY() + velocity.getY() * deltaTime);
    }
}
