package com.example.testproject;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ContestSetUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static final int TOTALPOINTS = 60;
    private int pointsUsed = 0;

    private EditText contestantNameBox;
    private Button contestantSetButton;

    private Spinner contestType;

    private EditText roundsNumberBox;
    private Button roundsSetButton;

    private Button reset;

    private EditText originalityNumberBox;
    private Button originalitySetButton;
    private EditText craftmanshipNumberBox;
    private Button craftmanshipSetButton;
    private EditText compositionNumberBox;
    private Button compositionSetButton;
    private EditText unityNumberBox;
    private Button unitySetButton;
    private EditText spaceNumberBox;
    private Button spaceSetButton;
    private EditText interpretationNumberBox;
    private Button interpretationSetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_set_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contestantNameBox = findViewById(R.id.addcontestant);
        contestantSetButton = findViewById(R.id.addcontestantbutton);

        contestantSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toParse = contestantNameBox.getText().toString();
                if (TextUtils.isEmpty(toParse))
                    contestantNameBox.setError("Required");
                else {
                    // add contestant to DB
                }
            }
        });

        roundsNumberBox = findViewById(R.id.rounds);
        roundsSetButton = findViewById(R.id.setroundsbutton);

        roundsSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toParse = roundsNumberBox.getText().toString();
                if (TextUtils.isEmpty(toParse))
                    roundsNumberBox.setError("Required");
                else {
                    // add contestant to DB
                }
            }
        });

        contestType = findViewById(R.id.contesttype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.contest_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contestType.setAdapter(adapter);
        contestType.setOnItemSelectedListener(this);

        originalityNumberBox = findViewById(R.id.originality);
        originalitySetButton = findViewById(R.id.originalitybutton);

        originalitySetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyPoints(originalityNumberBox, "originality");

            }
        });

        craftmanshipNumberBox = findViewById(R.id.craftmanship);
        craftmanshipSetButton = findViewById(R.id.craftmanshipbutton);

        craftmanshipSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyPoints(craftmanshipNumberBox, "craftmanship");
            }
        });

        compositionNumberBox = findViewById(R.id.composition);
        compositionSetButton = findViewById(R.id.compositionbutton);

        compositionSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyPoints(compositionNumberBox, "composition");
            }
        });

        unityNumberBox = findViewById(R.id.unity);
        unitySetButton = findViewById(R.id.unitybutton);

        unitySetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyPoints(unityNumberBox, "unity");
            }
        });

        spaceNumberBox = findViewById(R.id.space);
        spaceSetButton = findViewById(R.id.spacebutton);

        spaceSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyPoints(spaceNumberBox, "space");
            }
        });

        interpretationNumberBox = findViewById(R.id.interpretation);
        interpretationSetButton = findViewById(R.id.interpretationbutton);

        interpretationSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyPoints(interpretationNumberBox, "interpretation");
            }
        });

        reset = findViewById(R.id.pointsbutton);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pointsUsed = 0;
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void verifyPoints (EditText box, String type) {
        String toParse = box.getText().toString();
        if (TextUtils.isEmpty(toParse))
            box.setError("Required");
        else {
            int number = Integer.parseInt(toParse);
            if (number + pointsUsed > TOTALPOINTS) {
                box.setError("Limit Exceeded");
            } else {
                // check if already added to DB
                pointsUsed += number;
                // add to DB with type type
                Toast.makeText(getApplicationContext(), "Points left: " + Integer.toString(TOTALPOINTS - pointsUsed), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
