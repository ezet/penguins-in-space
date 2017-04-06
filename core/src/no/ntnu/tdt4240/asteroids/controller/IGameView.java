package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;

import no.ntnu.tdt4240.asteroids.view.IView;

public interface IGameView extends IView {

    void setInputController(Actor inputController);

    void updateScore(int score);

    void updateLevel(int level);

    void setDebug(boolean debug);

    void resize(int width, int height);

    void updateHitpoints(int hitpoints);
}
