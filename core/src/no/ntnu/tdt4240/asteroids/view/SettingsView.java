package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.controller.ISettingsController;
import no.ntnu.tdt4240.asteroids.controller.SettingsController;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;


public class SettingsView extends Stage implements SettingsController.ISettingsView {

    private static final String TAG = SettingsView.class.getSimpleName();
    private static Viewport viewport = new FitViewport(Asteroids.GUI_VIRTUAL_WIDTH, Asteroids.GUI_VIRTUAL_HEIGHT);
    private final Skin buttonSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
    private final TextButton backButton = new TextButton("Save Settings", buttonSkin);
    private final TextButton leftButton = new TextButton("Previous", buttonSkin);
    private final TextButton rightButton = new TextButton("Next", buttonSkin);
    private final TextButton decreaseVolumeButton = new TextButton("  -  " , buttonSkin);
    private final TextButton increaseVolumeButton = new TextButton("  +  ", buttonSkin);
    private final TextButton toggleMuteButton = new TextButton("Toggle mute", buttonSkin);

    private final Table table = new Table();

    private final BitmapFont defaultFont = new BitmapFont();
    private final Label.LabelStyle defaultLabelStyle = new Label.LabelStyle(defaultFont, Color.WHITE);
    private final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
    private final Label headlineLablel = new Label("Settings", defaultLabelStyle);
    private final Label changeCharacterLabel = new Label("Change the appearance of your character:", defaultLabelStyle);
    private Label currentCharacterTextField = new Label(ServiceLocator.getAppComponent().getSettings().getPlayerAppearance().replace(".png", ""), defaultLabelStyle);

    private final ISettingsController controller;


    public void setCurrentCharacter(String string){
        currentCharacterTextField.setText(string.replace(".png",""));
    }

    public SettingsView(Batch batch, ISettingsController controller) {
        super(viewport, batch);
        this.controller = controller;
        viewport.apply(true);
        setDebugAll(true);
        table.addAction(Actions.alpha(0));
        addActor(table);
        init();
        table.addAction(Actions.fadeIn(1));
    }

    private void init() {
        table.setFillParent(true);
        table.center();
        table.row();
        table.add(headlineLablel).pad(10);
        table.row();
        table.add(new Label("Volume:", defaultLabelStyle)).pad(10);
        table.row();
        HorizontalGroup h = new HorizontalGroup();
        h.space(10);
        h.addActor(decreaseVolumeButton);
        h.addActor(toggleMuteButton);
        h.addActor(increaseVolumeButton);
        table.add(h);
        table.row();
        table.add(changeCharacterLabel).pad(10);
        table.row();
        table.add(leftButton);
        table.row();
        table.add(currentCharacterTextField);
        table.row();
        table.add(rightButton);
        table.row();


        table.add(backButton).pad(10);

        decreaseVolumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.decreaseVolume();
            }
        });

        increaseVolumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.increaseVolume();
            }
        });

        toggleMuteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.toggleMute();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onQuitLevel();
            }
        });

        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                controller.previousCharacter();
            }
        });

        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                controller.nextCharacter();
            }
        });
    }


    @Override
    public void update(float delta) {
        act(delta);
    }

    @Override
    public void draw() {
        super.draw();

    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }

    @Override
    public void resize(int width, int height) {

    }



}
