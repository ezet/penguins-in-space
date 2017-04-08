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


public class ScoreScreenController extends ScreenAdapter implements IScoreScreenController {

    @SuppressWarnings("unused")
    private static final String TAG = MainMenu.class.getSimpleName();
    private final Asteroids game;
    private final ScoreScreenView view;
    private Screen parent;
    private List<PlayerData> playerData;

    public ScoreScreenController(final Asteroids game, final Screen parent, List<PlayerData> playerData){
        this.parent = parent;
        this.game = game;
        this.playerData = playerData;
        this.view = new ScoreScreenView(game.getBatch(), this);
        Collections.sort(playerData, new Comparator<PlayerData>() {
            @Override
            public int compare(PlayerData s, PlayerData t1) {
                return t1.totalScore - s.totalScore;
            }
        });
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
    public void show() {
        super.show();
        view.displayScores(playerData);
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

    public interface IScoreScreenView extends IView {
        void displayScores(List<PlayerData> data);
    }
}
