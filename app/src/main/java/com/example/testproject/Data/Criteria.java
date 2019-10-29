package com.example.testproject.Data;

import java.util.ArrayList;

public class Criteria {

    private int id;
    private String name;
    private String details;
    private float weight;

    public Criteria(String name, String details, float weight, int id) {
        this.name = name;
        this.details = details;
        this.weight = weight;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public float getWeight() {
        return weight;
    }

    public static Criteria findCriteriaaById(ArrayList<Criteria> array, int id) {
        for(Criteria c : array) {
            if(c.id == id) {
                return c;
            }
        }

        return null;
    }

}
