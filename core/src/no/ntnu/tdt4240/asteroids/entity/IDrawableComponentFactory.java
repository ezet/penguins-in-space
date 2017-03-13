package no.ntnu.tdt4240.asteroids.entity;

import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;

public interface IDrawableComponentFactory {

    DrawableComponent getPlayer();

    DrawableComponent getBullet();
}
