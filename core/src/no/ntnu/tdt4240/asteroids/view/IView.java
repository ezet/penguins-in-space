package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.InputProcessor;

public interface IView {

    void update(float delta);

    void show();

    void resume();

    void hide();

    void pause();

    void draw();

    InputProcessor getInputProcessor();

    void resize(int width, int height);

}
