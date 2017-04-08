package no.ntnu.tdt4240.asteroids.entity.util;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import no.ntnu.tdt4240.asteroids.entity.component.AnimationComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BoundaryComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.BulletClass;
import no.ntnu.tdt4240.asteroids.entity.component.CircularBoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.CollisionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DamageComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.EffectComponent;
import no.ntnu.tdt4240.asteroids.entity.component.GravityComponent;
import no.ntnu.tdt4240.asteroids.entity.component.HealthComponent;
import no.ntnu.tdt4240.asteroids.entity.component.MovementComponent;
import no.ntnu.tdt4240.asteroids.entity.component.NetworkSyncComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ObstacleClass;
import no.ntnu.tdt4240.asteroids.entity.component.PlayerClass;
import no.ntnu.tdt4240.asteroids.entity.component.PowerupClass;
import no.ntnu.tdt4240.asteroids.entity.component.RectangularBoundsComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ScoreComponent;
import no.ntnu.tdt4240.asteroids.entity.component.ShootComponent;
import no.ntnu.tdt4240.asteroids.entity.component.TransformComponent;

public abstract class ComponentMappers {

    public static final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<MovementComponent> movementMapper = ComponentMapper.getFor(MovementComponent.class);
    public static final ComponentMapper<DrawableComponent> drawableMapper = ComponentMapper.getFor(DrawableComponent.class);
    public static final ComponentMapper<BoundaryComponent> boundaryMapper = ComponentMapper.getFor(BoundaryComponent.class);
    public static final ComponentMapper<GravityComponent> gravityMapper = ComponentMapper.getFor(GravityComponent.class);
    public static final ComponentMapper<CollisionComponent> collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
    public static final BoundsComponentMapperWrapper boundsMapper = new BoundsComponentMapperWrapper();
    public static final ComponentMapper<ObstacleClass> obstacleMapper = ComponentMapper.getFor(ObstacleClass.class);
    public static final ComponentMapper<BulletClass> bulletMapper = ComponentMapper.getFor(BulletClass.class);
    public static final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<EffectComponent> effectMapper = ComponentMapper.getFor(EffectComponent.class);
    public static final ComponentMapper<HealthComponent> healthMapper = ComponentMapper.getFor(HealthComponent.class);
    public static final ComponentMapper<DamageComponent> damageMapper = ComponentMapper.getFor(DamageComponent.class);
    public static final ComponentMapper<ShootComponent> shootMapper = ComponentMapper.getFor(ShootComponent.class);
    public static final ComponentMapper<PlayerClass> playerMapper = ComponentMapper.getFor(PlayerClass.class);
    public static final ComponentMapper<PowerupClass> powerupMapper = ComponentMapper.getFor(PowerupClass.class);
    public static final ComponentMapper<ScoreComponent> scoreMapper = ComponentMapper.getFor(ScoreComponent.class);

    public static class BoundsComponentMapperWrapper {

        private static final ComponentMapper<CircularBoundsComponent> circularBoundsMapper = ComponentMapper.getFor(CircularBoundsComponent.class);
        private static final ComponentMapper<RectangularBoundsComponent> rectangularBoundsMapper = ComponentMapper.getFor(RectangularBoundsComponent.class);

        private BoundsComponentMapperWrapper() {

        }

        public BoundsComponent get(Entity entity) {
            BoundsComponent component = rectangularBoundsMapper.get(entity);
            if (component == null) component = circularBoundsMapper.get(entity);
            return component;
        }
    }
}
