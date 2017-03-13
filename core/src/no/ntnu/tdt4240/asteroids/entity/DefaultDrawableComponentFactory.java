package no.ntnu.tdt4240.asteroids.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import no.ntnu.tdt4240.asteroids.entity.component.DrawableComponent;

public class DefaultDrawableComponentFactory implements IDrawableComponentFactory {
    @Override
    public DrawableComponent getPlayer() {
        Texture texture = new Texture("slide_1.png");
        return new DrawableComponent(new TextureRegion(texture));
    }

    @Override
    public DrawableComponent getBullet() {
        Texture texture = new Texture("slide_1.png");
        return new DrawableComponent(new TextureRegion(texture));
    }
}
