package no.ntnu.tdt4240.asteroids.service.settings;

public interface IGameConfig {

    double getObstacleSpawnChance();

    int getMaxObstacles();

    int getMinObstacles();

    double getPowerupSpawnChance();

    float getPlayerGravity();

    int getObstacleMaxSpeed();

    float getAccelerationScalar();

    int getPlayerNoSpawnRadius();

    float getBombDelay();

    float getMissileDelay();
}
