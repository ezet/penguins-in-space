package no.ntnu.tdt4240.asteroids.game.shothandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.game.entity.component.IdComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers;
import no.ntnu.tdt4240.asteroids.game.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioService;

import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.idMapper;

public class StandardShotHandler implements IShotHandler {

    public int BULLET_SPEED = 800;

    private EntityFactory factory = ServiceLocator.getEntityComponent().getEntityFactory();
    private AudioService audioService = ServiceLocator.getAppComponent().getAudioService();

    @Override
    public void fire(PooledEngine engine, Entity controlledEntity) {
        audioService.playSound(AssetService.SoundAsset.SOUND_SHOOT_WAV);
        IdComponent idComponent = idMapper.get(controlledEntity);
        Entity bullet = factory.createBullet(idComponent.participantId);
        TransformComponent playerPosition = ComponentMappers.transformMapper.get(controlledEntity);
        TransformComponent bulletPosition = bullet.getComponent(TransformComponent.class);
        bulletPosition.position.set(playerPosition.position);
        MovementComponent bulletMovement = bullet.getComponent(MovementComponent.class);
        bulletMovement.velocity.set(playerPosition.rotation).setLength(BULLET_SPEED);
        engine.addEntity(bullet);
    }
}
