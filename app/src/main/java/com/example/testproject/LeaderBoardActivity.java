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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class LeaderBoardActivity extends AppCompatActivity {
    int nrOfRounds;
    LinearLayout roundsLayout, tableLayout;
    ViewFlipper vf;
    Button back;
    TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        nrOfRounds = (int) Math.ceil
                (Math.log(ContestSetUpActivity.getParticipantsNumberFromDB())/
                        Math.log(2));
        roundsLayout = findViewById(R.id.roundsExtended);
        tableLayout = findViewById(R.id.linearlayout);
        vf = findViewById(R.id.viewflipper);
        back = findViewById(R.id.back);
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
                vf.showNext();
                display();
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tl.removeAllViews();
                        vf.showPrevious();
                    }
                });
            }
        });
    }

    private void display(){
        tl = new TableLayout(this);

        TableRow row = new TableRow(this);
        TextView cellN = new TextView(this);
        cellN.setText("Name");
        row.addView(cellN);
        TextView cellS = new TextView(this);
        cellS.setText("Series");
        row.addView(cellS);
        TextView cellD = new TextView(this);
        cellD.setText("Points");
        row.addView(cellD);
        tl.addView(row);
        tableLayout.addView(tl);
    }
}
