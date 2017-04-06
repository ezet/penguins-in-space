package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.controller.IGameView;
import no.ntnu.tdt4240.asteroids.controller.IGameController;


public class GameView extends Stage implements IGameView {

    private static final String TAG = GameView.class.getSimpleName();
    // TODO: define proper default GUI resources like font, label style etc.
    private static Viewport guiViewport;

    static {
        OrthographicCamera camera = new OrthographicCamera();
        guiViewport = new FitViewport(Asteroids.GUI_VIRTUAL_WIDTH, Asteroids.GUI_VIRTUAL_HEIGHT, camera);
        guiViewport.apply();
        camera.position.set((Asteroids.GUI_VIRTUAL_WIDTH) / 2, (Asteroids.GUI_VIRTUAL_WIDTH) / 2, 0);
    }

    private final IGameController inputHandler;
    private final Table table = new Table();
    private final BitmapFont defaultFont = new BitmapFont();
    private final Label.LabelStyle defaultLabelStyle = new Label.LabelStyle(defaultFont, Color.WHITE);
    private final Label scoreLabel = new Label("SCORE: 0", defaultLabelStyle);
    private final Label levelLabel = new Label("LEVEL: 0", defaultLabelStyle);
    private final Label hitpointsLabel = new Label("", defaultLabelStyle);
    private final Skin buttonSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
    private final TextButton resume = new TextButton("RESUME", buttonSkin);
    private final TextButton settings = new TextButton("SETTINGS", buttonSkin);
    private final TextButton quitToMenu = new TextButton("QUIT TO MENU", buttonSkin);
    private final TextButton quit = new TextButton("QUIT", buttonSkin);
    private final TextButton pauseButton = new TextButton("Pause", buttonSkin);
    private Cell mainMenuCell;


    public GameView(Batch batch, IGameController inputHandler) {
        super(guiViewport, batch);
        this.inputHandler = inputHandler;
        this.addActor(table);
        initGui();
    }

    @Override
    public void setInputController(Actor inputController) {
        getActors().removeValue(inputController, true);
        addActor(inputController);
    }

    // TODO: clean up
    private void initGui() {
        table.setFillParent(true);
        table.center().top();
        // TODO: use proper style, remove scaling
        table.add(pauseButton).colspan(3).center();
        table.row();
        table.add(scoreLabel).space(20);
        table.add(levelLabel).space(20);
        table.add(hitpointsLabel).space(20);
        table.row();

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.setVisible(false);
        verticalGroup.getColor().a = 0;
        mainMenuCell = table.add(verticalGroup).colspan(3).expandY();
        verticalGroup.space(20);
        verticalGroup.addActor(resume);
        verticalGroup.addActor(settings);
        verticalGroup.addActor(quitToMenu);
        verticalGroup.addActor(quit);
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (event.isHandled()) return;
                pauseGame();
                event.handle();
            }
        });

        resume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (event.isHandled()) return;
                resumeGame();
                event.handle();
            }
        });
        settings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (event.isHandled()) return;
                settings();
                event.handle();
            }
        });
        quitToMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (event.isHandled()) return;
                quitToMenu();
                event.handle();
            }
        });
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (event.isHandled()) return;
                quit();
                event.handle();
            }
        });
    }

    private void quitToMenu() {
        inputHandler.onQuitLevel();
    }

    private void settings() {
        inputHandler.onSettings();
    }

    private void quit() {
        inputHandler.onQuit();
    }

    private void pauseGame() {
        pauseButton.addAction(Actions.sequence(Actions.visible(false), Actions.fadeOut(1)));
        inputHandler.onPause();
        mainMenuCell.getActor().addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(1)));
    }

    private void resumeGame() {
        pauseButton.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(1)));
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
    public void setDebug(boolean debug) {
        setDebugAll(debug);
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height);
    }

    @Override
    public void update(float delta) {
        act(delta);
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }

    @Override
    public void updateHitpoints(int hitpoints) {
        hitpointsLabel.setText("HP: " + hitpoints);
    }


}
