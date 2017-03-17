package no.ntnu.tdt4240.asteroids.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class GameScreenStage extends Stage {

    // TODO: define proper default GUI resources like font, label style etc.
    private static Viewport guiViewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private Table table;
    private BitmapFont defaultFont = new BitmapFont();
    private Label.LabelStyle defaultLabelStyle = new Label.LabelStyle(defaultFont, Color.WHITE);
    private Label scoreLabel = new Label("SCORE: 0", defaultLabelStyle);
    private Label levelLabel = new Label("LEVEL: 0", defaultLabelStyle);

    public GameScreenStage(Batch batch) {
        super(guiViewport, batch);
        table = new Table();
        this.addActor(table);
        initGui();
//        setDebugAll(true);
    }

    public void setInputController(Actor inputController) {
        getActors().removeValue(inputController, true);
        addActor(inputController);
    }

    private void initGui() {
        table.setFillParent(true);
        table.top();
        scoreLabel.scaleBy(2);
        levelLabel.scaleBy(2);
        table.add(scoreLabel).pad(20);
        table.add(levelLabel).pad(20);
    }

    public void setScore(int score) {
        scoreLabel.setText("SCORE: " + score);
    }

    public void setLevel(int level) {
        levelLabel.setText("LEVEL: " + level);
    }


}
