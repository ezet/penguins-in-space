package no.ntnu.tdt4240.asteroids.game.shothandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.entity.component.IdComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.service.Assets;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.idMapper;

public class BombShotHandler extends BaseShotHandler {

    private static final int FIRE_DELAY = 800;

    private EntityFactory factory = ServiceLocator.getEntityComponent().getEntityFactory();

    public BombShotHandler() {
        super(FIRE_DELAY);
    }

    @Override
    protected String getSound() {
        return Assets.SoundAsset.BOMB_DROP;
    }

    @Override
    public void handle(PooledEngine engine, Entity controlledEntity) {
        IdComponent idComponent = idMapper.get(controlledEntity);
        Entity bullet = factory.createBomb(idComponent.participantId);
        TransformComponent playerPosition = ComponentMappers.transformMapper.get(controlledEntity);
        TransformComponent bulletPosition = bullet.getComponent(TransformComponent.class);
        bulletPosition.position.set(playerPosition.position);
        engine.addEntity(bullet);
    }
}
