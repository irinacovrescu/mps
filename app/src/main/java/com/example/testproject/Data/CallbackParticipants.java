package com.example.testproject.Data;

import com.example.testproject.Participant;

import java.util.ArrayList;
import java.util.HashMap;

public interface CallbackParticipants {

    void onCallBack(HashMap<String, HashMap<String, Participant>> value, ArrayList<Criteria> criteria);
}
