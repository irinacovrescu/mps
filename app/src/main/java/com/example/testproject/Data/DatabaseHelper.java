package com.example.testproject.Data;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

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

        final DatabaseReference databaseRef =  FirebaseDatabase.getInstance()
                .getReference("criteria");
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
    public static void getParticipants(final ArrayList<Criteria> criteria,
                                       final CallbackParticipants myCallback){

        //Obtine referinta catre baza de date la adresa dorita
        final DatabaseReference databaseRef =  FirebaseDatabase.getInstance()
                .getReference("participants");

        //La fiecare schimbare de date
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Creaza lista
                HashMap<String, HashMap<String, Participant>> list = new HashMap<>();

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
                    list.put(key, hashMap);
                }
                myCallback.onCallBack(list, criteria);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void evaluateParticipants(String serie, final String id, final Integer nrRound,
                                            final ArrayList<Integer> results,
                                            final ArrayList<CriteriaExtended> criteria,
                                            final Integer lastParticipantId,
                                            final CallbackSubmit myCallback) {

        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("participants").child(serie).child(id);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                Participant p = dataSnapshot.getValue(Participant.class);

                if(p == null)
                    return;

                if(!p.getDisqualified() && !p.getOutOfCompetition()) {
                    if(nrRound != p.getThisRound_number()){
                        p.setThisRound_number(nrRound);

                        //This round points set to 0
                        p.setThisRounds_points(0);
                        p.setThisRound_juriesThatVoted(0);

                        //This results set to 0
                        int size = criteria.size();
                        p.resetThisRound_results(size);
                    }

                    p.addToThisRound_results(results,criteria);
                    p.addThisRound_juriesThatVoted();

                    //overwrite
                    databaseRef.setValue(p);

                    if(id.equals(lastParticipantId.toString())) {
                        myCallback.onCallBack();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getNrOfParticipants(final CallbackInt myCallback) {
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("nrOfParticipants");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer nrOfParticipants = dataSnapshot.getValue(Integer.class);

                //Here you have a number of participants to do whatever you want with it 🙂
                myCallback.onCallBack(nrOfParticipants);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getJury(final Integer lastRound, final CallbackJury myCallback) {
        final ArrayList<Judge> list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("JUDGE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot judgeSnapshot : dataSnapshot.getChildren()) {
                    list.add(judgeSnapshot.getValue(Judge.class));
                }

                //Here you have a list of Judges to do whatever you want
                myCallback.onCallBack(list, lastRound);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Admin", "onCancelled", databaseError.toException());
            }
        });
    }

    public static void getNrOfCriterias(final CallbackInt myCallback) {
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("criterias");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer nrOfParticipants = dataSnapshot.getValue(Integer.class);

                //Here you have a number of participants to do whatever you want with it 🙂
                myCallback.onCallBack(nrOfParticipants);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getCurrentRound(final CallbackInt myCallback) {
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("currentRound");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer currentRound = dataSnapshot.getValue(Integer.class);
                myCallback.onCallBack(currentRound);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                
            }
        });
    }

    public static void getParticipants(final CallbackParticipants myCallback){

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

                myCallback.onCallBack(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getRoundStarted(final CallbackBool myCallback) {
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("roundStarted");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean roundStarted = dataSnapshot.getValue(Boolean.class);

                //Here you have a number of participants to do whatever you want with it 🙂
                myCallback.onCallBack(roundStarted);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getNrRounds(final CallbackInt myCallback) {
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("nrOfRounds");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer nrRounds = dataSnapshot.getValue(Integer.class);
                myCallback.onCallBack(nrRounds);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
