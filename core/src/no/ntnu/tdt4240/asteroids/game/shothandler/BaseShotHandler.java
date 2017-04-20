package no.ntnu.tdt4240.asteroids.game.shothandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioService;

public abstract class BaseShotHandler implements IShotHandler {

    private long lastShot;
    private AudioService audioService = ServiceLocator.appComponent.getAudioService();
    private long fireDelay;

    protected BaseShotHandler(long fireDelay) {
        this.fireDelay = fireDelay;
    }

    protected abstract String getSound();

    protected abstract void handle(PooledEngine engine, Entity controlledEntity);

    @Override
    public void fire(PooledEngine engine, Entity controlledEntity) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis < lastShot + fireDelay) {
            return;
        }
        audioService.playSound(getSound());
        handle(engine, controlledEntity);
        lastShot = currentTimeMillis;
    }
}
