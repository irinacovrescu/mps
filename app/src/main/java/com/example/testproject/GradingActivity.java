package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.testproject.Data.CallbackBool;
import com.example.testproject.Data.CallbackInt;
import com.example.testproject.Data.CallbackParticipants;
import com.example.testproject.Data.CallbackSubmit;
import com.example.testproject.Data.Constants;
import com.example.testproject.Data.CriteriaExtended;
import com.example.testproject.Data.Criteria;
import com.example.testproject.Data.DatabaseHelper;
import com.example.testproject.Data.GradingForm;
import com.example.testproject.Data.CallbackCriteria;
import com.example.testproject.databinding.ActivityGradingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.HashMap;

public class GradingActivity extends AppCompatActivity {

    private static final String TAG = "GradingActivity";
    private static RatingAdapter adapter;

    private ArrayList<GradingForm> formData;
    private ArrayList<CriteriaExtended> criteriaExtended;
    private ArrayList<ParticipantExtended> participantsExtended;

    // to do: get value from database
    private Integer roundNumber;
    private Integer currentParticipantId;

    private ListView listView;
    private ActivityGradingBinding binding;
    private Button submitButton;
    private Button returnButton;
    private ViewFlipper vf;
    LinearLayout participantsLayout;
    LinearLayout finishedGradingLayout;

    private boolean inGradingForm = false;
    private boolean finishedGrading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromServer();
    }

    public void getDataFromServer() {
        DatabaseHelper.getCriteria(new CallbackCriteria() {
            @Override
            public void onCallBack(ArrayList<Criteria> criteria) {

                DatabaseHelper.getParticipants(criteria, new CallbackParticipants() {
                    @Override
                    public void onCallBack(HashMap<String, HashMap<String, Participant>> participants,
                                           ArrayList<Criteria> criteria) {
                        participantsExtended = getParticipantExtended(participants, criteria);
                        prepareData(criteria);
                        prepareUIElements();
                        createContestantElements(criteria, participantsExtended);
                        createSubmitButton();

                        DatabaseHelper.getCurrentRound(new CallbackInt() {
                            @Override
                            public void onCallBack(Integer value) {
                                roundNumber = value;
                            }
                        });
                    }

                    @Override
                    public void onCallBack(ArrayList<HashMap<Pair<String, String>, Participant>> value) {

                    }
                });
            }
        });
    }

    public void prepareData(ArrayList<Criteria> criteria) {
        criteriaExtended = getExtendedCriteria(criteria);
    }

    private void prepareUIElements() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_grading);
        vf = findViewById( R.id.grading_activity);
        listView = findViewById(R.id.values_list);
        listView.setItemsCanFocus(true);
        participantsLayout = findViewById(R.id.participantsExtended);
        finishedGradingLayout = findViewById(R.id.finished_grading);

        TextView participantsText = findViewById(R.id.contestants_text);
        participantsText.setText("PARTICIPANTS");

        final Button submitButtonC = findViewById(R.id.submit_button_contestant);
        submitButtonC.getBackground().setLevel(Constants.DONE);
        submitButtonC.setText("DONE");
        submitButtonC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submitForOneContestant();
            }
        });
    }

    private void createContestantElements(ArrayList<Criteria> criteria,
                                          ArrayList<ParticipantExtended> participantsExtended) {
        formData = new ArrayList<>();

        for(ParticipantExtended c : participantsExtended) {
            createButtonForContestant(c);
            formData.add(createGradingFormForContestant(c, criteria));
        }
    }

    public GradingForm createGradingFormForContestant(ParticipantExtended c,
                                                      ArrayList<Criteria> criteria) {
        GradingForm gradingForm = new GradingForm(c);
        for(int i = 0; i < criteria.size(); i++) {
            gradingForm.addGrade(i);
        }

        return gradingForm;
    }

    private void createReturnButton() {
        returnButton = findViewById(R.id.returnbutton);
        returnButton.getBackground().setLevel(Constants.RETURN);
        returnButton.setText("Return");
        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(GradingActivity.this, MainActivity.class);
                GradingActivity.this.startActivity(myIntent);
            }
        });
    }

    private void createButtonForContestant(ParticipantExtended c) {
        final Button participantButton = (Button) getLayoutInflater()
                .inflate(R.layout.contestant_button_template, participantsLayout, false);
        participantButton.getBackground().setLevel(Constants.CONTESTANT_NOT_GRADED);
        participantButton.setId(c.getId());
        participantButton.setText(c.getName());
        participantsLayout.addView(participantButton);
        participantButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int participantId = v.getId();
                openForm(participantId);
            }
        });

        setColorForParticipantButton(c, participantButton);
    }

    private void setColorForParticipantButton(ParticipantExtended c, Button participantButton) {
        if(c.getDisqualified()) {
            participantButton.setEnabled(false);
            participantButton.getBackground().setLevel(Constants.CONTESTANT_DISQUALIFIED);

        }

        if(c.getOutOfCompetition()) {
            participantButton.setEnabled(false);
            participantButton.getBackground().setLevel(Constants.CONTESTANT_OUT);
        }
    }

    private void createSubmitButton() {
        submitButton = (Button) getLayoutInflater()
                .inflate(R.layout.submit_button_template, participantsLayout, false);
        submitButton.getBackground().setLevel(Constants.SUBMIT);
        submitButton.setEnabled(false);
        submitButton.setText("Submit");
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submit();
            }
        });

        participantsLayout.addView(submitButton);
    }

    public void submit() {

        //prepare data and send to database
        Integer lastParticipantId = formData.size();
        for(GradingForm gf : formData) {
            ParticipantExtended p = gf.getParticipantExtended();
            DatabaseHelper.evaluateParticipants(p.getNrSerie(), p.getId().toString(), roundNumber,
                    gf.getResultsForDatabase(), this.criteriaExtended, lastParticipantId,
                    new CallbackSubmit() {
                        @Override
                        public void onCallBack() {
                            vf.setDisplayedChild(Constants.FINISHED_GRADING_LAYOUT);
                            finishedGrading = true;
                            TextView thank_you_text = findViewById(R.id.thank_you_text);
                            thank_you_text.setText("Thank you for submitting your grades!");
                            signoutJury();

                            createReturnButton();
                        }
                    });
        }
    }

    private void signoutJury() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String juryUID = user.getEmail().substring(4, 5);
            final DatabaseReference juryRef = FirebaseDatabase.getInstance()
                   .getReference("JUDGE").child(juryUID).child("loggedIn");
            final DatabaseReference juryRef1 = FirebaseDatabase.getInstance()
                    .getReference("JUDGE").child(juryUID).child("voted");
            AuthActivity.setLoggedStatus(juryRef, false);
            AuthActivity.setLoggedStatus(juryRef1, true);
            Log.d(TAG, "Jury signed out!");
        }
    }

    public void submitForOneContestant() {
        vf.setDisplayedChild(Constants.CONTESTANTS_LAYOUT);
        updateContestantButton();
        updateSubmitButton();
        inGradingForm = false;
    }

    public void openForm(int participantId) {
        currentParticipantId = participantId;
        DatabaseHelper.getRoundStarted(new CallbackBool() {
            @Override
            public void onCallBack(Boolean value) {
                if(value) {
                    GradingForm currentGradingForm = GradingForm.getGradingFormByContestantId(formData, currentParticipantId);
                    adapter = new RatingAdapter(getApplicationContext(), criteriaExtended, currentGradingForm);
                    listView.setAdapter(adapter);
                    binding.setViewModel(currentGradingForm.getParticipantExtended());

                    TextView nameOfContestant = findViewById(R.id.name_of_contestant);
                    nameOfContestant.setText(currentGradingForm.getParticipantExtended().getName());

                    currentGradingForm.getParticipantExtended().setGraded(true);
                    inGradingForm = true;

                    changeDisplayedLayout(Constants.GRADING_LAYOUT);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(finishedGrading)
            return;

        if(inGradingForm) {
            updateContestantButton();
            updateSubmitButton();
            changeDisplayedLayout(Constants.CONTESTANTS_LAYOUT);
        } else {
           // changeDisplayedLayout(Constants.GRADING_LAYOUT);
            return;
        }
        inGradingForm = !inGradingForm;
    }

    private void updateContestantButton() {
        ParticipantExtended currentParticipantExtended = binding.getViewModel();
        Button participantButton = findViewById(currentParticipantExtended.getId());
        if(currentParticipantExtended.isGraded()) {
            participantButton.getBackground().setLevel(Constants.CONTESTANT_GRADED);
        }
    }

    private void updateSubmitButton() {

        submitButton.setEnabled(isSubmitEnabled(participantsExtended));

    }

    private boolean isSubmitEnabled(ArrayList<ParticipantExtended> participantsExtended) {

        for(ParticipantExtended c : participantsExtended) {
            if(!c.isGraded() && !c.getOutOfCompetition() && !c.getDisqualified()) {
                return false;
            }
        }
        return true;
    }

    private void changeDisplayedLayout(int layout) {

        vf.setDisplayedChild(layout);
    }

    private ArrayList<ParticipantExtended> getParticipantExtended(HashMap<String, HashMap<String, Participant>> participants,
                                                                  ArrayList<Criteria> criteria) {

        ArrayList<ParticipantExtended> participantsExtended = new ArrayList<>();

        for(String seriesKey : participants.keySet()) {
            for(String key : participants.get(seriesKey).keySet()) {
                ParticipantExtended p = new ParticipantExtended(participants.get(seriesKey).get(key),
                        criteria.size(),
                        Integer.parseInt(key),
                        seriesKey);
                participantsExtended.add(p);
            }
        }

        return participantsExtended;
    }

    private ArrayList<CriteriaExtended> getExtendedCriteria(ArrayList<Criteria> criteria) {

        ArrayList<CriteriaExtended> criteriaExtended = new ArrayList<>();
        for(int i = 0; i < criteria.size(); i++) {
            criteriaExtended.add(new CriteriaExtended(criteria.get(i).getNume(),
                    criteria.get(i).getPondere(), i));
        }
        return criteriaExtended;
    }
}
