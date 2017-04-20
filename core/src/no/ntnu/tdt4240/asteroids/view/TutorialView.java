package no.ntnu.tdt4240.asteroids.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.tdt4240.asteroids.presenter.TutorialPresenter;
import no.ntnu.tdt4240.asteroids.service.AssetService;
import no.ntnu.tdt4240.asteroids.service.ServiceLocator;

import static com.badlogic.gdx.utils.Align.bottom;

public class TutorialView extends BaseView implements TutorialPresenter.IView {

    @SuppressWarnings("unused")
    private static final String TAG = MainView.class.getSimpleName();
    private final Skin uiSkin = ServiceLocator.appComponent.getAssetService().getSkin(AssetService.SkinAsset.UISKIN);
    private final TextButton back = new TextButton("BACK", uiSkin);
    private final Table table = new Table();

    private final Label textLabel1 = new Label("Use the analog stick to the left to move around. " +
            "Use the right button to shoot.", uiSkin);
    private final Label textLabel2 = new Label("Avoid running into the angry snowballs, they will hurt you! ", uiSkin);
    private final Label textLabel3 = new Label("When you shoot the snowballs, they might drop new abilities...", uiSkin);

    private final TutorialPresenter.ViewHandler controller;


    public TutorialView(Batch batch, TutorialPresenter.ViewHandler controller) {
        super(batch);
        this.controller = controller;
        table.addAction(Actions.alpha(0));
        addActor(table);
        init();
    }

    @Override
    public void show() {
        super.show();
        table.addAction(getDefaultShowAnimation());
    }

    private void init() {
        table.setFillParent(true);
        table.row();
        table.add(textLabel1).space(30).row();
        table.add(textLabel2).space(30).row();
        table.add(textLabel3).space(30).row();
        table.add(back).align(bottom);

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.onBack();
            }
        });
    }
}
