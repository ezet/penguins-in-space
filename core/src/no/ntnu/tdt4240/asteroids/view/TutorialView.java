package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import no.ntnu.tdt4240.asteroids.controller.ITutorialController;
import no.ntnu.tdt4240.asteroids.controller.TutorialController;

/**
 * Created by morte on 3/28/2017.
 */

public class TutorialView extends Stage implements TutorialController.ITutorialView {

    private static final String TAG = MainView.class.getSimpleName();
    private static Viewport viewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private final Skin buttonSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
    private final TextButton back = new TextButton("QUIT", buttonSkin);
    private final Table table = new Table();

    private final BitmapFont defaultFont = new BitmapFont();
    private final Label.LabelStyle defaultLabelStyle = new Label.LabelStyle(defaultFont, Color.WHITE);
    private final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
    private final Label label1 = new Label("Use the analog stick to the left to move around. " +
            "Use the right button to shoot.", defaultLabelStyle);
    private final Label label2 = new Label("Avoid running into the angry snowballs, they will hurt you! ", defaultLabelStyle);
    private final Label label3 = new Label("When you shoot the snowballs, they might drop new abilities...", defaultLabelStyle);

    private final ITutorialController controller;


    public TutorialView(Batch batch, ITutorialController controller) {
        super(viewport, batch);
        this.controller = controller;
        setDebugAll(true);
        table.addAction(Actions.alpha(0));
        addActor(table);
        init();
        table.addAction(Actions.fadeIn(1));
    }

    private void init() {
        back.getLabel().setFontScale(3);
        label1.setFontScale(3);
        label2.setFontScale(3);
        label3.setFontScale(3);
        table.setFillParent(true);
        //Labels
        table.row();
        table.add(label1).pad(30);
        table.row();
        table.add(label2).pad(30);
        table.row();
        table.add(label3).pad(30);

        //BACK button
        table.row();
        table.add(back).pad(30);

        back.addListener(new ClickListener() {
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
