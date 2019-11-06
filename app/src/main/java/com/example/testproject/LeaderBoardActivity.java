package com.example.testproject;

import android.os.Bundle;

import com.example.testproject.Data.CallbackInt;
import com.example.testproject.Data.Constants;
import com.example.testproject.Data.DatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class LeaderBoardActivity extends AppCompatActivity {
    Long nrOfRounds;
    LinearLayout roundsLayout, tableLayout;
    ViewFlipper vf;
    TableLayout tl;
    Boolean isContestFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        final DatabaseReference contestFinishedRef = FirebaseDatabase.getInstance().getReference("isContestFinished");
        contestFinishedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modifyContestFinished((Boolean) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        createButtons();

    }

    private void modifyContestFinished(Boolean value) {
        isContestFinished = value;
    }

    @Override
    public void onBackPressed() {
        if (vf.getDisplayedChild() == 1)
            vf.showPrevious();
        else {
            finish();
        }
    }

    private void createRoundButtons(final Integer buttonID, String buttonText) {
        final Button participantButton = (Button) getLayoutInflater()
                .inflate(R.layout.contestant_button_template, roundsLayout, false);
        participantButton.getBackground().setLevel(Constants.CONTESTANT_NOT_GRADED);
        participantButton.setId(buttonID);
        participantButton.setText(buttonText);
        roundsLayout.addView(participantButton);

        participantButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO:
                vf.showNext();
                leaderboard(buttonID);
                //displayLeaderboard();
            }
        });
    }

    private void displayLeaderboard(ArrayList<Participant> participantsList) {

        tl = findViewById(R.id.leaderboard);

        for (int i = 1, j = tl.getChildCount(); i < j; i++) {
            View view = tl.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                tl.removeView(row);
            }
        }

        while (tl.getChildCount() > 1) {
            for (int i = 1, j = tl.getChildCount(); i < j; i++) {
                View view = tl.getChildAt(i);
                if (view instanceof TableRow) {
                    TableRow row = (TableRow) view;
                    tl.removeView(row);
                }
            }
        }

        int rank = 1;

        for (Participant participant : participantsList) {
            TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.leaderboard_row, null);

            TextView cellN = (TextView) row.getChildAt(0);
            cellN.setText(Integer.toString(rank));

            TextView cellS = (TextView) row.getChildAt(1);
            cellS.setText(participant.getName());

            TextView cellD = (TextView) row.getChildAt(2);

            if (participant.getDisqualified()) {
                cellD.setText("Disqualified");
            } else if (participant.getOutOfCompetition()) {
                cellD.setText("Out of competition!");
            } else {
                cellD.setText(Integer.toString(participant.getThisRounds_points()));
            }

            rank++;
            tl.addView(row);
        }
    }

    private void createButtons() {
        DatabaseHelper.getNrOfParticipants(new CallbackInt() {
            @Override
            public void onCallBack(Integer value) {
                final DatabaseReference nrOfRoundsRef = FirebaseDatabase.getInstance().getReference("nrOfRounds");
                nrOfRoundsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nrOfRounds = (Long) dataSnapshot.getValue();
                        roundsLayout = findViewById(R.id.roundsExtended);
                        vf = findViewById(R.id.viewflipper);
                        for (int i = 1; i <= nrOfRounds; i++) {
                            if (i != nrOfRounds) {
                                createRoundButtons(i, "Round " + i);
                            }
                            else {
                                createRoundButtons(i, "Final Round");
                            }
                        }
                        createRoundButtons(9999, "Final Results");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void leaderboard(final int roundNumber) {
        //Obtine referinta catre baza de date la adresa dorita
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("participants");

        //La fiecare schimbare de date
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Participant> partipantsList = new ArrayList<>();

                Iterator<DataSnapshot> serieIterator = dataSnapshot.getChildren().iterator();
                while (serieIterator.hasNext()) {
                    Iterator<DataSnapshot> idIterator = serieIterator.next().getChildren().iterator();
                    while (idIterator.hasNext()) {
                        DataSnapshot d = idIterator.next();
                        Participant p = d.getValue(Participant.class);

                        //Pastreaza numai participantii eligibili
                        if (!isContestFinished) {
                            if (p.getDisqualified() || p.getOutOfCompetition()) {
                                p.setThisRounds_points(0);
                                partipantsList.add(p);
                            } else if (p.getThisRound_number() == roundNumber) {
                                partipantsList.add(p);
                            }
                        } else if (roundNumber == 9999) {
                            if (p.getDisqualified() || p.getOutOfCompetition()) {
                                p.setThisRounds_points(0);
                            } else {
                                p.setThisRounds_points(p.getThisRounds_points() + p.getLastRounds_points());
                            }
                            partipantsList.add(p);
                        }
                    }
                }

                //sorteaza descrescator dupa punctajul de la thisRound_points
                Collections.sort(partipantsList);
                displayLeaderboard(partipantsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
