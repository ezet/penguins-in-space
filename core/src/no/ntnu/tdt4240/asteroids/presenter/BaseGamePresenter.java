package no.ntnu.tdt4240.asteroids.presenter;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.game.World;
import no.ntnu.tdt4240.asteroids.game.entity.component.IdComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.game.entity.system.AchievementSystem;
import no.ntnu.tdt4240.asteroids.game.entity.system.AnimationSystem;
import no.ntnu.tdt4240.asteroids.game.entity.system.BoundarySystem;
import no.ntnu.tdt4240.asteroids.game.entity.system.RenderSystem;
import no.ntnu.tdt4240.asteroids.input.ControllerInputHandler;
import no.ntnu.tdt4240.asteroids.model.PlayerData;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.view.GameView;
import no.ntnu.tdt4240.asteroids.view.IGameView;
import no.ntnu.tdt4240.asteroids.view.widget.GamepadController;

import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.idMapper;

abstract class BaseGamePresenter extends ScreenAdapter implements World.IWorldListener, IGamePresenter {

    @SuppressWarnings("unused")
    protected static final String TAG = BaseGamePresenter.class.getSimpleName();
    protected final Asteroids game;
    protected final PooledEngine engine;
    private final boolean DEBUG = false;
    private final ControllerInputHandler controllerInputHandler;
    protected IGameView view;
    String playerParticipantId;
    HashMap<String, PlayerData> players = new HashMap<>();
    HashSet<String> remainingPlayers = new HashSet<>();
    World world;
    private Screen parent;


    BaseGamePresenter(Asteroids game, Screen parent) {
        this.parent = parent;
        this.game = game;
        engine = new PooledEngine();
        initializeEntityComponent(engine);
        controllerInputHandler = new ControllerInputHandler(engine);
        setupView();
        setupWorld();
        setupEngine(engine, game.getBatch());
        world.run();
    }

    protected abstract void initializeEntityComponent(PooledEngine engine);

    private void setupView() {
        view = new GameView(game.getBatch(), this);
        view.setInputController(new GamepadController(controllerInputHandler));
        view.setDebug(DEBUG);
    }

    protected void setupWorld() {
        world = new World(engine);
        world.addWorldListener(this);
    }

    @Override
    public final void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        engine.getSystem(RenderSystem.class).resize(width, height);
        view.resize(width, height);
    }

    @Override
    public final void show() {
//        Gdx.app.debug(TAG, "show: ");
        Gdx.input.setInputProcessor(view.getInputProcessor());
        super.show();
    }

    @Override
    public final void hide() {
//        Gdx.app.debug(TAG, "hide: ");
        Gdx.input.setInputProcessor(null);
        super.hide();
//        view.hide();
    }

    @Override
    public void pause() {
//        Gdx.app.debug(TAG, "pause: ");
        super.pause();
//        view.hide();
    }

    @Override
    public void resume() {
//        Gdx.app.debug(TAG, "resume: ");
        super.resume();
//        view.resume();
    }

    @Override
    public void dispose() {
//        Gdx.app.debug(TAG, "dispose: ");
        super.dispose();
    }

    private void update(float delta) {
        if (delta > 0.1f) delta = 0.1f;
        world.update(delta);
        view.update(delta);
    }

    private void draw() {
        view.draw();
    }

    protected void setupEngine(PooledEngine engine, SpriteBatch batch) {
        RenderSystem renderSystem = new RenderSystem(batch);
        engine.addSystem(renderSystem);
        engine.addSystem(new BoundarySystem(Asteroids.VIRTUAL_WIDTH, Asteroids.VIRTUAL_HEIGHT));
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new AchievementSystem());
    }

    @Override
    public void handle(World model, int event) {
        switch (event) {
            case World.EVENT_WORLD_RESET: {
                addPlayers(players.values(), false);
                break;
            }
        }
    }

    @Override
    public void notifyScoreChanged(Entity entity, int oldScore, int newScore) {
        view.updateScore(newScore);
    }

    @Override
    public void notifyPlayerRemoved(Entity entity) {
        IdComponent id = idMapper.get(entity);
        remainingPlayers.remove(id.participantId);
    }

    @Override
    public void notifyHealthChanged(Entity entity, int hitPoints, int damageTaken) {
        IdComponent id = idMapper.get(entity);
        if (Objects.equals(id.participantId, playerParticipantId)) {
            view.updateHitpoints(hitPoints);
        }
    }

    private void setControlledEntity(Entity entity) {
        controllerInputHandler.setControlledEntity(entity);
    }

    void addPlayers(Collection<PlayerData> data, boolean multiplayer) {
        players = new HashMap<>();
        remainingPlayers = new HashSet<>();
        ServiceLocator.getEntityComponent().getDrawableComponentFactory().resetOpponentCount();
        for (PlayerData player : data) {
            players.put(player.participantId, player);
            remainingPlayers.add(player.participantId);
            Entity entity;
            if (player.isSelf) {
                entity = ServiceLocator.entityComponent.getEntityFactory().createPlayer(player.participantId, player.displayName, multiplayer);
                playerParticipantId = player.participantId;
                setControlledEntity(entity);
            } else {
                entity = ServiceLocator.entityComponent.getEntityFactory().createOpponent(player.participantId, player.displayName);
            }
            world.addPlayer(entity);
        }
    }

    void onGameEnd() {
        world.stop();
        game.setScreen(new ScorePresenter(game, parent, new ArrayList<>(players.values())));
    }

    @Override
    public void onPause() {
        world.pause();
    }

    @Override
    public void onResume() {
        // Update the player's texture,
        // might want to update more things once settings consists of more options.
        ImmutableArray<Entity> players = engine.getEntitiesFor(Family.all(PlayerClass.class, IdComponent.class).get());
        for (Entity entity : players) {
            if (Objects.equals(idMapper.get(entity).participantId, playerParticipantId)) {
                entity.add(ServiceLocator.entityComponent.getDrawableComponentFactory().getPlayer(true));
            }
        }
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
        game.setScreen(new SettingsPresenter(game, this));
    }

}
