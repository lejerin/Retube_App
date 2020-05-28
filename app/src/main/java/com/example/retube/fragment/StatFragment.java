package com.example.retube.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.retube.Helper.MyValueFormatter;
import com.example.retube.R;
import com.example.retube.Realm.Category;
import com.example.retube.Realm.Search;
import com.example.retube.Realm.User;
import com.example.retube.Realm.ViewVideo;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

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
    PieChart pieChart;
    Realm realm;

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

        pieChart = view.findViewById(R.id.piechart);

        realm = Realm.getDefaultInstance();

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


        //선호 카테고리 그래프 그리기
        setCategoryPieChart();



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

    private void setCategoryPieChart() {

        pieChart.setRotationEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setHoleRadius(40f);

        //DB에서 데이터 갖고오기
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();


        RealmResults<Category> categories = realm.where(Category.class).sort("categoryCount",Sort.DESCENDING)
                .findAll();


        ArrayList<Integer> colors = new ArrayList<Integer>();

        if(categories.size() > 0){

            yValues.add(new PieEntry(categories.get(0).getCategoryCount(),categories.get(0).getCategoryName()));
            colors.add(Color.rgb(209, 209, 209));

            if(categories.size() > 1){
                yValues.add(new PieEntry(categories.get(1).getCategoryCount(),categories.get(1).getCategoryName()));
                colors.add(Color.rgb(143, 177, 209));

                if(categories.size() > 2){
                    yValues.add(new PieEntry(categories.get(2).getCategoryCount(),categories.get(2).getCategoryName()));
                    colors.add(Color.rgb(163, 127, 109));

                    if(categories.size() > 3){
                        yValues.add(new PieEntry(categories.get(3).getCategoryCount(),categories.get(3).getCategoryName()));
                        colors.add(Color.rgb(193, 127, 169));

                        if(categories.size() > 4) {
                            yValues.add(new PieEntry(categories.get(4).getCategoryCount(), categories.get(4).getCategoryName()));
                            colors.add(Color.rgb(63, 127, 129));

                        }
                    }
                }
            }
        }


        pieChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"category");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueTextSize(20);
        data.setValueFormatter(new MyValueFormatter("%"));

        pieChart.setData(data);
        pieChart.getLegend().setEnabled(false);


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
