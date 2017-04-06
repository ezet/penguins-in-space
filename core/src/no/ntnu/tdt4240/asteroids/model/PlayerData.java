package no.ntnu.tdt4240.asteroids.model;

public class PlayerData {

    public String participantId = "";

    public String playerId = "";

    public String displayName = "";

    public boolean isSelf = false;


    public PlayerData(String participantId, String displayName) {
        this.participantId = participantId;
        this.displayName = displayName;
    }

    public PlayerData(String participantId, String displayName, boolean isSelf) {
        this(participantId, displayName);
        this.isSelf = isSelf;
    }

    public PlayerData(String playerId, String participantId, String displayName) {
        this(participantId, displayName);
        this.playerId = playerId;
    }
}
