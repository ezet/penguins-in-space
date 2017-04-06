package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ScoreComponent implements Component, Poolable {

    public int score;

    @Override
    public void reset() {
        score = 0;
    }
}
