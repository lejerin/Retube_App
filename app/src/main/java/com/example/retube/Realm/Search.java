package com.example.retube.Realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Search extends RealmObject {

    @PrimaryKey
    private int id; //기본키
    private String noun;
    private int count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
