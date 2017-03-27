package no.ntnu.tdt4240.asteroids.game.shothandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.AssetLoader;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;

public class StandardShotHandler implements IShotHandler {

    private int BULLET_SPEED = 800;

    @Override
    public void fire(PooledEngine engine, Entity controlledEntity) {
        AssetLoader.shot.play();
        EntityFactory factory = EntityFactory.getInstance();
        Entity bullet = factory.createPlayerBullet();
        TransformComponent playerPosition = ComponentMappers.transformMapper.get(controlledEntity);
        TransformComponent bulletPosition = bullet.getComponent(TransformComponent.class);
        bulletPosition.position.set(playerPosition.position);
        MovementComponent bulletMovement = bullet.getComponent(MovementComponent.class);
        bulletMovement.velocity.set(playerPosition.rotation).setLength(BULLET_SPEED);
        engine.addEntity(bullet);
    }
}
