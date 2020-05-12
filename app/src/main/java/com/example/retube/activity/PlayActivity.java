package com.example.retube.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retube.DetectPapago;
import com.example.retube.R;
import com.example.retube.Retrofit.GetDataService;
import com.example.retube.Retrofit.RetrofitInstance;
import com.example.retube.adapter.CommentsAdapter;
import com.example.retube.models.VideoStats.VideoStats;
import com.example.retube.models.comments.Comment;
import com.example.retube.models.comments.Replies;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayActivity extends YouTubeBaseActivity {

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;

    private TextView viewCount,likeCount, dislikeCount, commentNum;

    private String videoid;

    private ProgressBar commentProgressBar;

    private RecyclerView commentRecyclerView;
    private CommentsAdapter commentsAdapter;

    private String nextToken;
    private List<Comment.Item> commentsList = new ArrayList<>();
    private List<List<Replies.Item>> repliesList = new ArrayList<>();


    //현재 선택 언어 숫자 : 디폴트 -한국어
    private int nowSelectedLanNum = 0;
    //언어 선택 한국어, 영어, 일본어, 베트남어
    private List<Comment.Item> koComments = new ArrayList<>();
    private List<Comment.Item> enComments = new ArrayList<>();
    private List<Comment.Item> jaComments = new ArrayList<>();
    private List<Comment.Item> viComments = new ArrayList<>();


    //댓글 request
    Call<Comment.Model> commentsRequest;


    private int rvScrollX = 0;
    private int rvScrollY = 0;

    private boolean firstCommentToken = false;

    private DetectPapago detectPapago = new DetectPapago();

    private int beforeListNum = 0;
    private boolean beforeAdd = false;

    private int papagoCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent = getIntent(); /*데이터 수신*/
        videoid = intent.getExtras().getString("videoID"); /*String형*/


        youTubePlayerView = findViewById(R.id.youtubePlay);
        commentRecyclerView = findViewById(R.id.recyclerView);
        viewCount = findViewById(R.id.viewCount);
        likeCount = findViewById(R.id.likeCount);
        dislikeCount = findViewById(R.id.dislikeCount);
        commentNum = findViewById(R.id.commentNum);
        commentProgressBar = findViewById(R.id.commentProgressBar);


        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.loadVideo(videoid);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        youTubePlayerView.initialize("AIzaSyDDy3bLYFNDyZP7E5C4u8TZ_60F_BpL5J0", onInitializedListener);

        System.out.println("ddd");
        getVideoDetail();
        getCommentData();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PlayActivity.this);
        commentsAdapter = new CommentsAdapter(PlayActivity.this, commentsList);
        commentRecyclerView.setLayoutManager(layoutManager);
        commentRecyclerView.setAdapter(commentsAdapter);
        commentsAdapter.setOnItemClickListener(new CommentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                System.out.println("갱신시작");
                getCommentData();
            }
        }) ;

        commentsAdapter.setOnLanClickListener(new CommentsAdapter.OnLanClickListener() {
            @Override
            public void onLanClick(View v, int position) {

                rvScrollX = commentRecyclerView.getScrollX();
                rvScrollY = commentRecyclerView.getScrollY();

                commentsList.clear();
                nowSelectedLanNum = position;
                switch (position){
                    case 0:
                        //한국어
                        commentsList.addAll(koComments);
                        break;
                    case 1:
                        commentsList.addAll(enComments);
                        break;
                    case 2:
                        commentsList.addAll(jaComments);
                        break;
                    case 3:
                        commentsList.addAll(viComments);
                        break;

                }
                commentsAdapter.notifyDataSetChanged();

            }
        });

    }

    private void getVideoDetail() {

        GetDataService dataService = RetrofitInstance.getRetrofit().create((GetDataService.class));
        Call<VideoStats> videoDetailRequest = dataService
                .getVideoDetail("statistics", "AIzaSyDDy3bLYFNDyZP7E5C4u8TZ_60F_BpL5J0",videoid);
        videoDetailRequest.enqueue(new Callback<VideoStats>() {
            @Override
            public void onResponse(Call<VideoStats> call, Response<VideoStats> response) {

                if(response.isSuccessful()){
                    if(response.body()!=null){

                        viewCount.setText("조회수 " + response.body().getItems().get(0).getStatistics().getViewCount() + "회");
                        likeCount.setText(response.body().getItems().get(0).getStatistics().getLikeCount());
                        dislikeCount.setText(response.body().getItems().get(0).getStatistics().getDislikeCount());
                        commentNum.setText("댓글개수 " + response.body().getItems().get(0).getStatistics().getCommentCount());


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


    private void getCommentData() {
        System.out.println("getCommentData() 시작");
        commentProgressBar.setVisibility(View.VISIBLE);

        GetDataService dataService = RetrofitInstance.getRetrofit().create((GetDataService.class));
        commentsRequest = null;



        if(nextToken == null){
            if(firstCommentToken == false) {
                firstCommentToken = true;
                commentsRequest = dataService
                        .getCommentsData("snippet", videoid, "relevance",10, "AIzaSyDDy3bLYFNDyZP7E5C4u8TZ_60F_BpL5J0");
            }else{
                return;
            }
        }else{
            commentsRequest = dataService
                    .getMoreCommentData("snippet",videoid, "relevance",nextToken,10, "AIzaSyDDy3bLYFNDyZP7E5C4u8TZ_60F_BpL5J0");


        }

        commentsRequest.enqueue(new Callback<Comment.Model>() {
            @Override
            public void onResponse(Call<Comment.Model> call, Response<Comment.Model> response) {

                if(response.isSuccessful()){
                    if(response.body()!=null){
                        // commentNum.setText("댓글 개수".concat(String.valueOf(response.body().getItems().size())));

                        if(response.body().getnextPageToken() != null){
                            nextToken = response.body().getnextPageToken();
                        }else{
                            nextToken = null;
                        }


                        if(!beforeAdd){ //추가시도 안했으면
                            beforeListNum = commentsList.size();
                        }

                        System.out.println(beforeListNum);

                        commentsList.addAll(detectPapago(response.body().getItems()));
                        System.out.println("commentsList" + commentsList.size());
                        System.out.println("repliesList" + repliesList.size());



                        if(commentsList.size() - beforeListNum < 10 && nextToken != null){
                            System.out.println("추가시도");
                            beforeAdd = true;
                            getCommentData();

                        }else{
                            System.out.println("추가시도xx");
                            beforeAdd = false;


                            commentsAdapter.notifyDataSetChanged();
                            commentRecyclerView.smoothScrollBy(rvScrollX,rvScrollY);
                            //commentRecyclerView.smoothScrollToPosition(lastVisibleItemPosition + 1);
                            System.out.println(commentsList.size());

                            commentProgressBar.setVisibility(View.GONE);
                        }


                    }else{
                        System.out.println("실패");
                        commentProgressBar.setVisibility(View.GONE);
                    }
                }else{
                    System.out.println("댓글 불러오기 실패");
                    commentProgressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Comment.Model> call, Throwable t) {
                commentProgressBar.setVisibility(View.GONE);
            }
        });


    }

    private List<Comment.Item> detectPapago(final List<Comment.Item> item) {

        final List<Comment.Item> list = new ArrayList<>();
        papagoCount += 1;


        Thread thread = new Thread()
        {
            public void run() {
                for (int i = 0; i < item.size(); i++) {

                    String before = item.get(i).getSnippet().getTopLevelComment().getSnippet().getTextOriginal();
                    String code = detectPapago.startDetect(before);

                    switch (code){
                        case "ko":
                            if(nowSelectedLanNum == 0) list.add(item.get(i));
                            koComments.add(item.get(i));
                            break;

                        case "en":
                            if(nowSelectedLanNum == 1) list.add(item.get(i));
                            enComments.add(item.get(i));
                            break;

                        case "ja":
                            if(nowSelectedLanNum == 2) list.add(item.get(i));
                            jaComments.add(item.get(i));
                            break;

                        case "vi":
                            if(nowSelectedLanNum == 3) list.add(item.get(i));
                            viComments.add(item.get(i));
                            break;

                        default:
                            break;
                    }




                }

            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commentsRequest.cancel();
    }
}

