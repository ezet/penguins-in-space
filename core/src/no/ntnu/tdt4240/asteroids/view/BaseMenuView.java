package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.List;

abstract class BaseMenuView extends BaseView {

    @SuppressWarnings("unused")
    private static final String TAG = BaseMenuView.class.getSimpleName();
    private Table table;

    BaseMenuView(Batch batch) {
        super(batch);
    }

    protected abstract List<TextButton> getButtons();

    public void startAction(RunnableAction action) {
        table.addAction(new SequenceAction(getDefaultHideAnimation(), action));
    }

    @Override
    public void update(float delta) {
        super.update(delta);
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
    public void show() {
        if (table == null) {
            createMenuTable();
            table.getColor().a = 0;
            addActor(table);
        }
        table.addAction(getDefaultShowAnimation());
    }

    @Override
    public void hide() {
        table.getColor().a = 0;
    }

    private void createMenuTable() {
        final List<TextButton> buttons = getButtons();
        table = new Table();
        table.setFillParent(true);
        int totalButtonHeight = 0;
        for (TextButton button : buttons) {
            totalButtonHeight += button.getHeight();
        }
        int remainingSpace = (int) (getHeight() - totalButtonHeight - totalButtonHeight / buttons.size());
        int space = remainingSpace / buttons.size();
        for (TextButton button : buttons) {
            table.add(button).space(space).row();
        }
    }
}
