package no.ntnu.tdt4240.asteroids.game.shothandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import javax.inject.Inject;

import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioManager;

public class MultiShotHandler implements IShotHandler {

    private int BULLET_SPEED = 800;

    private int numBullets = 3;

    private int spread = 5;

    private int fireDelay = 100 * numBullets;

    private long lastShot;

    private AudioManager audioManager = ServiceLocator.getAppComponent().getAudioManager();

    @Inject
    EntityFactory entityFactory = ServiceLocator.getEntityComponent().getEntityFactory();

    public MultiShotHandler(int bulletSpeed, int numBullets, int spread) {
        this.BULLET_SPEED = bulletSpeed;
        this.numBullets = numBullets;
        this.spread = spread;
        fireDelay = 100 * numBullets;
    }

    @Override
    public void fire(PooledEngine engine, Entity controlledEntity) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis < lastShot + fireDelay) {

            return;
        }
        audioManager.playShoot();
        lastShot = currentTimeMillis;

        for (int i = 0; i < numBullets; ++i) {
            Entity bullet = entityFactory.createBullet(controlledEntity.getComponent(PlayerClass.class).participantId);
            TransformComponent playerPosition = ComponentMappers.transformMapper.get(controlledEntity);
            TransformComponent bulletPosition = bullet.getComponent(TransformComponent.class);
            bulletPosition.position.set(playerPosition.position);
            MovementComponent bulletMovement = bullet.getComponent(MovementComponent.class);
            float rotation = spread * numBullets / 2 - i * spread;
            bulletMovement.velocity.set(playerPosition.rotation).rotate(rotation).setLength(BULLET_SPEED);
            engine.addEntity(bullet);

        }

    }
}
