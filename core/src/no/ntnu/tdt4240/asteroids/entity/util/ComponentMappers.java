package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.ashley.core.ComponentMapper;

import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;

public abstract class ComponentMappers {

    public static final ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<MovementComponent> movementMapper = ComponentMapper.getFor(MovementComponent.class);
    public static final ComponentMapper<DrawableComponent> drawableMapper = ComponentMapper.getFor(DrawableComponent.class);
}
