package no.ntnu.tdt4240.asteroids.game.shothandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import javax.inject.Inject;

import no.ntnu.tdt4240.asteroids.entity.component.IdComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.service.Assets;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.idMapper;

public class MultiShotHandler extends BaseShotHandler {

    private final static int DEFAULT_NUM_BULLETS = 1;
    private static final int DELAY_PER_BULLET = 200;
    public static final int DEFAULT_BULLET_SPEED = 800;
    public static final int DEFAULT_BULLET_SPREAD = 5;
    private final int numBullets;
    @Inject
    EntityFactory entityFactory = ServiceLocator.getEntityComponent().getEntityFactory();
    private int bulletSpeed = DEFAULT_BULLET_SPEED;
    private int spreadDegrees = DEFAULT_BULLET_SPREAD;

    public MultiShotHandler() {
        super(DELAY_PER_BULLET * DEFAULT_NUM_BULLETS);
        numBullets = DEFAULT_NUM_BULLETS;
    }

    public MultiShotHandler(int bulletSpeed, int numBullets, int spreadDegrees) {
        super(DELAY_PER_BULLET * numBullets);
        this.bulletSpeed = bulletSpeed;
        this.numBullets = numBullets;
        this.spreadDegrees = spreadDegrees;
    }

    @Override
    protected String getSound() {
        return Assets.SoundAsset.SOUND_SHOOT_WAV;
    }

    @Override
    public void handle(PooledEngine engine, Entity controlledEntity) {
        IdComponent idComponent = idMapper.get(controlledEntity);
        for (int i = 0; i < numBullets; ++i) {
            Entity bullet = entityFactory.createBullet(idComponent.participantId);
            TransformComponent playerPosition = ComponentMappers.transformMapper.get(controlledEntity);
            TransformComponent bulletPosition = bullet.getComponent(TransformComponent.class);
            bulletPosition.position.set(playerPosition.position);
            MovementComponent bulletMovement = bullet.getComponent(MovementComponent.class);
            float rotation = spreadDegrees * numBullets / 2 - i * spreadDegrees;
            bulletMovement.velocity.set(playerPosition.rotation).rotate(rotation).setLength(bulletSpeed);
            engine.addEntity(bullet);

        }
    }
}
