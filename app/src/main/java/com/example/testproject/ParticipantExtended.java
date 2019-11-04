package com.example.testproject;

public class ParticipantExtended extends Participant {

    private Integer id;
    private boolean graded;
    private String nrSerie;

    public ParticipantExtended(Participant p, Integer nrProbe, int id, String nrSerie) {
        super(p.getName(), nrProbe);
        this.id = id;
        this.nrSerie = nrSerie;
        this.setDisqualified(p.getDisqualified());
        this.setOutOfCompetition(p.getOutOfCompetition());
    }

    public Integer getId() {
        return this.id;
    }

    public boolean isGraded() {
        return graded;
    }

    public void setGraded(boolean graded) {
        this.graded = graded;
    }

    public String getNrSerie() {
        return nrSerie;
    }
}
