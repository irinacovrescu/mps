package com.example.testproject;

import android.os.Bundle;

import com.example.testproject.Data.CallbackInt;
import com.example.testproject.Data.Constants;
import com.example.testproject.Data.DatabaseHelper;

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

import java.util.HashMap;

public class LeaderBoardActivity extends AppCompatActivity {
    int nrOfRounds;
    LinearLayout roundsLayout, tableLayout;
    ViewFlipper vf;
    //Button back;
    TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        createButtons();

    }

    @Override
    public void onBackPressed() {
        if (vf.getDisplayedChild() == 1)
            vf.showPrevious();
        else {
            finish();
        }
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
                leaderboardHandler();
            }
        });
    }

    private void leaderboardHandler() {

        tl = findViewById(R.id.leaderboard);
        tl.setStretchAllColumns(true);

        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.leaderboard_row, null);

        TextView cellN = (TextView) row.getChildAt(0);
        cellN.setText("ala");
        TextView cellS = (TextView) row.getChildAt(1);
        cellS.setText("bala");
        TextView cellD = (TextView) row.getChildAt(2);
        cellD.setText("portocala");

        tl.addView(row);
    }

    private void createButtons() {
        DatabaseHelper.getNrOfParticipants(new CallbackInt() {
            @Override
            public void onCallBack(Integer value) {
                nrOfRounds = (int) Math.ceil
                        (Math.log(value) /
                                Math.log(2));
                roundsLayout = findViewById(R.id.roundsExtended);
                vf = findViewById(R.id.viewflipper);
                for (int i = 1; i <= nrOfRounds; i++) {
                    createRoundButtons(i, "Round " + i);
                }
                createRoundButtons(nrOfRounds + 1, "Final");
            }
        });
    }
}
