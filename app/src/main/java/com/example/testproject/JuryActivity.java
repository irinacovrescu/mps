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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class JuryActivity extends AppCompatActivity {

    private EditText juryNumberBox;
    private Button jurySetButton;
    int juryNumber = 0;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jury);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InitUIElem();
        setJury();


    }

    private void InitUIElem() {
        juryNumberBox = findViewById(R.id.jurynumber);
        jurySetButton = findViewById(R.id.setjury);
    }

    private void setJury() {
        jurySetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toParse = juryNumberBox.getText().toString();
                if (TextUtils.isEmpty(toParse)) {
                    juryNumberBox.setError("Required");
                } else {
                    juryNumber = Integer.parseInt(toParse);
                    Toast.makeText(getApplicationContext(), "Numbers of judges set to " + Integer.toString(juryNumber), Toast.LENGTH_SHORT).show();
                    addJuryNumberToDB(juryNumber);
                    Intent myIntent = new Intent(JuryActivity.this, AdminMenuActivity.class);
                    JuryActivity.this.startActivity(myIntent);
                }

            }
        });
    }

    private void addJuryNumberToDB(int number){

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //do your stuff
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
