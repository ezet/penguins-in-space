package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;


public class AnimationComponent implements Component, Pool.Poolable {

    public final Array<TextureRegion> frames = new Array<>();
    public final Vector2 scale = new Vector2(1, 1);
    public final Vector2 originalScale = new Vector2(1, 1);
    public final Array<Class<? extends Component>> removeAfterAnimation = new Array<>();
    public final Array<Class<? extends Component>> removeDuringAnimation = new Array<>();
    public boolean removeEntityAfterAnimation = false;
    public int currentFrame = 0;
    public TextureRegion originalRegion = null;
    public long duration = 0;
    public float delay = 0;
    public String soundOnStart = null;
    public String soundOnComplete = null;

    @Override
    public void reset() {
        removeEntityAfterAnimation = false;
        originalRegion = null;
        frames.clear();
        currentFrame = 0;
        duration = 0;
        delay = 0;
        scale.set(1, 1);
        originalScale.set(1, 1);
        removeAfterAnimation.clear();
        removeDuringAnimation.clear();
        soundOnStart = null;
        soundOnComplete = null;
    }
}
