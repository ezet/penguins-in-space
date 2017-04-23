package no.ntnu.tdt4240.asteroids.game.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

import dagger.Component;
import no.ntnu.tdt4240.asteroids.game.entity.util.EffectFactory;
import no.ntnu.tdt4240.asteroids.game.entity.util.EffectTextureFactory;
import no.ntnu.tdt4240.asteroids.game.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.game.entity.util.IDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.service.AppComponent;
import no.ntnu.tdt4240.asteroids.service.config.IGameConfig;

@EntityComponent.GameScope
@Component(modules = EntityModule.class, dependencies = AppComponent.class)
public interface EntityComponent {

    EntityFactory getEntityFactory();

    EffectTextureFactory getEffectTextureFactory();

    EffectFactory getEffectFactory();

    IDrawableComponentFactory getDrawableComponentFactory();

    IGameConfig getGameSettings();

    @Scope
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @interface GameScope {

    }

}
