package no.ntnu.tdt4240.asteroids.view.widget;

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

import no.ntnu.tdt4240.asteroids.input.ControllerInputHandler;

// TODO: should extend WidgetGroup
public class GamepadController extends WidgetGroup {

    private static final int BUTTON_SIZE = 100;
    private static final int BUTTON_MARGIN = 100;
    @SuppressWarnings("unused")
    private static final String TAG = GamepadController.class.getSimpleName();
    private Touchpad touchPad;
    private GamepadButton button;

    public GamepadController(ControllerInputHandler controllerInputHandler) {
        initTouchpad();
        touchPad.addListener(new GamepadJoystickListener(controllerInputHandler));
        button.addListener(new GamepadButtonListener(controllerInputHandler));
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

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });
        NoClickZone padZone = new NoClickZone(touchPad, 100);
        addActor(padZone);

//        addActor(touchPad);
        touchpadSkin.add("touchButton", new Texture("data/touchKnob.png"));
        button = new GamepadButton(touchpadSkin.getDrawable("touchButton"));
        button.setSize(BUTTON_SIZE, BUTTON_SIZE);
        NoClickZone buttonZone = new NoClickZone(button, 100);
        addActor(buttonZone);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    private static class GamepadButtonListener extends ClickListener {

        private final ControllerInputHandler controllerInputHandler;

        GamepadButtonListener(ControllerInputHandler controllerInputHandler) {
            this.controllerInputHandler = controllerInputHandler;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            controllerInputHandler.fire();
            event.stop();
            event.handle();
        }
    }

    // TODO: refactor use command pattern
    private static class GamepadJoystickListener extends ChangeListener {

        @SuppressWarnings("unused")
        private static final String TAG = GamepadJoystickListener.class.getSimpleName();
        private final ControllerInputHandler controllerInputHandler;

        GamepadJoystickListener(ControllerInputHandler controllerInputHandler) {
            this.controllerInputHandler = controllerInputHandler;
        }

        @Override
        public void changed(ChangeEvent event, Actor actor) {
            Touchpad touchpad = (Touchpad) actor;
            float knobPercentX = touchpad.getKnobPercentX();
            float knobPercentY = touchpad.getKnobPercentY();
            controllerInputHandler.accelerate(knobPercentX, knobPercentY);
            event.handle();
            event.stop();
        }
    }
}
