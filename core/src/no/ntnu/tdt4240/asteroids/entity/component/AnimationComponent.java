package no.ntnu.tdt4240.asteroids.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;


public class AnimationComponent implements Component, Pool.Poolable {

    public final Array<TextureRegion> frames = new Array<>();
    public boolean removeOnAnimationComplete = false;
    public int currentFrame = 0;
    public TextureRegion originalRegion = null;
    public long duration = 0;


    @Override
    public void reset() {
        removeOnAnimationComplete = false;
        originalRegion = null;
        frames.clear();
        currentFrame = 0;
        duration = 0;
    }
}
