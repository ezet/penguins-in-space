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
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import no.ntnu.tdt4240.asteroids.Asteroids;
import no.ntnu.tdt4240.asteroids.controller.IMultiplayerMenu;
import no.ntnu.tdt4240.asteroids.controller.MultiplayerMenu;


public class MultiplayerView extends Stage implements MultiplayerMenu.IMainView {

    @SuppressWarnings("unused")
    private static final String TAG = MultiplayerView.class.getSimpleName();
    private static OrthographicCamera camera = new OrthographicCamera();
    private static Viewport guiViewport = new FitViewport(Asteroids.GUI_VIRTUAL_WIDTH, Asteroids.GUI_VIRTUAL_HEIGHT, camera);
    private final Skin buttonSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
    private final TextButton quickGame = new TextButton("QUICK GAME", buttonSkin);
    private final TextButton invitePlayers = new TextButton("INVITE FRIENDS", buttonSkin);
    private final TextButton hostGame = new TextButton("HOST GAME", buttonSkin);
    private final TextButton back = new TextButton("BACK", buttonSkin);
    private final Table table = new Table();
    private final BitmapFont defaultFont = new BitmapFont();
    private final Label.LabelStyle defaultLabelStyle = new Label.LabelStyle(defaultFont, Color.WHITE);
    private final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
    private final IMultiplayerMenu controller;
    private boolean active = true;
    // TODO: implement main screen gui

    public MultiplayerView(Batch batch, IMultiplayerMenu controller) {
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
        table.add(quickGame).pad(30);
        table.row();
        table.add(invitePlayers).pad(30);
        table.row();
        table.add(hostGame).pad(30);
        table.row();
        table.add(back).pad(30);
        table.row();
        quickGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.addAction(Actions.sequence(Actions.fadeOut(1), new RunnableAction() {
                    @Override
                    public void run() {
                        active = !active;
                        controller.onQuickgame();
                    }
                }));
            }
        });
        invitePlayers.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.addAction(Actions.sequence(Actions.fadeOut(1), new RunnableAction() {
                    @Override
                    public void run() {
                        active = !active;
                        controller.onInvitePlayers();
                    }
                }));
            }
        });
        hostGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onHostGame();
            }
        });
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onBack();
            }
        });
    }

    @Override
    public void update(float delta) {
        act(delta);
        if(!active){
            active = true;
            table.addAction(Actions.sequence(Actions.fadeIn(1)));
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

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

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height);
    }
}
