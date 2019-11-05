package com.example.testproject.AdminMenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.testproject.AuthActivity;
import com.example.testproject.Data.CallbackJury;
import com.example.testproject.Data.CallbackInt;
import com.example.testproject.Data.DatabaseHelper;
import com.example.testproject.Data.Judge;
import com.example.testproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminMenuActivity extends AppCompatActivity {
    private Button logoutButton;
    private Button juryButton, contestSetButton, contestantsSetButton, startContestButton, contestantsButton;
    private boolean hasStarted = false;
    private static int round = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        InitUIElem();
        jurySetUp();
        contestSetUp();
        participantsSetUp();
        participants();
        contest();


        logout();
    }

    private void InitUIElem() {
        juryButton = findViewById(R.id.jurybutton);
        contestSetButton = findViewById(R.id.contestbutton);
        contestantsSetButton = findViewById(R.id.contestantssetbutton);
        startContestButton = findViewById(R.id.startcontestbutton);
        startContestButton.setText("Start Round");
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

    private void participants(){
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

    private void contest() {
        startContestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasStarted)
                    isJuryLoggedIn();
                else
                    hasJuryVoted();
            }


        });
    }

    private void isJuryLoggedIn() {
        DatabaseHelper.getJury(new CallbackJury() {
            @Override
            public void onCallBack(ArrayList<Judge> value) {
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
                             startContestButton.setText("Stop Round");
                             final DatabaseReference databaseRef =  FirebaseDatabase.getInstance().getReference("currentRound");
                             databaseRef.setValue(round);
                             round++;
                             hasStarted = !hasStarted;
                             FirebaseDatabase.getInstance().getReference("isContestFinished").setValue(!hasStarted);
                         } else {
                             Toast.makeText(getApplicationContext(), "Set up contest first", Toast.LENGTH_SHORT).show();
                         }

                     }
                 });
            }
        });
    }

    private void hasJuryVoted() {
        DatabaseHelper.getJury(new CallbackJury() {
            @Override
            public void onCallBack(ArrayList<Judge> value) {
                for (Judge j : value) {
                    if (!j.getVoted()) {

                        Toast.makeText(getApplicationContext(), "Can't stop round yet", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                startContestButton.setText("Start Round");
                hasStarted = !hasStarted;
                FirebaseDatabase.getInstance().getReference("isContestFinished").setValue(!hasStarted);


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
