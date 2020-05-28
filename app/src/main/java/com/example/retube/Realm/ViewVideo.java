package com.example.retube.Realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ViewVideo extends RealmObject {


    private String noun;
    private int count;


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
