package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import no.ntnu.tdt4240.asteroids.presenter.IGamePresenter;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;


public class GameView extends BaseView implements IGameView {

    private static final String TAG = GameView.class.getSimpleName();
    // TODO: define proper default GUI resources like font, label style etc.
    private final IGamePresenter inputHandler;
    private final AssetService assetLoader;
    private Table table;
    private Label scoreLabel;
    private Label levelLabel;
    private Label hitpointsLabel;
    private TextButton resume;
    private TextButton settings;
    private TextButton quitToMenu;
    private TextButton quit;
    private TextButton pauseButton;
    private Cell mainMenuCell;


    public GameView(Batch batch, IGamePresenter inputHandler) {
        super(batch);
        getViewport().apply(true);
        assetLoader = ServiceLocator.getAppComponent().getAssetService();
        this.inputHandler = inputHandler;
        loadAssets();
        initGui();
    }

    private void loadAssets() {
        resume = new TextButton("RESUME", assetLoader.getSkin(AssetService.SkinAsset.UISKIN));
        settings = new TextButton("SETTINGS", assetLoader.getSkin(AssetService.SkinAsset.UISKIN));
        quitToMenu = new TextButton("QUIT TO MENU", assetLoader.getSkin(AssetService.SkinAsset.UISKIN));
        quit = new TextButton("QUIT", assetLoader.getSkin(AssetService.SkinAsset.UISKIN));
        pauseButton = new TextButton("Pause", assetLoader.getSkin(AssetService.SkinAsset.UISKIN));

        scoreLabel = new Label("SCORE: 0", assetLoader.getSkin(AssetService.SkinAsset.UISKIN));
        levelLabel = new Label("LEVEL: 0", assetLoader.getSkin(AssetService.SkinAsset.UISKIN));
        hitpointsLabel = new Label("", assetLoader.getSkin(AssetService.SkinAsset.UISKIN));
    }

    @Override
    public void setInputController(Actor inputController) {
        getActors().removeValue(inputController, true);
        addActor(inputController);
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
    public void updateHitpoints(int hitpoints) {
        hitpointsLabel.setText("HP: " + hitpoints);
    }

    @Override
    public void resume() {
        Gdx.app.debug(TAG, "show: ");
        super.resume();
        loadAssets();
        initGui();
    }

    // TODO: clean up
    private void initGui() {
        if (table != null) {
            this.getActors().removeValue(table, true);
        }
        table = new Table();
        this.addActor(table);
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


}
