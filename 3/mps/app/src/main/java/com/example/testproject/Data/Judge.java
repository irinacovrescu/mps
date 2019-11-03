package com.example.testproject.Data;

public class Judge {
    private Boolean loggedIn;
    private Boolean voted;

    public Boolean getVoted() {
        return voted;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Judge() {
        this.loggedIn = false;
        this.voted = false;
    }

    public Judge(Boolean loggedIn, Boolean voted) {
        this.loggedIn = loggedIn;
        this.voted = voted;
    }
}
