package no.ntnu.tdt4240.asteroids.presenter;

import com.badlogic.gdx.Gdx;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;
import no.ntnu.tdt4240.asteroids.view.MainView;

public class MainMenuPresenter extends BasePresenter {

    @SuppressWarnings("unused")
    private static final String TAG = MainMenuPresenter.class.getSimpleName();
    private final Asteroids game;
    private final IView view;

    public MainMenuPresenter(final Asteroids game) {
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
            game.setScreen(new SpGamePresenter(game, MainMenuPresenter.this));
        }

        public void onMultiplayer() {
            game.setScreen(new MpMenuPresenter(game));
        }

        public void onQuit() {
            Gdx.app.exit();
        }

        public void onTutorial() {
            game.setScreen(new TutorialPresenter(game, MainMenuPresenter.this));
        }

        public void onSettings() {
            game.setScreen(new SettingsPresenter(game, MainMenuPresenter.this));
        }

        public void onShowAchievements() {
            ServiceLocator.getAppComponent().getNetworkService().showAchievement();
        }

        public void onShowLeaderboard() {
            ServiceLocator.getAppComponent().getNetworkService().showScore();
        }
    }
}
