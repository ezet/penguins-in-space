package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Array;
import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.view.SettingsView;



public class SettingsController extends ScreenAdapter implements ISettingsController {

    private static final String TAG = MainController.class.getSimpleName();
    private final Asteroids game;
    private final SettingsView view;
    private Screen parent;

    public SettingsController(final Asteroids game, final Screen parent){
        this.parent = parent;
        this.game = game;
        this.view = new SettingsView(game.getBatch(),this);
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
        Array<String> characters = ServiceLocator.gameComponent.getAssetLoader().getCharacters();
        int index = characters.indexOf(ServiceLocator.gameComponent.getGameSettings().playerAppearance, false);
        if (index == 0) return;
        ServiceLocator.gameComponent.getGameSettings().playerAppearance = characters.get(index-1);
        view.setCurrentCharacter(ServiceLocator.gameComponent.getGameSettings().playerAppearance);
    }

    @Override
    public void nextCharacter() {
        Array<String> characters = ServiceLocator.gameComponent.getAssetLoader().getCharacters();
        int index = characters.indexOf(ServiceLocator.gameComponent.getGameSettings().playerAppearance, false);
        if (index == characters.size-1) return;
        ServiceLocator.gameComponent.getGameSettings().playerAppearance = characters.get(index+1);
        view.setCurrentCharacter(ServiceLocator.gameComponent.getGameSettings().playerAppearance);
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

}
