package com.example.testproject.Data;

import android.util.Pair;

import com.example.testproject.Participant;

import java.util.ArrayList;
import java.util.HashMap;

public interface CallbackParticipants {

    void onCallBack(HashMap<String, HashMap<String, Participant>> value, ArrayList<Criteria> criteria);

    void onCallBack(ArrayList<HashMap<Pair<String, String>, Participant>> value);
}
