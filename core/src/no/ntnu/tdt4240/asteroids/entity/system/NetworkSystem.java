package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import java.math.BigInteger;

import no.ntnu.tdt4240.asteroids.entity.component.NetworkSyncComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.transformMapper;


public class NetworkSystem extends IteratingSystem {

    private final INetworkService networkService;

    public NetworkSystem(INetworkService networkService) {
        //noinspection unchecked
        super(Family.all(NetworkSyncComponent.class, TransformComponent.class).get());
        this.networkService = networkService;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // TODO: send position and other relevant data
        TransformComponent transformComponent = transformMapper.get(entity);
        byte[] bytes = BigInteger.valueOf((long) transformComponent.position.x).toByteArray();
        networkService.sendUnreliableMessageToOthers(bytes);
    }
}
