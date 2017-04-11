package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.asteroids.controller.MainMenu;
import no.ntnu.tdt4240.asteroids.service.Assets;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;


public class MainView extends BaseMenuView implements MainMenu.IView {

    @SuppressWarnings("unused")
    private static final String TAG = MainView.class.getSimpleName();
    private final Skin uiSkin = ServiceLocator.appComponent.getAssetLoader().getSkin(Assets.SkinAsset.UISKIN);
    private final TextButton play = new TextButton("PLAY", uiSkin);
    private final TextButton multiplayer = new TextButton("MULTIPLAYER", uiSkin);
    private final TextButton settings = new TextButton("SETTINGS", uiSkin);
    private final TextButton tutorial = new TextButton("TUTORIAL", uiSkin);
    private final TextButton highscore = new TextButton("HIGHSCORE", uiSkin);
    private final TextButton quit = new TextButton("QUIT", uiSkin);

    private final TextButton achievements = new TextButton("ACHIEVEMENTS", uiSkin);
    private final MainMenu.ViewHandler controller;
    private final List<TextButton> buttons = new ArrayList<>();

    public MainView(Batch batch, MainMenu.ViewHandler controller) {
        super(batch);
        this.controller = controller;
        buttons.add(play);
        buttons.add(multiplayer);
        buttons.add(settings);
        buttons.add(achievements);
        buttons.add(highscore);
        buttons.add(tutorial);
        buttons.add(quit);
        setListeners();
    }

    protected List<TextButton> getButtons() {
        return buttons;
    }

    private void setListeners() {
        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startAction(new RunnableAction() {
                    @Override
                    public void run() {
                        controller.onPlay();
                    }
                });
            }
        });
        multiplayer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startAction(new RunnableAction() {
                    @Override
                    public void run() {
                        controller.onMultiplayer();
                    }
                });
            }
        });
        settings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startAction(new RunnableAction() {
                    @Override
                    public void run() {
                        controller.onSettings();
                    }
                });
            }
        });
        tutorial.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startAction(new RunnableAction() {
                    @Override
                    public void run() {
                        controller.onTutorial();
                    }
                });
            }
        });
        highscore.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onShowLeaderboard();
            }
        });
        achievements.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onShowAchievements();
            }
        });
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onQuit();
            }
        });
    }
}
