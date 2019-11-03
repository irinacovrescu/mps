package com.example.testproject;

public class ParticipantExtended extends Participant {

    private int id;
    private boolean graded;

    public ParticipantExtended(String name, Integer nrProbe, int id) {
        super(name, nrProbe);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public boolean isGraded() {
        return graded;
    }

    public void setGraded(boolean graded) {
        this.graded = graded;
    }
}
