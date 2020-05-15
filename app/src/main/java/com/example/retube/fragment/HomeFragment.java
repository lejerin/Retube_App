package com.example.retube.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retube.R;
import com.example.retube.Retrofit.GetDataService;
import com.example.retube.Retrofit.RetrofitInstance;
import com.example.retube.activity.PlayActivity;
import com.example.retube.adapter.HomeAdapter;
import com.example.retube.models.Channel.ChannelList;
import com.example.retube.models.Home.First;
import com.example.retube.models.Home.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private HomeAdapter adapter;
    private LinearLayoutManager manager;
    private List<Item> videoMostPopularList = new ArrayList<>();
    private HashMap<Integer,ChannelList.Item> channelList = new HashMap<Integer, ChannelList.Item>();
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        adapter = new HomeAdapter(getContext(),videoMostPopularList,channelList);
        manager = new LinearLayoutManager(getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);


        if(videoMostPopularList.size() == 0) {
            System.out.println("추가해야함");
            getVideoDetail();
        }

        adapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                Intent intent = new Intent(getActivity(), PlayActivity.class);
                intent.putExtra("title",videoMostPopularList.get(pos).getSnippet().getTitle());
                intent.putExtra("videoID",videoMostPopularList.get(pos).getId());
                startActivity(intent);

            }
        });



        return view;
    }


    private void getVideoDetail() {

        GetDataService dataService = RetrofitInstance.getRetrofit().create((GetDataService.class));
        final Call<First> videoDetailRequest = dataService
                .getMostPopular("snippet", "mostPopular",
                        "AIzaSyDDy3bLYFNDyZP7E5C4u8TZ_60F_BpL5J0","KR",25);
        videoDetailRequest.enqueue(new Callback<First>() {
            @Override
            public void onResponse(Call<First> call, Response<First> response) {

                if(response.isSuccessful()){
                    if(response.body()!=null){

                        videoMostPopularList.addAll(response.body().getItems());
                        System.out.println("성공"+videoMostPopularList.size());
                        getChannelThumbs();
                    }else{
                        System.out.println("실패");
                    }
                }else{
                    System.out.println("실패dd");
                }

            }

            @Override
            public void onFailure(Call<First> call, Throwable t) {

            }
        });
    }

    private void getChannelThumbs(){
        for(int i=0;i<videoMostPopularList.size();i++){
            getChannelThumb(videoMostPopularList.get(i).getSnippet().getChannelId(),i);
        }
        System.out.println("이미지 불러오기 끝");

    }

    private void getChannelThumb(String id, final int pos){
        GetDataService dataService = RetrofitInstance.getRetrofit().create((GetDataService.class));
        final Call<ChannelList> channelListRequest = dataService
                .getChannels("snippet", id,
                        "AIzaSyDDy3bLYFNDyZP7E5C4u8TZ_60F_BpL5J0",10);
        channelListRequest.enqueue(new Callback<ChannelList>() {
            @Override
            public void onResponse(Call<ChannelList> call, Response<ChannelList> response) {

                if(response.isSuccessful()){
                    if(response.body()!=null){

                        System.out.println("이미지 불러오기 성공");
                        channelList.put(pos, response.body().getItems().get(0));
                        if(pos == 24){
                            adapter.notifyDataSetChanged();
                            adapter.getItemCount();
                        }
                    }else{
                        System.out.println("실패");
                    }
                }else{
                    System.out.println("실패dd");
                }

            }

            @Override
            public void onFailure(Call<ChannelList> call, Throwable t) {

            }
        });
    }

}
