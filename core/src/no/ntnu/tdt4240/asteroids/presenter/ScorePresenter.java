package no.ntnu.tdt4240.asteroids.presenter;

import com.badlogic.gdx.Screen;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.model.PlayerData;
import no.ntnu.tdt4240.asteroids.view.IView;
import no.ntnu.tdt4240.asteroids.view.ScoreView;


public class ScorePresenter extends BasePresenter {

    @SuppressWarnings("unused")
    private static final String TAG = MainMenuPresenter.class.getSimpleName();
    private final Asteroids game;
    private final IScoreScreenView view;
    private Screen parent;
    private List<PlayerData> playerData;

    public ScorePresenter(final Asteroids game, final Screen parent, List<PlayerData> playerData) {
        this.parent = parent;
        this.game = game;
        this.playerData = playerData;
        this.view = new ScoreView(game.getBatch(), new ViewHandler());
        Collections.sort(playerData, new Comparator<PlayerData>() {
            @Override
            public int compare(PlayerData s, PlayerData t1) {
                return t1.totalScore - s.totalScore;
            }
        });
    }

    @Override
    public IView getView() {
        return view;
    }

    @Override
    public void show() {
        super.show();
        view.displayScores(playerData);
    }

    public interface IScoreScreenView extends IView {
        void displayScores(List<PlayerData> data);
    }

    public class ViewHandler {

        public void onBack() {
            game.setScreen(parent);
            dispose();

        }
    }
}
