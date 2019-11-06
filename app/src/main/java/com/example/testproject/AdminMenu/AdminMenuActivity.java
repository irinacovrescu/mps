package com.example.testproject.AdminMenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.testproject.AuthActivity;
import com.example.testproject.Data.CallbackJury;
import com.example.testproject.Data.CallbackInt;
import com.example.testproject.Data.CallbackParticipants;
import com.example.testproject.Data.Criteria;
import com.example.testproject.Data.DatabaseHelper;
import com.example.testproject.Data.Judge;
import com.example.testproject.Participant;
import com.example.testproject.ParticipantExtended;
import com.example.testproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AdminMenuActivity extends AppCompatActivity {
    private Button logoutButton;
    private Button juryButton, contestSetButton, contestantsSetButton, startContestButton, contestantsButton;
    private boolean hasStarted = false;
    private static Integer round = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        DatabaseHelper.getNrRounds(new CallbackInt() {
            @Override
            public void onCallBack(Integer nrRounds) {
                InitUIElem();
                jurySetUp();
                contestSetUp();
                participantsSetUp();
                participants();
                contest(nrRounds);

                logout();
            }
        });


    }

    private void InitUIElem() {
        juryButton = findViewById(R.id.jurybutton);
        contestSetButton = findViewById(R.id.contestbutton);
        contestantsSetButton = findViewById(R.id.contestantssetbutton);
        startContestButton = findViewById(R.id.startcontestbutton);
        startContestButton.setText("Start Round " + round.toString());
        logoutButton = findViewById(R.id.logoutbutton);
        contestantsButton = findViewById(R.id.contestantsbutton);
    }

    private void jurySetUp() {
        juryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AdminMenuActivity.this, JuryActivity.class);
                AdminMenuActivity.this.startActivity(myIntent);
            }
        });
    }

    private void contestSetUp() {
        contestSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper.getNrOfParticipants(new CallbackInt() {
                    @Override
                    public void onCallBack(Integer value) {
                        if (value > 0) {
                            Intent myIntent = new Intent(AdminMenuActivity.this, ContestSetUpActivity.class);
                            AdminMenuActivity.this.startActivity(myIntent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Set up contestants first!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void logout() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set ADMIN as logged out in the database
                AuthActivity.setLoggedStatus(FirebaseDatabase.getInstance().getReference("ADMIN").child("loggedIn"), false);
                Toast.makeText(getApplicationContext(), "Log out successfully!!", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(AdminMenuActivity.this, AuthActivity.class);
                AdminMenuActivity.this.startActivity(myIntent);
            }
        });
    }

    private void participantsSetUp() {
        contestantsSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AdminMenuActivity.this, ParticipantsSetUpActivity.class);
                AdminMenuActivity.this.startActivity(myIntent);
            }
        });
    }

    private void participants() {
        contestantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasStarted) {
                    Intent myIntent = new Intent(AdminMenuActivity.this, ParticipantsActivity.class);
                    AdminMenuActivity.this.startActivity(myIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Start contest first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void contest(final Integer lastRound) {
        startContestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasStarted)
                    isJuryLoggedIn(lastRound);
                else
                    hasJuryVoted(lastRound);
            }


        });
    }

    private void isJuryLoggedIn(Integer lastRound) {
        DatabaseHelper.getJury(lastRound, new CallbackJury() {
            @Override
            public void onCallBack(ArrayList<Judge> value, Integer lastRound) {
                for (Judge j : value) {
                    if (!j.getLoggedIn()) {

                        Toast.makeText(getApplicationContext(), "Jury not logged in", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                DatabaseHelper.getNrOfCriterias(new CallbackInt() {
                    @Override
                    public void onCallBack(Integer value) {
                        if (value > 0) {
                            startContestButton.setText("Stop Round " + round.toString());
                            round++;
                            final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("currentRound");
                            databaseRef.setValue(round);

                            hasStarted = !hasStarted;
                            FirebaseDatabase.getInstance().getReference("roundStarted").setValue(hasStarted);
                        } else {
                            Toast.makeText(getApplicationContext(), "Set up contest first", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    private void hasJuryVoted(Integer lastRound) {
        DatabaseHelper.getJury(lastRound, new CallbackJury() {
            @Override
            public void onCallBack(ArrayList<Judge> value, Integer lastRound) {
                int i = 1;
                for (Judge j : value) {
                    if (!j.getVoted()) {

                        Toast.makeText(getApplicationContext(), "Can't stop round yet", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("JUDGE");
                    databaseRef.child(Integer.toString(i++)).child("voted").setValue(false);

                }

                if(round > lastRound) {
                    startContestButton.setText("Contest has finished");
                    startContestButton.setEnabled(false);
                    return;
                }
                startContestButton.setText("Start Round " + round.toString());
                hasStarted = !hasStarted;

                FirebaseDatabase.getInstance().getReference("roundStarted").setValue(hasStarted);

                DatabaseHelper.getParticipants(new CallbackParticipants() {
                    @Override
                    public void onCallBack(HashMap<String, HashMap<String, Participant>> value, ArrayList<Criteria> criteria) {

                    }

                    @Override
                    public void onCallBack(ArrayList<HashMap<Pair<String, String>, Participant>> value) {
                        ArrayList<ParticipantExtended> list = new ArrayList<>();
                        for (HashMap<Pair<String, String>, Participant> s : value) {
                            for (HashMap.Entry<Pair<String, String>, Participant> entry : s.entrySet()) {

                                Pair<String, String> aux = entry.getKey();
                                String series = aux.first;
                                String id = aux.second;
                                Participant p = entry.getValue();
                                ParticipantExtended newP = new ParticipantExtended(p, 0, Integer.parseInt(id), series);
                                list.add(newP);
                            }
                        }
                        Collections.sort(list);
                        int lastConcurent = list.size() / 2;

                        for (int i = lastConcurent; i < list.size(); i++) {
                            ParticipantExtended aux = list.get(i);
                            final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("participants/" + aux.getNrSerie());
                            databaseRef.child(Integer.toString(aux.getId())).child("outOfCompetition").setValue(true);
                            databaseRef.child(Integer.toString(aux.getId())).child("thisRound_number").setValue(0);
                        }
                    }
                });



            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do your stuff
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
