package lej.happy.retube.data.models.Realm;

import io.realm.RealmObject;

public class User extends RealmObject {

    private String startDate;
    private int viewCount=0, down=0, am=0, pm=0, night=0 , week=0, holy=0;

    /*
    down: 새벽 : am 12 - 5:59:59
    am : 오전 : am 6 - 11:59:59
    pm : 오후 : pm 12 - 5:69:59
    night : 저녁 : pm 6 - 11:59:59
     */

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getNight() {
        return night;
    }

    public void setNight(int night) {
        this.night = night;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getAm() {
        return am;
    }

    public void setAm(int am) {
        this.am = am;
    }

    public int getPm() {
        return pm;
    }

    public void setPm(int pm) {
        this.pm = pm;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getHoly() {
        return holy;
    }

    public void setHoly(int holy) {
        this.holy = holy;
    }
}
