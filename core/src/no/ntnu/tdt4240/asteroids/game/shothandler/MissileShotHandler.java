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

import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.idMapper;

public class MissileShotHandler extends BaseShotHandler {

    private static final int FIRE_DELAY = 300;
    private static final int BULLET_SPEED = 300;

    private EntityFactory factory = ServiceLocator.getEntityComponent().getEntityFactory();

    public MissileShotHandler() {
        super(FIRE_DELAY);
    }

    @Override
    protected String getSound() {
        return AssetService.SoundAsset.FIRE_MISSILE;
    }

    @Override
    public void handle(PooledEngine engine, Entity controlledEntity) {
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
