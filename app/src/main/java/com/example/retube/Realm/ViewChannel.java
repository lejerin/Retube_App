package com.example.retube.Realm;

import io.realm.RealmObject;

public class ViewChannel extends RealmObject {

    private String channelId;
    private int channelCount = 0;



    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getChannelCount() {
        return channelCount;
    }

    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }
}
