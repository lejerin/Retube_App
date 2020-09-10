package lej.happy.retube.ui.play;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lej.happy.retube.helper.DetectPapago;
import lej.happy.retube.R;
import lej.happy.retube.data.Realm.Category;
import lej.happy.retube.data.Realm.User;
import lej.happy.retube.data.Realm.ViewChannel;
import lej.happy.retube.data.Realm.ViewVideo;
import lej.happy.retube.data.network.GetDataService;
import lej.happy.retube.data.network.RetrofitInstance;
import lej.happy.retube.data.models.Video;
import lej.happy.retube.data.models.comments.Comment;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayActivity extends YouTubeBaseActivity {

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;

    private TextView viewCount,likeCount, dislikeCount, commentNum,tags;

    private String videoid;

    private RecyclerView commentRecyclerView;
    private CommentsAdapter commentsAdapter;


    private TextView findCommentNum;
    private ConstraintLayout loadingLayout;


    private String nextToken;
    private List<Comment.Item> commentsList = new ArrayList<>();
    //현재 선택 언어 숫자 : 디폴트 -한국어
    private int nowSelectedLanNum = 0;
    //언어 선택 한국어, 영어, 일본어, 베트남어, 중국어 간체, 중국어 번체, 인도네시아어, 태국어, 독일어, 러시아어, 스페인어, 이탈리아어, 프랑스어
    private List<Comment.Item> koComments = new ArrayList<>();
    private List<Comment.Item> enComments = new ArrayList<>();
    private List<Comment.Item> jaComments = new ArrayList<>();
    private List<Comment.Item> viComments = new ArrayList<>();
    private List<Comment.Item> zhCNComments = new ArrayList<>();
    private List<Comment.Item> zhTWComments = new ArrayList<>();
    private List<Comment.Item> idComments = new ArrayList<>();
    private List<Comment.Item> thComments = new ArrayList<>();
    private List<Comment.Item> deComments = new ArrayList<>();
    private List<Comment.Item> ruComments = new ArrayList<>();
    private List<Comment.Item> esComments = new ArrayList<>();
    private List<Comment.Item> itComments = new ArrayList<>();
    private List<Comment.Item> frComments = new ArrayList<>();

    //댓글 request
    Call<Comment.Model> commentsRequest;


    private int rvScrollX = 0;
    private int rvScrollY = 0;


    private DetectPapago detectPapago = new DetectPapago();
    private boolean firstCommentToken = false;
    private int beforeListNum = 0;
    private boolean beforeAdd = false;

    private TextView title,descTextView;
    private ConstraintLayout moreLayout;
    private ImageView moreBtn;
    private boolean clickTitle = false;
    private int viewCountAll = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent = getIntent(); /*데이터 수신*/
        videoid = intent.getExtras().getString("videoID"); /*String형*/
        title = findViewById(R.id.title);
        descTextView = findViewById(R.id.DescTextView);
        tags = findViewById(R.id.tagText);

        saveDBUserData();

        String action = intent.getAction();
        String type = intent.getType();

        // 인텐트 정보가 있는 경우 실행

        if (Intent.ACTION_SEND.equals(action) && type != null) {

            if ("text/plain".equals(type)) {

                // 가져온 인텐트의 텍스트 정보
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                videoid = sharedText.substring(sharedText.lastIndexOf("/") +1);
                System.out.println("공유링크 :" + videoid);
            }

        }


        youTubePlayerView = findViewById(R.id.youtubePlay);
        commentRecyclerView = findViewById(R.id.recyclerView);
        viewCount = findViewById(R.id.viewCount);
        likeCount = findViewById(R.id.likeCount);
        dislikeCount = findViewById(R.id.dislikeCount);
        commentNum = findViewById(R.id.commentNum);

        findCommentNum = findViewById(R.id.findCommentNum);
        loadingLayout = findViewById(R.id.loadingLayout);

        Button stopGetCommentBtn = findViewById(R.id.stopGetCommentBtn);
        stopGetCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsRequest.cancel();
                commentsAdapter.notifyDataSetChanged();
                commentRecyclerView.smoothScrollBy(rvScrollX,rvScrollY);
                //commentRecyclerView.smoothScrollToPosition(lastVisibleItemPosition + 1);
                System.out.println(commentsList.size());

                loadingLayout.setVisibility(View.GONE);
            }
        });




        moreBtn = findViewById(R.id.moreBtn);
        moreLayout = findViewById(R.id.moreLayout);
        moreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!clickTitle) {

                    moreBtn.setImageDrawable(getResources().getDrawable(
                            R.drawable.triangle));
                    title.setMaxLines(4);
                    title.setEllipsize(TextUtils.TruncateAt.END);
                    descTextView.setVisibility(View.VISIBLE);
                    viewCount.setText("조회수 " + viewCountAll +"회");
                } else {
                    moreBtn.setImageDrawable(getResources().getDrawable(
                            R.drawable.retriangle));
                    title.setMaxLines(2);
                    title.setEllipsize(TextUtils.TruncateAt.END);
                    descTextView.setVisibility(View.GONE);
                    viewCount.setText("조회수 " + getNumlength(viewCountAll) +"회");
                }
                clickTitle = !clickTitle;

            }
        });

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.loadVideo(videoid);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        youTubePlayerView.initialize(getString(R.string.api_key), onInitializedListener);

        getVideoDetail();
        getCommentData("relevance");
        initAdapter("relevance");


        ImageButton sortBtn = (ImageButton)findViewById(R.id.sortBtn);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeData();

                //creating a popup menu
                PopupMenu popup = new PopupMenu(PlayActivity.this, sortBtn);
                //inflating menu from xml resource
                popup.inflate(R.menu.sort_item);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.relative:
                                //handle menu1 click
                                getCommentData("relevance");
                                initAdapter("relevance");
                                return true;
                            case R.id.newest:
                                getCommentData("time");
                                initAdapter("time");
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });


    }


    private void removeData(){
        nextToken = null;
        commentsList.clear();
        nowSelectedLanNum = 0;
        koComments.clear();
        enComments.clear();
        jaComments.clear();
        viComments.clear();
        zhCNComments.clear();
        zhTWComments.clear();
        idComments.clear();
        thComments.clear();
        deComments.clear();
        ruComments.clear();
        esComments.clear();
        zhTWComments.clear();
        itComments.clear();
        frComments.clear();

        findCommentNum.setText("0개 댓글 찾음");

        rvScrollX = 0;
        rvScrollY = 0;

        firstCommentToken = false;
        beforeListNum = 0;
        beforeAdd = false;

    }


    private void initAdapter(String order){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PlayActivity.this);
        commentsAdapter = new CommentsAdapter(PlayActivity.this, commentsList);
        commentRecyclerView.setLayoutManager(layoutManager);
        commentRecyclerView.setAdapter(commentsAdapter);
        commentsAdapter.setOnItemClickListener(new CommentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                System.out.println("갱신시작");
                findCommentNum.setText("0개 댓글 찾음");
                getCommentData(order);
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
                    case 4:
                        commentsList.addAll(zhCNComments);
                        break;
                    case 5:
                        commentsList.addAll(zhTWComments);
                        break;
                    case 6:
                        commentsList.addAll(idComments);
                        break;
                    case 7:
                        commentsList.addAll(thComments);
                        break;
                    case 8:
                        commentsList.addAll(deComments);
                        break;
                    case 9:
                        commentsList.addAll(ruComments);
                        break;
                    case 10:
                        commentsList.addAll(esComments);
                        break;
                    case 11:
                        commentsList.addAll(itComments);
                        break;
                    case 12:
                        commentsList.addAll(frComments);
                        break;

                }
                commentsAdapter.notifyDataSetChanged();

            }
        });
    }



    private void getVideoDetail() {

        GetDataService dataService = RetrofitInstance.getRetrofit().create((GetDataService.class));

        final Call<Video> videoTitleRequest = dataService
                .getPlayVideo("snippet,statistics",
                        getString(R.string.api_key),"items(id,snippet(title,description,publishedAt,categoryId,channelId,channelTitle,tags),statistics)",videoid);
        videoTitleRequest.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {

                if(response.isSuccessful()){
                    if(response.body()!=null){
                        title.setText(response.body().getItems().get(0).getSnippet().getTitle());

                        List<String> tag = response.body().getItems().get(0).getSnippet().getTags();
                        System.out.println("태그" + tag);
                        if(tag != null && tag.size() > 0){
                            //글자수 작은 것만
                            List<String> shortTag = new ArrayList<>();
                            for(int i=0;i<tag.size();i++){
                                if(tag.get(i).length() < 6){
                                    shortTag.add(tag.get(i));
                                }
                            }

                            //태그를 3개만 저장하기 위해서
                            if(shortTag.size() < 4){
                                saveDBNoun(shortTag);
                                for(int i=0;i<shortTag.size();i++){
                                    tags.setText(tags.getText().toString() + " #" +shortTag.get(i));
                                }
                            }else{
                                List<String> newList = new ArrayList<>();
                                newList.add(shortTag.get(0));
                                newList.add(shortTag.get(shortTag.size()/2));
                                newList.add(shortTag.get(shortTag.size()-1));
                                saveDBNoun(newList);
                                for(int i=0;i<newList.size();i++){
                                    tags.setText(tags.getText().toString() + " #" +newList.get(i));
                                }
                            }


                        }else{
                            tags.setVisibility(View.GONE);
                        }

                        //saveDBNoun(wiseNLUExample.getNoun(response.body().getItems().get(0).getSnippet().getTitle()));


                        descTextView.setText(response.body().getItems().get(0).getSnippet().getDescription());
                        viewCountAll = Integer.parseInt(response.body().getItems().get(0).getStatistics().getViewCount());
                        viewCount.setText("조회수 " + getNumlength(viewCountAll) +"회");
                        if(response.body().getItems().get(0).getStatistics().getLikeCount() != null){
                            likeCount.setText(getNumlength(Integer.parseInt(response.body().getItems().get(0).getStatistics().getLikeCount())));
                        }else{
                            likeCount.setText("");
                        }

                        if(response.body().getItems().get(0).getStatistics().getDislikeCount() != null){
                            dislikeCount.setText(getNumlength(Integer.parseInt(response.body().getItems().get(0).getStatistics().getDislikeCount())));
                        }else{
                            dislikeCount.setText("");
                        }

                        commentNum.setText("댓글 " + getNumlength(Integer.parseInt(response.body().getItems().get(0).getStatistics().getCommentCount()))) ;

                        saveDBCategory(Integer.parseInt(response.body().getItems().get(0).getSnippet().getCategoryId()));
                        saveDBChannel(response.body().getItems().get(0).getSnippet().getChannelId(),response.body().getItems().get(0).getSnippet().getChannelTitle());

                    }else{
                        System.out.println("실패");
                    }
                }else{
                    System.out.println("실패dd");
                }

            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {

            }
        });
    }



    private void getCommentData(String order) {
        System.out.println("getCommentData() 시작");
        loadingLayout.setVisibility(View.VISIBLE);

        GetDataService dataService = RetrofitInstance.getRetrofit().create((GetDataService.class));
        commentsRequest = null;



        if(nextToken == null){
            if(firstCommentToken == false) {
                firstCommentToken = true;
                commentsRequest = dataService
                        .getCommentsData("snippet", videoid, order,10, getString(R.string.api_key));
            }else{

                loadingLayout.setVisibility(View.GONE);
                Toast.makeText(this,"더 이상 불러올 댓글이 없습니다",Toast.LENGTH_LONG).show();
                return;
            }
        }else{
            commentsRequest = dataService
                    .getMoreCommentData("snippet",videoid, order,nextToken,10, getString(R.string.api_key));


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



                        findCommentNum.setText(commentsList.size() - beforeListNum + "개 댓글 찾음");
                        if(commentsList.size() - beforeListNum < 10 && nextToken != null){

                            System.out.println("추가시도");
                            beforeAdd = true;
                            getCommentData(order);

                        }else{
                            System.out.println("추가시도xx");
                            beforeAdd = false;


                            commentsAdapter.notifyDataSetChanged();
                            commentRecyclerView.smoothScrollBy(rvScrollX,rvScrollY);
                            //commentRecyclerView.smoothScrollToPosition(lastVisibleItemPosition + 1);
                            System.out.println(commentsList.size());

                            loadingLayout.setVisibility(View.GONE);
                        }


                    }else{
                        System.out.println("실패");
                        loadingLayout.setVisibility(View.GONE);
                    }
                }else{
                    System.out.println("댓글 불러오기 실패");
                    loadingLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Comment.Model> call, Throwable t) {
                loadingLayout.setVisibility(View.GONE);
            }
        });


    }

    private List<Comment.Item> detectPapago(final List<Comment.Item> item) {

        final List<Comment.Item> list = new ArrayList<>();


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

                        case "zh-CN":
                            if(nowSelectedLanNum == 4) list.add(item.get(i));
                            zhCNComments.add(item.get(i));
                            break;

                        case "zh-TW":
                            if(nowSelectedLanNum == 5) list.add(item.get(i));
                            zhTWComments.add(item.get(i));
                            break;

                        case "id":
                            if(nowSelectedLanNum == 6) list.add(item.get(i));
                            idComments.add(item.get(i));
                            break;

                        case "th":
                            if(nowSelectedLanNum == 7) list.add(item.get(i));
                            thComments.add(item.get(i));
                            break;
                        case "de":
                            if(nowSelectedLanNum == 8) list.add(item.get(i));
                            deComments.add(item.get(i));
                            break;

                        case "ru":
                            if(nowSelectedLanNum == 9) list.add(item.get(i));
                            ruComments.add(item.get(i));
                            break;

                        case "es":
                            if(nowSelectedLanNum == 10) list.add(item.get(i));
                            esComments.add(item.get(i));
                            break;

                        case "it":
                            if(nowSelectedLanNum == 11) list.add(item.get(i));
                            itComments.add(item.get(i));
                            break;

                        case "fr":
                            if(nowSelectedLanNum == 12) list.add(item.get(i));
                            frComments.add(item.get(i));
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
        if(commentsRequest!= null){
            commentsRequest.cancel();
        }

    }

    private String getNumlength(int num){

        int length = (int)(Math.log10(num)+1);

        if(length == 4){
            Double a = num/1000.0;
            return  (Math.floor((a) * 10) / 10.0) + "천";
        }else if(length == 5){
            Double a = num/10000.0;
            return (Math.floor((a) * 10) / 10.0) + "만";
        }else if(length > 5){
            length = num/10000;
            return  length + "만";
        }


        return  String.valueOf(num);

    }

    private void saveDBChannel(String channelId, String channelTitle) {

        Realm realm = Realm.getDefaultInstance();//데이터 넣기(insert)
        ViewChannel viewChannel = realm.where(ViewChannel.class).equalTo("channelId",channelId)
                .findFirst();

        if(viewChannel != null){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    viewChannel.setChannelCount(viewChannel.getChannelCount() + 1);

                }
            });


        }else{

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    ViewChannel newViewChannel = realm.createObject(ViewChannel.class);
                    newViewChannel.setChannelId(channelId);
                    newViewChannel.setChannelCount(1);

                }
            });
        }
    }


    private void saveDBCategory(int categoryid){

        //영화&애니메이션 중복
        if(categoryid == 30 || categoryid == 31) categoryid = 1;
        //코미디
        if(categoryid == 34) categoryid = 23;
        //공포&스릴러
        if(categoryid == 41) categoryid = 39;
        //단편
        if(categoryid == 42) categoryid = 18;

        String categoryName = getCategoryName(categoryid);

        Realm realm = Realm.getDefaultInstance();//데이터 넣기(insert)
        Category category = realm.where(Category.class).equalTo("categoryId",categoryid)
                .findFirst();

        if(category != null){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    category.setCategoryCount(category.getCategoryCount() + 1);

                }
            });

        }else{
            int finalCategoryid = categoryid;
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    Category newCategory = realm.createObject(Category.class);
                    newCategory.setCategoryId(finalCategoryid);
                    newCategory.setCategoryCount(1);
                    newCategory.setCategoryName(getCategoryName(finalCategoryid));

                }
            });
        }


    }

    private String getCategoryName(int id){

        switch (id){
            case 1:
                return "영화&애니메이션";
            case 2:
                return "자동차";
            case 10:
                return "음악";
            case 15:
                return "동물";
            case 17:
                return "스포츠";
            case 18:
                return "단편영화";
            case 19:
                return "여행&이벤트";
            case 20:
                return "게임";
            case 21:
                return "브이로그";
            case 22:
                return "인물&블로그";
            case 23:
                return "코미디";
            case 24:
                return "엔터테인먼트";
            case 25:
                return "뉴스&정치";
            case 26:
                return "노하우&스타일";
            case 27:
                return "교육";
            case 28:
                return "과학&기술";
            case 29:
                return "비영리&사회운동";
            case 32:
                return "액션&모험";
            case 33:
                return "클래식";
            case 35:
                return "다큐멘터리";
            case 36:
                return "드라마";
            case 37:
                return "가족";
            case 38:
                return "외국";
            case 39:
                return "공포&스릴러";
            case 40:
                return "공상과학&판타지";
            case 43:
                return "예능";
            case 44:
                return "트레일러";
            default:
                return "영화&애니메이션";

        }

    }


    private void saveDBUserData() {
        Realm realm = Realm.getDefaultInstance();//데이터 넣기(insert)
        User user = realm.where(User.class).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.setViewCount(user.getViewCount() + 1);
                int v = 0;
                try {
                    v = viewTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                switch (v){
                    case 0:
                        user.setDown(user.getDown() + 1);
                        break;
                    case 1:
                        user.setAm(user.getAm() + 1);
                        break;
                    case 2:
                        user.setPm(user.getPm() + 1);
                        break;
                    case 3:
                        user.setNight(user.getNight() + 1);
                        break;
                    default:
                        break;
                }

                Calendar c1 = Calendar.getInstance();
                if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                        c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                    user.setHoly(user.getHoly() + 1);
                }else{
                    user.setWeek(user.getWeek() + 1);
                }

            }
        });

    }


    private int viewTime() throws ParseException {

        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat fm = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

        //dawn
        String dawn =  dateFormat.format(today) +  " 05:59:59";
        if(today.getTime() <= fm.parse(dawn).getTime()){
            return 0;
        }
        //am
        String am =  dateFormat.format(today) +  " 11:59:59";
        if(today.getTime() <= fm.parse(am).getTime()){
            return 1;
        }
        //pm
        String pm =  dateFormat.format(today) +  " 17:59:59";
        if(today.getTime() <= fm.parse(pm).getTime()){
            return 2;
        }
        //night
        String night =  dateFormat.format(today) +  " 23:59:59";
        if(today.getTime() <= fm.parse(night).getTime()){
            return 3;
        }

        return -1;
    }

    private void saveDBNoun(List<String> list){
        Realm realm = Realm.getDefaultInstance();//데이터 넣기(insert)


        for(int i=0;i<list.size();i++){

            int finalI = i;

            ViewVideo isSearch = realm.where(ViewVideo.class).equalTo("noun",list.get(finalI)).findFirst();
            if(isSearch != null){
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        isSearch.setCount(isSearch.getCount() + 1);
                        isSearch.setDate(new Date());
                    }
                });
            }else{
                realm.executeTransaction(new Realm.Transaction() { @Override public void execute(Realm realm) {

                    ViewVideo search = realm.createObject(ViewVideo.class);
                    search.setNoun(list.get(finalI));
                    search.setCount(1);
                    search.setDate(new Date());

                }
                } );
            }


        }

    }
}


