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

import no.ntnu.tdt4240.asteroids.model.PlayerData;
import no.ntnu.tdt4240.asteroids.presenter.ScorePresenter;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;


public class ScoreView extends BaseView implements ScorePresenter.IScoreScreenView {

    @SuppressWarnings("unused")
    private static final String TAG = ScoreView.class.getSimpleName();
    private final Table table = new Table();
    private final Skin uiSkin = ServiceLocator.appComponent.getAssetService().getSkin(AssetService.SkinAsset.UISKIN);
    private final TextButton menuButton = new TextButton("Quit to menu", uiSkin);

    private final ScorePresenter.ViewHandler controller;

    public ScoreView(Batch batch, ScorePresenter.ViewHandler controller) {
        super(batch);
        this.controller = controller;
        table.addAction(Actions.alpha(0));
        addActor(table);
        table.setFillParent(true);
    }

    private String getBestText(PlayerData player) {
        String text = "";
        if (player.alltimeBest)
            text = " ALL TIME BEST!";
        else if (player.weeklyBest)
            text = " WEEKLY BEST!";
        else if (player.dailyBest)
            text = " DAILY BEST!";
        return text;
    }

    @Override
    public void displayScores(List<PlayerData> data) {
        table.clear();
        table.row();
        Label winner = new Label("Last penguin standing: " + data.get(0).displayName, uiSkin);
        table.add(winner).pad(10);

        for (int i = 0; i < data.size(); i++) {
            int rank = i + 1;
            PlayerData player = data.get(i);
            table.row();
            String newBest = getBestText(player);
            Label l = new Label(rank + ". " + player.displayName + " " + data.get(i).totalScore + newBest, uiSkin);
            table.add(l).pad(5);
        }
        table.row();
        table.add(menuButton).pad(30);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onBack();
            }
        });
        table.addAction(Actions.fadeIn(1));
    }

}
