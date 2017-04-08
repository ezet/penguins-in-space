package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.tdt4240.asteroids.controller.ISettingsController;
import no.ntnu.tdt4240.asteroids.controller.SettingsController;
import no.ntnu.tdt4240.asteroids.service.Assets;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;


public class SettingsView extends BaseView implements SettingsController.ISettingsView {

    @SuppressWarnings("unused")
    private static final String TAG = SettingsView.class.getSimpleName();
    private final Skin uiSkin = ServiceLocator.appComponent.getAssetLoader().getSkin(Assets.SkinAsset.UISKIN);
    private final TextButton backButton = new TextButton("Save Settings", uiSkin);
    private final TextButton leftButton = new TextButton("Previous", uiSkin);
    private final TextButton rightButton = new TextButton("Next", uiSkin);
    private final TextButton decreaseVolumeButton = new TextButton("  -  ", uiSkin);
    private final TextButton increaseVolumeButton = new TextButton("  +  ", uiSkin);
    private final TextButton toggleMuteButton = new TextButton("Toggle mute", uiSkin);

    private final Table table = new Table();

    private final Label headlineLabel = new Label("Settings", uiSkin);
    private final Label changeCharacterLabel = new Label("Change the appearance of your character:", uiSkin);
    private final ISettingsController controller;
    private Label currentCharacterTextField = new Label(ServiceLocator.getAppComponent().getSettings().getPlayerAppearance().replace(".png", ""), uiSkin);


    public SettingsView(Batch batch, ISettingsController controller) {
        super(batch);
        this.controller = controller;
        setDebugAll(true);
        table.addAction(Actions.alpha(0));
        addActor(table);
        init();
        table.addAction(Actions.fadeIn(1));
    }

    public void setCurrentCharacter(String string) {
        currentCharacterTextField.setText(string.replace(".png", ""));
    }

    private void init() {
        table.setFillParent(true);
        table.center();
        table.row();
        table.add(headlineLabel).pad(10);
        table.row();
        table.add(new Label("Volume:", uiSkin)).pad(10);
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
            public void clicked(InputEvent event, float x, float y) {
                controller.previousCharacter();
            }
        });

        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.nextCharacter();
            }
        });
    }
}
