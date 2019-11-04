package com.example.testproject;

import com.example.testproject.Data.CriteriaExtended;

import java.util.ArrayList;

public class Participant implements Comparable<Participant>{
    private String name;
    private Boolean outOfCompetition;
    private Integer thisRound_juriesThatVoted;
    private Integer thisRound_number;
    private Integer lastRounds_points;
    private Integer thisRounds_points;
    private ArrayList<Integer> thisRound_results;
    private Boolean isDisqualified;

    public Participant() {
    }


    public Participant(String name, Integer nrProbe) {
        this.name = name;

        outOfCompetition = false;

        thisRound_juriesThatVoted = 0;

        lastRounds_points = 0;

        thisRounds_points = 0;

        thisRound_number = 0;

        thisRound_results = new ArrayList<>();
        for (int i = 0; i < nrProbe; i++) {
            thisRound_results.add(0);
        }

        isDisqualified = false;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOutOfCompetition() {
        return outOfCompetition;
    }

    public void setOutOfCompetition(Boolean outOfCompetition) {
        this.outOfCompetition = outOfCompetition;
    }

    public Integer getThisRound_juriesThatVoted() {
        return thisRound_juriesThatVoted;
    }

    public void addThisRound_juriesThatVoted() {
        thisRound_juriesThatVoted ++;
    }

    public Integer getThisRound_number() {
        return thisRound_number;
    }

    public void setThisRound_number(Integer thisRound_number) {
        this.thisRound_number = thisRound_number;
    }

    public Integer getLastRounds_points() {
        return lastRounds_points;
    }

    public void setLastRounds_points(Integer lastRounds_points) {
        this.lastRounds_points = lastRounds_points;
    }

    public void setThisRound_juriesThatVoted(Integer thisRound_juriesThatVoted) {
        this.thisRound_juriesThatVoted = thisRound_juriesThatVoted;
    }

    public void setThisRounds_points(Integer thisRounds_points) {
        this.thisRounds_points = thisRounds_points;
    }

    public void setThisRound_results(ArrayList<Integer> thisRound_results) {
        this.thisRound_results = thisRound_results;
    }

    public void resetThisRound_results(Integer size) {
        this.thisRound_results = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            this.thisRound_results.add(0);
        }
    }

    public Integer getThisRounds_points() {
        return thisRounds_points;
    }


    public ArrayList<Integer> getThisRound_results() {
        return thisRound_results;
    }

    public void addToThisRound_results(ArrayList<Integer> thisRound_results,ArrayList<CriteriaExtended> weight) {
        int points = 0;
        for (int i = 0; i < thisRound_results.size() ; i++) {
            this.thisRound_results.set(i,(thisRound_results.get(i) + this.thisRound_results.get(i)));
            points += thisRound_results.get(i) * weight.get(i).getWeight();
        }
        thisRounds_points += points;
    }

    public Boolean getDisqualified() {
        return isDisqualified;
    }

    public void setDisqualified(Boolean disqualified) {
        isDisqualified = disqualified;
    }

    @Override
    public int compareTo(Participant o) {
        return o.thisRounds_points - this.thisRounds_points;
    }
}
