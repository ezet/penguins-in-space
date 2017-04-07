package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.List;

import no.ntnu.tdt4240.asteroids.controller.IScoreScreenController;
import no.ntnu.tdt4240.asteroids.controller.ScoreScreenController;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;


public class ScoreScreenView extends BaseView implements ScoreScreenController.IScoreScreenView {

    @SuppressWarnings("unused")
    private static final String TAG = ScoreScreenView.class.getSimpleName();
    private final Table table = new Table();
    private final Skin uiSkin = ServiceLocator.appComponent.getAssetLoader().getUiSkin();
    private final TextButton menuButton = new TextButton("Quit to menu", uiSkin);

    private final IScoreScreenController controller;

    public ScoreScreenView(Batch batch, IScoreScreenController controller) {
        super(batch);
        this.controller = controller;
        setDebugAll(true);
        table.addAction(Actions.alpha(0));
        addActor(table);
        init();
        table.addAction(Actions.fadeIn(1));
    }

    private void init() {
        table.setFillParent(true);
        List<String> scores = controller.getScores();
        table.row();
        Label winner = new Label("Last penguin standing: " + scores.get(0).split(" ")[0], uiSkin);
        table.add(winner).pad(10);

        for (int i = 0; i < scores.size(); i++) {
            table.row();
            Label l = new Label(String.valueOf(i + 1) + ". " + scores.get(i), uiSkin);
            table.add(l).pad(5);
        }

        table.row();
        table.add(menuButton).pad(30);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onQuitLevel();
            }
        });
    }

}
