package no.ntnu.tdt4240.asteroids.controller;

public interface ISettingsController {

    void onQuitLevel();

    void previousCharacter();

    void nextCharacter();

    void toggleMute();

    void increaseVolume();

    void decreaseVolume();
}
