package com.example.retube.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.retube.Helper.CircleIndicator;
import com.example.retube.Helper.MyValueFormatter;
import com.example.retube.R;
import com.example.retube.Realm.Category;
import com.example.retube.Realm.Search;
import com.example.retube.Realm.User;
import com.example.retube.Realm.ViewChannel;
import com.example.retube.Realm.ViewVideo;
import com.example.retube.data.GetDataService;
import com.example.retube.data.network.RetrofitInstance;
import com.example.retube.activity.RecommendActivity;
import com.example.retube.adapter.StatChannelVPAdapter;
import com.example.retube.models.Channel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatFragment extends Fragment {

    ImageView timeImg, weekImg;
    TextView timePercent, timeStatusText, weekPercent, weekStatusText;
    PieChart pieChart;
    Realm realm;

    ViewPager viewPager;
    StatChannelVPAdapter statChannelVPAdapter;
    private HashMap<Integer, Channel.Item> channelList = new HashMap<Integer, Channel.Item>();
    int count1 = 0, count2 = 0, count3 = 0;

    boolean isPieChartShow = false;
    ScrollView scrollView;

    RealmResults<Search> searches = null;
    RealmResults<ViewVideo> viewVideos = null;
    Date today = new Date();


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

        scrollView = view.findViewById(R.id.scrollView2);
        pieChart = view.findViewById(R.id.piechart);

        viewPager = view.findViewById(R.id.channelViewPager);
        CircleIndicator circleIndicator = view.findViewById(R.id.indicator);


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


        //선호 채널 뷰페이저
        setChannelViewPager();
        viewPager.setClipToPadding(false);
        viewPager.setPadding(40, 0, 40, 0);
        viewPager.setPageMargin(getResources().getDisplayMetrics().widthPixels / -9);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                circleIndicator.selectDot(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        circleIndicator.createDotPanel(3, R.drawable.indicator_dot_off, R.drawable.indicator_dot_on, 0);



        //키워드

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                switch (checkedId)
                {
                    case R.id.radioDay:
                        searches  = realm.where(Search.class)
                                .greaterThanOrEqualTo("date",stringToDate("00:00:00", today))
                                .lessThanOrEqualTo("date",stringToDate("23:59:59", today))
                                .sort("count", Sort.DESCENDING).findAll();
                        break;
                    case R.id.radioWeek:
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        cal.add(Calendar.DATE, -7);

                        System.out.println("1주일전" + cal.getTime());

                        searches  = realm.where(Search.class)
                                .greaterThanOrEqualTo("date",stringToDate("00:00:00", cal.getTime()))
                                .lessThanOrEqualTo("date",stringToDate("23:59:59",today))
                                .sort("count", Sort.DESCENDING).findAll();
                        break;
                    case R.id.radioMonth:
                        Calendar calMonth = Calendar.getInstance();
                        calMonth.setTime(new Date());
                        calMonth.add(Calendar.MONTH, -1);

                        searches  = realm.where(Search.class)
                                .greaterThanOrEqualTo("date",stringToDate("00:00:00",calMonth.getTime()))
                                .lessThanOrEqualTo("date",stringToDate("23:59:59",today))
                                .sort("count", Sort.DESCENDING).findAll();
                        break;
                    default:
                        break;
                }

                for(int i=1;i<10;i++){
                            if(i <= searches.size()){
                                String text = searches.get(i-1).getNoun();
                                if (text.length() > 5) {
                                    text = text.substring(0, 5) + "..";
                                }
                                searchTexts.get(i-1).setVisibility(View.VISIBLE);
                                searchTexts.get(i-1).setText(text);

                            }else{
                                searchTexts.get(i-1).setVisibility(View.INVISIBLE);
                                searchTexts.get(i-1).setText("");
                    }
                }
            }
        });

        RadioGroup radioGroup2 = view.findViewById(R.id.radioGroup2);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                switch (checkedId)
                {
                    case R.id.radioDay2:
                        viewVideos  = realm.where(ViewVideo.class)
                                .greaterThanOrEqualTo("date",stringToDate("00:00:00", today))
                                .lessThanOrEqualTo("date",stringToDate("23:59:59", today))
                                .sort("count", Sort.DESCENDING).findAll();
                        break;
                    case R.id.radioWeek2:
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        cal.add(Calendar.DATE, -7);

                        viewVideos  = realm.where(ViewVideo.class)
                                .greaterThanOrEqualTo("date",stringToDate("00:00:00", cal.getTime()))
                                .lessThanOrEqualTo("date",stringToDate("23:59:59",today))
                                .sort("count", Sort.DESCENDING).findAll();
                        break;
                    case R.id.radioMonth2:
                        Calendar calMonth = Calendar.getInstance();
                        calMonth.setTime(new Date());
                        calMonth.add(Calendar.MONTH, -1);

                        viewVideos  = realm.where(ViewVideo.class)
                                .greaterThanOrEqualTo("date",stringToDate("00:00:00",calMonth.getTime()))
                                .lessThanOrEqualTo("date",stringToDate("23:59:59",today))
                                .sort("count", Sort.DESCENDING).findAll();
                        break;
                    default:
                        break;
                }

                for(int i=1;i<10;i++){
                    if(i <= viewVideos.size()){
                        String text = viewVideos.get(i-1).getNoun();
                        if (text.length() > 5) {
                            text = text.substring(0, 5) + "..";
                        }
                        viewTexts.get(i-1).setVisibility(View.VISIBLE);
                        viewTexts.get(i-1).setText(text);
                    }else{
                        viewTexts.get(i-1).setVisibility(View.INVISIBLE);
                        viewTexts.get(i-1).setText("");
                    }
                }
            }
        });

        //오늘 날짜 검색만
        searches  = realm.where(Search.class)
                .greaterThanOrEqualTo("date",stringToDate("00:00:00", today))
                .lessThanOrEqualTo("date",stringToDate("23:59:59", today))
                .sort("count", Sort.DESCENDING).findAll();
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

        //오늘 날짜 검색만
        viewVideos  = realm.where(ViewVideo.class)
                .greaterThanOrEqualTo("date",stringToDate("00:00:00", today))
                .lessThanOrEqualTo("date",stringToDate("23:59:59", today))
                .sort("count", Sort.DESCENDING).findAll();
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



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if(checkPieChartShow()){
                        if(!isPieChartShow){
                            pieChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션
                        }
                        isPieChartShow = true;
                    }else{
                        isPieChartShow = false;
                    }

                }
            });
        }


        ImageButton shareSearchBtn = view.findViewById(R.id.shareSearch);
        shareSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecommendActivity.class);
                intent.putExtra("keyword1", searchTexts.get(0).getText().toString());
                intent.putExtra("keyword2", searchTexts.get(1).getText().toString());
                intent.putExtra("keyword3", searchTexts.get(2).getText().toString());
                intent.putExtra("keyword4", searchTexts.get(3).getText().toString());
                intent.putExtra("keyword5", searchTexts.get(4).getText().toString());
                intent.putExtra("keyword6", searchTexts.get(5).getText().toString());
                intent.putExtra("keyword7", searchTexts.get(6).getText().toString());
                intent.putExtra("keyword8", searchTexts.get(7).getText().toString());
                intent.putExtra("keyword9", searchTexts.get(8).getText().toString());
                startActivity(intent);
            }
        });

        ImageButton shareTagBtn = view.findViewById(R.id.shareTagBtn);
        shareTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecommendActivity.class);
                intent.putExtra("keyword1", viewTexts.get(0).getText().toString());
                intent.putExtra("keyword2", viewTexts.get(1).getText().toString());
                intent.putExtra("keyword3", viewTexts.get(2).getText().toString());
                intent.putExtra("keyword4", viewTexts.get(3).getText().toString());
                intent.putExtra("keyword5", viewTexts.get(4).getText().toString());
                intent.putExtra("keyword6", viewTexts.get(5).getText().toString());
                intent.putExtra("keyword7", viewTexts.get(6).getText().toString());
                intent.putExtra("keyword8", viewTexts.get(7).getText().toString());
                intent.putExtra("keyword9", viewTexts.get(8).getText().toString());
                startActivity(intent);
            }
        });


        return view;
    }



    private boolean checkPieChartShow(){
        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);
        if (pieChart.getLocalVisibleRect(scrollBounds)) {
            // Any portion of the imageView, even a single pixel, is within the visible window
            System.out.println("보임");
            return true;
        } else {
            // NONE of the imageView is within the visible window
            System.out.println("안보임");
            return false;
        }
    }

    private void setChannelViewPager() {

        RealmResults<ViewChannel> viewChannels = realm.where(ViewChannel.class)
                .sort("channelCount",Sort.DESCENDING)
                .findAll();

        List<Channel> channels = new ArrayList<>();

        int num = viewChannels.size();

        if(num > 0){
            System.out.println("상위 채널 이미지 요청1");
            count1 = viewChannels.get(0).getChannelCount();
            getChannelThumb(viewChannels.get(0).getChannelId(),0);

            if(num > 1){
                System.out.println("상위 채널 이미지 요청2");
                count2 = viewChannels.get(1).getChannelCount();
                getChannelThumb(viewChannels.get(1).getChannelId(),1);

                if(num > 2){
                    System.out.println("상위 채널 이미지 요청3");
                    count3 = viewChannels.get(2).getChannelCount();
                    getChannelThumb(viewChannels.get(2).getChannelId(),2);

                }
            }
        }


    }

    private void getChannelThumb(String id, final int pos){
        GetDataService dataService = RetrofitInstance.getRetrofit().create((GetDataService.class));
        final Call<Channel> channelListRequest = dataService
                .getChannels("snippet", id,
                        getString(R.string.api_key),10);
        channelListRequest.enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {

                if(response.isSuccessful()){
                    if(response.body()!=null){

                        System.out.println("상위 채널 이미지 불러오기 성공");
                        channelList.put(pos, response.body().getItems().get(0));

                        if(channelList.size() == 3) {

                            statChannelVPAdapter = new StatChannelVPAdapter(channelList, getContext(), count1, count2, count3);
                            viewPager.setAdapter(statChannelVPAdapter);
                        }{
                            //최소 3개의 데이터가 필요합니다.
                        }

                    }else{
                        System.out.println("실패");
                    }
                }else{
                    System.out.println("실패dd");
                }

            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {

            }
        });
    }


    private void setCategoryPieChart() {

        pieChart.setRotationEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setHoleRadius(40f);

        //DB에서 데이터 갖고오기
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();


        RealmResults<Category> categories = realm.where(Category.class).sort("categoryCount",Sort.DESCENDING)
                .findAll();


        ArrayList<Integer> colors = new ArrayList<Integer>();
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);

        if(categories.size() > 0){

            yValues.add(new PieEntry(categories.get(0).getCategoryCount(),categories.get(0).getCategoryName()));
            colors.add(Color.rgb(212, 223, 230));

            if(categories.size() > 1){
                yValues.add(new PieEntry(categories.get(1).getCategoryCount(),categories.get(1).getCategoryName()));
                colors.add(Color.rgb(142, 192, 228));

                if(categories.size() > 2){
                    yValues.add(new PieEntry(categories.get(2).getCategoryCount(),categories.get(2).getCategoryName()));

                    colors.add(Color.rgb(202, 219, 233));

                    if(categories.size() > 3){
                        yValues.add(new PieEntry(categories.get(3).getCategoryCount(),categories.get(3).getCategoryName()));
                        colors.add(Color.rgb(106, 175, 230));

                        if(categories.size() > 4) {
                            yValues.add(new PieEntry(categories.get(4).getCategoryCount(), categories.get(4).getCategoryName()));
                            colors.add(Color.rgb(214, 236, 250));

                        }
                    }
                }
            }
        }


        //pieChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"category");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueTextSize(20);
        data.setValueFormatter(new MyValueFormatter("%"));

        pieChart.setData(data);
        pieChart.setEntryLabelColor(R.color.colorPrimary);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
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

    //time String -> Date
    private Date stringToDate(String str, Date date){

        SimpleDateFormat transFormat = new SimpleDateFormat("EE, MM월 dd일 yyyy년");
        String ori = transFormat.format(date);

        String from = ori  + " " + str;
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE, MM월 dd일 yyyy년 HH:mm:ss");

        try {
            return  dateFormat.parse(from);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }
}
