package no.ntnu.tdt4240.asteroids.service.network;

import java.util.List;

import no.ntnu.tdt4240.asteroids.model.PlayerData;

public interface INetworkService {
    void signIn();

    String getDisplayName();

    void signOut();

    void rateGame();

    void unlockAchievement();

    void submitScore(int highScore);

    void submitScoreWithResult(int highScore, IScoreCallback singleplayerGame);

    void showAchievement();

    void showScore();

    void sendUnreliableMessageToOthers(byte[] messageData);

    boolean isSignedIn();

    void startQuickGame();

    void startSelectOpponents();

    void setGameListener(IGameListener gameListener);

    void setNetworkListener(INetworkListener networkListener);

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