package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import no.ntnu.tdt4240.asteroids.entity.component.BoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.boundsMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.drawableMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.positionMapper;

public class BoundsSystem extends IteratingSystem {

    public static final int MARGIN = 10;

    public BoundsSystem() {
        //noinspection unchecked
        super(Family.all(BoundsComponent.class, PositionComponent.class, DrawableComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BoundsComponent bounds = boundsMapper.get(entity);
        PositionComponent position = positionMapper.get(entity);
        DrawableComponent drawable = drawableMapper.get(entity);
        if (bounds.bounds.width == 0) {
            bounds.bounds.width = drawable.region.getRegionWidth() - MARGIN * 2;
        }
        if (bounds.bounds.height == 0) {
            bounds.bounds.height = drawable.region.getRegionHeight() - MARGIN * 2;
        }
        bounds.bounds.setCenter(position.position);
    }
}
