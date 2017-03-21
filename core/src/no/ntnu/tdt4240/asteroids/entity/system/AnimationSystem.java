package no.ntnu.tdt4240.asteroids.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import no.ntnu.tdt4240.asteroids.entity.component.AnimationComponent;
import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.animationMapper;
import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.drawableMapper;


public class AnimationSystem extends IteratingSystem {

    private final static Family FAMILY = Family.all(AnimationComponent.class).get();

    public AnimationSystem() {
        super(FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animationComponent = animationMapper.get(entity);
        DrawableComponent drawableComponent = drawableMapper.get(entity);
        if (animationComponent.currentFrame == 0) {
            animationComponent.originalRegion = drawableComponent.texture;
        } else if (animationComponent.currentFrame == animationComponent.frames.size) {
            entity.remove(AnimationComponent.class);
            if (animationComponent.removeOnAnimationComplete) {
                getEngine().removeEntity(entity);
            } else {
                drawableComponent.texture = animationComponent.originalRegion;
            }
            return;
        }
        drawableComponent.texture = animationComponent.frames.get(animationComponent.currentFrame);
        animationComponent.currentFrame++;
    }
}
