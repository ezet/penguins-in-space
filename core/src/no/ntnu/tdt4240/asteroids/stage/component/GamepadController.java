package no.ntnu.tdt4240.asteroids.stage.component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.tdt4240.asteroids.input.InputHandler;

// TODO: should extend WidgetGroup
public class GamepadController extends WidgetGroup {

    private static final int BUTTON_SIZE = 100;
    private static final int BUTTON_MARGIN = 100;
    @SuppressWarnings("unused")
    private static final String TAG = GamepadController.class.getSimpleName();
    private Touchpad touchPad;
    private GamepadButton button;

    public GamepadController(InputHandler inputHandler) {
        initTouchpad();
        touchPad.addListener(new GamepadJoystickListener(inputHandler));
        button.addListener(new GamepadButtonListener(inputHandler));
    }

    @Override
    public void layout() {
        super.layout();
    }

    @Override
    public float getPrefWidth() {
        return getParent().getWidth();
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        button.setPosition(getStage().getWidth() - BUTTON_SIZE - BUTTON_MARGIN, BUTTON_MARGIN);
    }

    private void initTouchpad() {
        // TODO: use uiskin.json
//        Skin touchpadSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("data/touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("data/touchKnob.png"));
        Touchpad.TouchpadStyle style = new Touchpad.TouchpadStyle();
        style.background = touchpadSkin.getDrawable("touchBackground");
        style.knob = touchpadSkin.getDrawable("touchKnob");
        touchPad = new Touchpad(10, style);
        touchPad.setBounds(50, 50, 200, 200);
        addActor(touchPad);
        touchpadSkin.add("touchButton", new Texture("data/touchKnob.png"));
        button = new GamepadButton(touchpadSkin.getDrawable("touchButton"));
        button.setSize(BUTTON_SIZE, BUTTON_SIZE);
        addActor(button);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    private static class GamepadButtonListener extends ClickListener {

        private final InputHandler inputHandler;

        GamepadButtonListener(InputHandler inputHandler) {
            this.inputHandler = inputHandler;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            inputHandler.fire();
        }
    }

    // TODO: refactor use command pattern
    private static class GamepadJoystickListener extends ChangeListener {

        @SuppressWarnings("unused")
        private static final String TAG = GamepadJoystickListener.class.getSimpleName();
        private final InputHandler inputHandler;

        GamepadJoystickListener(InputHandler inputHandler) {
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
}
