package no.ntnu.tdt4240.asteroids;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidLauncher extends AndroidApplication implements GameHelper.GameHelperListener {


    private static final String TAG = AndroidLauncher.class.getSimpleName();
    private PlayService playService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useCompass = false;
        config.useAccelerometer = false;
        config.useImmersiveMode = true;
        config.useWakelock = true;
        playService = new PlayService(this, this);
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
        playService.getGameHelper().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        playService.getGameHelper().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSignInFailed() {
        Log.d(TAG, "onSignInFailed: ");
    }

    @Override
    public void onSignInSucceeded() {
        Log.d(TAG, "onSignInSucceeded: ");
    }
}
