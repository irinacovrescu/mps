package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JuryActivity extends AppCompatActivity {

    private EditText juryNumberBox;
    private Button jurySetButton;
    int juryNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jury);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        juryNumberBox = findViewById(R.id.jurynumber);
        jurySetButton = findViewById(R.id.setjury);

        jurySetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                juryNumber = setJury(juryNumberBox);
            }
        });


    }

    private int setJury(EditText text) {
        int number = Integer.parseInt(text.getText().toString());
        Toast.makeText(getApplicationContext(), "Numbers of judges set", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(JuryActivity.this,   AdminMenuActivity.class);
        JuryActivity.this.startActivity(myIntent);
        return number;
    }

}
