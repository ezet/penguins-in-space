package no.ntnu.tdt4240.asteroids.entity;

import com.badlogic.ashley.core.PooledEngine;

import dagger.Module;
import dagger.Provides;
import no.ntnu.tdt4240.asteroids.entity.util.DefaultDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EffectFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EffectTextureFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.entity.util.IDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.service.Assets;
import no.ntnu.tdt4240.asteroids.service.settings.IGameSettings;
import no.ntnu.tdt4240.asteroids.service.settings.MultiPlayerSettings;
import no.ntnu.tdt4240.asteroids.service.settings.SinglePlayerSettings;

@Module
public class EntityModule {

    private final PooledEngine engine;
    private boolean multiplayer;

    public EntityModule(PooledEngine engine, boolean multiplayer) {
        this.engine = engine;
        this.multiplayer = multiplayer;
    }

    @Provides
    @EntityComponent.GameScope
    IGameSettings provideGameSettings() {
        if (multiplayer) return new MultiPlayerSettings();
        return new SinglePlayerSettings();
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
    public EntityFactory provideEntityFactory(PooledEngine engine, IDrawableComponentFactory drawableComponentFactory, IGameSettings gameSettings) {
        return new EntityFactory(engine, drawableComponentFactory, gameSettings);
    }
}
