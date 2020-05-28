package com.example.retube.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retube.R;
import com.example.retube.models.Channel;
import com.example.retube.models.HomeMostPopular.Item;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Item> videoList;
    private HashMap<Integer, Channel.Item> chennelList = new HashMap<Integer, Channel.Item>();
    private OnItemClickListener mListener = null;
    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }


    public HomeAdapter(Context context, List<Item> videoList,HashMap<Integer, Channel.Item> chennelList) {
        this.context = context;
        this.videoList = videoList;
        this.chennelList = chennelList;
    }

    class YoutubeHolder extends RecyclerView.ViewHolder{

        ImageView thumbnail,chImg;
        TextView title,subtitle,chnnelName;

        public YoutubeHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.rc_img);
            title = itemView.findViewById(R.id.rc_title);
            subtitle = itemView.findViewById(R.id.rc_subtitle);
            chImg = itemView.findViewById(R.id.chImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    // 리스너 객체의 메서드 호출
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        mListener.onItemClick(v, pos);
                    }
                }
            });
        }
        public void setData(Item data, Channel.Item ch){
            String getTitle = data.getSnippet().getTitle();
            String getSubTitle =  formatTimeString(data.getSnippet().getPublishedAt());
            String getThumb = data.getSnippet().getThumbnails().getHigh().getUrl();
            String getchName = data.getSnippet().getChannelTitle();
            String getViewCount = getNumlength(Integer.parseInt(data.getStatistics().getViewCount()));
            if(ch != null){
                String getch = ch.getSnippet().getThumbnails().getHigh().getUrl();

                Picasso.get()
                        .load(getch)
                        .placeholder(R.drawable.gray)
                        .fit()
                        .centerCrop()
                        .into(chImg, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "Thumbnail success");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.d(TAG, "Thumbnail error");
                            }
                        });
            }else{
                chImg.setImageResource(R.drawable.gray);
            }



            title.setText(getTitle);
            subtitle.setText(getchName +  " \u00b7 " + getViewCount +"회" +  " \u00b7 " +getSubTitle);
            Picasso.get()
                    .load(getThumb)
                    .placeholder(R.drawable.gray)
                    .fit()
                    .centerCrop()
                    .into(thumbnail, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Thumbnail success");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d(TAG, "Thumbnail error");
                        }
                    });


        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_item_home, parent, false);
        return new YoutubeHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item videoYT = videoList.get(position);
        Channel.Item videochennel =  chennelList.get(position);



        YoutubeHolder yth = (YoutubeHolder) holder;
        yth.setData(videoYT,videochennel);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    private String formatTimeString(String str){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            ZonedDateTime dateTime = ZonedDateTime.parse(str + "[Europe/London]");

            String a = dateTime.getYear()+"-"+dateTime.getMonthValue() + "-"+ dateTime.getDayOfMonth() +
                    " " + dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date to = transFormat.parse(a);
                long regTime = to.getTime();
                long curTime = new Date().getTime() - 9*60*60*1000;

                long diffTime = (curTime - regTime) / 1000;
                String msg = null;
                if (diffTime < 60) {
                    msg = "방금 전";
                } else if ((diffTime /= 60) < 60) {
                    msg = diffTime + "분 전";
                } else if ((diffTime /= 60) < 24) {
                    msg = (diffTime) + "시간 전";
                } else if ((diffTime /= 24) < 30) {
                    msg = (diffTime) + "일 전";
                } else if ((diffTime /= 30) < 12) {
                    msg = (diffTime) + "달 전";
                } else {
                    msg = (diffTime) + "년 전";
                }
                return msg;

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }



       return "";
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

}
