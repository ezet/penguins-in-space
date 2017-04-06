package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PlayerClass implements Component, Pool.Poolable {

    public String participantId;

    public String displayName;

    public boolean isSelf;

    public PlayerClass(String participantId, String displayName) {
        this.participantId = participantId;
        this.displayName = displayName;
    }

    @SuppressWarnings("unused")
    public PlayerClass() {
        reset();
    }

    @Override
    public void reset() {
        participantId = "";
        displayName = "";
    }

}
