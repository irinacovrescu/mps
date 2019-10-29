package com.example.testproject;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContestantsActivity extends AppCompatActivity {

    /* list of contestants extracted from DB
        so they can be disqualified as needed
     */

    private EditText contestantNameBox;
    private Button contestantSetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contestants);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contestantNameBox = findViewById(R.id.disqualifiedname);
        contestantSetButton = findViewById(R.id.disqualifybutton);

        contestantSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toParse = contestantNameBox.getText().toString();
                // check for contestant in DB and set to disqualified
            }
        });
    }

}
