package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import java.math.BigInteger;

import no.ntnu.tdt4240.asteroids.entity.component.NetworkSyncComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.positionMapper;


public class NetworkSystem extends IteratingSystem {

    private final INetworkService networkService;

    public NetworkSystem(INetworkService networkService) {
        //noinspection unchecked
        super(Family.all(NetworkSyncComponent.class, PositionComponent.class).get());
        this.networkService = networkService;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // TODO: send position and other relevant data
        PositionComponent positionComponent = positionMapper.get(entity);
        byte[] bytes = BigInteger.valueOf((long) positionComponent.position.x).toByteArray();
        networkService.sendUnreliableMessageToOthers(bytes);
    }
}
