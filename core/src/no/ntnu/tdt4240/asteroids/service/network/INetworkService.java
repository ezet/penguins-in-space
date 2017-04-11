package no.ntnu.tdt4240.asteroids.service.network;

import java.util.List;

import no.ntnu.tdt4240.asteroids.model.PlayerData;

public interface INetworkService {

    String ACHIEVEMENT_KILL_100_ENEMIES = "CgkIs8TFzMUHEAIQAw";
    String ACHIEVEMENT_KILL_250_ENEMIES = "CgkIs8TFzMUHEAIQBA";
    String ACHIEVEMENT_KILL_500_ENEMIES = "CgkIs8TFzMUHEAIQBQ";
    String ACHIEVEMENT_KILL_1000_ENEMIES = "CgkIs8TFzMUHEAIQBw";
    String ACHIEVEMENT_WIN_MP_DM = "CgkIs8TFzMUHEAIQBg";

    void signIn();

    String getDisplayName();

    void signOut();

    void rateGame();

    void unlockAchievement(String string);

    void submitScore(int highScore);

    void submitScoreWithResult(int highScore, IScoreCallback singleplayerGame);

    void showAchievement();

    void showScore();

    void sendUnreliableMessageToOthers(byte[] messageData);

    boolean isSignedIn();

    void startQuickGame();

    void startSelectOpponents(boolean autoMatch);

    void setGameListener(IGameListener gameListener);

    void setNetworkListener(INetworkListener networkListener);

    void quitGame();

    interface IGameListener {

        void onMultiplayerGameStarting();
    }

    interface INetworkListener {

        void onReliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData);

        void onUnreliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData);

        void onRoomReady(List<PlayerData> players);
    }

    interface IScoreCallback {

        void onScoreResult(boolean allTimeBest, boolean weeklyBest, boolean dailyBest);
    }
}