package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.drawableMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.positionMapper;

public class RenderSystem extends IteratingSystem {

    @SuppressWarnings("unused")
    private static final String TAG = RenderSystem.class.getSimpleName();
    private final Camera camera;
    private final SpriteBatch batch;

    public RenderSystem(SpriteBatch batch) {
        //noinspection unchecked
        super(Family.all(PositionComponent.class, DrawableComponent.class).get());
        // TODO: camera config
        this.camera = new OrthographicCamera();
        this.batch = batch;
    }

    @Override
    public void update(float deltaTime) {
        camera.update();
        batch.begin();
        super.update(deltaTime);
        batch.setProjectionMatrix(camera.combined);
        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = positionMapper.get(entity);
        DrawableComponent drawable = drawableMapper.get(entity);
        batch.draw(drawable.getRegion(), position.position.x, position.position.y);
    }
}
