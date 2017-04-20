package no.ntnu.tdt4240.asteroids.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import no.ntnu.tdt4240.asteroids.service.AssetService;

import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.PLAYER_BLUE;
import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.PLAYER_GREEN;
import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.PLAYER_RED;
import static no.ntnu.tdt4240.asteroids.service.AssetService.TextureAsset.PLAYER_YELLOW;

public class AnimationFactory {

    private final Array<TextureRegion> mediumExplosion;
    private final Array<TextureRegion> powerupPickup;
    private final Array<TextureRegion> longExplosion;
    private final Array<TextureRegion> shortExplosion;
    private AssetService assetService;

    public AnimationFactory(AssetService assetService) {
        this.assetService = assetService;
        mediumExplosion = createObstacleDestroyedAnimation();
        powerupPickup = createPowerupPickupAnimation();
        longExplosion = createLongExplosion();
        shortExplosion = createShortExplosion();
    }

    public Array<TextureRegion> getMediumExplosion() {
        return mediumExplosion;
    }

    public Array<TextureRegion> getPowerupPickup() {
        return powerupPickup;
    }

    private Array<TextureRegion> createObstacleDestroyedAnimation() {
        Array<TextureRegion> explosions = new Array<>();
        Texture texture = assetService.getTexture(AssetService.TextureAsset.OBSTACLE_EXPLOSION);
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
                texture = assetService.getTexture(PLAYER_RED);
            } else if (i < 4){
                texture = assetService.getTexture(PLAYER_GREEN);
            } else if (i < 6){
                texture = assetService.getTexture(PLAYER_BLUE);
            } else {
                texture = assetService.getTexture(PLAYER_YELLOW);
            }
            animationSequence.add(new TextureRegion(texture));
        }
        return animationSequence;
    }

    private Array<TextureRegion> createShortExplosion() {
        Array<TextureRegion> explosions = new Array<>();
        Texture texture = assetService.getTexture(AssetService.TextureAsset.BOMB_EXPLOSION);
        TextureRegion[][] split = TextureRegion.split(texture, 128, 128);
        for (TextureRegion[] row : split) {
            for (TextureRegion region : row) {
                explosions.add(region);
                explosions.add(region);
            }
        }
        return explosions;
    }

    private Array<TextureRegion> createLongExplosion() {
        Array<TextureRegion> explosions = new Array<>();
        Texture texture = assetService.getTexture(AssetService.TextureAsset.EXPLOSION);
        TextureRegion[][] split = TextureRegion.split(texture, 100, 100);
        for (TextureRegion[] row : split) {
            for (TextureRegion region : row) {
                explosions.add(region);
            }
        }
        return explosions;
    }

    public Array<TextureRegion> getShortExplosion() {
        return shortExplosion;
    }

    public Array<TextureRegion> getLongExplosion() {
        return longExplosion;
    }
}
