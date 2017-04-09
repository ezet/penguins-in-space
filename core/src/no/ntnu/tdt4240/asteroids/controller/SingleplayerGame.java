package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Screen;

import java.util.Collections;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.game.World;
import no.ntnu.tdt4240.asteroids.model.PlayerData;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.playerMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.scoreMapper;

class SingleplayerGame extends BaseGameController implements World.IGameListener, IGameController, INetworkService.IScoreCallback {

    @SuppressWarnings("unused")
    protected static final String TAG = SingleplayerGame.class.getSimpleName();

    SingleplayerGame(Asteroids game, Screen parent) {
        super(game, parent);
    }

    protected void initializeEntityComponent(PooledEngine engine) {
        ServiceLocator.initializeSinglePlayerEntityComponent(engine);
    }

    @Override
    public void notifyPlayerRemoved(Entity entity) {
        super.notifyPlayerRemoved(entity);
        final int score = scoreMapper.get(entity).score;
        final String participantId = playerMapper.get(entity).participantId;
        players.get(participantId).totalScore = score;
        ServiceLocator.getAppComponent().getNetworkService().submitScoreWithResult(score, this);
    }

    @Override
    protected void setupWorld() {
        super.setupWorld();
        String displayName = ServiceLocator.getAppComponent().getNetworkService().getDisplayName();
        PlayerData data = new PlayerData("", displayName, true);
        addPlayers(Collections.singletonList(data), false);
    }

    @Override
    public void onScoreResult(boolean allTimeBest, boolean weeklyBest, boolean dailyBest) {
        players.get(playerParticipantId).alltimeBest = allTimeBest;
        players.get(playerParticipantId).weeklyBest = weeklyBest;
        players.get(playerParticipantId).dailyBest = dailyBest;
        onGameEnd();
    }
}
