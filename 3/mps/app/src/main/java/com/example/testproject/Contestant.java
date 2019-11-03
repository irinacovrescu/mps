package com.example.testproject;

public class Contestant {

    private String name;
    private int id;
    private boolean graded;
    private boolean outOfCompetition;
    private boolean disqualified;

    public Contestant(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isGraded() {
        return graded;
    }

    public void setGraded(boolean graded) {
        this.graded = graded;
    }

    public boolean isOutOfCompetition() {
        return outOfCompetition;
    }

    public void setOutOfCompetition(boolean outOfCompetition) {
        this.outOfCompetition = outOfCompetition;
    }

    public boolean isDisqualified() {
        return disqualified;
    }

    public void setDisqualified(boolean disqualified) {
        this.disqualified = disqualified;
    }
}
