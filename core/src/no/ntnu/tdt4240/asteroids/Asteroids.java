package no.ntnu.tdt4240.asteroids;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.VelocityComponent;
import no.ntnu.tdt4240.asteroids.entity.system.MovementSystem;
import no.ntnu.tdt4240.asteroids.entity.system.RenderSystem;

public class Asteroids extends ApplicationAdapter {

    PooledEngine engine;
    private OrthographicCamera camera;

    @Override
    public void create() {
        camera = new OrthographicCamera(640, 480);
        initEcs();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        engine.update(Gdx.graphics.getDeltaTime());
    }

    private void initEcs() {
        Texture texture = new Texture("badlogic.jpg");
        engine = new PooledEngine();
        engine.addSystem(new RenderSystem(camera));
        engine.addSystem(new MovementSystem());

        Entity player = engine.createEntity();
        player.add(new PositionComponent(50, 50));
        player.add(new VelocityComponent(5, 5));
        player.add(new DrawableComponent(new TextureRegion(texture)));
        engine.addEntity(player);
    }
}
