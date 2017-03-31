package no.ntnu.tdt4240.asteroids.game.shothandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioManager;

public class StandardShotHandler implements IShotHandler {

    public int BULLET_SPEED = 800;

    private EntityFactory factory = ServiceLocator.getEntityComponent().getEntityFactory();
    private AudioManager audioManager = ServiceLocator.getAppComponent().getAudioManager();

    @Override
    public void fire(PooledEngine engine, Entity controlledEntity) {
        audioManager.playShoot();
        Entity bullet = factory.createPlayerBullet();
        TransformComponent playerPosition = ComponentMappers.transformMapper.get(controlledEntity);
        TransformComponent bulletPosition = bullet.getComponent(TransformComponent.class);
        bulletPosition.position.set(playerPosition.position);
        MovementComponent bulletMovement = bullet.getComponent(MovementComponent.class);
        bulletMovement.velocity.set(playerPosition.rotation).setLength(BULLET_SPEED);
        engine.addEntity(bullet);
    }
}
