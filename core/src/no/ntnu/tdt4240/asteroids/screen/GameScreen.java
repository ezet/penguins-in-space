package no.ntnu.tdt4240.asteroids.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.entity.World;
import no.ntnu.tdt4240.asteroids.entity.system.AnimationSystem;
import no.ntnu.tdt4240.asteroids.entity.system.BoundarySystem;
import no.ntnu.tdt4240.asteroids.entity.system.BoundsSystem;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;
import no.ntnu.tdt4240.asteroids.entity.system.GravitySystem;
import no.ntnu.tdt4240.asteroids.entity.system.MovementSystem;
import no.ntnu.tdt4240.asteroids.entity.system.RenderSystem;
import no.ntnu.tdt4240.asteroids.entity.util.DefaultDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.entity.util.IDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.input.InputHandler;
import no.ntnu.tdt4240.asteroids.stage.GameScreenStage;
import no.ntnu.tdt4240.asteroids.stage.component.GamepadController;

class GameScreen extends ScreenAdapter {
    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_GAME_OVER = 4;

    @SuppressWarnings("unused")
    private static final String TAG = GameScreen.class.getSimpleName();

    private final Asteroids game;
    private final GameScreenStage stage;
    private final PooledEngine engine;
    Entity player;
    private World world;
    private int state = 0;


    GameScreen(Asteroids game) {
        this.game = game;
        engine = new PooledEngine();
        // TODO: get factory from config
        IDrawableComponentFactory drawableComponentFactory = new DefaultDrawableComponentFactory(engine);

        EntityFactory.initialize(engine, drawableComponentFactory);

        // TODO: figure out camera/viewport/stage stuff

        stage = new GameScreenStage(game.getBatch());
        InputHandler inputHandler = new InputHandler(engine);
        Gdx.input.setInputProcessor(stage);
        stage.setInputController(new GamepadController(inputHandler));
        initController();

        world = new World(engine);
        world.create();

        inputHandler.setControlledEntity(world.getPlayer());

        initEngine(engine, game.getBatch());
        resumeGame();
    }

    private void initController() {
        // TODO: get input controller from config
    }

    private void resumeGame() {
        state = GAME_RUNNING;

    }

    private void pauseGame() {
        state = GAME_PAUSED;
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void update(float delta) {
        if (delta > 0.1f) delta = 0.1f;
        if (state == GAME_RUNNING) engine.update(delta);
        stage.act();
        // TODO: update everything else
    }

    private void draw() {
        switch (state) {
            // TODO: draw GUI based on state
            default:
                stage.draw();
        }
    }

    void initEngine(final PooledEngine engine, SpriteBatch batch) {
        engine.addSystem(new RenderSystem(batch));
        engine.addSystem(new GravitySystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new BoundarySystem(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new AnimationSystem());
    }

}
