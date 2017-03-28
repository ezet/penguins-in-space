package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;

import no.ntnu.tdt4240.asteroids.view.IView;

public interface IGameController {
    void onPause();

    void onResume();

    void onQuitLevel();

    void onQuit();

}
