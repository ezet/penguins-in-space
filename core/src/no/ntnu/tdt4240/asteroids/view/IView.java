package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.InputProcessor;

public interface IView {

    void update(float delta);

    void draw();

    InputProcessor getInputProcessor();

}
