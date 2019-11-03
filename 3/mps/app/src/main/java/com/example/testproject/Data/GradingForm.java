package com.example.testproject.Data;

import androidx.databinding.BaseObservable;
import com.example.testproject.Contestant;
import java.util.ArrayList;

public class GradingForm extends BaseObservable {

    private Contestant contestant;
    private ArrayList<Grade> grades;

    public GradingForm(Contestant contestant) {

        this.contestant = contestant;
    }

    public Contestant getContestant() {
        return contestant;
    }

    public ArrayList<Grade> getGrades() {
        return grades;
    }

    public void setGrade(int criteriaId, int noStars) {

        for(Grade g : grades) {
            if(g.getCriteriaId() == criteriaId) {
                g.setNoStars(noStars);
            }
        }
    }

    public void addGrade(int criteriaId) {
        if(grades == null) {
            grades = new ArrayList<>();
        }

        grades.add(new Grade(criteriaId));
    }

    public static GradingForm getGradingFormByContestantId(ArrayList<GradingForm> array, int contestantId) {
        for(GradingForm g : array) {
            if(g.contestant.getId() == contestantId)
                return g;
        }

        return null;
    }
}