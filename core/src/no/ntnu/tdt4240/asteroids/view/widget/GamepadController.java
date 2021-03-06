package no.ntnu.tdt4240.asteroids.view.widget;

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
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.TOUCH_BACKGROUND;
import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.TOUCH_KNOB;

// TODO: should extend WidgetGroup
public class GamepadController extends WidgetGroup {

    private static final int NO_CLICK_MARGIN = 50;
    private static final int TOUCHPAD_MARGIN = 30;
    private static final int TOUCHPAD_SIZE = 100;
    private static final int BUTTON_SIZE = 80;
    private static final int BUTTON_MARGIN = TOUCHPAD_MARGIN + TOUCHPAD_SIZE / 2 - BUTTON_SIZE / 2;
    @SuppressWarnings("unused")
    private static final String TAG = GamepadController.class.getSimpleName();
    private Touchpad touchPad;
    private GamepadButton button;
    private AssetService assetService;

    public GamepadController(ControllerInputHandler controllerInputHandler) {
        assetService = ServiceLocator.getAppComponent().getAssetService();
        initTouchpad();
        touchPad.addListener(new GamepadJoystickListener(controllerInputHandler));
        button.addListener(new GamepadButtonListener(controllerInputHandler));
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
        touchpadSkin.add("touchBackground", assetService.getTexture(TOUCH_BACKGROUND));
        touchpadSkin.add("touchKnob", assetService.getTexture(TOUCH_KNOB));
        Touchpad.TouchpadStyle style = new Touchpad.TouchpadStyle();

        style.background = touchpadSkin.getDrawable("touchBackground");
        style.knob = touchpadSkin.getDrawable("touchKnob");
        style.knob.setMinHeight(TOUCHPAD_SIZE / 2);
        style.knob.setMinWidth(TOUCHPAD_SIZE / 2);

        touchPad = new Touchpad(15, style);
        touchPad.setBounds(TOUCHPAD_MARGIN, TOUCHPAD_MARGIN, TOUCHPAD_SIZE, TOUCHPAD_SIZE);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });
        NoClickZone padZone = new NoClickZone(touchPad, NO_CLICK_MARGIN);
        addActor(padZone);

        touchpadSkin.add("touchButton", assetService.getTexture(TOUCH_KNOB));
        button = new GamepadButton(touchpadSkin.getDrawable("touchButton"));
        button.setSize(BUTTON_SIZE, BUTTON_SIZE);
        NoClickZone buttonZone = new NoClickZone(button, NO_CLICK_MARGIN);
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
