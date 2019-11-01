package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContestantsSetUpActivity extends AppCompatActivity {

    /* list of contestants extracted from DB
        so they can be disqualified as needed
     */

    private int contestantsNumber = 0;
    private int contestantsLeft;

    private EditText contestantNameBox;
    private Button contestantSetButton;
    private EditText contestantNumberBox;
    private Button contestantAddButton;
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contestants_set_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InitUIElem();
        contestantsNumber();
        contestantAdd();
        submit();

    }

    private void InitUIElem() {
        contestantNameBox = findViewById(R.id.contestantsname);
        contestantAddButton = findViewById(R.id.contestantsadd);

        contestantNumberBox = findViewById(R.id.contestantsnumber);
        contestantSetButton = findViewById(R.id.contestantsset);

        submitButton = findViewById(R.id.submit);
    }

    private void contestantsNumber() {
        contestantSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toParse = contestantNumberBox.getText().toString();
                if (TextUtils.isEmpty(toParse)) {
                    contestantNumberBox.setError("Required");
                } else {
                    contestantsNumber = Integer.parseInt(toParse);
                    contestantsLeft = contestantsNumber;
                    addNumberOfContestantsToDB(contestantsNumber);
                }
            }
        });
    }

    private void contestantAdd(){
        contestantAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toParse = contestantNameBox.getText().toString();
                if (TextUtils.isEmpty(toParse)) {
                    contestantNameBox.setError("Required");
                } else {
                    addContestantToDB(toParse);
                    contestantsLeft--;
                }
            }
        });
    }

    private void addContestantToDB(String name) {
        // add to DB;
    }

    private void addNumberOfContestantsToDB(int number) {
        // add to DB
    }

    private void submit() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((contestantsNumber == 0) || (contestantsLeft != 0)) {
                    Toast.makeText(getApplicationContext(), "Set up contestants first!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent myIntent = new Intent(ContestantsSetUpActivity.this, AdminMenuActivity.class);
                    ContestantsSetUpActivity.this.startActivity(myIntent);
                }
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //do your stuff
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
