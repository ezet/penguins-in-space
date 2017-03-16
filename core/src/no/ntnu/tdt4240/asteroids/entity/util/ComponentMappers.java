package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.ashley.core.ComponentMapper;

import no.ntnu.tdt4240.asteroids.entity.component.AnimationComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BoundaryComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BulletComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.GravityComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ObstacleComponent;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;

public abstract class ComponentMappers {

    public static final ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<MovementComponent> movementMapper = ComponentMapper.getFor(MovementComponent.class);
    public static final ComponentMapper<DrawableComponent> drawableMapper = ComponentMapper.getFor(DrawableComponent.class);
    public static final ComponentMapper<BoundaryComponent> boundaryMapper = ComponentMapper.getFor(BoundaryComponent.class);
    public static final ComponentMapper<GravityComponent> gravityMapper = ComponentMapper.getFor(GravityComponent.class);
    public static final ComponentMapper<CollisionComponent> collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
    public static final ComponentMapper<BoundsComponent> boundsMapper = ComponentMapper.getFor(BoundsComponent.class);
    public static final ComponentMapper<ObstacleComponent> obstacleMapper = ComponentMapper.getFor(ObstacleComponent.class);
    public static final ComponentMapper<BulletComponent> bulletMapper = ComponentMapper.getFor(BulletComponent.class);
    public static final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);
}
