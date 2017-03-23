package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.entity.system.AnimationSystem;
import no.ntnu.tdt4240.asteroids.entity.system.BoundarySystem;
import no.ntnu.tdt4240.asteroids.entity.system.RenderSystem;
import no.ntnu.tdt4240.asteroids.entity.util.DefaultDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.entity.util.IDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.game.GameModel;
import no.ntnu.tdt4240.asteroids.input.ControllerInputHandler;
import no.ntnu.tdt4240.asteroids.view.GameScreenStage;
import no.ntnu.tdt4240.asteroids.view.IGameScreenView;
import no.ntnu.tdt4240.asteroids.view.widget.GamepadController;

public class GameScreen extends ScreenAdapter implements GameModel.IGameListener {

    @SuppressWarnings("unused")
    private static final String TAG = GameScreen.class.getSimpleName();
    private static final boolean DEBUG = true;
    private final Asteroids game;
    private final PooledEngine engine;
    private IGameScreenView view;
    private GameModel world;


    GameScreen(Asteroids game) {
        this.game = game;
        engine = setupEngine(game.getBatch());

        // TODO: get factory from config
        IDrawableComponentFactory drawableComponentFactory = new DefaultDrawableComponentFactory(engine);
        EntityFactory.initialize(engine, drawableComponentFactory);

        // TODO: figure out camera/viewport/stage stuff
        world = setupModel(engine);
        view = setupView(engine, world);
        world.run();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        engine.getSystem(RenderSystem.class).resize(width, height);
        view.resize(width, height);

    }

    private IGameScreenView setupView(PooledEngine engine, GameModel world) {
        ControllerInputHandler controllerInputHandler = new ControllerInputHandler(engine);
        controllerInputHandler.setControlledEntity(world.getPlayer());
        view = new GameScreenStage(game.getBatch(), new InputHandler());
        view.setInputController(new GamepadController(controllerInputHandler));
        Gdx.input.setInputProcessor(view.getInputProcessor());
        return view;
    }

    private GameModel setupModel(PooledEngine engine) {
        world = new GameModel(engine);
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

    private PooledEngine setupEngine(SpriteBatch batch) {
        PooledEngine engine = new PooledEngine();
        RenderSystem renderSystem = new RenderSystem(batch);
        renderSystem.setDebug(DEBUG);
        engine.addSystem(renderSystem);
        engine.addSystem(new BoundarySystem(Asteroids.VIRTUAL_WIDTH, Asteroids.VIRTUAL_HEIGHT));
        engine.addSystem(new AnimationSystem());

        return engine;
    }

    @Override
    public void update(GameModel model, int event) {
        switch (event) {
            case GameModel.EVENT_SCORE: {
                onUpdateScore();
                break;
            }
            case GameModel.EVENT_LEVEL_COMPLETE: {
                onLevelComplete();
                break;
            }
            case GameModel.EVENT_GAME_OVER: {
                onGameOver();
                break;
            }
        }
    }

    private void onGameOver() {
        world.stop();
        // TODO: show game over screen
//        stage.setScore(0);
//        world.initialize();
//        world.run();
        game.setScreen(new MainScreen(game));
    }

    private void onLevelComplete() {
        world.stop();
        // TODO: show level complete screen
        // TODO: update world, reinitialize
        view.updateLevel(world.getLevel());
        world.run();
    }

    private void onUpdateScore() {
        view.updateScore(world.getScore());
    }

    public class InputHandler {

        InputHandler() {
        }

        public void onPause() {
            world.pause();
        }

        public void onResume() {
            world.run();
        }

        public void onQuitLevel() {

        }

        public void onQuit() {

        }

    }

}
