package no.ntnu.tdt4240.asteroids.multiplayer;

public interface IMultiplayerService {
    void signIn();

    void signOut();

    void rateGame();

    void unlockAchievement();

    void submitScore(int highScore);

    void showAchievement();

    void showScore();

    boolean isSignedIn();
}