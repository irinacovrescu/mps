package com.example.testproject.Data;

import androidx.databinding.BaseObservable;
import com.example.testproject.ParticipantExtended;
import java.util.ArrayList;

public class GradingForm extends BaseObservable {

    private ParticipantExtended participantExtended;
    private ArrayList<Grade> grades;

    public GradingForm(ParticipantExtended participantExtended) {

        this.participantExtended = participantExtended;
    }

    public ParticipantExtended getParticipantExtended() {
        return participantExtended;
    }

    public ArrayList<Integer> getResultsForDatabase() {
        ArrayList<Integer> results = new ArrayList<>();
        for(Grade g : grades) {
            results.add(g.getNoStars());
        }

        return results;
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
            if(g.participantExtended.getId() == contestantId)
                return g;
        }

        return null;
    }
}