package no.ntnu.tdt4240.asteroids.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import no.ntnu.tdt4240.asteroids.view.IView;

abstract class BaseController extends ScreenAdapter {
    @SuppressWarnings("unused")
    private static final String TAG = BaseController.class.getSimpleName();

    public abstract IView getView();

    @Override
    public void show() {
//        Gdx.app.debug(TAG, "show: ");
        super.show();
        getView().show();
        Gdx.input.setInputProcessor(getView().getInputProcessor());
    }

    @Override
    public void hide() {
//        Gdx.app.debug(TAG, "hide: ");
        Gdx.input.setInputProcessor(null);
        getView().hide();
    }

    @Override
    public void render(float delta) {
        getView().update(delta);
        getView().draw();
    }

    @Override
    public void resize(int width, int height) {
        getView().resize(width, height);
    }

    @Override
    public void pause() {
        getView().pause();
    }

    @Override
    public void resume() {
        getView().resume();
    }

    @Override
    public void dispose() {
//        Gdx.app.debug(TAG, "dispose: ");
        super.dispose();
    }

}
