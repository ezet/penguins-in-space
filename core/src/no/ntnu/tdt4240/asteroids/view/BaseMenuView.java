package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.List;

abstract class BaseMenuView extends BaseView {

    private static final String TAG = BaseMenuView.class.getSimpleName();
    private Table table;

    BaseMenuView(Batch batch) {
        super(batch);
    }

    private static Action getShowAnimation() {
        return Actions.fadeIn(0.5f);
    }

    private static Action getHideAnimation() {
        return Actions.fadeOut(0.5f);
    }

    protected abstract List<TextButton> getButtons();

    public void startAction(RunnableAction action) {
        table.addAction(new SequenceAction(getHideAnimation(), action));
    }

    @Override
    public void show() {
        if (table == null) {
            createMenuTable();
            table.getColor().a = 0;
            addActor(table);
        }
        table.addAction(getShowAnimation());
    }

    @Override
    public void hide() {
        table.getColor().a = 0;
    }

    @Override
    public void resume() {
//        show();
    }

    @Override
    public void pause() {
//        hide();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    private void createMenuTable() {
        final List<TextButton> buttons = getButtons();
        table = new Table();
        table.setFillParent(true);
        int totalButtonHeight = 0;
        for (TextButton button : buttons) {
            totalButtonHeight += button.getHeight();
        }
        int remainingSpace = (int) (getHeight() - totalButtonHeight);
        int space = remainingSpace / buttons.size();

        for (TextButton button : buttons) {
            table.add(button).space(space).row();
        }
    }
}
