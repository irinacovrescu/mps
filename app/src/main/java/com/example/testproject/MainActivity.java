package com.example.testproject;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Button authenticateBtn, leaderboardsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        authenticateBtn = findViewById(R.id.authenticateBtn);
        leaderboardsBtn = findViewById(R.id.leaderboardsBtn);

        authenticateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,   AuthActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        leaderboardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,   LeaderBoardActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
