package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AdminMenuActivity extends AppCompatActivity {
    private Button juryButton, logoutButton, contestSetUpButton, contestantsButton, startContestButton;
    private boolean contestStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contestStarted = false;

        juryButton = (Button)findViewById(R.id.jurybutton);
        juryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contestStarted){
                    Toast.makeText(getApplicationContext(), "Can't modify if contest started", Toast.LENGTH_SHORT).show();
                } else {
                    Intent myIntent = new Intent(AdminMenuActivity.this, JuryActivity.class);
                    AdminMenuActivity.this.startActivity(myIntent);
                }
            }
        });

        // this doesn't jump back to AuthActivity because it remembers the user!!!
        logoutButton = (Button)findViewById(R.id.logoutbutton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AdminMenuActivity.this,   AuthActivity.class);
                AdminMenuActivity.this.startActivity(myIntent);
            }
        });

        contestSetUpButton = (Button)findViewById(R.id.contestbutton);
        contestSetUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contestStarted){
                    Toast.makeText(getApplicationContext(), "Can't modify if contest started", Toast.LENGTH_SHORT).show();
                } else {
                    Intent myIntent = new Intent(AdminMenuActivity.this, ContestSetUpActivity.class);
                    AdminMenuActivity.this.startActivity(myIntent);
                }
            }
        });

        contestantsButton = (Button)findViewById(R.id.contestantsbutton);
        contestantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AdminMenuActivity.this,   ContestantsActivity.class);
                AdminMenuActivity.this.startActivity(myIntent);
            }
        });

        startContestButton = (Button)findViewById(R.id.startcontestbutton);
        startContestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contestStarted = !contestStarted;
            }
        });

    }

}
