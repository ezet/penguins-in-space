package no.ntnu.tdt4240.asteroids.game.effect;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import no.ntnu.tdt4240.asteroids.game.entity.component.DrawableComponent;
import no.ntnu.tdt4240.asteroids.game.entity.component.EffectComponent;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioService;

import static no.ntnu.tdt4240.asteroids.game.entity.util.ComponentMappers.drawableMapper;

public abstract class BaseEffect implements IEffect {

    @SuppressWarnings("unused")
    private static final String TAG = BaseEffect.class.getSimpleName();

    private AudioService audioService = ServiceLocator.getAppComponent().getAudioService();
    private TextureRegion oldRegion;
    private boolean applied;
    private float remainingDuration;

    protected abstract float getDuration();

    protected abstract TextureRegion getEffectTexture();

    protected abstract void applyEffect(PooledEngine engine, Entity entity, EffectComponent effectComponent);

    protected abstract void removeEffect(PooledEngine engine, Entity entity, EffectComponent effectComponent);

    @Override
    public void refresh(PooledEngine engine, Entity target, EffectComponent effectComponent, IEffect effect) {
        BaseEffect baseEffect = (BaseEffect) effect;
        this.remainingDuration += baseEffect.getDuration();
    }

    @Override
    public boolean tick(PooledEngine engine, Entity entity, EffectComponent component, float deltaTime) {
        if (!applied) {
            applyEffectInternal(engine, entity, component);
            return false;
        } else if (remainingDuration > 0) {
            remainingDuration -= deltaTime;
            return false;
        } else {
            removeEffectInternal(engine, entity, component);
            return true;
        }
    }

    @Override
    public void replace(PooledEngine engine, Entity target, EffectComponent effectComponent, IEffect newEffect) {
        removeEffectInternal(engine, target, effectComponent);
    }

    private void applyEffectInternal(PooledEngine engine, Entity entity, EffectComponent effectComponent) {
        applyEffect(engine, entity, effectComponent);
        audioService.playSound(AssetService.SoundAsset.SOUND_POWERUP_WAV);
        setTexture(entity);
        remainingDuration = getDuration();
        applied = true;
    }

    private void removeEffectInternal(PooledEngine engine, Entity entity, EffectComponent component) {
        removeEffect(engine, entity, component);
        restoreTexture(entity);
        applied = false;
        remainingDuration = 0;
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
