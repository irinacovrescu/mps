package com.example.testproject.Data;

public class Criteria {
    String nume;
    Integer pondere;

    public Criteria() {

    }

    public Criteria(String nume, Integer pondere) {

            this.nume = nume;
            this.pondere = pondere;
    }
    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Integer getPondere() {
        return pondere;
    }

    public void setPondere(Integer pondere) {
        this.pondere = pondere;
    }
}
