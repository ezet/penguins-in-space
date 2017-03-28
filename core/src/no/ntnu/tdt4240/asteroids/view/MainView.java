package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import no.ntnu.tdt4240.asteroids.controller.IMainController;
import no.ntnu.tdt4240.asteroids.controller.MainController;


public class MainView extends Stage implements MainController.IMainView {

    private static final String TAG = MainView.class.getSimpleName();
    private static Viewport viewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private final Skin buttonSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
    private final TextButton play = new TextButton("PLAY", buttonSkin);
    private final TextButton quit = new TextButton("QUIT", buttonSkin);
    private final Table table = new Table();
    private final BitmapFont defaultFont = new BitmapFont();
    private final Label.LabelStyle defaultLabelStyle = new Label.LabelStyle(defaultFont, Color.WHITE);
    private final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
    private final IMainController controller;

    // TODO: implement main screen gui


    public MainView(Batch batch, IMainController controller) {
        super(viewport, batch);
        this.controller = controller;
        setDebugAll(true);
        table.addAction(Actions.alpha(0));
        addActor(table);
        init();
        table.addAction(Actions.fadeIn(1));
    }

    private void init() {
        play.getLabel().setFontScale(3);
        quit.getLabel().setFontScale(3);
        table.setFillParent(true);
        table.add(play).pad(30);
        table.row();
        table.add(quit).pad(30);
        table.row();
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.addAction(Actions.sequence(Actions.fadeOut(1), new RunnableAction() {
                    @Override
                    public void run() {
                        controller.onPlay();
                    }
                }));
            }
        });
        quit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onQuit();
            }
        });
    }

    @Override
    public void update(float delta) {
        act(delta);
    }

    @Override
    public void draw() {
//        Batch batch = getBatch();
//        batch.disableBlending();
//        batch.begin();
//         TODO: draw background
//        batch.end();
//        batch.enableBlending();
        super.draw();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }
}
