package no.ntnu.tdt4240.asteroids;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidLauncher extends AndroidApplication {


    private PlayService playService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useCompass = false;
        config.useAccelerometer = false;
        config.useImmersiveMode = true;
        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
            @Override
            public void onSignInFailed() {
                playService.getGameHelper().showFailureDialog();

            }

            @Override
            public void onSignInSucceeded() {
                playService.getGameHelper().showFailureDialog();
            }
        };
        playService = new PlayService(this, gameHelperListener);
        playService.getGameHelper().setShowErrorDialogs(true);
        initialize(new Asteroids(playService), config);
    }

    @Override
    protected void onStart() {
        super.onStart();
        playService.getGameHelper().onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        playService.signOut();
        playService.getGameHelper().onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playService.signOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        playService.getGameHelper().onActivityResult(requestCode, resultCode, data);
    }



}
