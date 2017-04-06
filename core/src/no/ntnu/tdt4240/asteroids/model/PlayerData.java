package no.ntnu.tdt4240.asteroids.model;

public class PlayerData {

    public String playerId = "";

    public String displayName = "";

    public boolean isSelf = false;


    public PlayerData(String playerId, String displayName) {
        this.playerId = playerId;
        this.displayName = displayName;
    }

    public PlayerData(String playerId, String displayName, boolean isSelf) {
        this(playerId, displayName);
        this.isSelf = isSelf;
    }
}
