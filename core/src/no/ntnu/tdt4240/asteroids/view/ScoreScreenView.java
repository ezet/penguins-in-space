package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.controller.IScoreScreenController;
import no.ntnu.tdt4240.asteroids.controller.ScoreScreenController;


public class ScoreScreenView extends Stage implements ScoreScreenController.IScoreScreenView {

    private static final String TAG = ScoreScreenView.class.getSimpleName();
    private static OrthographicCamera camera = new OrthographicCamera();
    private static Viewport guiViewport = new FitViewport(Asteroids.GUI_VIRTUAL_WIDTH, Asteroids.GUI_VIRTUAL_HEIGHT, camera);

    private final Table table = new Table();

    private final BitmapFont defaultFont = new BitmapFont();
    private final Label.LabelStyle defaultLabelStyle = new Label.LabelStyle(defaultFont, Color.WHITE);
    private final Skin buttonSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
    private final TextButton menuButton = new TextButton("Quit to menu", buttonSkin);

    private final IScoreScreenController controller;

    public ScoreScreenView(Batch batch, IScoreScreenController controller) {
        super(guiViewport, batch);
        this.controller = controller;
        guiViewport.apply(true);
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
        Label winner = new Label("Last penguin standing: " + scores.get(0).split(" ")[0], defaultLabelStyle);
        table.add(winner).pad(10);

        for(int i = 0; i < scores.size(); i++){
            table.row();
            Label l = new Label(String.valueOf(i+1) + ". " + scores.get(i), defaultLabelStyle);
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


    @Override
    public void update(float delta) {
        act(delta);
    }

    @Override
    public void draw() {
        super.draw();

    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }

    @Override
    public void resize(int width, int height) {

    }



}
