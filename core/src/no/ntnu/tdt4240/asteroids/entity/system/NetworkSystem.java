package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;

import java.nio.ByteBuffer;

import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.NetworkAddComponent;
import no.ntnu.tdt4240.asteroids.entity.component.NetworkSyncComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.movementMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.networkSyncMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.playerMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.transformMapper;


public class NetworkSystem extends IteratingSystem implements EntityListener {

    private static final Family FAMILY = Family.all(NetworkSyncComponent.class, TransformComponent.class, MovementComponent.class).get();
    private static final String TAG = NetworkSystem.class.getSimpleName();
    private static final byte MOVE = 1;
    private static final byte BULLET = 2;
    private static final byte OBSTACLE = 3;
    private INetworkService networkService;
    private ImmutableArray<Entity> syncEntities;
    private EntityFactory entityFactory;

    public NetworkSystem(INetworkService networkService) {
        super(FAMILY);
        this.networkService = networkService;
        this.entityFactory = ServiceLocator.getEntityComponent().getEntityFactory();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        syncEntities = engine.getEntitiesFor(Family.all(NetworkSyncComponent.class).get());
        engine.addEntityListener(Family.all(NetworkAddComponent.class).get(), this);
    }

    public void processPackage(String playerId, byte[] messageData) {
        ByteBuffer wrap = ByteBuffer.wrap(messageData);
        byte b = wrap.get();
        switch (b) {
            case MOVE:
                updateEntity(playerId, wrap);
                break;
            case BULLET:
                bullet(playerId, wrap);
                break;
            default:
                Gdx.app.debug(TAG, "processPackage: DEFAULT");
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        NetworkSyncComponent networkSyncComponent = networkSyncMapper.get(entity);
        TransformComponent transform = transformMapper.get(entity);
        MovementComponent movement = movementMapper.get(entity);
        ByteBuffer buffer = ByteBuffer.allocate(4 * 8 + 1);
        buffer.put(MOVE);
        buffer.putFloat(transform.position.x);
        buffer.putFloat(transform.position.y);
        buffer.putFloat(transform.rotation.x);
        buffer.putFloat(transform.rotation.y);
        buffer.putFloat(movement.velocity.x);
        buffer.putFloat(movement.velocity.y);
        buffer.putFloat(movement.acceleration.x);
        buffer.putFloat(movement.acceleration.y);
        networkService.sendUnreliableMessageToOthers(buffer.array());
    }

    private void updateEntity(String playerId, ByteBuffer wrap) {
        Entity entity = null;
        for (Entity player : syncEntities) {
            PlayerClass playerClass = playerMapper.get(player);
            if (playerClass.id.equals(playerId)) {
                entity = player;
                break;
            }
        }
        if (entity == null) {
            Gdx.app.debug(TAG, "updateEntity: NULL");
            return;
        }
        TransformComponent transformComponent = transformMapper.get(entity);
        MovementComponent movement = movementMapper.get(entity);
        transformComponent.position.x = wrap.getFloat();
        transformComponent.position.y = wrap.getFloat();
        transformComponent.rotation.x = wrap.getFloat();
        transformComponent.rotation.y = wrap.getFloat();
        movement.velocity.x = wrap.getFloat();
        movement.velocity.y = wrap.getFloat();
        movement.acceleration.x = wrap.getFloat();
        movement.acceleration.y = wrap.getFloat();
    }


    private void bullet(String playerId, ByteBuffer wrap) {
        Entity entity = entityFactory.createBullet(playerId);
        TransformComponent transform = transformMapper.get(entity);
        MovementComponent movement = movementMapper.get(entity);
        transform.position.x = wrap.getFloat();
        transform.position.y = wrap.getFloat();
        movement.velocity.x = wrap.getFloat();
        movement.velocity.y = wrap.getFloat();
        getEngine().addEntity(entity);
    }


    @Override
    public void entityAdded(Entity entity) {
        TransformComponent transform = transformMapper.get(entity);
        MovementComponent movement = movementMapper.get(entity);
        ByteBuffer buffer = ByteBuffer.allocate(4 * 4 + 1);
        buffer.put(BULLET);
        buffer.putFloat(transform.position.x);
        buffer.putFloat(transform.position.y);
        buffer.putFloat(movement.velocity.x);
        buffer.putFloat(movement.velocity.y);
        networkService.sendUnreliableMessageToOthers(buffer.array());
    }

    @Override
    public void entityRemoved(Entity entity) {

    }

}
