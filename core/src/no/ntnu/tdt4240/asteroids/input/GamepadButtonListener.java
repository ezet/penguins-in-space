package no.ntnu.tdt4240.asteroids.input;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GamepadButtonListener extends ClickListener {


    private final InputHandler inputHandler;

    public GamepadButtonListener(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        inputHandler.fire();
    }
}
