package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.entity.system.AnimationSystem;
import no.ntnu.tdt4240.asteroids.entity.system.BoundarySystem;
import no.ntnu.tdt4240.asteroids.entity.system.NetworkSystem;
import no.ntnu.tdt4240.asteroids.entity.system.RenderSystem;
import no.ntnu.tdt4240.asteroids.game.World;
import no.ntnu.tdt4240.asteroids.input.ControllerInputHandler;
import no.ntnu.tdt4240.asteroids.model.PlayerData;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;
import no.ntnu.tdt4240.asteroids.view.GameView;
import no.ntnu.tdt4240.asteroids.view.widget.GamepadController;

public class MultiplayerGame extends ScreenAdapter implements World.IGameListener, IGameController, INetworkService.INetworkListener {

    @SuppressWarnings("unused")
    private static final String TAG = MultiplayerGame.class.getSimpleName();
    private static final boolean DEBUG = false;
    private final Asteroids game;
    private final PooledEngine engine;
    private IGameView view;
    private World world;
    private Screen parent;
    private String playerId;

    public MultiplayerGame(Asteroids game, Screen parent) {
        this.parent = parent;
        this.game = game;
        ServiceLocator.getAppComponent().getNetworkService().setNetworkListener(this);
        engine = new PooledEngine();
        ServiceLocator.initializeMultiPlayerEntityComponent(engine);
        setupEngine(game.getBatch());
        world = setupWorld(engine);
        view = setupView(engine, world);
        world.run();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        engine.getSystem(RenderSystem.class).resize(width, height);
        view.resize(width, height);
    }

    private IGameView setupView(PooledEngine engine, World world) {
        ControllerInputHandler controllerInputHandler = new ControllerInputHandler(engine);
        controllerInputHandler.setControlledEntity(world.getPlayer());
        view = new GameView(game.getBatch(), this);
        view.setDebug(DEBUG);
        view.setInputController(new GamepadController(controllerInputHandler));
        return view;
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(view.getInputProcessor());

    }

    @Override
    public void hide() {
        super.hide();
        Gdx.input.setInputProcessor(null);

    }

    private World setupWorld(PooledEngine engine) {
        world = new World(engine);
        world.listeners.add(this);
        world.initialize();
        return world;
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void update(float delta) {
        if (delta > 0.1f) delta = 0.1f;
        world.update(delta);
        view.update(delta);
    }

    private void draw() {
        view.draw();
    }

    private void setupEngine(SpriteBatch batch) {
        RenderSystem renderSystem = new RenderSystem(batch);
        renderSystem.setDebug(DEBUG);
        engine.addSystem(renderSystem);
        engine.addSystem(new BoundarySystem(Asteroids.VIRTUAL_WIDTH, Asteroids.VIRTUAL_HEIGHT));
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new NetworkSystem(ServiceLocator.getAppComponent().getNetworkService()));
    }

    @Override
    public void handle(World model, int event) {
        switch (event) {
            case World.EVENT_LEVEL_COMPLETE: {
                onLevelComplete();
                break;
            }
            case World.EVENT_GAME_OVER: {
                onGameOver();
                break;
            }
        }
    }

    @Override
    public void notifyScoreChanged(String id, int oldScore, int score) {
        if (Objects.equals(id, playerId)) {
            view.updateScore(score);
        }
        // TODO: 05-Apr-17 handle opponent scores
    }

    private void onGameOver() {
        world.stop();
        game.setScreen(new ScoreScreenController(game, parent, getPlayersAndScores()));
    }

    private List<String> getPlayersAndScores() {
        ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(PlayerClass.class).get());
        List<String> playersAndScores = new ArrayList<>();
        for (Entity e : entities) {
            PlayerClass playerComponent = e.getComponent(PlayerClass.class);
            // TODO: 05-Apr-17 get real scores
//            playersAndScores.add(playerComponent.id + " " + String.valueOf(playerComponent.getScore()));
            playersAndScores.add(playerComponent.displayName + " " + 0);
        }
        Collections.sort(playersAndScores, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                int sScore, tScore;
                sScore = Integer.parseInt(s.split(" ")[1]);
                tScore = Integer.parseInt(t1.split(" ")[1]);
                // Opposite to get descending
                if (sScore == tScore) {
                    return 0;
                } else if (sScore > tScore) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return playersAndScores;
    }

    private void onLevelComplete() {
        world.stop();
        // TODO: show level complete screen
        // TODO: update world, reinitialize
        view.updateLevel(world.getLevel());
        world.run();
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
            if (!player.isSelf) {
                world.addMultiplayer(player.playerId, player.displayName);
            } else {
                playerId = player.playerId;
                world.addPlayerData(player.playerId, player.displayName);
            }
        }
    }

    @Override
    public void onPause() {
        world.pause();
    }

    @Override
    public void onResume() {
        // Update the player's texture,
        // might want to update more things once settings consists of more options.
        world.getPlayer().getComponent(DrawableComponent.class).texture
                = new TextureRegion(ServiceLocator.getAppComponent().getAssetLoader().getPlayer());
        world.run();
    }

    @Override
    public void onQuitLevel() {
        game.setScreen(parent);
    }

    @Override
    public void onQuit() {
        Gdx.app.exit();
    }

    @Override
    public void onSettings() {
        game.setScreen(new SettingsController(game, this));
    }

}
