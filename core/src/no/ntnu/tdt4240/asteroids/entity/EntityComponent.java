package no.ntnu.tdt4240.asteroids.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

import dagger.Component;
import no.ntnu.tdt4240.asteroids.AppComponent;
import no.ntnu.tdt4240.asteroids.entity.util.EffectFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EffectTextureFactory;
import no.ntnu.tdt4240.asteroids.entity.util.EntityFactory;
import no.ntnu.tdt4240.asteroids.entity.util.IDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.service.settings.IGameConfig;

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
