package no.ntnu.tdt4240.asteroids.model;

public class PlayerData {

    public String participantId = "";

    public String playerId = "";

    public String displayName = "";

    public boolean isSelf = false;

    public int totalScore = 0;

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

    @Override
    public int hashCode() {
        return participantId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlayerData && participantId.equals(obj);
    }
}
