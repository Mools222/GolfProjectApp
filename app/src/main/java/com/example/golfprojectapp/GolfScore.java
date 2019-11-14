package com.example.golfprojectapp;

public class GolfScore {
    private long time;
    private String color;

    public GolfScore() {
    }

    public GolfScore(long time, String color) {
        this.time = time;
        this.color = color;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}