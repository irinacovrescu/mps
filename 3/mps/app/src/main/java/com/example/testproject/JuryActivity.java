package com.example.testproject;

import android.os.Bundle;

import com.example.testproject.Data.Judge;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JuryActivity extends AppCompatActivity {

    private static final String TAG = "Jury";
    private EditText juryNumberBox;
    private Button jurySetButton;

    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jury);

        InitUIElem();
        setJury();

        db = FirebaseDatabase.getInstance();
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
                } else if (Integer.parseInt(toParse) <= 7) {
                    clearOldJudgeData();
                    setJury(toParse);
                }
                else
                    juryNumberBox.setError("Should be less than 7");
            }
        });
    }

    private void clearOldJudgeData() {
        FirebaseDatabase.getInstance().getReference("JUDGE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot judgeSnapshot : dataSnapshot.getChildren()) {
                    if (!judgeSnapshot.getKey().equals("nrOfJudges"))
                        judgeSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    /**
     * Updates nrOfJudges field in judges document
     *
     * @param nr number of judges as String to be set
     */
    private void setJury(final String nr) {
        final int nrOfJudges = Integer.parseInt(nr);
        final DatabaseReference judgesRef = db.getReference("JUDGE").child("nrOfJudges");

        judgesRef.setValue(nrOfJudges).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "NrOfJudges successfully updated!");
                createJuryObjects(Integer.parseInt(nr));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
                Toast.makeText(getApplicationContext(), "Failed to set" + nrOfJudges, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createJuryObjects(Integer nrOfJudges) {
        for (int i = 1; i <= nrOfJudges; i++) {
            FirebaseDatabase.getInstance().getReference("JUDGE").child(Integer.toString(i)).setValue(new Judge(false, false));
        }
        Toast.makeText(getApplicationContext(), "Numbers of judges set to " + nrOfJudges, Toast.LENGTH_SHORT).show();
        finish();
    }
}
