package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testproject.AdminMenu.AdminMenuActivity;
import com.example.testproject.Data.Judge;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "Auth";

    Button loginButton, cancelButton;
    EditText usernameBox, passwordBox;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.loginButton);
        usernameBox = findViewById(R.id.usernameBox);
        passwordBox = findViewById(R.id.passwordBox);
        cancelButton = findViewById(R.id.cancelButton);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartLogIn(usernameBox.getText().toString(), passwordBox.getText().toString());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent myIntent = new Intent(AuthActivity.this,   MainActivity.class);
                AuthActivity.this.startActivity(myIntent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        //in case of user already logged in switchActivity accordingly
        signOut();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        switchActivity(currentUser);
    }

    private void StartLogIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        // hardcode email address because reasons :)
        email += "@mps.com";

        if (email.equals("admin@mps.com")) {
            final DatabaseReference adminRef =  FirebaseDatabase.getInstance().getReference("ADMIN").child("loggedIn");
            getAdminAuthAndSignIn(adminRef, email, password);
        } else if (email.contains("jury")) {
            String juryUID = email.substring(4,5);
            final DatabaseReference juryRef = FirebaseDatabase.getInstance().getReference("JUDGE").child(juryUID).child("loggedIn");

            getJuryAuthAndSignIn(juryRef, juryUID, email, password);
        }
    }

    private void getJuryAuthAndSignIn(final DatabaseReference juryRef, final String juryUID, final String email, final String password) {
        FirebaseDatabase.getInstance().getReference("JUDGE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Judge judge = dataSnapshot.child(juryUID).getValue(Judge.class);
                if (!judge.getLoggedIn()) {
                    signIn(email, password, juryRef);
                } else {
                    Toast.makeText(getApplicationContext(), "Already LOGGED IN", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAdminAuthAndSignIn(final DatabaseReference docRef, final String email, final String password) {
        docRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean adminAuth = (Boolean) dataSnapshot.getValue();
                if (!adminAuth)
                    signIn(email, password, docRef);
                else {
                    Toast.makeText(getApplicationContext(), "Already LOGGED IN", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void signIn(final String email, final String password, final DatabaseReference documentReference) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signedInSuccesfully");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    //setLoggedStatus(documentReference, email.substring(0, email.indexOf('@')), true);
                    setLoggedStatus(documentReference, true);
                    switchActivity(user);
                } else {
                    Log.d(TAG, "signInFailed!");
                    Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signOut() {
        //TODO: SET TO FALSE
        firebaseAuth.signOut();
    }

    public static void setLoggedStatus(DatabaseReference documentReference, final Boolean loggedIn) {
        documentReference.setValue(loggedIn);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = usernameBox.getText().toString();
        if (TextUtils.isEmpty(email)) {
            usernameBox.setError("Required.");
            valid = false;
        } else {
            usernameBox.setError(null);
        }

        String password = passwordBox.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordBox.setError("Required.");
            valid = false;
        } else {
            passwordBox.setError(null);
        }

        return valid;
    }

    private void switchActivity(FirebaseUser user) {
        if (user != null) {
            if (user.getEmail().equals("admin@mps.com")) {
                //switch activity to admin
                Toast.makeText(getApplicationContext(), "Welcome Admin!", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(AuthActivity.this, AdminMenuActivity.class);
                AuthActivity.this.startActivity(myIntent);
            } else if (user.getEmail().contains("jury")) {
                //switch activity to jury
                Toast.makeText(getApplicationContext(), "Welcome Jury!", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(AuthActivity.this, GradingActivity.class);
                AuthActivity.this.startActivity(myIntent);
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
