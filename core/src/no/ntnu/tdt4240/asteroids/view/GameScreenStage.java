package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import no.ntnu.tdt4240.asteroids.controller.GameScreen;


public class GameScreenStage extends Stage implements IGameScreenView {

    private static final String TAG = GameScreenStage.class.getSimpleName();
    // TODO: define proper default GUI resources like font, label style etc.
    private static Viewport guiViewport = new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private final GameScreen.InputHandler inputHandler;
    private final Table table = new Table();
    private final BitmapFont defaultFont = new BitmapFont();
    private final Label.LabelStyle defaultLabelStyle = new Label.LabelStyle(defaultFont, Color.WHITE);
    private final Label scoreLabel = new Label("SCORE: 0", defaultLabelStyle);
    private final Label levelLabel = new Label("LEVEL: 0", defaultLabelStyle);
    private final Skin buttonSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
    private final TextButton resume = new TextButton("RESUME", buttonSkin);
    private final TextButton settings = new TextButton("SETTINGS", buttonSkin);
    private final TextButton quitToMenu = new TextButton("QUIT TO MENU", buttonSkin);
    private final TextButton quit = new TextButton("QUIT", buttonSkin);
    private Cell mainMenuCell;


    public GameScreenStage(Batch batch, GameScreen.InputHandler inputHandler) {
        super(guiViewport, batch);
        this.inputHandler = inputHandler;
        this.addActor(table);
        initGui();
        setDebugAll(true);
    }

    @Override
    public void setInputController(Actor inputController) {
        getActors().removeValue(inputController, true);
        addActor(inputController);
    }

    // TODO: clean up
    private void initGui() {
        table.setFillParent(true);
        table.top();
        // TODO: use proper style, remove scaling
        scoreLabel.setFontScale(2);
        levelLabel.setFontScale(2);
        table.add(scoreLabel).space(20);
        table.add(levelLabel).space(20);
        table.row();

        resume.getLabel().setFontScale(3);
        settings.getLabel().setFontScale(3);
        quitToMenu.getLabel().setFontScale(3);
        quit.getLabel().setFontScale(3);

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.setVisible(false);
        verticalGroup.getColor().a = 0;
        mainMenuCell = table.add(verticalGroup).colspan(2).expandY();
        verticalGroup.space(20);
        verticalGroup.addActor(resume);
        verticalGroup.addActor(settings);
        verticalGroup.addActor(quit);
        verticalGroup.addActor(quitToMenu);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.isHandled()) return;
                Gdx.app.debug(TAG, "ChangeListener: pause");
                pauseGame();
                event.handle();
            }
        });

        resume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (event.isHandled()) return;
                Gdx.app.debug(TAG, "ChangeListener: resume");
                resumeGame();
                event.handle();
            }
        });
        settings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (event.isHandled()) return;
                Gdx.app.debug(TAG, "ChangeListener: settings");
                settings();
                event.handle();
            }
        });
        quitToMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (event.isHandled()) return;
                Gdx.app.debug(TAG, "ChangeListener: quitToMenu");
                quitToMenu();
                event.handle();
            }
        });
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (event.isHandled()) return;
                Gdx.app.debug(TAG, "ChangeListener: quit");
                quit();
                event.handle();
            }
        });
    }

    private void quitToMenu() {

    }

    private void settings() {

    }

    private void quit() {

    }

    private void pauseGame() {
        inputHandler.onPause();
        mainMenuCell.getActor().addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(1)));
    }

    private void resumeGame() {
        mainMenuCell.getActor().addAction(Actions.sequence(Actions.fadeOut(1), Actions.visible(false), new RunnableAction() {
            @Override
            public void run() {
                inputHandler.onResume();
            }
        }));

    }

    @Override
    public void updateScore(int score) {
        scoreLabel.setText("SCORE: " + score);
    }

    @Override
    public void updateLevel(int level) {
        levelLabel.setText("LEVEL: " + level);
    }

    @Override
    public void update(float delta) {
        act(delta);
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }


}
