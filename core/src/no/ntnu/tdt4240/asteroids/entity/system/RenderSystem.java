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
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.transformMapper;

public class RenderSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(TransformComponent.class, DrawableComponent.class).get();
    @SuppressWarnings("unused")
    private static final String TAG = RenderSystem.class.getSimpleName();
    private static ShapeRenderer shapeRenderer;
    private final Camera camera;
    private final Batch batch;
    private boolean debug;

    public RenderSystem(Batch batch) {
        //noinspection unchecked
        super(FAMILY);
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
        if (debug) {
            drawBounds();
        }

    }

    private void drawBounds() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Entity entity : getEntities()) {
            Shape2D bounds = boundsMapper.get(entity).getBounds();
            if (bounds instanceof Rectangle) {
                Rectangle region = (Rectangle) bounds;
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
        TransformComponent transform = transformMapper.get(entity);
        DrawableComponent drawable = drawableMapper.get(entity);
        TextureRegion region = drawable.texture;
        float width = region.getRegionWidth() * transform.scaleX;
        float height = region.getRegionHeight() * transform.scaleY;
        float originX = width * 0.5f;
        float originY = height * 0.5f;
        float x = transform.position.x - originX;
        float y = transform.position.y - originY;
        batch.draw(drawable.texture, x, y, originX, originY, width, height, transform.scaleX, transform.scaleY, transform.rotation.angle());
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
