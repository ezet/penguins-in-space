package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.scenario.Settings;

import no.ntnu.tdt4240.asteroids.controller.ISettingsController;
import no.ntnu.tdt4240.asteroids.controller.SettingsController;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;


public class SettingsView extends Stage implements SettingsController.ISettingsView {

    private static final String TAG = SettingsView.class.getSimpleName();
    private static Viewport viewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private final Skin buttonSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
    private final TextButton backButton = new TextButton("Save Settings", buttonSkin);
    private final TextButton leftButton = new TextButton("Previous", buttonSkin);
    private final TextButton rightButton = new TextButton("Next", buttonSkin);

    private final Table table = new Table();

    private final BitmapFont defaultFont = new BitmapFont();
    private final Label.LabelStyle defaultLabelStyle = new Label.LabelStyle(defaultFont, Color.WHITE);
    private final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
    private final Label headlineLablel = new Label("Settings", defaultLabelStyle);
    private final Label changeCharacterLabel = new Label("Change the appearance of your character:", defaultLabelStyle);
    private Label currentCharacterTextField = new Label(ServiceLocator.gameComponent.getGameSettings().playerAppearance.replace(".png", ""), defaultLabelStyle);

    private final ISettingsController controller;


    public void setCurrentCharacter(String string){
        currentCharacterTextField.setText(string.replace(".png",""));
    }

    public SettingsView(Batch batch, ISettingsController controller) {
        super(viewport, batch);
        this.controller = controller;
        setDebugAll(true);
        table.addAction(Actions.alpha(0));
        addActor(table);
        init();
        table.addAction(Actions.fadeIn(1));
    }

    private void init() {
        backButton.getLabel().setFontScale(5);
        headlineLablel.setFontScale(7);
        changeCharacterLabel.setFontScale(4);
        currentCharacterTextField.setFontScale(4);
        leftButton.getLabel().setFontScale(4);
        rightButton.getLabel().setFontScale(4);

        table.setFillParent(true);

        table.row();
        table.add(headlineLablel).pad(30);
        table.row();
        table.add(changeCharacterLabel).pad(30);
        table.row();
        table.add(leftButton).pad(30);
        table.row();
        table.add(currentCharacterTextField).pad(30);
        table.row();
        table.add(rightButton).pad(30);


        table.row();
        table.add(backButton).pad(30);

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
