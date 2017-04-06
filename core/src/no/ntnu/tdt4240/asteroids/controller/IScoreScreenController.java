package no.ntnu.tdt4240.asteroids.controller;


import java.util.List;

public interface IScoreScreenController {

    void onQuitLevel();

    List<String> getScores();
}
