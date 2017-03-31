package no.ntnu.tdt4240.asteroids;

import javax.inject.Inject;
import javax.inject.Singleton;

public class GameSettings {

    public static double obstacleSpawnChance = 0.3;
    public double powerupSpawnChance = 0.2;
    public static int maxObstacles = 8;
    public static int minObstacles = 3;
    public float playerGravity = 0.01f;
    public int obstacleMaxSpeed = 200;
    public float accelerationScalar = 500;
    public String playerAppearance = "playerBlack.png";
}
