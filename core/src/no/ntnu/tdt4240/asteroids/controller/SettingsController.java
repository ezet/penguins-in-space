package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Array;
import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.service.audio.AudioManager;
import no.ntnu.tdt4240.asteroids.view.IView;
import no.ntnu.tdt4240.asteroids.view.SettingsView;



public class SettingsController extends ScreenAdapter implements ISettingsController {

    private static final String TAG = MainMenu.class.getSimpleName();
    private final Asteroids game;
    private final SettingsView view;
    private Screen parent;
    private int soundVolume = 100;
    private AudioManager audioManager;

    public SettingsController(final Asteroids game, final Screen parent){
        this.parent = parent;
        this.game = game;
        this.view = new SettingsView(game.getBatch(),this);
        this.audioManager = ServiceLocator.getAppComponent().getAudioManager();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void onQuitLevel() {
        game.setScreen(this.parent);
        dispose();

    }

    @Override
    public void previousCharacter() {
        Array<String> characters = ServiceLocator.getAppComponent().getAssetLoader().getCharacters();
        int index = characters.indexOf(ServiceLocator.getAppComponent().getSettings().getPlayerAppearance(), false);
        if (index == 0) return;
        ServiceLocator.getAppComponent().getSettings().setPlayerAppearance(characters.get(index-1));
        view.setCurrentCharacter(ServiceLocator.getAppComponent().getSettings().getPlayerAppearance());
    }

    @Override
    public void nextCharacter() {
        Array<String> characters = ServiceLocator.getAppComponent().getAssetLoader().getCharacters();
        int index = characters.indexOf(ServiceLocator.getAppComponent().getSettings().getPlayerAppearance(), false);
        if (index == characters.size-1) return;
        ServiceLocator.getAppComponent().getSettings().setPlayerAppearance(characters.get(index+1));
        view.setCurrentCharacter(ServiceLocator.getAppComponent().getSettings().getPlayerAppearance());
    }

    @Override
    public void toggleMute() {
        soundVolume = (soundVolume == 100) ? 0: 100;
        audioManager.update(soundVolume);
    }

    @Override
    public void increaseVolume() {
        if (soundVolume < 100){
            soundVolume += 10;
            audioManager.update(soundVolume);
        }
    }

    @Override
    public void decreaseVolume() {
        if (soundVolume > 0){
            soundVolume -= 10;
            audioManager.update(soundVolume);
        }
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(view.getInputProcessor());

    }

    @Override
    public void hide() {
        super.hide();
        Gdx.input.setInputProcessor(null);

    }

    private void update(float delta){
        view.update(delta);
    }

    private void draw(){
        view.draw();
    }

    public interface ISettingsView extends IView {
    }
}
