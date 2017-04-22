package no.ntnu.tdt4240.asteroids.game.entity.component;

import no.ntnu.tdt4240.asteroids.game.effect.IEffect;

public class PowerupClass extends BaseComponent {

    public IEffect effect;


    @Override
    public void reset() {
        effect = null;
    }
}
