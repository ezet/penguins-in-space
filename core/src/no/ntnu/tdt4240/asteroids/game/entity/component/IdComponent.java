package no.ntnu.tdt4240.asteroids.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class IdComponent implements Component, Pool.Poolable{

    public String participantId = "";

    @SuppressWarnings("unused")
    public IdComponent() {
    }

    public IdComponent(String participantId) {
        this.participantId = participantId;
    }


    @Override
    public void reset() {
        participantId = "";
    }
}
