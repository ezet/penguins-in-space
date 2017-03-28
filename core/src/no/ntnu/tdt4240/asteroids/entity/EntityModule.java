package no.ntnu.tdt4240.asteroids.entity;

import com.badlogic.ashley.core.PooledEngine;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.ntnu.tdt4240.asteroids.entity.util.DefaultDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EffectFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EffectTextureFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.entity.util.IDrawableComponentFactory;

@Module
public class EntityModule {

    private final PooledEngine engine;

    public EntityModule(PooledEngine engine) {
        this.engine = engine;
    }

    @Provides
    @Singleton
    public PooledEngine providesEngine() {
        return engine;
    }

    @Provides
    @Singleton
    public IDrawableComponentFactory provideDrawableComponentFactory(PooledEngine engine) {
        return new DefaultDrawableComponentFactory(engine);
    }

    @Provides
    @Singleton
    public EffectFactory provideEffectFactory() {
        return new EffectFactory();
    }

    @Provides
    @Singleton
    public EffectTextureFactory provideEffectTextureFactory() {
        return new EffectTextureFactory();
    }

    @Provides
    @Singleton
    public EntityFactory provideEntityFactory(PooledEngine engine, IDrawableComponentFactory drawableComponentFactory) {
        return new EntityFactory(engine, drawableComponentFactory);
    }
}
