package com.example.golfprojectapp;

import java.util.HashMap;

public class GolfScoreTable {
    private HashMap<String, Integer> scores = new HashMap<>();

    public GolfScoreTable() {
    }

    public void addScore(String color) {
        scores.put(color, scores.containsKey(color) ? scores.get(color) + 1 : 1);
    }

    public HashMap<String, Integer> getScores() {
        return scores;
    }
}
