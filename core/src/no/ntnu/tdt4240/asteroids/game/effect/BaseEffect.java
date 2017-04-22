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

    private static final String TAG = BaseEffect.class.getSimpleName();

    AudioService audioService = ServiceLocator.getAppComponent().getAudioService();
    private TextureRegion oldRegion;
    private boolean applied;
    private float remainingDuration;

    protected abstract float getDuration();

    protected abstract TextureRegion getEffectTexture();

    protected abstract void applyEffect(PooledEngine engine, Entity entity, EffectComponent effectComponent);

    protected abstract void removeEffect(PooledEngine engine, Entity entity, EffectComponent effectComponent);

    @Override
    public void refresh(IEffect effect) {
        BaseEffect baseEffect = (BaseEffect) effect;
        this.remainingDuration += baseEffect.getDuration();
    }

    @Override
    public boolean tick(PooledEngine engine, Entity entity, EffectComponent component, float deltaTime) {
        if (!applied) {
            applyEffect(engine, entity, component);
            audioService.playSound(AssetService.SoundAsset.SOUND_POWERUP_WAV);
            setTexture(entity);
            remainingDuration = getDuration();
            applied = true;
            return false;
        } else if (remainingDuration > 0) {
            remainingDuration -= deltaTime;
            return false;
        } else {
            removeEffect(engine, entity, component);
            restoreTexture(entity);
            applied = false;
            remainingDuration = 0;
            return true;
        }
//        return false;
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
