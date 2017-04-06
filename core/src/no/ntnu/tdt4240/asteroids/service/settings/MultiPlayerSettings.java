package no.ntnu.tdt4240.asteroids.service.settings;

@SuppressWarnings("FieldCanBeLocal")
public class MultiPlayerSettings implements IGameSettings {

    private static double obstacleSpawnChance = 0.3;
    private static int maxObstacles = 0;
    private static int minObstacles = 0;
    private static double powerupSpawnChance = 0.2;
    private static float playerGravity = 0.005f;
    private static int obstacleMaxSpeed = 200;
    private static float accelerationScalar = 500;
    private static int playerNoSpawnRadius = 50;

    public double getObstacleSpawnChance() {
        return obstacleSpawnChance;
    }

    public int getMaxObstacles() {
        return maxObstacles;
    }

    public int getMinObstacles() {
        return minObstacles;
    }

    public double getPowerupSpawnChance() {
        return powerupSpawnChance;
    }

    public float getPlayerGravity() {
        return playerGravity;
    }

    public int getObstacleMaxSpeed() {
        return obstacleMaxSpeed;
    }

    public float getAccelerationScalar() {
        return accelerationScalar;
    }

    public int getPlayerNoSpawnRadius() {
        return playerNoSpawnRadius;
    }
}
