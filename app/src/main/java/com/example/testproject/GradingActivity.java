package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.testproject.Data.CriteriaExtended;
import com.example.testproject.Data.Criteria;
import com.example.testproject.Data.GradingForm;
import com.example.testproject.Data.MyCallback;
import com.example.testproject.databinding.ActivityGradingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Iterator;

public class GradingActivity extends AppCompatActivity {

    private static final String TAG = "GradingActivity";
    private static RatingAdapter adapter;

    private ArrayList<GradingForm> formData;
    private ArrayList<CriteriaExtended> criteriaExtended;
    private ArrayList<Contestant> contestants;

    private ListView listView;
    private ActivityGradingBinding binding;
    private Button submitButton;
    private Button returnButton;
    private ViewFlipper vf;
    LinearLayout contestantsLayout;
    LinearLayout finishedGradingLayout;

    private boolean inGradingForm = false;
    private boolean finishedGrading = false;

    private final int CONTESTANTS_LAYOUT = 0;
    private final int GRADING_LAYOUT = 1;
    private final int FINISHED_GRADING_LAYOUT = 2;

    private final int CONTESTANT_NOT_GRADED = 0;
    private final int CONTESTANT_OUT = 1;
    private final int CONTESTANT_DISQUALIFIED = 2;
    private final int CONTESTANT_GRADED = 3;
    private final int DONE = 4;
    private final int SUBMIT = 5;
    private final int RETURN = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromServer();
    }

    public void getDataFromServer() {
        getCriteria(new MyCallback() {
            @Override
            public void onCallBack(ArrayList<Criteria> criteria) {
                contestants = getContestants();
                prepareData(criteria);
                prepareUIElements();
                createContestantElements(criteria);
                createSubmitButton();
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
        contestantsLayout = findViewById(R.id.contestants);
        finishedGradingLayout = findViewById(R.id.finished_grading);

        TextView contestantsText = findViewById(R.id.contestants_text);
        contestantsText.setText("CONTESTANTS");

        final Button submitButtonC = findViewById(R.id.submit_button_contestant);
        submitButtonC.getBackground().setLevel(DONE);
        submitButtonC.setText("DONE");
        submitButtonC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submitForOneContestant();
            }
        });
    }

    private void createContestantElements(ArrayList<Criteria> criteria) {
        formData = new ArrayList<>();

        for(Contestant c : contestants) {
            createButtonForContestant(c);
            formData.add(createGradingFormForContestant(c, criteria));
        }
    }

    public GradingForm createGradingFormForContestant(Contestant c, ArrayList<Criteria> criteria) {
        GradingForm gradingForm = new GradingForm(c);
        for(int i = 0; i < criteria.size(); i++) {
            gradingForm.addGrade(i);
        }

        return gradingForm;
    }

    private void createReturnButton() {
        returnButton = findViewById(R.id.returnbutton);
        returnButton.getBackground().setLevel(RETURN);
        returnButton.setText("Return");
        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(GradingActivity.this,   MainActivity.class);
                GradingActivity.this.startActivity(myIntent);
            }
        });
    }

    private void createButtonForContestant(Contestant c) {
        final Button contestantButton = (Button) getLayoutInflater()
                .inflate(R.layout.contestant_button_template, contestantsLayout, false);
        contestantButton.getBackground().setLevel(CONTESTANT_NOT_GRADED);
        contestantButton.setId(c.getId());
        contestantButton.setText(c.getName());
        contestantsLayout.addView(contestantButton);
        contestantButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int contestantId = v.getId();
                openForm(contestantId);
            }
        });

        setColorForContestantButton(c, contestantButton);
    }

    private void setColorForContestantButton(Contestant c, Button contestantButton) {
        if(c.isDisqualified()) {
            contestantButton.setEnabled(false);
            contestantButton.getBackground().setLevel(CONTESTANT_DISQUALIFIED);

        }

        if(c.isOutOfCompetition()) {
            contestantButton.setEnabled(false);
            contestantButton.getBackground().setLevel(CONTESTANT_OUT);
        }
    }

    private void createSubmitButton() {
        submitButton = (Button) getLayoutInflater()
                .inflate(R.layout.submit_button_template, contestantsLayout, false);
        submitButton.getBackground().setLevel(SUBMIT);
        submitButton.setEnabled(false);
        submitButton.setText("Submit");
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submit();
            }
        });

        contestantsLayout.addView(submitButton);
    }


    public void submit() {
        vf.setDisplayedChild(FINISHED_GRADING_LAYOUT);
        finishedGrading = true;
        TextView thank_you_text = findViewById(R.id.thank_you_text);
        thank_you_text.setText("Thank you for submitting your grades!");
        // TO DO: prepare data and send to database

        createReturnButton();
    }

    public void submitForOneContestant() {
        vf.setDisplayedChild(CONTESTANTS_LAYOUT);
        updateContestantButton();
        updateSubmitButton();
        inGradingForm = false;
    }

    public void openForm(int contestantId) {
        GradingForm currentGradingForm = GradingForm.getGradingFormByContestantId(formData, contestantId);
        adapter = new RatingAdapter(getApplicationContext(), criteriaExtended, currentGradingForm);
        listView.setAdapter(adapter);
        binding.setViewModel(currentGradingForm.getContestant());

        TextView nameOfContestant = findViewById(R.id.name_of_contestant);
        nameOfContestant.setText(currentGradingForm.getContestant().getName());

        currentGradingForm.getContestant().setGraded(true);
        inGradingForm = true;

        changeDisplayedLayout(GRADING_LAYOUT);
    }

    @Override
    public void onBackPressed() {
        if(finishedGrading)
            return;

        if(inGradingForm) {
            updateContestantButton();
            updateSubmitButton();
            changeDisplayedLayout(CONTESTANTS_LAYOUT);
        } else {
            changeDisplayedLayout(GRADING_LAYOUT);
        }
        inGradingForm = !inGradingForm;
    }

    private void updateContestantButton() {
        Contestant currentContestant = binding.getViewModel();
        Button contestantButton = findViewById(currentContestant.getId());
        if(currentContestant.isGraded()) {
            contestantButton.getBackground().setLevel(CONTESTANT_GRADED);
        }
    }

    private void updateSubmitButton() {

        submitButton.setEnabled(isSubmitEnabled());
    }

    private boolean isSubmitEnabled() {

        for(Contestant c : contestants) {
            if(!c.isGraded() && !c.isOutOfCompetition() && !c.isDisqualified()) {
                return false;
            }
        }
        return true;
    }

    private void changeDisplayedLayout(int layout) {

        vf.setDisplayedChild(layout);
    }

    public void getCriteria(final MyCallback myCallback){

        final DatabaseReference databaseRef =  FirebaseDatabase.getInstance().getReference("criteria");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Criteria> list = new ArrayList<>();

                Iterator<DataSnapshot> serieIterator = dataSnapshot.getChildren().iterator();
                while (serieIterator.hasNext()) {
                    //Itereaza prin dataSnapshot-uri (ce reprezinta participanti)
                    DataSnapshot d = serieIterator.next();
                    list.add(d.getValue(Criteria.class));
                }
                myCallback.onCallBack(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<Contestant> getContestants() {
        ArrayList<Contestant> contestants = new ArrayList<>();
        contestants.add(new Contestant("Irina Covrescu", 1));
        contestants.add(new Contestant("Teodora Labusca", 2));
        contestants.add(new Contestant("Daniela Ion", 3));
        contestants.add(new Contestant("Raul Cremenescu", 4));
        contestants.add(new Contestant("Eric Postolache", 5));

        contestants.get(1).setDisqualified(true);
        contestants.get(3).setOutOfCompetition(true);

        return contestants;
    }

    private ArrayList<CriteriaExtended> getExtendedCriteria(ArrayList<Criteria> criteria) {

        ArrayList<CriteriaExtended> criteriaExtended = new ArrayList<>();
        for(int i = 0; i < criteria.size(); i++) {
            criteriaExtended.add(new CriteriaExtended(criteria.get(i).getNume(), criteria.get(i).getPondere(), i));
        }
        return criteriaExtended;
    }
}
