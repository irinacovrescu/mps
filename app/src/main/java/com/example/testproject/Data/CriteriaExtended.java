package com.example.testproject.Data;

import java.util.ArrayList;

public class CriteriaExtended extends Criteria {

    private int id;
    private String details;
    private String qname;

    public CriteriaExtended(String nume, Integer pondere, int id) {
        super(nume, pondere);
        String[] s = nume.split("=");
        qname = s[0];
        if(s.length > 1) {
            details = s[1];
        }
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return qname;
    }

    public String getDetails() {
        return details;
    }

    public float getWeight() {
        return pondere;
    }

    public static CriteriaExtended findCriteriaById(ArrayList<CriteriaExtended> array, int id) {
        for(CriteriaExtended c : array) {
            if(c.id == id) {
                return c;
            }
        }

        return null;
    }

}
