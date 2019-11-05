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

    private EditText participantNameBox;
    private Button participantSetButton;
    private EditText participantNumberBox;
    private Button participantAddButton;
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_set_up);

        InitUIElem();
        participantsNumber();
        participantAdd();
        submit();

    }

    private void InitUIElem() {
        participantNameBox = findViewById(R.id.contestantsname);
        participantAddButton = findViewById(R.id.contestantsadd);

        participantNumberBox = findViewById(R.id.contestantsnumber);
        participantSetButton = findViewById(R.id.contestantsset);

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

    private void participantAdd(){
        participantAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }

    private int setRounds(){
        return (int) Math.ceil(Math.log(participantsNumber)/ Math.log(2));
    }

    private int setSeries(int id){

        int series = participantsNumber / Constants.PARTICIPANTSPERSERIES;
        int new_cps;
        if (series == 0) {
            new_cps = Constants.PARTICIPANTSPERSERIES;
        } else {
            new_cps = Constants.PARTICIPANTSPERSERIES + ((participantsNumber % Constants.PARTICIPANTSPERSERIES) / series);
        }
        ArrayList<Integer> end = new ArrayList<Integer>(series);
        int offset = 0;
        for (int i = 0; i < series; i++)
        {
            end.add(i, (i + 1) * new_cps + Math.min(participantsNumber%new_cps, ++offset));
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
