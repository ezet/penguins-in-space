package no.ntnu.tdt4240.asteroids.controller;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.model.PlayerData;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.view.MultiplayerMenuView;

public class MultiplayerMenu extends BaseController {

    @SuppressWarnings("unused")
    private static final String TAG = MultiplayerMenu.class.getSimpleName();
    private final Asteroids game;
    private final IView view;


    public MultiplayerMenu(final Asteroids game) {
        this.game = game;
        view = new MultiplayerMenuView(game.getBatch(), new ViewHandler());
    }

    @Override
    public no.ntnu.tdt4240.asteroids.view.IView getView() {
        return view;
    }

    public interface IView extends no.ntnu.tdt4240.asteroids.view.IView {
    }

    public class ViewHandler {
        public void onQuickgame() {
            MultiplayerGame screen = new MultiplayerGame(game, MultiplayerMenu.this);
            game.setScreen(screen);
            List<PlayerData> players = new ArrayList<>();
            players.add(new PlayerData("player", "Player", true));
            players.add(new PlayerData("test1", "test1"));
            players.add(new PlayerData("test2", "test2"));
            players.add(new PlayerData("test3", "test3"));
            screen.onRoomReady(players);
        }

        public void onHostGame() {

        }



        public void onBack() {
            game.setScreen(new MainMenu(game));
        }

        public void onInvitePlayers() {
            ServiceLocator.getAppComponent().getNetworkService().startSelectOpponents();
        }

    }

}
