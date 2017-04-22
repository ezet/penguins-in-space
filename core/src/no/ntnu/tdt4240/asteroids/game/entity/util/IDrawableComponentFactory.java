package no.ntnu.tdt4240.asteroids.game.entity.util;

import com.badlogic.ashley.core.Component;

import no.ntnu.tdt4240.asteroids.game.effect.IEffect;
import no.ntnu.tdt4240.asteroids.game.entity.component.DrawableComponent;

public interface IDrawableComponentFactory {

    DrawableComponent getPlayer();

    DrawableComponent getProjectile();

    DrawableComponent getObstacle();

    DrawableComponent getPowerup(IEffect effect);

    void resetOpponentCount();

    Component getMultiPlayer();

    Component getBomb();

    Component getMissile();
}
