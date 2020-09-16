package lej.happy.retube.ui.statistic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import lej.happy.retube.R;
import lej.happy.retube.data.models.youtube.Searches;
import lej.happy.retube.data.models.youtube.VideoStats;
import lej.happy.retube.data.network.GetDataService;
import lej.happy.retube.data.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendActivity extends AppCompatActivity {

    private List<Searches.Items> videoSearchList = new ArrayList<>();
    RecyclerView rv;
    private SearchAdapter adapter;
    private LinearLayoutManager manager;
    private HashMap<Integer,Integer> viewCountList = new HashMap<Integer, Integer>();
    private int startNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);


        Intent intent = getIntent(); /*데이터 수신*/
        String keyword1 = intent.getExtras().getString("keyword1");
        String keyword2 = intent.getExtras().getString("keyword2");
        String keyword3 = intent.getExtras().getString("keyword3");
        String keyword4 = intent.getExtras().getString("keyword4");
        String keyword5 = intent.getExtras().getString("keyword5");
        String keyword6 = intent.getExtras().getString("keyword6");
        String keyword7 = intent.getExtras().getString("keyword7");
        String keyword8 = intent.getExtras().getString("keyword8");
        String keyword9 = intent.getExtras().getString("keyword9");

        rv = findViewById(R.id.rc_recyclerView);
        adapter = new SearchAdapter(RecommendActivity.this,videoSearchList,viewCountList);
        manager = new LinearLayoutManager(RecommendActivity.this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);

        adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                Intent intent = new Intent(RecommendActivity.this, PlayActivity.class);
                intent.putExtra("videoID",videoSearchList.get(pos).getId().getVideoId());
                startActivity(intent);

            }
        });



        if(!keyword1.equals("")) getSearchVideoDetail(keyword1);
        if(!keyword2.equals("")) getSearchVideoDetail(keyword2);
        if(!keyword3.equals("")) getSearchVideoDetail(keyword3);
        if(!keyword4.equals("")) getSearchVideoDetail(keyword4);
        if(!keyword5.equals("")) getSearchVideoDetail(keyword5);
        if(!keyword6.equals("")) getSearchVideoDetail(keyword6);
        if(!keyword7.equals("")) getSearchVideoDetail(keyword7);
        if(!keyword8.equals("")) getSearchVideoDetail(keyword8);
        if(!keyword9.equals("")) getSearchVideoDetail(keyword9);


        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void getSearchVideoDetail(String input) {



        GetDataService dataService = RetrofitInstance.getRetrofit().create((GetDataService.class));
        Call<Searches> videoDetailRequest = null;

        videoDetailRequest= dataService
                        .getSerchVideo("snippet", 5, "relevance", "video",
                                input,"none",getString(R.string.api_key));


        videoDetailRequest.enqueue(new Callback<Searches>() {
            @Override
            public void onResponse(Call<Searches> call, Response<Searches> response) {

                if(response.isSuccessful()){
                    if(response.body()!=null){

                        videoSearchList.addAll(response.body().getItems());

                        getViewCounts();
                       // adapter.notifyDataSetChanged();

                    }else{
                        System.out.println("실패");
                    }
                }else{
                    System.out.println("실패dd");
                }

            }

            @Override
            public void onFailure(Call<Searches> call, Throwable t) {

            }
        });
    }

    private void getViewCounts(){


        for(int i= startNum;i<startNum+5;i++){
            getVeiwCount(videoSearchList.get(i).getId().getVideoId(),i);
        }
        startNum = startNum + 5;
        System.out.println("조회수 불러오기 끝");
       // adapter.notifyDataSetChanged();

    }

    private void getVeiwCount(String id, final int pos) {

        GetDataService dataService = RetrofitInstance.getRetrofit().create((GetDataService.class));
        Call<VideoStats> videoDetailRequest = dataService
                .getVideoDetail("statistics", getString(R.string.api_key),id);
        videoDetailRequest.enqueue(new Callback<VideoStats>() {
            @Override
            public void onResponse(Call<VideoStats> call, Response<VideoStats> response) {

                if(response.isSuccessful()){
                    if(response.body()!=null){
                        System.out.println("검색 조회수" + pos);
                        viewCountList.put(pos, response.body().getItems().get(0).getStatistics().getViewCount());


                //        adapter.notifyDataSetChanged();

//                        if(pos == videoSearchList.size()-1){
//
//                        }
                    }else{
                        System.out.println("실패");
                    }
                }else{
                    System.out.println("실패dd");
                }

            }

            @Override
            public void onFailure(Call<VideoStats> call, Throwable t) {

            }
        });
    }

}