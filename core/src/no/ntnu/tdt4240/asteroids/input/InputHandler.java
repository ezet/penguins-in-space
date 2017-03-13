package no.ntnu.tdt4240.asteroids.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.entity.IDrawableComponentFactory;
import no.ntnu.tdt4240.asteroids.entity.component.PositionComponent;
import no.ntnu.tdt4240.asteroids.entity.component.VelocityComponent;
import no.ntnu.tdt4240.asteroids.entity.util.ComponentMappers;

public class InputHandler {

    public static final int MAX_VELOCITY = 500;
    private final Entity player;
    private final PooledEngine engine;
    private final IDrawableComponentFactory drawableComponentFactory;

    public InputHandler(Entity player, PooledEngine engine, IDrawableComponentFactory drawableComponentFactory) {
        this.player = player;
        this.engine = engine;
        this.drawableComponentFactory = drawableComponentFactory;
    }

    public void move(float velocityX, float velocityY) {
        // TODO: implement proper player speed and direction
        VelocityComponent velocityComponent = ComponentMappers.velocityMapper.get(player);
        velocityComponent.setX(velocityX * MAX_VELOCITY);
        velocityComponent.setY(velocityY * MAX_VELOCITY);
    }

    public void fire() {
        // TODO: implement proper bullet speed and direction
        PositionComponent playerPosition = ComponentMappers.positionMapper.get(player);
        PositionComponent bulletPosition = engine.createComponent(PositionComponent.class);
        bulletPosition.setX(playerPosition.getX());
        bulletPosition.setY(playerPosition.getY());

        VelocityComponent playerVelocity = ComponentMappers.velocityMapper.get(player);
        VelocityComponent bulletVelocity = engine.createComponent(VelocityComponent.class);
        bulletVelocity.setX(playerVelocity.getX() * 10);
        bulletVelocity.setY(playerVelocity.getY() * 10);

        Entity bullet = engine.createEntity();
        bullet.add(bulletPosition);
        bullet.add(bulletVelocity);
        bullet.add(drawableComponentFactory.getBullet());
        engine.addEntity(bullet);
    }
}
