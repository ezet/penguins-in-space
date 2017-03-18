package no.ntnu.tdt4240.asteroids;

import android.content.Intent;
import android.net.Uri;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

import no.ntnu.tdt4240.asteroids.service.network.INetworkService;


public class PlayService implements INetworkService {

    private static final int requestCode = 1;
    private final AndroidLauncher activity;
    private final GameHelper gameHelper;

    public PlayService(AndroidLauncher activity, GameHelper.GameHelperListener listener) {
        this.activity = activity;
        gameHelper = new GameHelper(activity, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(true);
        gameHelper.setShowErrorDialogs(true);
        gameHelper.setup(listener);
    }

    public GameHelper getGameHelper() {
        return gameHelper;
    }

    @Override
    public void signIn() {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void signOut() {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.signOut();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void rateGame() {
        String str = "Your PlayStore Link";
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }

    @Override
    public void unlockAchievement() {
        Games.Achievements.unlock(gameHelper.getApiClient(),
                activity.getString(R.string.achievement_dum_dum));
    }

    @Override
    public void submitScore(int highScore) {
        if (isSignedIn()) {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(),
                    activity.getString(R.string.leaderboard_highest), highScore);
        }
    }

    @Override
    public void showAchievement() {
        if (isSignedIn()) {
            activity.startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), requestCode);
        } else {
            signIn();
        }
    }

    @Override
    public void showScore() {
        if (isSignedIn()) {
            activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
                    activity.getString(R.string.leaderboard_highest)), requestCode);
        } else {
            signIn();
        }
    }

    @Override
    public void sendUnreliableMessageToOthers(byte[] messageData) {
        Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(gameHelper.getApiClient(), messageData, "roomId");
    }

    @Override
    public void setMessageReceivedListener(NetworkMessageReceivedListener listener) {
        // TODO: create adapter between NetworkMessageReceivedListener and RealTimeMessageReceivedListener
    }

    @Override
    public boolean isSignedIn() {
        return gameHelper.isSignedIn();
    }

}
