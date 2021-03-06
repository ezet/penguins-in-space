package no.ntnu.tdt4240.asteroids.game.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.game.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.game.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.boundsMapper;
import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.drawableMapper;
import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.playerMapper;
import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.transformMapper;

public class RenderSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(TransformComponent.class, DrawableComponent.class).get();
    @SuppressWarnings("unused")
    private static final String TAG = RenderSystem.class.getSimpleName();
    private static ShapeRenderer shapeRenderer;
    private final Camera camera;
    private final Batch batch;
    private boolean debug = false;
    private Viewport viewport;
    private BitmapFont font;


    public RenderSystem(Batch batch) {
        super(FAMILY);
        this.camera = new OrthographicCamera();
        viewport = new FitViewport(Asteroids.VIRTUAL_WIDTH, Asteroids.VIRTUAL_HEIGHT, camera);
        viewport.apply(true);
        this.batch = batch;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.RED);
        font = ServiceLocator.getAppComponent().getAssetService().getSkin(AssetService.SkinAsset.UISKIN).getFont("default-font");
    }

    @Override
    public void update(float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        if (debug) shapeRenderer.setProjectionMatrix(camera.combined);
        camera.update();
        if (debug) shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        batch.begin();
        super.update(deltaTime);
        batch.end();
        if (debug) shapeRenderer.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = transformMapper.get(entity);
        DrawableComponent drawable = drawableMapper.get(entity);
        TextureRegion region = drawable.texture;
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float originX = width * 0.5f;
        float originY = height * 0.5f;
        float x = transform.position.x - originX;
        float y = transform.position.y - originY;
        if (debug) drawBounds(entity);

        batch.draw(drawable.texture, x, y, originX, originY, width, height, transform.scale.x, transform.scale.y, transform.rotation.angle());
        PlayerClass playerClass = playerMapper.get(entity);
        if (playerClass != null && !playerClass.isSelf) {
            font.draw(batch, playerClass.displayName, x, y, width, -1, true);
        }
    }

    private void drawBounds(Entity entity) {
        Shape2D bounds = boundsMapper.get(entity).getBounds();
        if (bounds instanceof Rectangle) {
            Rectangle region = (Rectangle) bounds;
            shapeRenderer.rect(region.x, region.y, region.width, region.height);
        } else if (bounds instanceof Circle) {
            Circle region = (Circle) bounds;
            shapeRenderer.circle(region.x, region.y, region.radius);
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set((Asteroids.VIRTUAL_WIDTH) / 2, (Asteroids.VIRTUAL_HEIGHT) / 2, 0);
    }
}
