package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.entity.system.NetworkSystem;
import no.ntnu.tdt4240.asteroids.game.World;
import no.ntnu.tdt4240.asteroids.model.PlayerData;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

public class MultiplayerGame extends BaseGameController implements World.IGameListener, IGameController, INetworkService.INetworkListener {

    @SuppressWarnings("unused")
    private static final String TAG = MultiplayerGame.class.getSimpleName();
    private String playerId;

    public MultiplayerGame(Asteroids game, Screen parent) {
        super(game, parent);
        ServiceLocator.getAppComponent().getNetworkService().setNetworkListener(this);
    }

    @Override
    protected void initializeEntityComponent(PooledEngine engine) {
        ServiceLocator.initializeMultiPlayerEntityComponent(engine);
    }

    @Override
    protected void setupEngine(PooledEngine engine, SpriteBatch batch) {
        super.setupEngine(engine, batch);
        engine.addSystem(new NetworkSystem(ServiceLocator.getAppComponent().getNetworkService()));
    }

    @Override
    public void notifyScoreChanged(String id, int oldScore, int score) {
        return;
        //        if (Objects.equals(id, playerId)) {
//            view.updateScore(score);
//        }
        // TODO: 05-Apr-17 handle opponent scores
    }

    @Override
    public void onReliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData) {
        Gdx.app.debug(TAG, "onReliableMessageReceived: " + senderParticipantId + "," + describeContents);
    }

    @Override
    public void onUnreliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData) {
        engine.getSystem(NetworkSystem.class).processPackage(senderParticipantId, messageData);
    }

    @Override
    public void onRoomReady(List<PlayerData> players) {
        Gdx.app.debug(TAG, "onRoomReady: ");
        for (PlayerData player : players) {
            world.addPlayer(player);
        }
    }
}
