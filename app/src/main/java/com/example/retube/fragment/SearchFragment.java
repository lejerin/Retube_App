package com.example.retube.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retube.R;
import com.example.retube.Retrofit.GetDataService;
import com.example.retube.Retrofit.RetrofitInstance;
import com.example.retube.activity.PlayActivity;
import com.example.retube.adapter.SearchAdapter;
import com.example.retube.models.Search.Item;
import com.example.retube.models.Search.Searchs;
import com.example.retube.models.VideoStats.VideoStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    private SearchAdapter adapter;
    private LinearLayoutManager manager;
    private List<Item> videoSearchList = new ArrayList<>();
    private HashMap<Integer,Integer> viewCountList = new HashMap<Integer, Integer>();
    private int startNum = 0;

    private EditText searchText;
    private ImageButton checkBtn;

    private String nextToken;
    private int lastVisibleItemPosition = 0;
    private boolean firstCommentToken = false;

    RecyclerView rv;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        // Inflate the layout for this fragment
        rv = view.findViewById(R.id.seach_rc);
        searchText = view.findViewById(R.id.searchText);
        checkBtn = view.findViewById(R.id.checkBtn);


        adapter = new SearchAdapter(getContext(),videoSearchList,viewCountList);
        manager = new LinearLayoutManager(getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);

        adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                Intent intent = new Intent(getActivity(), PlayActivity.class);
                intent.putExtra("title",videoSearchList.get(pos).getSnippet().getTitle());
                intent.putExtra("desc","need");
                intent.putExtra("videoID",videoSearchList.get(pos).getId().getVideoId());
                startActivity(intent);

            }
        });

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    //비디오 추가 갱신할 때
                    String input = searchText.getText().toString();
                    getSearchVideoDetail(input);



                }
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {

                    case EditorInfo.IME_ACTION_SEARCH:

                        hideKeyboard(getActivity());
                        videoSearchList.clear();
                        firstCommentToken = false;
                        nextToken = null;
                        String input = searchText.getText().toString();
                        getSearchVideoDetail(input);
                        break;

                    default:

                        return false;
                }

                return true;


            }
        });

        checkBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                videoSearchList.clear();
                firstCommentToken = false;
                nextToken = null;
                String input = searchText.getText().toString();
                getSearchVideoDetail(input);
            }
        });

        return view;
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void getSearchVideoDetail(String input) {

        GetDataService dataService = RetrofitInstance.getRetrofit().create((GetDataService.class));
        Call<Searchs> videoDetailRequest = null;
        if(nextToken == null){
            if(firstCommentToken == false) {
                firstCommentToken = true;
                videoDetailRequest= dataService
                        .getSerchVideo("snippet", 30, "relevance", "video",
                                input,"none","AIzaSyDDy3bLYFNDyZP7E5C4u8TZ_60F_BpL5J0");
            }else{
                return;
            }
        }else{
            videoDetailRequest= dataService
                    .getMoreSerchVideo("snippet", nextToken,30, "relevance", "video",
                            input,"none","AIzaSyDDy3bLYFNDyZP7E5C4u8TZ_60F_BpL5J0");

        }

        videoDetailRequest.enqueue(new Callback<Searchs>() {
            @Override
            public void onResponse(Call<Searchs> call, Response<Searchs> response) {

                if(response.isSuccessful()){
                    if(response.body()!=null){

                        if(response.body().getNextPageToken() != null){
                            nextToken = response.body().getNextPageToken();
                        }else{
                            nextToken = null;
                        }

                        videoSearchList.addAll(response.body().getItems());
                        getViewCounts();

                        System.out.println("성공"+adapter.getItemCount());
                    }else{
                        System.out.println("실패");
                    }
                }else{
                    System.out.println("실패dd");
                }

            }

            @Override
            public void onFailure(Call<Searchs> call, Throwable t) {

            }
        });
    }

    private void getViewCounts(){


        for(int i= startNum;i<videoSearchList.size();i++){
            getVeiwCount(videoSearchList.get(i).getId().getVideoId(),i);
        }
        System.out.println("조회수 불러오기 끝");
    }

    private void getVeiwCount(String id, final int pos) {

        GetDataService dataService = RetrofitInstance.getRetrofit().create((GetDataService.class));
        Call<VideoStats> videoDetailRequest = dataService
                .getVideoDetail("statistics", "AIzaSyDDy3bLYFNDyZP7E5C4u8TZ_60F_BpL5J0",id);
        videoDetailRequest.enqueue(new Callback<VideoStats>() {
            @Override
            public void onResponse(Call<VideoStats> call, Response<VideoStats> response) {

                if(response.isSuccessful()){
                    if(response.body()!=null){
                        System.out.println("조회수" + pos);
                        viewCountList.put(pos, Integer.parseInt(response.body().getItems().get(0).getStatistics().getViewCount()));
                        if(pos == videoSearchList.size()-1){
                            startNum = videoSearchList.size();
                            adapter.notifyDataSetChanged();
                            rv.smoothScrollToPosition(lastVisibleItemPosition + 1);
                        }
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
