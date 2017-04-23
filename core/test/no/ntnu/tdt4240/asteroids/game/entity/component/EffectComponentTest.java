package no.ntnu.tdt4240.asteroids.game.entity.component;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import no.ntnu.tdt4240.asteroids.game.effect.BaseEffect;
import no.ntnu.tdt4240.asteroids.game.effect.IEffect;
import no.ntnu.tdt4240.asteroids.game.entity.EntityComponent;
import no.ntnu.tdt4240.asteroids.game.entity.util.EffectTextureFactory;
import no.ntnu.tdt4240.asteroids.service.AppComponent;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;


public class EffectComponentTest {

    private PooledEngine engine;
    private Entity entity;
    private EffectComponent fixture;
    private BaseEffect effect1;
    private BaseEffect effect2;

    @Before
    public void setup() {
        Gdx.app = mock(Application.class);
        engine = mock(PooledEngine.class);
        when(engine.createComponent(HealthComponent.class)).thenReturn(new HealthComponent());
        when(engine.createComponent(DamageComponent.class)).thenReturn(new DamageComponent());
        ServiceLocator.appComponent = mock(AppComponent.class);
        when(ServiceLocator.getAppComponent().getAudioService()).thenReturn(mock(AudioService.class));
        ServiceLocator.entityComponent = mock(EntityComponent.class);
        when(ServiceLocator.getEntityComponent().getEffectTextureFactory()).thenReturn(mock(EffectTextureFactory.class));
        entity = mock(Entity.class);
        fixture = new EffectComponent();
        effect1 = Mockito.spy(BaseEffect.class);
        when(effect1.tick(any(PooledEngine.class), any(Entity.class), any(EffectComponent.class), anyFloat())).thenReturn(false, false, false, true);
        effect2 = Mockito.spy(BaseEffect.class);
    }

    @Test
    public void applyTest() {
        fixture.addEffect(effect1);
        fixture.tick(engine, entity, 1);


        Mockito.verify(effect1).tick(engine, entity, fixture, 1);

        fixture.addEffect(effect2);
        Mockito.verify(effect1).refresh(effect2);

        fixture.tick(engine, entity, 10);

        Mockito.verify(effect1).tick(engine, entity, fixture, 10);

        Mockito.verify(effect2, never()).tick(any(PooledEngine.class), any(Entity.class), any(EffectComponent.class), any(Float.class));
        Mockito.verify(effect2, never()).refresh(any(IEffect.class));
    }
}
