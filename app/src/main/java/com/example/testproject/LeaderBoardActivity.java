package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;

import com.example.testproject.AdminMenu.ContestSetUpActivity;
import com.example.testproject.Data.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LeaderBoardActivity extends AppCompatActivity {
    int nrOfRounds;
    LinearLayout roundsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        nrOfRounds = (int) Math.ceil
                (Math.log(ContestSetUpActivity.getParticipantsNumberFromDB())/
                        Math.log(2));
        roundsLayout = findViewById(R.id.roundsExtended);
        for (int i = 1; i <= nrOfRounds; i++) {
            createRoundButtons(i, "Round " + i);
        }
        createRoundButtons(nrOfRounds + 1, "Final");
    }

    private void createRoundButtons(Integer buttonID, String buttonText) {
        final Button participantButton = (Button) getLayoutInflater()
                .inflate(R.layout.contestant_button_template, roundsLayout, false);
        participantButton.getBackground().setLevel(Constants.CONTESTANT_NOT_GRADED);
        participantButton.setId(buttonID);
        participantButton.setText(buttonText);
        roundsLayout.addView(participantButton);

        participantButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO:
            }
        });
    }
}
