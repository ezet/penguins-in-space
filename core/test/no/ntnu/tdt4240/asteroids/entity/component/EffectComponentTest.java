package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import no.ntnu.tdt4240.asteroids.GameComponent;
import no.ntnu.tdt4240.asteroids.entity.EntityComponent;
import no.ntnu.tdt4240.asteroids.entity.util.EffectTextureFactory;
import no.ntnu.tdt4240.asteroids.game.effect.BaseEffect;
import no.ntnu.tdt4240.asteroids.game.effect.IEffect;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class EffectComponentTest {

    private PooledEngine engine;
    private Entity entity;
    private EffectComponent fixture;
    private IEffect effect1;
    private IEffect effect2;

    @Before
    public void setup() {
        engine = mock(PooledEngine.class);
        when(engine.createComponent(HealthComponent.class)).thenReturn(new HealthComponent());
        when(engine.createComponent(DamageComponent.class)).thenReturn(new DamageComponent());
        ServiceLocator.gameComponent = mock(GameComponent.class);
        when(ServiceLocator.gameComponent.getAudioManager()).thenReturn(mock(AudioManager.class));
        ServiceLocator.entityComponent = mock(EntityComponent.class);
        when(ServiceLocator.entityComponent.getEffectTextureFactory()).thenReturn(mock(EffectTextureFactory.class));
        entity = mock(Entity.class);
        fixture = new EffectComponent();
        effect1 = Mockito.spy(BaseEffect.class);
        effect2 = Mockito.spy(BaseEffect.class);
    }


    @Test
    public void applyTest() {
        fixture.addEffect(effect1);
        fixture.addEffect(effect2);

        fixture.tick(engine, entity, 1);
        Mockito.verify(effect1).refresh(effect2);

        Mockito.verify(effect1).tick(engine, entity, fixture, 1);

        fixture.tick(engine, entity, 10);
        Mockito.verify(effect1).tick(engine, entity, fixture, 10);

    }


}
