package no.ntnu.tdt4240.asteroids.game.shothandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.AssetLoader;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;

public class MultiShotHandler implements IShotHandler {

    private int BULLET_SPEED = 800;

    private int numBullets = 3;

    private int spread = 5;

    private int fireDelay = 100 * numBullets;

    private long lastShot;

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
        AssetLoader.shot.play();
        lastShot = currentTimeMillis;

        EntityFactory factory = EntityFactory.getInstance();
        for (int i = 0; i < numBullets; ++i) {
            Entity bullet = factory.createPlayerBullet();
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
