package com.example.testproject.AdminMenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testproject.Data.Constants;
import com.example.testproject.Participant;
import com.example.testproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ParticipantsSetUpActivity extends AppCompatActivity {

    private int participantsNumber = 0;
    private int participantsLeft;
    private int series = 0;

    private EditText participantNameBox;
    private Button participantSetButton;
    private EditText participantNumberBox;
    private Button participantAddButton;
    private Button submitButton;

    private EditText seriesNumberBox;
    private Button seriesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_set_up);

        InitUIElem();
        participantsNumber();
        series();
        participantAdd();
        submit();

    }

    private void InitUIElem() {
        participantNameBox = findViewById(R.id.contestantsname);
        participantAddButton = findViewById(R.id.contestantsadd);

        participantNumberBox = findViewById(R.id.contestantsnumber);
        participantSetButton = findViewById(R.id.contestantsset);

        seriesNumberBox = findViewById(R.id.seriesnumber);
        seriesButton = findViewById(R.id.seriessset);

        submitButton = findViewById(R.id.submit);
    }

    private void participantsNumber() {
        participantSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toParse = participantNumberBox.getText().toString();
                if (TextUtils.isEmpty(toParse)) {
                    participantNumberBox.setError("Required");
                } else {
                    participantsNumber = Integer.parseInt(toParse);
                    if (participantsNumber == 0) {
                        participantNameBox.setError("Can't be 0");
                    } else {
                        participantsLeft = participantsNumber;
                        addNrOfParticipants(participantsNumber);
                        final DatabaseReference databaseRef =  FirebaseDatabase.getInstance().getReference("participants");

                        databaseRef.removeValue();
                        Toast.makeText(getApplicationContext(), "Participants number set to " + participantsNumber, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void participantAdd()
    {
        participantAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (series == 0) {
                    seriesNumberBox.setError("Set first");
                    return;
                }
                String toParse = participantNameBox.getText().toString();
                if (TextUtils.isEmpty(toParse)) {
                    participantNameBox.setError("Required");
                } else {
                    if (participantsLeft > 0) {
                        int id = participantsNumber - participantsLeft + 1;
                        final DatabaseReference databaseRef =  FirebaseDatabase.getInstance().getReference("participants").child("series"+Integer.toString(setSeries(id)));

                        Participant p = new Participant(toParse,setRounds());
                        databaseRef.child(Integer.toString(id)).setValue(p);
                        participantsLeft--;
                        participantNameBox.getText().clear();
                        Toast.makeText(getApplicationContext(), "Participants left to add: " + participantsLeft, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Too many participants!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void addNrOfParticipants(Integer number) {
        FirebaseDatabase.getInstance().getReference("nrOfParticipants").setValue(number);

        FirebaseDatabase.getInstance().getReference("nrOfRounds").setValue((int) Math.ceil(Math.log(number)/ Math.log(2)));
    }

    private int setRounds(){
        return (int) Math.ceil(Math.log(participantsNumber)/ Math.log(2));
    }

    private void series(){
        seriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toParse = seriesNumberBox.getText().toString();
                if (TextUtils.isEmpty(toParse)) {
                    seriesNumberBox.setError("Required");
                } else {
                    series = Integer.parseInt(toParse);
                    Toast.makeText(getApplicationContext(), "Series set!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private int setSeries(int id){
        int aux = participantsNumber/series;
        int r = participantsNumber % series;
        int [] end = new int[aux];
        int x = 0;
        for (int i = 0; i < aux; i++) {
            end[i] = (i + 1) * aux + Math.min(++x, r);
            if (id <= end[i]) {
                return i+1;
            }
        }
        return 0;
    }

    private void submit() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((participantsNumber == 0) || (participantsLeft != 0)) {
                    Toast.makeText(getApplicationContext(), "Set up participants first!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent myIntent = new Intent(ParticipantsSetUpActivity.this, AdminMenuActivity.class);
                    ParticipantsSetUpActivity.this.startActivity(myIntent);
                }
            }
        });
    }

}
