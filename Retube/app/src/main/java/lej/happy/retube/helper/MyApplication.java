package lej.happy.retube.helper;

import android.app.Application;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import lej.happy.retube.data.models.realm.User;

public class MyApplication extends Application {

    public SharedPreferences prefs;


    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder().name("appdb.realm").build();
        Realm.setDefaultConfiguration(config);

        prefs = getSharedPreferences("Pref", MODE_PRIVATE);


        checkFirstRun();


    }


    public void checkFirstRun() {
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    User user = realm.createObject(User.class);
                    user.setStartDate(changeDateToStr());

                }
            });
            prefs.edit().putBoolean("isFirstRun", false).apply();
        }
    }



    private String changeDateToStr() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

        return dateFormat.format(new Date());
    }

}
