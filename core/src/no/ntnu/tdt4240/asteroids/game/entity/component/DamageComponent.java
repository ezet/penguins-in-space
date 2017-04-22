package no.ntnu.tdt4240.asteroids.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Pool;


public class DamageComponent implements Component, Pool.Poolable {

    public int damage = 1;
    public Family ignoredEntities;

    @Override
    public void reset() {
        damage = 1;
    }
}
