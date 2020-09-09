package com.example.retube.data.Realm;

import java.util.Date;

import io.realm.RealmObject;

public class RealmSearch extends RealmObject {


    private String noun;
    private int count;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNoun() {
        return noun;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
