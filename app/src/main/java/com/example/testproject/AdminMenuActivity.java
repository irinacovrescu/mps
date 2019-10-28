package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class AdminMenuActivity extends AppCompatActivity {
    private Button juryButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        juryButton = (Button)findViewById(R.id.jurybutton);
        juryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AdminMenuActivity.this,   JuryActivity.class);
                AdminMenuActivity.this.startActivity(myIntent);
            }
        });

        // this doesn't jump back to AuthActivity because it remembers the user!!!
        logoutButton = (Button)findViewById(R.id.logoutbutton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AdminMenuActivity.this,   AuthActivity.class);
                AdminMenuActivity.this.startActivity(myIntent);
            }
        });

        /*juryButton = (Button)findViewById(R.id.jurybutton);
        juryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AdminMenuActivity.this,   JuryActivity.class);
                AdminMenuActivity.this.startActivity(myIntent);
            }
        });

        juryButton = (Button)findViewById(R.id.jurybutton);
        juryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AdminMenuActivity.this,   JuryActivity.class);
                AdminMenuActivity.this.startActivity(myIntent);
            }
        });*/

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //do your stuff
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
