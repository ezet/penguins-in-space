package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.entity.system.AnimationSystem;
import no.ntnu.tdt4240.asteroids.entity.system.BoundarySystem;
import no.ntnu.tdt4240.asteroids.entity.system.RenderSystem;
import no.ntnu.tdt4240.asteroids.game.World;
import no.ntnu.tdt4240.asteroids.input.ControllerInputHandler;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.view.GameView;
import no.ntnu.tdt4240.asteroids.view.IView;
import no.ntnu.tdt4240.asteroids.view.widget.GamepadController;

public class GameController extends ScreenAdapter implements World.IGameListener {

    @SuppressWarnings("unused")
    private static final String TAG = GameController.class.getSimpleName();
    private static final boolean DEBUG = true;
    private final Asteroids game;
    private final PooledEngine engine;
    private IGameView view;
    private World world;


    GameController(Asteroids game) {
        this.game = game;
        engine = setupEngine(game.getBatch());
        ServiceLocator.initializeEntityComponent(engine);

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

    private IGameView setupView(PooledEngine engine, World world) {
        ControllerInputHandler controllerInputHandler = new ControllerInputHandler(engine);
        controllerInputHandler.setControlledEntity(world.getPlayer());
        view = new GameView(game.getBatch(), new InputHandler());
        view.setInputController(new GamepadController(controllerInputHandler));
        Gdx.input.setInputProcessor(view.getInputProcessor());
        return view;
    }

    private World setupModel(PooledEngine engine) {
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
    public void handle(World model, int event) {
        switch (event) {
            case World.EVENT_SCORE: {
                onUpdateScore();
                break;
            }
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

    private void onGameOver() {
        world.stop();
        // TODO: show game over screen
//        stage.setScore(0);
//        world.initialize();
//        world.run();
        game.setScreen(new MainController(game));
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

    public interface IGameView extends IView {

        void setInputController(Actor inputController);

        void updateScore(int score);

        void updateLevel(int level);

        void setDebug(boolean debug);

        void resize(int width, int height);
    }

    private class InputHandler implements IGameController {

        @Override
        public void onPause() {
            world.pause();
        }

        @Override
        public void onResume() {
            world.run();
        }

        @Override
        public void onQuitLevel() {
            GameController.this.game.setScreen(new MainController(game));
        }

        @Override
        public void onQuit() {
            Gdx.app.exit();
        }

    }

}
