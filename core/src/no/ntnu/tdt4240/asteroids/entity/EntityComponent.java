package no.ntnu.tdt4240.asteroids.entity;

import javax.inject.Singleton;

import dagger.Component;
import no.ntnu.tdt4240.asteroids.entity.util.EffectFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EffectTextureFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;

@Component(modules = EntityModule.class)
@Singleton
public interface EntityComponent {

    EntityFactory provideEntityFactory();

    EffectTextureFactory provideEffectTextureFactory();

    EffectFactory provideEffectFactory();

}
