package com.example.testproject.AdminMenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testproject.R;

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
                    Toast.makeText(getApplicationContext(), "Contestants number set to " + Integer.toString(contestantsNumber), Toast.LENGTH_SHORT).show();
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
                    if (contestantsLeft > 0) {
                        addContestantToDB(toParse);
                        contestantsLeft--;
                        contestantNameBox.getText().clear();
                        Toast.makeText(getApplicationContext(), "Contestants left to add: " + Integer.toString(contestantsLeft), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Too many contestants!", Toast.LENGTH_SHORT).show();
                    }
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
