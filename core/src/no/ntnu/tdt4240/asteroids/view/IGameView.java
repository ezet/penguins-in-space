package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface IGameView extends IView {

    void setInputController(Actor inputController);

    void updateScore(int score);

    void updateLevel(int level);

    void setDebug(boolean debug);

    void resize(int width, int height);
}
