package no.ntnu.tdt4240.asteroids.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import no.ntnu.tdt4240.asteroids.service.Assets;

public class AnimationFactory {

    private final Array<TextureRegion> obstacleDestroyedAnimation;
    private final Array<TextureRegion> powerupPickupAnimation;

    public Array<TextureRegion> getObstacleDestroyedAnimation() {
        return obstacleDestroyedAnimation;
    }

    public Array<TextureRegion> getPowerupPickupAnimation() {
        return powerupPickupAnimation;
    }

    private Assets assets;

    public AnimationFactory(Assets assets) {
        this.assets = assets;
        obstacleDestroyedAnimation = createObstacleDestroyedAnimation();
        powerupPickupAnimation = createPowerupPickupAnimation();
    }

    private Array<TextureRegion> createObstacleDestroyedAnimation() {
        Array<TextureRegion> explosions = new Array<>();
        Texture texture = assets.getObstacleExplosion();
        TextureRegion[][] split = TextureRegion.split(texture, 64, 64);
        for (TextureRegion[] row : split) {
            for (TextureRegion region : row) {
                explosions.add(region);
            }
        }
        return explosions;
    }

    private Array<TextureRegion> createPowerupPickupAnimation(){
        Array<TextureRegion> animationSequence = new Array<>();
        for (int i = 0; i < 8; i++) {
            Texture texture;
            if (i < 2){
                texture = assets.getAssetManager().get("playerRed.png");
            } else if (i < 4){
                texture = assets.getAssetManager().get("playerGreen.png");
            } else if (i < 6){
                texture = assets.getAssetManager().get("playerBlue.png");
            } else {
                texture = assets.getAssetManager().get("playerYellow.png");
            }
            animationSequence.add(new TextureRegion(texture));
        }
        return animationSequence;
    }
}
