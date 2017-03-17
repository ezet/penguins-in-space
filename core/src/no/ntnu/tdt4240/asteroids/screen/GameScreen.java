package no.ntnu.tdt4240.asteroids.screen;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.entity.GameModel;
import no.ntnu.tdt4240.asteroids.entity.system.AnimationSystem;
import no.ntnu.tdt4240.asteroids.entity.system.BoundarySystem;
import no.ntnu.tdt4240.asteroids.entity.system.RenderSystem;
import no.ntnu.tdt4240.asteroids.entity.util.DefaultDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.entity.util.IDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.input.InputHandler;
import no.ntnu.tdt4240.asteroids.stage.GameScreenStage;
import no.ntnu.tdt4240.asteroids.stage.component.GamepadController;

class GameScreen extends ScreenAdapter implements GameModel.IGameListener {

    @SuppressWarnings("unused")
    private static final String TAG = GameScreen.class.getSimpleName();

    private final Asteroids game;
    private GameScreenStage stage;
    private GameModel world;


    GameScreen(Asteroids game) {
        this.game = game;
        PooledEngine engine = initEngine(game.getBatch());

        // TODO: get factory from config
        IDrawableComponentFactory drawableComponentFactory = new DefaultDrawableComponentFactory(engine);
        EntityFactory.initialize(engine, drawableComponentFactory);

        // TODO: figure out camera/viewport/stage stuff
        world = initWorld(engine);
        stage = initStage(engine, world);
        world.run();
    }

    private GameScreenStage initStage(PooledEngine engine, GameModel world) {
        stage = new GameScreenStage(game.getBatch());
        InputHandler inputHandler = new InputHandler(engine);
        Gdx.input.setInputProcessor(stage);
        stage.setInputController(new GamepadController(inputHandler));
        inputHandler.setControlledEntity(world.getPlayer());
        return stage;
    }

    private GameModel initWorld(PooledEngine engine) {
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
        stage.act(delta);
    }

    private void draw() {
        stage.draw();
    }

    private PooledEngine initEngine(SpriteBatch batch) {
        PooledEngine engine = new PooledEngine();
        engine.addSystem(new RenderSystem(batch));
        engine.addSystem(new BoundarySystem(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
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
        stage.setLevel(world.getLevel());
        world.run();
    }

    private void onUpdateScore() {
        stage.setScore(world.getScore());
    }
}
