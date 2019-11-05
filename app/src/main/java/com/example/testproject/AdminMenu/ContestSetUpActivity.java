package com.example.testproject.AdminMenu;

import android.content.Intent;
import android.os.Bundle;

import com.example.testproject.Data.CallbackInt;
import com.example.testproject.Data.Criteria;
import com.example.testproject.Data.DatabaseHelper;
import com.example.testproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContestSetUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static TextView rounds;
    private TextView series;

    private Spinner contestType;
    private static int nrOfParticipants;

    private Button reset;

    private EditText originalityNumberBox;
    private Button originalitySetButton;
    private EditText craftsmanshipNumberBox;
    private Button craftsmanshipSetButton;
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
    private int craftsmanship = 0;
    private int composition = 0;
    private int unity = 0;
    private int space = 0;
    private int interpretation = 0;
    private ArrayList<Criteria> criterias = new ArrayList<Criteria>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_set_up);


        InitUIElem();

        criteriaSet();
        getNrOfParticipants();

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

        contestType = findViewById(R.id.contesttype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.contest_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contestType.setAdapter(adapter);
        contestType.setOnItemSelectedListener(this);

        originalityNumberBox = findViewById(R.id.originality);
        originalitySetButton = findViewById(R.id.originalitybutton);

        craftsmanshipNumberBox = findViewById(R.id.craftmanship);
        craftsmanshipSetButton = findViewById(R.id.craftmanshipbutton);

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

    public void addManyCriteria(ArrayList<Criteria> list){

        final DatabaseReference databaseRef =  FirebaseDatabase.getInstance().getReference("criteria");
        databaseRef.setValue(list);

    }

    private void criteriaSet(){
        originalitySetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                originality = verifyPoints(originalityNumberBox);
                addCriteria("Originality", originality, criterias);

            }
        });

        craftsmanshipSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                craftsmanship = verifyPoints(craftsmanshipNumberBox);
                addCriteria("Craftsmanship", craftsmanship, criterias);
            }
        });

        compositionSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                composition = verifyPoints(compositionNumberBox);
                addCriteria("Composition", composition, criterias);
            }
        });

        unitySetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unity = verifyPoints(unityNumberBox);
                addCriteria("Unity", unity, criterias);
            }
        });

        spaceSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                space = verifyPoints(spaceNumberBox);
                addCriteria("Space", space, criterias);
            }
        });

        interpretationSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                interpretation = verifyPoints(interpretationNumberBox);
                addCriteria("Interpretation", interpretation, criterias);
            }
        });
    }


    private void reset(){
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                originality = 0;
                craftsmanship = 0;
                composition = 0;
                unity = 0;
                space = 0;
                interpretation = 0;;
            }
        });
    }

    private void submit() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(criterias.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Set up contest first!", Toast.LENGTH_SHORT).show();
                } else {
                    addManyCriteria(criterias);
                    Intent myIntent = new Intent(ContestSetUpActivity.this, AdminMenuActivity.class);
                    ContestSetUpActivity.this.startActivity(myIntent);
                }
            }
        });
    }

    private void addCriteria(String type, int number, ArrayList<Criteria> list){

        int index = 0;
        boolean found = false;
        for (Criteria c : list) {
            String name = c.getNume();
            if (name.equals(type)){
                found = true;
                break;
            }
            index++;
        }
        if (found)
            list.remove(index);
        Criteria c = new Criteria(type, number);
        list.add(c);
    }

    private int verifyPoints (EditText box) {
        String toParse = box.getText().toString();
        if (TextUtils.isEmpty(toParse))
            box.setError("Required");
        else {
            int number = Integer.parseInt(toParse);
            if(number < 1 || number > 10) {
                box.setError("Wrong Weight");
            } else {
                Toast.makeText(getApplicationContext(), "Set to " + number, Toast.LENGTH_SHORT).show();
                return number;
            }
        }
        return 0;
    }

    private void getNrOfParticipants() {
        DatabaseHelper.getNrOfParticipants(new CallbackInt() {
            @Override
            public void onCallBack(Integer value) {
                rounds.setText("Number of rounds: " + (int) Math.ceil(Math.log(value)/ Math.log(2)));
            }
        });
    }

}
