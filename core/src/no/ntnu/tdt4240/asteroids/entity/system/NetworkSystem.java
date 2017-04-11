package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;

import java.nio.ByteBuffer;
import java.util.Objects;

import no.ntnu.tdt4240.asteroids.entity.component.IdComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.NetworkAddComponent;
import no.ntnu.tdt4240.asteroids.entity.component.NetworkSyncComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.bulletMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.idMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.movementMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.obstacleMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.transformMapper;


public class NetworkSystem extends IteratingSystem implements EntityListener {

    private static final Family FAMILY = Family.all(NetworkSyncComponent.class, TransformComponent.class, MovementComponent.class).get();
    private static final String TAG = NetworkSystem.class.getSimpleName();
    private static final byte MOVE = 1;
    private static final byte BULLET = 2;
    private static final byte OBSTACLE = 3;
    private INetworkService networkService;
    private EntityFactory entityFactory;
    private ImmutableArray<Entity> syncedEntities;
    private ImmutableArray<Entity> player;

    public NetworkSystem(INetworkService networkService) {
        super(FAMILY);
        this.networkService = networkService;
        this.entityFactory = ServiceLocator.getEntityComponent().getEntityFactory();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        syncedEntities = engine.getEntitiesFor(Family.all(PlayerClass.class).get());
        engine.addEntityListener(Family.all(NetworkAddComponent.class).get(), this);
        player = engine.getEntitiesFor(Family.all(NetworkSyncComponent.class, PlayerClass.class, IdComponent.class).get());

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
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

    public void processPackage(String playerId, byte[] messageData) {
        ByteBuffer buffer = ByteBuffer.wrap(messageData);
        byte packetType = buffer.get();
        switch (packetType) {
            case MOVE:
                updateEntity(playerId, buffer);
                break;
            case BULLET:
                receiveBullet(playerId, buffer);
                break;
            case OBSTACLE:
                receiveObstacle(buffer);
            default:
                Gdx.app.debug(TAG, "processPackage: DEFAULT");
        }
    }

    private void updateEntity(String participantId, ByteBuffer wrap) {
        Entity entity = null;
        for (Entity syncedEntity : syncedEntities) {
            if (idMapper.get(syncedEntity).participantId.equals(participantId)) {
                entity = syncedEntity;
                break;
            }
        }
        if (entity == null) {
            Gdx.app.debug(TAG, "updateEntity: NULL");
            return;
        }
        TransformComponent transformComponent = transformMapper.get(entity);
        MovementComponent movement = movementMapper.get(entity);
        if (transformComponent == null || movement == null) return;
        transformComponent.position.x = wrap.getFloat();
        transformComponent.position.y = wrap.getFloat();
        transformComponent.rotation.x = wrap.getFloat();
        transformComponent.rotation.y = wrap.getFloat();
        movement.velocity.x = wrap.getFloat();
        movement.velocity.y = wrap.getFloat();
        movement.acceleration.x = wrap.getFloat();
        movement.acceleration.y = wrap.getFloat();
    }


    @Override
    public void entityAdded(Entity entity) {
        if (bulletMapper.has(entity)) {
            sendBullet(entity);
        } else if (obstacleMapper.has(entity)) {
            sendObstacle(entity);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {

    }

    private void sendObstacle(Entity entity) {
        TransformComponent transform = transformMapper.get(entity);
        MovementComponent movement = movementMapper.get(entity);
        ByteBuffer buffer = ByteBuffer.allocate(4 * 4 + 1);
        buffer.put(OBSTACLE);
        buffer.putFloat(transform.position.x);
        buffer.putFloat(transform.position.y);
        buffer.putFloat(movement.velocity.x);
        buffer.putFloat(movement.velocity.y);
        networkService.sendUnreliableMessageToOthers(buffer.array());
    }

    private void receiveObstacle(ByteBuffer buffer) {
        Entity entity = entityFactory.createObstacle();
        entity.remove(NetworkAddComponent.class);
        TransformComponent transform = transformMapper.get(entity);
        MovementComponent movement = movementMapper.get(entity);
        transform.position.x = buffer.getFloat();
        transform.position.y = buffer.getFloat();
        movement.velocity.x = buffer.getFloat();
        movement.velocity.y = buffer.getFloat();
        getEngine().addEntity(entity);
    }


    private void sendBullet(Entity entity) {
//        Gdx.app.debug(TAG, "sendBullet: ");
        IdComponent bulletId = idMapper.get(entity);
        IdComponent playerId = idMapper.get(player.first());
        if (!Objects.equals(bulletId.participantId, playerId.participantId)) {
            return;
        }
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

    private void receiveBullet(String playerId, ByteBuffer buffer) {
//        Gdx.app.debug(TAG, "receiveBullet: ");
        Entity entity = entityFactory.createBullet(playerId);
        entity.remove(NetworkAddComponent.class);
        TransformComponent transform = transformMapper.get(entity);
        MovementComponent movement = movementMapper.get(entity);
        transform.position.x = buffer.getFloat();
        transform.position.y = buffer.getFloat();
        movement.velocity.x = buffer.getFloat();
        movement.velocity.y = buffer.getFloat();
        getEngine().addEntity(entity);
    }

}
