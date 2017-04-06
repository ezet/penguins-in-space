package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PlayerClass implements Component, Pool.Poolable {

    public String id;

    public String displayName;

    public boolean isSelf;

    public PlayerClass(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public PlayerClass() {
        reset();
    }

    @Override
    public void reset() {
        id = "";
        displayName = "Player";
    }

}
