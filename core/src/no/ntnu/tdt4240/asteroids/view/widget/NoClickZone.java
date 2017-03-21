package no.ntnu.tdt4240.asteroids.view.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NoClickZone extends WidgetGroup {


    private final Widget child;
    private int margin;

    public NoClickZone(Widget child, int margin) {
        super(child);
        this.child = child;
        this.margin = margin;
        setDebug(true);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                event.handle();
                event.stop();
            }
        });

    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        Actor hit = super.hit(x, y, touchable);
        if (hit == null && x > child.getX() - margin && x < child.getX() + child.getWidth() + margin && y > child.getY() - margin && y < child.getY() + child.getHeight() + margin) {
            hit = this;
        }
        return hit;

    }
}
