package no.ntnu.tdt4240.asteroids.model;

import com.badlogic.ashley.core.Entity;

public class PlayerData {

    public String participantId = "";

    public String playerId = "";

    public String displayName = "";

    public boolean isSelf = false;

    public int totalScore = 0;

    public Entity entity;

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
