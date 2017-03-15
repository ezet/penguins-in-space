package no.ntnu.tdt4240.asteroids.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.entity.DefaultDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.entity.IDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.entity.component.BoundaryComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.GravityComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;
import no.ntnu.tdt4240.asteroids.entity.system.BoundarySystem;
import no.ntnu.tdt4240.asteroids.entity.system.BoundsSystem;
import no.ntnu.tdt4240.asteroids.entity.system.CollisionSystem;
import no.ntnu.tdt4240.asteroids.entity.system.GravitySystem;
import no.ntnu.tdt4240.asteroids.entity.system.MovementSystem;
import no.ntnu.tdt4240.asteroids.entity.system.ObstacleSystem;
import no.ntnu.tdt4240.asteroids.entity.system.RenderSystem;
import no.ntnu.tdt4240.asteroids.input.GamepadButtonListener;
import no.ntnu.tdt4240.asteroids.input.GamepadJoystickListener;
import no.ntnu.tdt4240.asteroids.input.InputHandler;
import no.ntnu.tdt4240.asteroids.input.VirtualGamepad;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.obstacleMapper;

class GameScreen extends ScreenAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = GameScreen.class.getSimpleName();

    private final Asteroids game;
    private final Camera guiCam;
    private final SpriteBatch batch;
    private final Stage guiStage;
    private final PooledEngine engine;
    Entity player;
    IDrawableComponentFactory drawableComponentFactory;
    private boolean running;
    private InputHandler inputHandler;
    private CollisionComponent.ICollisionHandler playerCollisionHandler = new CollisionComponent.ICollisionHandler() {
        @Override
        public void onCollision(Entity source, Entity target, Engine engine) {
            if (obstacleMapper.has(target)) {
                // TODO: We hit an obstacle, HANDLE IT
                engine.removeAllEntities();
            }
        }
    };

    GameScreen(Asteroids game) {
        this.game = game;
        engine = new PooledEngine();
        // TODO: get factory from config
        drawableComponentFactory = new DefaultDrawableComponentFactory(engine);

        // TODO: figure out camera/viewport/stage stuff
        batch = game.getBatch();

        guiCam = new OrthographicCamera();
        Viewport guiViewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), guiCam);
        guiStage = new Stage(guiViewport, batch);
        Gdx.input.setInputProcessor(guiStage);

        initEngine(engine, batch);
        initGamepad(guiStage);
        initPlayer(engine, inputHandler);
        running = true;
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void update(float delta) {
        if (delta > 0.1f) delta = 0.1f;
        if (running) engine.update(delta);
        guiStage.act();
        // TODO: update everything else
    }

    private void draw() {
        guiStage.draw();
        // TODO: draw UI
    }

    private void initGamepad(Stage guiStage) {
        inputHandler = new InputHandler(engine, drawableComponentFactory);
        VirtualGamepad gamepad = new VirtualGamepad();
        gamepad.addButtonListener(new GamepadButtonListener(inputHandler));
        gamepad.addJoystickListener(new GamepadJoystickListener(inputHandler));
        guiStage.addActor(gamepad);
    }

    void initEngine(final PooledEngine engine, SpriteBatch batch) {
        engine.addSystem(new RenderSystem(batch));
        engine.addSystem(new GravitySystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new ObstacleSystem(drawableComponentFactory));
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new BoundarySystem(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        engine.addSystem(new CollisionSystem());
        engine.addEntityListener(new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
            }

            @Override
            public void entityRemoved(Entity entity) {
                if (entity == player) initPlayer(engine, inputHandler);
            }
        });
    }

    private void initPlayer(final Engine engine, InputHandler inputHandler) {
        // TODO: player should be local var, change it when touch listener is refactored
        player = new Entity();
        player.add(new PositionComponent(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 1, 0));
        player.add(new MovementComponent());
        player.add(new GravityComponent(0.01f));
        player.add(new BoundsComponent());
        player.add(new BoundaryComponent(BoundaryComponent.MODE_FREE));
        player.add(drawableComponentFactory.getPlayer());
        player.add(new CollisionComponent(playerCollisionHandler));
        engine.addEntity(player);
        inputHandler.setControlledEntity(player);
    }
}
