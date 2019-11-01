package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ContestSetUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static final int TOTALPOINTS = 60;
    private int pointsUsed = 0;

    private EditText setsNumberBox;
    private Button setsButton;

    private TextView rounds;

    private Spinner contestType;

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

    private Button submitButton;

    private int originality = 0;
    private int craftmanship = 0;
    private int composition = 0;
    private int unity = 0;
    private int space = 0;
    private int interpretation = 0;
    private int sets = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_set_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InitUIElem();

        criteriaSet();

        setsSet();

        rounds.setText("Number of rounds: " +
                Integer.toString((int) Math.ceil
                (Math.log(getContestantsNumberFromDB())/
                Math.log(2))));

        reset();
        submit();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void InitUIElem(){
        setsNumberBox = findViewById(R.id.addsets);
        setsButton = findViewById(R.id.addsetsbutton);

        contestType = findViewById(R.id.contesttype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.contest_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contestType.setAdapter(adapter);
        contestType.setOnItemSelectedListener(this);

        originalityNumberBox = findViewById(R.id.originality);
        originalitySetButton = findViewById(R.id.originalitybutton);

        craftmanshipNumberBox = findViewById(R.id.craftmanship);
        craftmanshipSetButton = findViewById(R.id.craftmanshipbutton);

        compositionNumberBox = findViewById(R.id.composition);
        compositionSetButton = findViewById(R.id.compositionbutton);

        unityNumberBox = findViewById(R.id.unity);
        unitySetButton = findViewById(R.id.unitybutton);

        spaceNumberBox = findViewById(R.id.space);
        spaceSetButton = findViewById(R.id.spacebutton);

        interpretationNumberBox = findViewById(R.id.interpretation);
        interpretationSetButton = findViewById(R.id.interpretationbutton);

        reset = findViewById(R.id.pointsbutton);

        rounds = findViewById(R.id.rounds);

        submitButton = findViewById(R.id.submit);

    }

    private void criteriaSet(){
        originalitySetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                originality = verifyPoints(originalityNumberBox, originality);
                addCriteriaToDB("originality", originality);

            }
        });

        craftmanshipSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                craftmanship = verifyPoints(craftmanshipNumberBox, craftmanship);
                addCriteriaToDB("craftmanship", craftmanship);
            }
        });

        compositionSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                composition = verifyPoints(compositionNumberBox, composition);
                addCriteriaToDB("composition", composition);
            }
        });

        unitySetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unity = verifyPoints(unityNumberBox, unity);
                addCriteriaToDB("unity", unity);
            }
        });

        spaceSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                space = verifyPoints(spaceNumberBox, space);
                addCriteriaToDB("space", space);
            }
        });

        interpretationSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                interpretation = verifyPoints(interpretationNumberBox, interpretation);
                addCriteriaToDB("interpretation", interpretation);
            }
        });
    }

    private void setsSet(){
        setsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toParse = setsNumberBox.getText().toString();
                if (TextUtils.isEmpty(toParse))
                    setsNumberBox.setError("Required");
                else {
                    sets = Integer.parseInt(toParse);
                    addSetsToDB(sets);
                }
            }
        });
    }

    private void reset(){
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pointsUsed = 0;
            }
        });
    }

    private void submit() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sets == 0 || pointsUsed == 0) {
                    Toast.makeText(getApplicationContext(), "Set up contest first!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent myIntent = new Intent(ContestSetUpActivity.this, AdminMenuActivity.class);
                    ContestSetUpActivity.this.startActivity(myIntent);
                }
            }
        });
    }

    private void addCriteriaToDB(String type, int number){
        // add to DB
    }

    private int verifyPoints (EditText box, int criteria) {
        String toParse = box.getText().toString();
        if (TextUtils.isEmpty(toParse))
            box.setError("Required");
        else {
            int number = Integer.parseInt(toParse);
            if (criteria > 0) {
                pointsUsed -= criteria;
            }
            if (number + pointsUsed > TOTALPOINTS) {
                pointsUsed += criteria;
                box.setError("Limit Exceeded");
            } else {
                pointsUsed += number;
                Toast.makeText(getApplicationContext(), "Points left: " + Integer.toString(TOTALPOINTS - pointsUsed), Toast.LENGTH_SHORT).show();
                return number;
            }
        }
        return 0;
    }

    private int getContestantsNumberFromDB() {
        // get no of contestants from DB
        return 24;
    }

    private void addSetsToDB(int number){

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //do your stuff
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
