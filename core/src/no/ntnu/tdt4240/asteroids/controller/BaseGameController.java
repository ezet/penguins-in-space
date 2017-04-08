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

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.HealthComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.entity.component.ScoreComponent;
import no.ntnu.tdt4240.asteroids.entity.system.AnimationSystem;
import no.ntnu.tdt4240.asteroids.entity.system.BoundarySystem;
import no.ntnu.tdt4240.asteroids.entity.system.RenderSystem;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;
import no.ntnu.tdt4240.asteroids.game.World;
import no.ntnu.tdt4240.asteroids.input.ControllerInputHandler;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.view.GameView;
import no.ntnu.tdt4240.asteroids.view.widget.GamepadController;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.playerMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.scoreMapper;
import static no.ntnu.tdt4240.asteroids.service.Assets.TextureAsset.PLAYER_DEFAULT;

abstract class BaseGameController extends ScreenAdapter implements World.IGameListener, IGameController {

    @SuppressWarnings("unused")
    protected static final String TAG = BaseGameController.class.getSimpleName();
    protected final boolean DEBUG = false;
    protected final Asteroids game;
    protected final PooledEngine engine;
    final ControllerInputHandler controllerInputHandler;
    protected IGameView view;
    World world;
    private Screen parent;


    BaseGameController(Asteroids game, Screen parent) {
        this.parent = parent;
        this.game = game;
        engine = new PooledEngine();
        initializeEntityComponent(engine);
        setupEngine(engine, game.getBatch());
        controllerInputHandler = new ControllerInputHandler(engine);
        setupView();

        world = new World(engine);
        setupWorld();
        world.run();
    }

    protected abstract void initializeEntityComponent(PooledEngine engine);

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        engine.getSystem(RenderSystem.class).resize(width, height);
        view.resize(width, height);
    }

    private void setupView() {
        view = new GameView(game.getBatch(), this);
        view.setInputController(new GamepadController(controllerInputHandler));
        view.setDebug(DEBUG);
    }

    @Override
    public final void show() {
        Gdx.input.setInputProcessor(view.getInputProcessor());
        Gdx.app.debug(TAG, "show: ");
        super.show();
    }

    @Override
    public void resume() {
        Gdx.app.debug(TAG, "resume: ");
        super.resume();
//        view.resume();
    }

    @Override
    public void pause() {
        Gdx.app.debug(TAG, "pause: ");
        super.pause();
//        view.hide();
    }

    @Override
    public final void hide() {
        Gdx.input.setInputProcessor(null);
        Gdx.app.debug(TAG, "hide: ");
        super.hide();
//        view.hide();
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "dispose: ");
        super.dispose();
    }

    protected void setupWorld() {
        world = new World(engine);
        world.listeners.add(this);
        world.initialize();
    }

    @Override
    public final void render(float delta) {
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

    protected void setupEngine(PooledEngine engine, SpriteBatch batch) {
        RenderSystem renderSystem = new RenderSystem(batch);
        renderSystem.setDebug(DEBUG);
        engine.addSystem(renderSystem);
        engine.addSystem(new BoundarySystem(Asteroids.VIRTUAL_WIDTH, Asteroids.VIRTUAL_HEIGHT));
        engine.addSystem(new AnimationSystem());
    }

    @Override
    public void handle(World model, int event) {
        switch (event) {
            case World.EVENT_LEVEL_COMPLETE: {
                onLevelComplete();
                break;
            }
            case World.EVENT_GAME_END: {
                onGameEnd();
                break;
            }
            case World.EVENT_PLAYER_HITPOINTS: {
                updatePlayerHitpoints();
                break;
            }
            case World.EVENT_PLAYER_CHANGED: {
                controllerInputHandler.setControlledEntity(world.getPlayer());
            }
        }
    }

    @Override
    public void notifyScoreChanged(String id, int oldScore, int newScore) {
        view.updateScore(newScore);
    }

    private void updatePlayerHitpoints() {
        HealthComponent healthComponent = ComponentMappers.healthMapper.get(world.getPlayer());
        if (healthComponent != null)
            view.updateHitpoints(healthComponent.hitPoints);
    }

    protected void onGameEnd() {
        world.stop();
        game.setScreen(new ScoreScreenController(game, parent, getPlayersAndScores()));
    }

    private List<String> getPlayersAndScores() {
        ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(PlayerClass.class).get());
        List<String> playersAndScores = new ArrayList<>();
        for (Entity entity : entities) {
            PlayerClass playerComponent = playerMapper.get(entity);
            ScoreComponent scoreComponent = scoreMapper.get(entity);
            playersAndScores.add(playerComponent.displayName + " " + scoreComponent.score);
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
    public void onPause() {
        world.pause();
    }

    @Override
    public void onResume() {
        // Update the player's texture,
        // might want to update more things once settings consists of more options.
        world.getPlayer().getComponent(DrawableComponent.class).texture
                = new TextureRegion(ServiceLocator.getAppComponent().getAssetLoader().getTexture(PLAYER_DEFAULT));
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
