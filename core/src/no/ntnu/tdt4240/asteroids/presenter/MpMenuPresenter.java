package no.ntnu.tdt4240.asteroids.presenter;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.model.PlayerData;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.view.MultiplayerMenuView;

public class MpMenuPresenter extends BasePresenter {

    @SuppressWarnings("unused")
    private static final String TAG = MpMenuPresenter.class.getSimpleName();
    private final Asteroids game;
    private final IView view;


    public MpMenuPresenter(final Asteroids game) {
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
            MpGamePresenter screen = new MpGamePresenter(game, MpMenuPresenter.this);
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
            game.setScreen(new MainMenuPresenter(game));
        }

        public void onInvitePlayers() {
            ServiceLocator.getAppComponent().getNetworkService().startSelectOpponents(false);
        }

    }

}
