package no.ntnu.tdt4240.asteroids.game.effect;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.inject.Inject;

import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.entity.component.EffectComponent;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioManager;

import static no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers.drawableMapper;

abstract class BaseEffect implements IEffect {

    private TextureRegion oldRegion;

    private boolean applied;

    private float remainingDuration;

    protected abstract float getDuration();

    protected abstract TextureRegion getEffectTexture();

    protected abstract void applyEffect(PooledEngine engine, Entity entity, EffectComponent effectComponent);

    protected abstract void removeEffect(PooledEngine engine, Entity entity, EffectComponent effectComponent);

    @Inject
    protected AudioManager audioManager = ServiceLocator.gameComponent.getAudioManager();

    @Override
    public void tick(PooledEngine engine, Entity entity, EffectComponent component, float deltaTime) {
        if (!applied) {
            applyEffect(engine, entity, component);
            audioManager.playPowerup();
            setTexture(entity);
            remainingDuration = getDuration();
            applied = true;
        } else if (remainingDuration > 0) {
            remainingDuration -= deltaTime;
        } else {
            entity.remove(EffectComponent.class);
            removeEffect(engine, entity, component);
            restoreTexture(entity);
            applied = false;
            remainingDuration = 0;
        }
    }

    private void setTexture(Entity entity) {
        if (getEffectTexture() == null) return;
        DrawableComponent drawableComponent = drawableMapper.get(entity);
        oldRegion = drawableComponent.texture;
        drawableComponent.texture = getEffectTexture();
    }

    private void restoreTexture(Entity entity) {
        if (oldRegion == null) return;
        DrawableComponent drawableComponent = drawableMapper.get(entity);
        drawableComponent.texture = oldRegion;
    }

}
