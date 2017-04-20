package no.ntnu.tdt4240.asteroids.presenter;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;

import java.util.Collections;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.game.World;
import no.ntnu.tdt4240.asteroids.model.PlayerData;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.idMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.scoreMapper;

class SpGamePresenter extends BaseGamePresenter implements World.IGameListener, INetworkService.IScoreCallback {

    @SuppressWarnings("unused")
    protected static final String TAG = SpGamePresenter.class.getSimpleName();

    SpGamePresenter(Asteroids game, Screen parent) {
        super(game, parent);
    }

    protected void initializeEntityComponent(PooledEngine engine) {
        ServiceLocator.initializeSinglePlayerEntityComponent(engine);
    }

    @Override
    protected void setupWorld() {
        super.setupWorld();
        String displayName = ServiceLocator.getAppComponent().getNetworkService().getDisplayName();
        PlayerData data = new PlayerData("", displayName, true);
        addPlayers(Collections.singletonList(data), false);
        world.initialize();
    }

    @Override
    public void notifyPlayerRemoved(Entity entity) {
        super.notifyPlayerRemoved(entity);
        final int score = scoreMapper.get(entity).score;
        final String participantId = idMapper.get(entity).participantId;
        players.get(participantId).totalScore = score;
        ServiceLocator.getAppComponent().getNetworkService().submitScoreWithResult(score, this);
//        ServiceLocator.getAppComponent().getNetworkService().submitScore(score);
//        onGameEnd();
    }

    @Override
    public void onScoreResult(boolean allTimeBest, boolean weeklyBest, boolean dailyBest) {
        players.get(playerParticipantId).alltimeBest = allTimeBest;
        players.get(playerParticipantId).weeklyBest = weeklyBest;
        players.get(playerParticipantId).dailyBest = dailyBest;
        onGameEnd();
    }
}
