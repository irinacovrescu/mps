package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class JuryActivity extends AppCompatActivity {

    private EditText juryNumberBox;
    private Button jurySetButton;
    private TextView textView;
    int juryNumber = 12528632;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jury);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        juryNumberBox = findViewById(R.id.jurynumber);
        jurySetButton = findViewById(R.id.setjury);

        textView = (TextView) findViewById(R.id.lastnumberofjuryset);


        jurySetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toParse = juryNumberBox.getText().toString();
                if (TextUtils.isEmpty(toParse)) {
                    juryNumberBox.setError("Required.");
                } else {
                    juryNumber = setJury(toParse);
                    textView.setText("Last number set: "+ Integer.toString(juryNumber));
                }
            }
        });


    }

    private int setJury(String text) {
        int number = Integer.parseInt(text);
        Toast.makeText(getApplicationContext(), "Numbers of judges set to " + Integer.toString(number), Toast.LENGTH_SHORT).show();
        //Intent myIntent = new Intent(JuryActivity.this,   AdminMenuActivity.class);
        //JuryActivity.this.startActivity(myIntent);
        return number;

        
    }



}
