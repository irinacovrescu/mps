package com.example.testproject.AdminMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.testproject.Participant;
import com.example.testproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ParticipantsActivity extends AppCompatActivity {
    LinearLayout layout;

    private EditText contestantNameBox;
    private Button contestantSetButton;
    TableLayout tl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);

        InitUIElem();
        getParticipants();
    }

    private void InitUIElem(){
        contestantNameBox = findViewById(R.id.disqualifiedname);
        contestantSetButton = findViewById(R.id.disqualifybutton);
        layout = findViewById(R.id.constestants_layout_admin);
    }

    private void getParticipants(){

        //Obtine referinta catre baza de date la adresa dorita
        final DatabaseReference databaseRef =  FirebaseDatabase.getInstance().getReference("participants");

        //La fiecare schimbare de date
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Creaza lista
                ArrayList<HashMap<Pair<String, String>, Participant>> list = new ArrayList<>();

                //Itereaza prin dataSnapshot-uri (ce reprezinta serii)
                Iterator<DataSnapshot> serieIterator = dataSnapshot.getChildren().iterator();
                while (serieIterator.hasNext()) {
                    //Itereaza prin dataSnapshot-uri (ce reprezinta participanti)
                    DataSnapshot dS = serieIterator.next();
                    String key = dS.getKey();
                    Iterator<DataSnapshot> idIterator = dS.getChildren().iterator();
                    HashMap<Pair<String, String>, Participant> hashMap = new HashMap<>();
                    while (idIterator.hasNext()) {
                        DataSnapshot d = idIterator.next();
                        hashMap.put(new Pair<String, String>(key, d.getKey()), d.getValue(Participant.class));
                    }
                    list.add(hashMap);


                }

                displayCandidates(list);
                disqualify(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayCandidates (ArrayList<HashMap<Pair<String, String>, Participant>> list){

       // tl = new TableLayout(this);
        tl = (TableLayout) getLayoutInflater().inflate(R.layout.contestants_table_layout, null);

        for (HashMap<Pair<String, String>, Participant> s : list) {
            for (HashMap.Entry<Pair<String, String>, Participant> entry : s.entrySet()) {

                Pair<String, String> aux = entry.getKey();
                String series = aux.first;
                String id = aux.second;
                Participant p = entry.getValue();

                TableRow row = (TableRow)getLayoutInflater().inflate(R.layout.contestants_table_row_template, null);

                TextView cellN = (TextView)row.getChildAt(0);
                cellN.setText(p.getName());
                TextView cellS = (TextView)row.getChildAt(1);
                cellS.setText(series);
                TextView cellD = (TextView)row.getChildAt(2);
                cellD.setText(Boolean.toString(p.getDisqualified()));

                tl.addView(row);
            }
        }
        layout.addView(tl);
    }

    private void disqualify(final ArrayList<HashMap<Pair<String, String>, Participant>> list){
        contestantSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toParse = contestantNameBox.getText().toString();
                if (TextUtils.isEmpty(toParse)) {
                    contestantNameBox.setError("Required");
                } else {
                    for (HashMap<Pair<String, String>, Participant> s : list) {
                        for (HashMap.Entry<Pair<String, String>, Participant> entry : s.entrySet()) {

                            Pair<String, String> aux = entry.getKey();
                            String series = aux.first;
                            String id = aux.second;
                            Participant p = entry.getValue();
                            if (p.getName().equals(toParse)) {
                                final DatabaseReference databaseRef =  FirebaseDatabase.getInstance().getReference("participants/"+series);

                                databaseRef.child(id).child("disqualified").setValue(true);
                                layout.removeAllViews();
                            }
                        }
                    }
                }
            }
        });
    }
}
