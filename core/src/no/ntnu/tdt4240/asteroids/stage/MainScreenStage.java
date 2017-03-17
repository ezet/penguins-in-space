package no.ntnu.tdt4240.asteroids.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class MainScreenStage extends Stage {

    private static final String TAG = MainScreenStage.class.getSimpleName();
    private static Viewport viewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    public final Skin buttonSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
    public final TextButton play = new TextButton("PLAY", buttonSkin);
    public final TextButton exit = new TextButton("EXIT", buttonSkin);
    private final Table table = new Table();
    private final BitmapFont defaultFont = new BitmapFont();
    private final Label.LabelStyle defaultLabelStyle = new Label.LabelStyle(defaultFont, Color.WHITE);
    private final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
    // TODO: implement main screen gui


    public MainScreenStage(Batch batch) {
        super(viewport, batch);
//        setDebugAll(true);
        init();
        addActor(table);
    }

    private void init() {
        play.getLabel().setFontScale(3);
        exit.getLabel().setFontScale(3);

        table.setFillParent(true);
        table.add(play).pad(30);
        table.row();
        table.add(exit).pad(30);
        table.row();
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
}
