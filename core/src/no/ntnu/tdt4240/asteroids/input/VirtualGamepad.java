package no.ntnu.tdt4240.asteroids.input;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class VirtualGamepad extends Group {

    public static final int BUTTON_SIZE = 100;
    public static final int BUTTON_MARGIN = 100;
    private static final String TAG = VirtualGamepad.class.getSimpleName();
    private Touchpad touchPad;
    private TouchpadButton button;

    public VirtualGamepad() {
        initTouchpad();
        initButton();
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        button.setPosition(getStage().getWidth() - BUTTON_SIZE - BUTTON_MARGIN, BUTTON_MARGIN);
    }

    public void addJoystickListener(ChangeListener changeListener) {
        touchPad.addListener(changeListener);
    }

    public void addButtonListener(ClickListener clickListener) {
        button.addListener(clickListener);
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
        button = new TouchpadButton(touchpadSkin.getDrawable("touchButton"));
        button.setSize(BUTTON_SIZE, BUTTON_SIZE);
        addActor(button);
    }

    private void initButton() {


    }
}
