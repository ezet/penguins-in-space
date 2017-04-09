package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.asteroids.controller.MultiplayerMenu;
import no.ntnu.tdt4240.asteroids.service.Assets;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;


public class MultiplayerMenuView extends BaseMenuView implements MultiplayerMenu.IView {

    @SuppressWarnings("unused")
    private static final String TAG = MultiplayerMenuView.class.getSimpleName();
    private final Skin uiSkin = ServiceLocator.appComponent.getAssetLoader().getSkin(Assets.SkinAsset.UISKIN);
    private final TextButton quickGame = new TextButton("QUICK GAME", uiSkin);
    private final TextButton invitePlayers = new TextButton("INVITE FRIENDS", uiSkin);
    private final TextButton hostGame = new TextButton("HOST GAME", uiSkin);
    private final TextButton back = new TextButton("BACK", uiSkin);
    private final MultiplayerMenu.ViewHandler controller;
    private final List<TextButton> buttons = new ArrayList<>();
    // TODO: implement main screen gui

    public MultiplayerMenuView(Batch batch, MultiplayerMenu.ViewHandler controller) {
        super(batch);
        this.controller = controller;
        buttons.add(quickGame);
        buttons.add(invitePlayers);
        buttons.add(hostGame);
        buttons.add(back);
        setListeners();
    }

    @Override
    protected List<TextButton> getButtons() {
        return buttons;
    }

    private void setListeners() {
        quickGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startAction(new RunnableAction() {
                    @Override
                    public void run() {
                        controller.onQuickgame();
                    }
                });
            }
        });
        invitePlayers.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startAction(new RunnableAction() {
                    @Override
                    public void run() {
                        controller.onInvitePlayers();
                    }
                });
            }
        });
        hostGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onHostGame();
            }
        });

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startAction(new RunnableAction() {
                    @Override
                    public void run() {
                        controller.onBack();
                    }
                });
            }
        });
    }

}
