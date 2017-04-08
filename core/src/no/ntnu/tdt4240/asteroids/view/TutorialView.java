package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.tdt4240.asteroids.controller.ITutorialController;
import no.ntnu.tdt4240.asteroids.controller.TutorialController;
import no.ntnu.tdt4240.asteroids.service.Assets;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

public class TutorialView extends BaseView implements TutorialController.ITutorialView {

    @SuppressWarnings("unused")
    private static final String TAG = MainView.class.getSimpleName();
    private final Skin uiSkin = ServiceLocator.appComponent.getAssetLoader().getSkin(Assets.SkinAsset.UISKIN);
    private final TextButton back = new TextButton("QUIT", uiSkin);
    private final Table table = new Table();

    private final Label label1 = new Label("Use the analog stick to the left to move around. " +
            "Use the right button to shoot.", uiSkin);
    private final Label label2 = new Label("Avoid running into the angry snowballs, they will hurt you! ", uiSkin);
    private final Label label3 = new Label("When you shoot the snowballs, they might drop new abilities...", uiSkin);

    private final ITutorialController controller;


    public TutorialView(Batch batch, ITutorialController controller) {
        super(batch);
        this.controller = controller;
        setDebugAll(true);
        table.addAction(Actions.alpha(0));
        addActor(table);
        init();
        table.addAction(Actions.fadeIn(1));
    }

    private void init() {
        back.getLabel().setFontScale(3);
        label1.setFontScale(3);
        label2.setFontScale(3);
        label3.setFontScale(3);
        table.setFillParent(true);
        //Labels
        table.row();
        table.add(label1).pad(30);
        table.row();
        table.add(label2).pad(30);
        table.row();
        table.add(label3).pad(30);

        //BACK button
        table.row();
        table.add(back).pad(30);

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onQuitLevel();
            }
        });
    }
}
