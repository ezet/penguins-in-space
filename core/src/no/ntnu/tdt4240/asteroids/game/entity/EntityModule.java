package no.ntnu.tdt4240.asteroids.game.entity;

import com.badlogic.ashley.core.PooledEngine;

import dagger.Module;
import dagger.Provides;
import no.ntnu.tdt4240.asteroids.game.AnimationFactory;
import no.ntnu.tdt4240.asteroids.game.entity.util.DefaultDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.game.entity.util.EffectFactory;
import no.ntnu.tdt4240.asteroids.game.entity.util.EffectTextureFactory;
import no.ntnu.tdt4240.asteroids.game.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.game.entity.util.IDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.config.IGameConfig;
import no.ntnu.tdt4240.asteroids.service.config.MultiPlayerConfig;
import no.ntnu.tdt4240.asteroids.service.config.SinglePlayerConfig;

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
    IGameConfig provideGameSettings() {
        if (multiplayer) return new MultiPlayerConfig();
        return new SinglePlayerConfig();
    }

    @Provides
    @EntityComponent.GameScope
    public PooledEngine providesEngine() {
        return engine;
    }

    @Provides
    @EntityComponent.GameScope
    public IDrawableComponentFactory provideDrawableComponentFactory(PooledEngine engine, AssetService assetService) {
        return new DefaultDrawableComponentFactory(engine, assetService);
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
    public EntityFactory provideEntityFactory(PooledEngine engine, IDrawableComponentFactory drawableComponentFactory, IGameConfig gameSettings, AnimationFactory animationFactory) {
        return new EntityFactory(engine, drawableComponentFactory, gameSettings, animationFactory);
    }
}
