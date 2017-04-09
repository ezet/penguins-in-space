package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.model.PlayerData;
import no.ntnu.tdt4240.asteroids.view.IView;
import no.ntnu.tdt4240.asteroids.view.ScoreScreenView;


public class ScoreScreenController extends BaseController {

    @SuppressWarnings("unused")
    private static final String TAG = MainMenu.class.getSimpleName();
    private final Asteroids game;
    private final IScoreScreenView view;
    private Screen parent;
    private List<PlayerData> playerData;

    public ScoreScreenController(final Asteroids game, final Screen parent, List<PlayerData> playerData){
        this.parent = parent;
        this.game = game;
        this.playerData = playerData;
        this.view = new ScoreScreenView(game.getBatch(), new ViewHandler());
        Collections.sort(playerData, new Comparator<PlayerData>() {
            @Override
            public int compare(PlayerData s, PlayerData t1) {
                return t1.totalScore - s.totalScore;
            }
        });
    }

    @Override
    public void show() {
        super.show();
        view.displayScores(playerData);
    }

    @Override
    public IView getView() {
        return view;
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
