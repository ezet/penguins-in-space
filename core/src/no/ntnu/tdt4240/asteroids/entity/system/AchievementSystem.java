package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import java.util.Objects;

import no.ntnu.tdt4240.asteroids.entity.component.AchievementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.IdComponent;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.achievementMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.idMapper;

public class AchievementSystem extends EntitySystem implements DamageSystem.IDamageHandler {

    public static final Family FAMILY = Family.all(IdComponent.class, AchievementComponent.class).get();
    private INetworkService networkService;
    private ImmutableArray<Entity> players;

    public AchievementSystem() {
        networkService = ServiceLocator.getAppComponent().getNetworkService();
    }

    @Override
    public void addedToEngine(Engine engine) {
        engine.getSystem(DamageSystem.class).getDamageListeners().add(this);
        players = engine.getEntitiesFor(FAMILY);
    }

    @Override
    public void onDamageTaken(Engine engine, Entity entity, Entity source, int damageTaken) {

    }

    @Override
    public void onEntityDestroyed(Engine engine, Entity entity, Entity source) {
        IdComponent sourceId = idMapper.get(source);
        if (sourceId == null) return;
        AchievementComponent component = null;
        for (Entity player : players) {
            IdComponent playerId = idMapper.get(player);
            if (Objects.equals(sourceId.participantId, playerId.participantId)) {
                component = achievementMapper.get(player);
                break;
            }
        }
        if (component == null) return;
        component.kills++;
        if (component.kills > 100) return;
        if (component.kills == 100)
            networkService.unlockAchievement(INetworkService.ACHIEVEMENT_KILL_100_ENEMIES);

        if (component.kills > 15) return;
        if (component.kills == 15)
            networkService.unlockAchievement(INetworkService.ACHIEVEMENT_KILL_15_ENEMIES);

        if (component.kills > 10) return;
        if (component.kills == 10)
            networkService.unlockAchievement(INetworkService.ACHIEVEMENT_KILL_10_ENEMIES);

        if (component.kills > 5) return;
        if (component.kills == 5)
            networkService.unlockAchievement(INetworkService.ACHIEVEMENT_KILL_5_ENEMIES);
    }
}
