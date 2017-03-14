package no.ntnu.tdt4240.asteroids.input;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

// TODO: refactor so we don't depend on the entity, use command pattern
public class GamepadJoystickListener extends ChangeListener {

    @SuppressWarnings("unused")
    private static final String TAG = GamepadJoystickListener.class.getSimpleName();
    private final InputHandler inputHandler;

    public GamepadJoystickListener(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        Touchpad touchpad = (Touchpad) actor;
        float knobPercentX = touchpad.getKnobPercentX();
        float knobPercentY = touchpad.getKnobPercentY();

        inputHandler.move(knobPercentX, knobPercentY);

    }

}
