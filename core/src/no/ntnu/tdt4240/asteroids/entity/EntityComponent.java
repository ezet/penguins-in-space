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

@EntityComponent.GameScope
@Component(modules = EntityModule.class, dependencies = AppComponent.class)
public interface EntityComponent {

    EntityFactory getEntityFactory();

    EffectTextureFactory getEffectTextureFactory();

    EffectFactory getEffectFactory();


    @Scope
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface GameScope {

    }

}
