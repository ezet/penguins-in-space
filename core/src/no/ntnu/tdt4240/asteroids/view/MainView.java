package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.tdt4240.asteroids.controller.IMainMenu;
import no.ntnu.tdt4240.asteroids.controller.MainMenu;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;


public class MainView extends BaseView implements MainMenu.IMainView {

    @SuppressWarnings("unused")
    private static final String TAG = MainView.class.getSimpleName();
    private final Skin uiSkin = ServiceLocator.appComponent.getAssetLoader().getUiSkin();
    private final TextButton play = new TextButton("PLAY", uiSkin);
    private final TextButton multiplayer = new TextButton("MULTIPLAYER", uiSkin);
    private final TextButton quit = new TextButton("QUIT", uiSkin);
    private final TextButton TUTORIAL = new TextButton("TUTORIAL", uiSkin);
    private final Table table = new Table();
    private final IMainMenu controller;
    private boolean active = true;
    // TODO: implement main screen gui

    public MainView(Batch batch, IMainMenu controller) {
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
        table.add(play).pad(30);
        table.row();
        table.add(multiplayer).pad(30);
        table.row();
        table.add(TUTORIAL).pad(30);
        table.row();
        table.add(quit).pad(30);
        table.row();
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.addAction(Actions.sequence(Actions.fadeOut(1), new RunnableAction() {
                    @Override
                    public void run() {
                        active = !active;
                        controller.onPlay();
                    }
                }));
            }
        });
        multiplayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.addAction(Actions.sequence(Actions.fadeOut(1), new RunnableAction() {
                    @Override
                    public void run() {
                        active = !active;
                        controller.onMultiplayer();
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
        TUTORIAL.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onTutorial();
            }
        });
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!active) {
            active = true;
            table.addAction(Actions.sequence(Actions.fadeIn(1)));
        }
    }
}
