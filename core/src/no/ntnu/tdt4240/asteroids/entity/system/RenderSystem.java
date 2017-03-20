package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.drawableMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.positionMapper;

public class RenderSystem extends IteratingSystem {

    @SuppressWarnings("unused")
    private static final String TAG = RenderSystem.class.getSimpleName();
    private final Camera camera;
    private final Batch batch;

    public RenderSystem(Batch batch) {
        //noinspection unchecked
        super(Family.all(TransformComponent.class, DrawableComponent.class).get());
        // TODO: camera config
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.batch = batch;
    }

    @Override
    public void update(float deltaTime) {
//        batch.setProjectionMatrix(camera.combined);
        camera.update();
        batch.begin();
        super.update(deltaTime);
        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent position = positionMapper.get(entity);
        DrawableComponent drawable = drawableMapper.get(entity);
        TextureRegion region = drawable.region;
//        batch.draw(drawable.region, position.position.x, position.position.y);
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float originX = width * 0.5f;
        float originY = width * 0.5f;
        float scale = 1;
        batch.draw(drawable.region, position.position.x - originX, position.position.y - originY, originX, originY, width, height, scale, scale, position.rotation.angle());
    }
}
