package com.example.testproject.Data;

public class Grade {

    private int criteriaId;
    private Integer noStars;

    public Grade(int criteriaId) {
        this.criteriaId = criteriaId;
        this.noStars = 0;
    }

    public int getCriteriaId() {
        return criteriaId;
    }

    public Integer getNoStars() {
        return noStars;
    }

    public void setNoStars(int noStars) {
        this.noStars = noStars;
    }
}
