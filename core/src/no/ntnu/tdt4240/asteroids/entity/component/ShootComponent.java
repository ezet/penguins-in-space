package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;

public class ShootComponent implements Component {

    public IShotHandler handler;

    public void fire(PooledEngine engine, Entity controlledEntity) {
        if (handler == null) handler = new StandardShotHandler();
        handler.fire(engine, controlledEntity);
    }


    public interface IShotHandler {

        void fire(PooledEngine engine, Entity controlledEntity);

    }

    public class StandardShotHandler implements IShotHandler {

        private int BULLET_SPEED = 800;

        @Override
        public void fire(PooledEngine engine, Entity controlledEntity) {
            EntityFactory factory = EntityFactory.getInstance();
            Entity bullet = factory.createPlayerBullet();
            TransformComponent playerPosition = ComponentMappers.positionMapper.get(controlledEntity);
            TransformComponent bulletPosition = bullet.getComponent(TransformComponent.class);
            bulletPosition.position.set(playerPosition.position);
            MovementComponent bulletMovement = bullet.getComponent(MovementComponent.class);
            bulletMovement.velocity.set(playerPosition.rotation).setLength(BULLET_SPEED);
            engine.addEntity(bullet);
        }
    }


    public class MultiShotHandler implements IShotHandler {

        private int BULLET_SPEED = 800;

        private int numBullets = 3;

        private int spread = 5;

        @Override
        public void fire(PooledEngine engine, Entity controlledEntity) {
            EntityFactory factory = EntityFactory.getInstance();
            for (int i = 0; i < numBullets; ++i) {
                Entity bullet = factory.createPlayerBullet();
                TransformComponent playerPosition = ComponentMappers.positionMapper.get(controlledEntity);
                TransformComponent bulletPosition = bullet.getComponent(TransformComponent.class);
                bulletPosition.position.set(playerPosition.position);
                MovementComponent bulletMovement = bullet.getComponent(MovementComponent.class);
                float rotation = spread * numBullets / 2 - i * spread;
                bulletMovement.velocity.set(playerPosition.rotation).rotate(rotation).setLength(BULLET_SPEED);
                engine.addEntity(bullet);

            }
        }
    }


}
