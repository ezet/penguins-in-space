package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import no.ntnu.tdt4240.asteroids.entity.component.GravityComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.gravityMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.movementMapper;

public class GravitySystem extends IteratingSystem {

    public GravitySystem() {
        //noinspection unchecked
        super(Family.all(GravityComponent.class, MovementComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        GravityComponent gravity = gravityMapper.get(entity);
        MovementComponent movement = movementMapper.get(entity);
        movement.velocity.scl(1 - gravity.gravity);
    }
}
