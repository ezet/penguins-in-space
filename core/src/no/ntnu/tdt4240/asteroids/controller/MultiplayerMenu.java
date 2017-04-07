package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.model.PlayerData;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.view.IView;
import no.ntnu.tdt4240.asteroids.view.MultiplayerView;

public class MultiplayerMenu extends ScreenAdapter implements IMultiplayerMenu {

    @SuppressWarnings("unused")
    private static final String TAG = MultiplayerMenu.class.getSimpleName();
    private final Asteroids game;
    private final MultiplayerView view;


    public MultiplayerMenu(final Asteroids game) {
        this.game = game;
        view = new MultiplayerView(game.getBatch(), this);
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

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        view.resize(width, height);
    }

    private void update(float delta) {
        view.update(delta);
    }

    private void draw() {
        view.draw();
    }

    @Override
    public void onQuickgame() {
        MultiplayerGame screen = new MultiplayerGame(game, this);
        game.setScreen(screen);
        List<PlayerData> players = new ArrayList<>();
        players.add(new PlayerData("player", "Player", true));
        players.add(new PlayerData("test1", "test1"));
        players.add(new PlayerData("test2", "test2"));
        players.add(new PlayerData("test3", "test3"));
        screen.onRoomReady(players);
    }

    @Override
    public void onHostGame() {

    }

    @Override
    public void onBack() {
        game.setScreen(new MainMenu(game));
    }

    @Override
    public void onInvitePlayers() {
        ServiceLocator.getAppComponent().getNetworkService().startSelectOpponents();
    }

    public interface IMainView extends IView {
        void resize(int width, int height);
    }
}
