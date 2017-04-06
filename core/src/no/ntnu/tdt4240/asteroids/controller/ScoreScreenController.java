package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;

import java.util.List;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.view.IView;
import no.ntnu.tdt4240.asteroids.view.ScoreScreenView;


public class ScoreScreenController extends ScreenAdapter implements IScoreScreenController {

    private static final String TAG = MainMenu.class.getSimpleName();
    private final Asteroids game;
    private final ScoreScreenView view;
    private Screen parent;
    private List<String> playersAndScores;

    public ScoreScreenController(final Asteroids game, final Screen parent, List<String> playersAndScores){
        this.parent = parent;
        this.game = game;
        this.playersAndScores = playersAndScores;
        this.view = new ScoreScreenView(game.getBatch(), this);
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
    public List<String> getScores() {
        return playersAndScores;
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

    public interface IScoreScreenView extends IView {
    }
}
