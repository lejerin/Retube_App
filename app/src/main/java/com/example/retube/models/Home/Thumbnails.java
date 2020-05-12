package com.example.retube.models.Home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnails {


@SerializedName("medium")
@Expose
private Medium medium;
    @SerializedName("high")
    @Expose
    private High high;

public Medium getMedium() {
return medium;
}

public void setMedium(Medium medium) {
this.medium = medium;
}

    public High getHigh() {
        return high;
    }

    public void setHigh(High high) {
        this.high = high;
    }

}