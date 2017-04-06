package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

import no.ntnu.tdt4240.asteroids.entity.component.BulletClass;
import no.ntnu.tdt4240.asteroids.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.entity.component.ScoreComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.bulletMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.playerMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.scoreMapper;

public class ScoreSystem extends EntitySystem implements DamageSystem.IDamageHandler {


    private static final int SCORE_INCREASE = 1;
    private final DamageSystem damageSystem;
    private final Array<IScoreListener> listeners = new Array<>();
    private ImmutableArray<Entity> players;

    public ScoreSystem(DamageSystem damageSystem) {
        this.damageSystem = damageSystem;
    }

    public Array<IScoreListener> getListeners() {
        return listeners;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        damageSystem.getDamageListeners().add(this);
        players = engine.getEntitiesFor(Family.all(PlayerClass.class).get());

    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        damageSystem.getDamageListeners().removeValue(this, true);
    }

    @Override
    public void onDamageTaken(Engine engine, Entity entity, int damageTaken) {
    }

    @Override
    public void onEntityDestroyed(Engine engine, Entity source, Entity target) {
        BulletClass bulletClass = bulletMapper.get(source);
        // TODO: 06-Apr-17 Improve how we find the source
        if (bulletClass != null) {
            for (Entity player : players) {
                PlayerClass playerClass = playerMapper.get(player);
                if (Objects.equals(playerClass.id, bulletClass.id)) {
                    if (player.isScheduledForRemoval()) return;
                    ScoreComponent scoreComponent = scoreMapper.get(player);
                    int oldScore = scoreComponent.score;
                    scoreComponent.score += SCORE_INCREASE;
                    notifyListeners(engine, player, oldScore);
                    return;
                }
            }
        }
        // TODO: 05-Apr-17 calculate rounds won
    }

    private void notifyListeners(Engine engine, Entity source, int oldScore) {
        for (IScoreListener listener : listeners) {
            listener.onScoreChanged(engine, source, oldScore);
        }
    }

    public interface IScoreListener {

        void onScoreChanged(Engine engine, Entity entity, int scoreChange);
    }
}
