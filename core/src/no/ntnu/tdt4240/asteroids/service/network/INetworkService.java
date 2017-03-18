package no.ntnu.tdt4240.asteroids.service.network;

public interface INetworkService {
    void signIn();

    void signOut();

    void rateGame();

    void unlockAchievement();

    void submitScore(int highScore);

    void showAchievement();

    void showScore();

    void sendUnreliableMessageToOthers(byte[] messageData);

    void setMessageReceivedListener(NetworkMessageReceivedListener listener);

    boolean isSignedIn();

    interface NetworkMessageReceivedListener {
        void onMessageReceived();
    }
}