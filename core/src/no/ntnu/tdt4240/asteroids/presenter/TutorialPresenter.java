package no.ntnu.tdt4240.asteroids.presenter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.view.TutorialView;

public class TutorialPresenter extends BasePresenter {
    private static final String TAG = TutorialPresenter.class.getSimpleName();
    private final Asteroids game;
    private final IView view;
    private Screen parent;

    public TutorialPresenter(final Asteroids game, final Screen parent) {
        this.parent = parent;
        this.game = game;
        this.view = new TutorialView(game.getBatch(), new ViewHandler());
    }

    private void update(float delta) {
        view.update(delta);
    }

    private void draw() {
        view.draw();
    }

    @Override
    public no.ntnu.tdt4240.asteroids.view.IView getView() {
        return view;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
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

    public interface IView extends no.ntnu.tdt4240.asteroids.view.IView {
    }

    public class ViewHandler {
        public void onBack() {
            game.setScreen(parent);
            dispose();
        }

    }
}
