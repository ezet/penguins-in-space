package no.ntnu.tdt4240.asteroids;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidLauncher extends AndroidApplication implements GameHelper.GameHelperListener {


    private static final String TAG = AndroidLauncher.class.getSimpleName();
    private static AndroidLauncher instance;
    private PlayService playService;

    public AndroidLauncher() {
        instance = this;
    }

    public static AndroidLauncher getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playService = new PlayService(this);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useCompass = false;
        config.useAccelerometer = false;
        config.useImmersiveMode = true;
        config.useWakelock = true;
        initialize(new Asteroids(playService), config);
        playService.setup(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        playService.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onSignInFailed() {
        Log.d(TAG, "onSignInFailed: ");
//        playService.getGameHelper().beginUserInitiatedSignIn();
    }

    @Override
    public void onSignInSucceeded() {
        Log.d(TAG, "onSignInSucceeded: ");
        if (playService.getGameHelper().hasInvitation()) {
            playService.acceptInviteToRoom(playService.getGameHelper().getInvitationId());
        }
    }


    // Sets the flag to keep this screen on. It's recommended to do that during
    // the
    // handshake when setting up a game, because if the screen turns off, the
    // game will be
    // cancelled.
    void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Clears the flag that keeps the screen on.
    void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    public void showGameError() {
        // TODO: 03-Apr-17 improve error message
        BaseGameUtils.makeSimpleDialog(this, "ERROR");
    }
}
