package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import no.ntnu.tdt4240.asteroids.controller.SettingsController;
import no.ntnu.tdt4240.asteroids.service.Assets;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;


public class SettingsView extends BaseView implements SettingsController.ISettingsView {

    @SuppressWarnings("unused")
    private static final String TAG = SettingsView.class.getSimpleName();
    private final Skin uiSkin = ServiceLocator.appComponent.getAssetLoader().getSkin(Assets.SkinAsset.UISKIN);
    private final TextButton backButton = new TextButton("Save", uiSkin);
    private final TextButton previousButton = new TextButton("Previous", uiSkin);
    private final TextButton nextButton = new TextButton("Next", uiSkin);
    private final TextButton decreaseVolumeButton = new TextButton("  -  ", uiSkin);
    private final TextButton increaseVolumeButton = new TextButton("  +  ", uiSkin);
    private final TextButton toggleMuteButton = new TextButton("Toggle mute", uiSkin);

    private final Table table = new Table();
    private boolean musicMuted = true;

    private final Label headlineLabel = new Label("Settings", uiSkin);
    private final Label changeCharacterLabel = new Label("Appearance:", uiSkin);
    private final SettingsController.ViewHandler controller;
    private final Assets assetsLoader = ServiceLocator.getAppComponent().getAssetLoader();
    private final Image currentCharacterImage = new Image(null, Scaling.fit, Align.center);


    public SettingsView(Batch batch, SettingsController.ViewHandler controller) {
        super(batch);
        this.controller = controller;
        table.getColor().a = 0;
        init();
        addActor(table);
        currentCharacterImage.setScale(0.5f);
    }

    @Override
    public void resume() {
        show();
    }

    @Override
    public void pause() {
        hide();
    }

    @Override
    public void show() {
        table.addAction(getDefaultShowAnimation());
    }

    @Override
    public void hide() {
        table.addAction(getDefaultHideAnimation());
    }

    @Override
    public void setCurrentCharacter(String textureString) {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(assetsLoader.getTexture(textureString)));
        currentCharacterImage.setDrawable(drawable);
    }

    @Override
    public void setMusicMuted(boolean muted) {
        musicMuted = muted;
    }

    private void init() {
        table.setFillParent(true);
        table.center();
//        table.row();
//        table.add(headlineLabel).space(10);
        table.row();
        table.add(new Label("Volume:", uiSkin)).space(10);
        table.row();
        HorizontalGroup h = new HorizontalGroup();
        h.space(10);
        h.addActor(decreaseVolumeButton);
        h.addActor(toggleMuteButton);
        h.addActor(increaseVolumeButton);
        table.add(h);
        table.row();
        table.add(changeCharacterLabel).space(10).align(Align.center);
        table.row();
        HorizontalGroup h2 = new HorizontalGroup();
        h2.space(10);
        h2.addActor(previousButton);
        h2.addActor(currentCharacterImage);
        h2.addActor(nextButton);
        table.add(h2);
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
                musicMuted = !musicMuted;
                controller.toggleMute(musicMuted);
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onBack();
            }
        });

        previousButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.previousCharacter();
            }
        });

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.nextCharacter();
            }
        });
    }
}
