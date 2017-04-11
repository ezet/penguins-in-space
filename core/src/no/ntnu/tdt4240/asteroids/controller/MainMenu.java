package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.Gdx;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.view.MainView;

public class MainMenu extends BaseController {

    @SuppressWarnings("unused")
    private static final String TAG = MainMenu.class.getSimpleName();
    private final Asteroids game;
    private final IView view;

    public MainMenu(final Asteroids game) {
        this.game = game;
        this.view = new MainView(game.getBatch(), new ViewHandler());
    }

    @Override
    public no.ntnu.tdt4240.asteroids.view.IView getView() {
        return view;
    }

    public interface IView extends no.ntnu.tdt4240.asteroids.view.IView {
    }

    public class ViewHandler {

        public void onPlay() {
            game.setScreen(new SingleplayerGame(game, MainMenu.this));
        }

        public void onMultiplayer() {
            game.setScreen(new MultiplayerMenu(game));
        }

        public void onQuit() {
            Gdx.app.exit();
        }

        public void onTutorial() {
            game.setScreen(new TutorialController(game, MainMenu.this));
        }

        public void onSettings() {
            game.setScreen(new SettingsController(game, MainMenu.this));
        }

        public void onShowAchievements() {
            ServiceLocator.getAppComponent().getNetworkService().showAchievement();
        }

        public void onShowLeaderboard() {
            ServiceLocator.getAppComponent().getNetworkService().showScore();
        }
    }
}
