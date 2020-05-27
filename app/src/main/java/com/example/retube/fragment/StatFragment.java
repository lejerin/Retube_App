package com.example.retube.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.retube.R;
import com.example.retube.Realm.Search;
import com.example.retube.Realm.User;
import com.example.retube.Realm.ViewVideo;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatFragment extends Fragment {

    ImageView timeImg, weekImg;
    TextView timePercent, timeStatusText, weekPercent, weekStatusText;


    public StatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stat, container, false);

        List<TextView> searchTexts = new ArrayList<>();
        List<TextView> viewTexts = new ArrayList<>();

        searchTexts.add(view.findViewById(R.id.search_1));
        searchTexts.add(view.findViewById(R.id.search_2));
        searchTexts.add(view.findViewById(R.id.search_3));
        searchTexts.add(view.findViewById(R.id.search_4));
        searchTexts.add(view.findViewById(R.id.search_5));
        searchTexts.add(view.findViewById(R.id.search_6));
        searchTexts.add(view.findViewById(R.id.search_7));
        searchTexts.add(view.findViewById(R.id.search_8));
        searchTexts.add(view.findViewById(R.id.search_9));

        viewTexts.add(view.findViewById(R.id.view_1));
        viewTexts.add(view.findViewById(R.id.view_2));
        viewTexts.add(view.findViewById(R.id.view_3));
        viewTexts.add(view.findViewById(R.id.view_4));
        viewTexts.add(view.findViewById(R.id.view_5));
        viewTexts.add(view.findViewById(R.id.view_6));
        viewTexts.add(view.findViewById(R.id.view_7));
        viewTexts.add(view.findViewById(R.id.view_8));
        viewTexts.add(view.findViewById(R.id.view_9));

        TextView viewCountText = view.findViewById(R.id.viewCountText);
        TextView text_startDate = view.findViewById(R.id.text_startDate);
        timeImg = view.findViewById(R.id.timeImg);
        timePercent = view.findViewById(R.id.timePercent);
        timeStatusText = view.findViewById(R.id.timeStatusText);

        weekImg = view.findViewById(R.id.weekImg);
        weekPercent = view.findViewById(R.id.weekPercent);
        weekStatusText = view.findViewById(R.id.weekStatusText);


        Realm realm = Realm.getDefaultInstance();

        //전체 영상 감상수
        User user = realm.where(User.class).findFirst();
        text_startDate.setText(user.getStartDate() + " 부터");
        viewCountText.setText(user.getViewCount()+"");

        int dawn = user.getDown();
        int am = user.getAm();
        int pm = user.getPm();
        int night = user.getNight();

        setTimeImgText(dawn,am,pm,night);

        int week = user.getWeek();
        int holy = user.getHoly();

        setWeekImgText(week,holy);





        //키워드
        RealmResults<Search> searches = realm.where(Search.class).sort("count", Sort.DESCENDING).findAll();
        RealmResults<ViewVideo> viewVideos = realm.where(ViewVideo.class).sort("count", Sort.DESCENDING).findAll();

        for(int i=1;i<10;i++){
            if(i <= searches.size()){
                String text = searches.get(i-1).getNoun();
                if (text.length() > 5) {
                    text = text.substring(0, 5) + "..";
                }
                searchTexts.get(i-1).setText(text);
            }else{
                searchTexts.get(i-1).setVisibility(View.INVISIBLE);
            }
        }

        for(int i=1;i<10;i++){
            if(i <= viewVideos.size()){
                String text = viewVideos.get(i-1).getNoun();
                if (text.length() > 5) {
                    text = text.substring(0, 5) + "..";
                }
                viewTexts.get(i-1).setText(text);
            }else{
                viewTexts.get(i-1).setVisibility(View.INVISIBLE);
            }
        }







        return view;
    }

    private void setWeekImgText(int week, int holy) {
        String maxString = "주중";
        int maxInt = week;

        if(holy > maxInt){
            maxString = "주말";
            maxInt = holy;
        }
        int all = week + holy;
        int percent =  (int)( (double)maxInt/ (double)all * 100.0 );
        weekPercent.setText(percent + "%");
        weekStatusText.setText(maxString);

        if(maxString.equals("주말")){
            weekImg.setImageResource(R.drawable.holy);
        }

    }

    private void setTimeImgText(int dawn, int am, int pm, int night) {

        String maxString = "새벽";
        int maxInt = dawn;

        if(maxInt < am){
            maxString = "오전";
            maxInt = am;
        }
        if(maxInt < pm){
            maxString = "오후";
            maxInt = pm;
        }
        if(maxInt < night){
            maxString = "밤";
            maxInt = night;
        }

        int all = dawn + am + pm + night;
        int percent =  (int)( (double)maxInt/ (double)all * 100.0 );
        timePercent.setText(percent + "%");
        timeStatusText.setText(maxString);

        switch (maxString){
            case "새벽":
                timeImg.setImageResource(R.drawable.dawn);
                break;
            case "오전":
                timeImg.setImageResource(R.drawable.am);
                break;
            case "오후":
                timeImg.setImageResource(R.drawable.pm);
                break;
            case "밤":
                timeImg.setImageResource(R.drawable.night);
                break;
            default:
                break;
        }


    }

}
