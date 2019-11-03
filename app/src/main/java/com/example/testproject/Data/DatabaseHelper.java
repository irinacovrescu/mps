package com.example.testproject.Data;

import com.example.testproject.Participant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DatabaseHelper {

    public static void getCriteria(final CallbackCriteria myCallback){

        final DatabaseReference databaseRef =  FirebaseDatabase.getInstance().getReference("criteria");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Criteria> list = new ArrayList<>();

                Iterator<DataSnapshot> serieIterator = dataSnapshot.getChildren().iterator();
                while (serieIterator.hasNext()) {
                    //Itereaza prin dataSnapshot-uri (ce reprezinta participanti)
                    DataSnapshot d = serieIterator.next();
                    list.add(d.getValue(Criteria.class));
                }
                myCallback.onCallBack(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static void getParticipants(final ArrayList<Criteria> criteria, final CallbackParticipants myCallback){

        //Obtine referinta catre baza de date la adresa dorita
        final DatabaseReference databaseRef =  FirebaseDatabase.getInstance().getReference("participants");

        //La fiecare schimbare de date
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Creaza lista
                ArrayList<HashMap<String, Participant>> list = new ArrayList<>();

                //Itereaza prin dataSnapshot-uri (ce reprezinta serii)
                Iterator<DataSnapshot> serieIterator = dataSnapshot.getChildren().iterator();
                while (serieIterator.hasNext()) {
                    //Itereaza prin dataSnapshot-uri (ce reprezinta participanti)
                    DataSnapshot dS = serieIterator.next();
                    String key = dS.getKey();
                    Iterator<DataSnapshot> idIterator = dS.getChildren().iterator();
                    HashMap<String, Participant> hashMap = new HashMap<>();
                    while (idIterator.hasNext()) {
                        DataSnapshot d = idIterator.next();
                        hashMap.put(d.getKey(), d.getValue(Participant.class));
                    }
                    list.add(hashMap);
                }
                myCallback.onCallBack(list, criteria);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
