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

import com.example.testproject.Participant;
import com.example.testproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ContestantsSetUpActivity extends AppCompatActivity {

    /* list of contestants extracted from DB
        so they can be disqualified as needed
     */
    private static final int CONTESTANTSPERSERIES = 10;
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
                    if (contestantsNumber == 0) {
                        contestantNumberBox.setError("Can't be 0");
                    } else {
                        contestantsLeft = contestantsNumber;
                        addNumberOfContestantsToDB(contestantsNumber);
                        Toast.makeText(getApplicationContext(), "Contestants number set to " + Integer.toString(contestantsNumber), Toast.LENGTH_SHORT).show();
                    }
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
                        int id = contestantsNumber - contestantsLeft + 1;
                        addParticipant(Integer.toString(setSeries(id)), Integer.toString(id), toParse, setRounds());
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

    public void addParticipant(String series, String id, String name, Integer rounds){

        //Obtine referinta catre baza de date la adresa dorita
        final DatabaseReference databaseRef =  FirebaseDatabase.getInstance().getReference("participants").child(series);

        Participant p = new Participant(name,rounds);
        databaseRef.child(id).setValue(p);

    }

    private void addNumberOfContestantsToDB(int number) {
        // add to DB
    }

    private int setRounds(){
        return (int) Math.ceil(Math.log(contestantsNumber)/ Math.log(2));
    }

    private int setSeries(int id){

        int series = contestantsNumber / CONTESTANTSPERSERIES;
        int new_cps;
        if (series == 0) {
            new_cps = CONTESTANTSPERSERIES;
        } else {
            new_cps = CONTESTANTSPERSERIES + ((contestantsNumber % CONTESTANTSPERSERIES) / series);
        }
        ArrayList<Integer> end = new ArrayList<Integer>(series);
        int offset = 0;
        for (int i = 0; i < series; i++)
        {
            end.add(i, (i + 1) * new_cps + Math.min(contestantsNumber%new_cps, ++offset));
        }
        int x_series = 1;
        for (int i = 0; i < series; i++)
        {
            if (id <= end.get(i))
            {
                x_series += i;
                break;
            }
        }
        return x_series;
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

}
