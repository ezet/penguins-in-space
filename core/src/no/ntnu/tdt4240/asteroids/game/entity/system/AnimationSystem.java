package no.ntnu.tdt4240.asteroids.game.entity.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import no.ntnu.tdt4240.asteroids.game.entity.component.AnimationComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.TransformComponent;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioService;

import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.animationMapper;
import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.drawableMapper;
import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.transformMapper;


public class AnimationSystem extends IteratingSystem {

    private final static Family FAMILY = Family.all(AnimationComponent.class).get();
    private final AudioService audioService;

    public AnimationSystem() {
        super(FAMILY);
        audioService = ServiceLocator.getAppComponent().getAudioService();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animationComponent = animationMapper.get(entity);
        DrawableComponent drawableComponent = drawableMapper.get(entity);
        animationComponent.delay -= deltaTime;
        if (animationComponent.delay > 0) return;
        if (animationComponent.currentFrame == 0) {
            animationComponent.originalRegion = drawableComponent.texture;
            TransformComponent transformComponent = transformMapper.get(entity);
            for (Class<? extends Component> component : animationComponent.removeDuringAnimation) {
                entity.remove(component);
            }
            if (transformComponent != null) {
                animationComponent.originalScale.set(transformComponent.scale);
                transformComponent.scale.set(animationComponent.scale);
            }
            if (animationComponent.soundOnStart != null)
                audioService.playSound(animationComponent.soundOnStart);

        } else if (animationComponent.currentFrame == animationComponent.frames.size) {
            if (animationComponent.removeEntityAfterAnimation) {
                getEngine().removeEntity(entity);
            } else {
                drawableComponent.texture = animationComponent.originalRegion;
                TransformComponent transformComponent = transformMapper.get(entity);
                if (transformComponent != null) {
                    animationComponent.originalScale.set(transformComponent.scale);
                    transformComponent.scale.set(animationComponent.scale);
                }
                for (Class<? extends Component> component : animationComponent.removeAfterAnimation) {
                    entity.remove(component);
                }
            }
            if (animationComponent.soundOnComplete != null)
                audioService.playSound(animationComponent.soundOnComplete);

            entity.remove(AnimationComponent.class);
            return;
        }
        drawableComponent.texture = animationComponent.frames.get(animationComponent.currentFrame);
        animationComponent.currentFrame++;
    }
}
