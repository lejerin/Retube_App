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
import com.example.retube.models.Channel.ChannelList;
import com.example.retube.models.Home.Item;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Item> videoList;
    private HashMap<Integer, ChannelList.Item> chennelList = new HashMap<Integer, ChannelList.Item>();
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

    public HomeAdapter(Context context, List<Item> videoList,HashMap<Integer,ChannelList.Item> chennelList) {
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
        public void setData(Item data,ChannelList.Item ch){
            String getTitle = data.getSnippet().getTitle();
            String getSubTitle = data.getSnippet().getPublishedAt();
            String getThumb = data.getSnippet().getThumbnails().getHigh().getUrl();
            String getch = ch.getSnippet().getThumbnails().getHigh().getUrl();
            String getchName = ch.getSnippet().getTitle();

            title.setText(getTitle);
            subtitle.setText(getchName + " . " +getSubTitle);
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
        ChannelList.Item videochennel =  chennelList.get(position);
        YoutubeHolder yth = (YoutubeHolder) holder;
        yth.setData(videoYT,videochennel);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }


}
