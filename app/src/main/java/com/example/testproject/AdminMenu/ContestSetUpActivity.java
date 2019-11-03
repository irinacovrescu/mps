package com.example.testproject.AdminMenu;

import android.content.Intent;
import android.os.Bundle;

import com.example.testproject.Data.Criteria;
import com.example.testproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

public class ContestSetUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private TextView rounds;
    private TextView series;

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
    private int originalityId = 1;
    private String originalityDetail = "Consider: Exhibits creativity";
    private int craftmanship = 0;
    private int craftmanshipId = 2;
    private String craftmanshipDetail = "Consider: Artist’s skills in the use of material";
    private int composition = 0;
    private int compositionId = 3;
    private String compositionDetail = "Consider: Effective use of forms or abstract techniques";
    private int unity = 0;
    private int unityId = 4;
    private String unityDetail = "Consider: Balance of elements, repetition, visual rhythm";
    private int space = 0;
    private int spaceId = 5;
    private String spaceDetail = "Consider: Perspective and mass";
    private int interpretation = 0;
    private int interpretationId = 6;
    private String interpretationDetail = "Consider: Clarity of the theme to the viewer";
    private ArrayList<Criteria> criterias = new ArrayList<Criteria>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_set_up);


        InitUIElem();

        criteriaSet();

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

    public void addCriterias(ArrayList<Criteria> list){

        final DatabaseReference databaseRef =  FirebaseDatabase.getInstance().getReference("criteria");
        databaseRef.setValue(list);

    }

    private void criteriaSet(){
        originalitySetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                originality = verifyPoints(originalityNumberBox);
                addCriteria("originality", originality, criterias, originalityId, originalityDetail);

            }
        });

        craftmanshipSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                craftmanship = verifyPoints(craftmanshipNumberBox);
                addCriteria("craftmanship", craftmanship, criterias, craftmanshipId, craftmanshipDetail);
            }
        });

        compositionSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                composition = verifyPoints(compositionNumberBox);
                addCriteria("composition", composition, criterias, compositionId, compositionDetail);
            }
        });

        unitySetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unity = verifyPoints(unityNumberBox);
                addCriteria("unity", unity, criterias, unityId, unityDetail);
            }
        });

        spaceSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                space = verifyPoints(spaceNumberBox);
                addCriteria("space", space, criterias, spaceId, spaceDetail);
            }
        });

        interpretationSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                interpretation = verifyPoints(interpretationNumberBox);
                addCriteria("interpretation", interpretation, criterias, interpretationId, interpretationDetail);
            }
        });
    }


    private void reset(){
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                originality = 0;
                craftmanship = 0;
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
                if(true) {
                    Toast.makeText(getApplicationContext(), "Set up contest first!", Toast.LENGTH_SHORT).show();
                } else {
                    addCriterias(criterias);
                    Intent myIntent = new Intent(ContestSetUpActivity.this, AdminMenuActivity.class);
                    ContestSetUpActivity.this.startActivity(myIntent);
                }
            }
        });
    }

    private void addCriteria(String type, int number, ArrayList<Criteria> list, int id, String details){

        int index = 0;
        for (Criteria c : list) {
            String name = c.getName();
            if (name.equals(type)){
                break;
            }
            index++;
        }
        if (index != 0) {
            list.remove(index);
        } else {
            Criteria c = new Criteria(type, details, number,id);
        }
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
                Toast.makeText(getApplicationContext(), "Set to " + Integer.toString(number), Toast.LENGTH_SHORT).show();
                return number;
            }
        }
        return 0;
    }

    private int getContestantsNumberFromDB() {
        // get no of contestants from DB
        return 24;
    }

}
