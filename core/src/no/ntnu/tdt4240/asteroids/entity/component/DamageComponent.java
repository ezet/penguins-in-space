package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;


public class DamageComponent implements Component, Pool.Poolable {

    final public Array<Component> ignoredComponents = new Array<>();
    public int damage = 1;
    public Family ignoreComponents;

    @Override
    public void reset() {
        damage = 1;
    }
}
