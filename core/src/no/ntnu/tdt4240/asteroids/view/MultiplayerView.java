package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.tdt4240.asteroids.controller.IMultiplayerMenu;
import no.ntnu.tdt4240.asteroids.controller.MultiplayerMenu;
import no.ntnu.tdt4240.asteroids.service.Assets;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;


public class MultiplayerView extends BaseView implements MultiplayerMenu.IMainView {

    @SuppressWarnings("unused")
    private static final String TAG = MultiplayerView.class.getSimpleName();
    private final Skin uiSkin = ServiceLocator.appComponent.getAssetLoader().getSkin(Assets.SkinAsset.UISKIN);
    private final TextButton quickGame = new TextButton("QUICK GAME", uiSkin);
    private final TextButton invitePlayers = new TextButton("INVITE FRIENDS", uiSkin);
    private final TextButton hostGame = new TextButton("HOST GAME", uiSkin);
    private final TextButton back = new TextButton("BACK", uiSkin);
    private final Table table = new Table();
    private final IMultiplayerMenu controller;
    private boolean active = true;
    // TODO: implement main screen gui

    public MultiplayerView(Batch batch, IMultiplayerMenu controller) {
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
        if (!active) {
            active = true;
            table.addAction(Actions.sequence(Actions.fadeIn(1)));
        }
    }

}
