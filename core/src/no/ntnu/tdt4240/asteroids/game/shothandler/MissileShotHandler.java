package no.ntnu.tdt4240.asteroids.game.shothandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.entity.component.IdComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioManager;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.idMapper;

public class MissileShotHandler implements IShotHandler {

    public int BULLET_SPEED = 300;

    private EntityFactory factory = ServiceLocator.getEntityComponent().getEntityFactory();
    private AudioManager audioManager = ServiceLocator.getAppComponent().getAudioManager();

    @Override
    public void fire(PooledEngine engine, Entity controlledEntity) {
        audioManager.playShoot();
        IdComponent idComponent = idMapper.get(controlledEntity);
        Entity bullet = factory.createMissile(idComponent.participantId);
        TransformComponent playerTransform = ComponentMappers.transformMapper.get(controlledEntity);
        TransformComponent bulletTransform = bullet.getComponent(TransformComponent.class);
        bulletTransform.position.set(playerTransform.position);
        bulletTransform.rotation.set(playerTransform.rotation);
        MovementComponent bulletMovement = bullet.getComponent(MovementComponent.class);
        bulletMovement.velocity.set(playerTransform.rotation).setLength(BULLET_SPEED);
        engine.addEntity(bullet);
    }
}
