package com.example.testproject.AdminMenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.testproject.AuthActivity;
import com.example.testproject.R;
import com.google.firebase.database.FirebaseDatabase;

public class AdminMenuActivity extends AppCompatActivity {
    private Button logoutButton;
    private Button juryButton, contestSetButton, contestantsSetButton, startContestButton, contestantsButton;
    private boolean hasStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        InitUIElem();
        jurySetUp();
        contestSetUp();
        contestantsSetUp();
        contestants();

        contest();

        logout();
    }

    private void InitUIElem() {
        juryButton = findViewById(R.id.jurybutton);
        contestSetButton = findViewById(R.id.contestbutton);
        contestantsSetButton = findViewById(R.id.contestantssetbutton);
        startContestButton = findViewById(R.id.startcontestbutton);
        startContestButton.setText("Start Contest");
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
                Intent myIntent = new Intent(AdminMenuActivity.this, ContestSetUpActivity.class);
                AdminMenuActivity.this.startActivity(myIntent);
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

    private void contestantsSetUp() {
        contestantsSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areContestantsSet()) {
                    Intent myIntent = new Intent(AdminMenuActivity.this, ContestantsSetUpActivity.class);
                    AdminMenuActivity.this.startActivity(myIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Set up contestants first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void contestants(){
        contestantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areContestantsSet()) {
                    Intent myIntent = new Intent(AdminMenuActivity.this, ContestantsActivity.class);
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

                if (contestCanStart()) {
                    hasStarted = !hasStarted;
                    if (hasStarted)
                        startContestButton.setText("Stop Contest");
                    else
                        startContestButton.setText("Start Contest");
                } else {
                    Toast.makeText(getApplicationContext(), "Can't start contest yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean contestCanStart() {

        if (isContestSet() && isJurySet())
            return true;
        return false;
    }

    private boolean isJurySet() {

        // val > 0 from DB, true else false
        return true;
    }

    private boolean isContestSet() {
        // val > 0 from DB, true else false
        return true;
    }

    private boolean areContestantsSet() {
        // val > 0 from DB, true else false
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do your stuff
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
