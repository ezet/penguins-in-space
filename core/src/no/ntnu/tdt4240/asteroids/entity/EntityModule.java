package no.ntnu.tdt4240.asteroids.entity;

import com.badlogic.ashley.core.PooledEngine;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.ntnu.tdt4240.asteroids.Assets;
import no.ntnu.tdt4240.asteroids.GameSettings;
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
    @EntityComponent.GameScope
    public PooledEngine providesEngine() {
        return engine;
    }

    @Provides
    @EntityComponent.GameScope
    public IDrawableComponentFactory provideDrawableComponentFactory(PooledEngine engine, Assets assets) {
        return new DefaultDrawableComponentFactory(engine, assets);
    }

    @Provides
    @EntityComponent.GameScope
    public EffectFactory provideEffectFactory() {
        return new EffectFactory();
    }

    @Provides
    @EntityComponent.GameScope
    public EffectTextureFactory provideEffectTextureFactory() {
        return new EffectTextureFactory();
    }

    @Provides
    @EntityComponent.GameScope
    public EntityFactory provideEntityFactory(PooledEngine engine, IDrawableComponentFactory drawableComponentFactory, GameSettings gameSettings) {
        return new EntityFactory(engine, drawableComponentFactory, gameSettings);
    }
}
