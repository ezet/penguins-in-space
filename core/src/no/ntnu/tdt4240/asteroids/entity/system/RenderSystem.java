package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.boundsMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.drawableMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.positionMapper;

public class RenderSystem extends IteratingSystem {

    @SuppressWarnings("unused")
    private static final String TAG = RenderSystem.class.getSimpleName();
    private static ShapeRenderer shapeRenderer;
    private final Camera camera;
    private final Batch batch;

    public RenderSystem(Batch batch) {
        //noinspection unchecked
        super(Family.all(TransformComponent.class, DrawableComponent.class).get());
        // TODO: camera config
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.batch = batch;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
    }

    @Override
    public void update(float deltaTime) {
//        batch.setProjectionMatrix(camera.combined);
        camera.update();
        batch.begin();
        super.update(deltaTime);
        batch.end();
        drawBounds();

    }

    private void drawBounds() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Entity entity : getEntities()) {
            TransformComponent transform = positionMapper.get(entity);
//            DrawableComponent drawable = drawableMapper.get(entity);
            Shape2D bounds = boundsMapper.get(entity).getBounds();
            if (bounds instanceof Rectangle) {
                Rectangle region = (Rectangle) bounds;
//                float width = region.getWidth();
//                float height = region.getHeight();
//                float originX = width * 0.5f;
//                float originY = height * 0.5f;
//                float x = transform.position.x - originX;
//                float y = transform.position.y - originY;
//                shapeRenderer.rect(x, y, width, height);
                shapeRenderer.rect(region.x, region.y, region.width, region.height);
            } else if (bounds instanceof Circle) {
                Circle region = (Circle) bounds;
                shapeRenderer.circle(region.x, region.y, region.radius);
            }
        }
        shapeRenderer.end();

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = positionMapper.get(entity);
        DrawableComponent drawable = drawableMapper.get(entity);
        TextureRegion region = drawable.region;
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float originX = width * 0.5f;
        float originY = height * 0.5f;
        float x = transform.position.x - originX;
        float y = transform.position.y - originY;
        float scale = 1;
        batch.draw(drawable.region, x, y, originX, originY, width, height, scale, scale, transform.rotation.angle());
    }
}
