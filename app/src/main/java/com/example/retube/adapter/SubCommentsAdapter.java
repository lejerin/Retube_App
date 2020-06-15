package com.example.retube.adapter;

import android.content.Context;
import android.print.PageRange;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retube.R;
import com.example.retube.models.comments.Comment;
import com.example.retube.models.comments.Replies;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SubCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Replies.Item> commentsList;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    private int allRepliesNum;

    public SubCommentsAdapter(Context context, List<Replies.Item> videoDetailsList, int allRepliesNum) {
        this.context = context;
        this.commentsList = videoDetailsList;
        this.allRepliesNum = allRepliesNum;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_item_replies_comment, parent, false);
            return new YoutubeHolder(view);
        }
        if(viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_replies_header, parent, false);
            return new HeaderViewHolder(view);


        }else if (viewType == TYPE_FOOTER) {
            //Inflating footer view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_replies_footer, parent, false);
            return new FooterViewHolder(itemView);
        } else return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {
            //   HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        } else if (holder instanceof FooterViewHolder) {
            if(allRepliesNum < 11){
                ((FooterViewHolder) holder).moreText.setVisibility(View.GONE);
            }else{
                ((FooterViewHolder) holder).moreText.setVisibility(View.VISIBLE);
            }

        } else if (holder instanceof YoutubeHolder) {
            position = position - 1;
            Replies.Item videoYT = commentsList.get(position);
            YoutubeHolder yth = (YoutubeHolder) holder;
            yth.setData(videoYT);
        }

    }

    @Override
    public int getItemCount() {
        return commentsList.size()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;

        } else if (position == commentsList.size() + 1) {

            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    class YoutubeHolder extends RecyclerView.ViewHolder{

        ImageView thumbnail;
        TextView title,subtitle,likeNum;


        public YoutubeHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.commentImg);
            title = itemView.findViewById(R.id.userName);
            subtitle = itemView.findViewById(R.id.comment);
            likeNum = itemView.findViewById(R.id.commentLikeNum);



        }
        public void setData(Replies.Item data){
            String getTitle = data.getSnippet().getAuthorDisplayName();
            String getSubTitle = data.getSnippet().getTextOriginal();
            String getThumb = data.getSnippet().getAuthorProfileImageUrl();
            String getlikeCount = data.getSnippet().getLikeCount().toString();
            String getTime = data.getSnippet().getPublishedAt();

            title.setText(getTitle +  " \u00b7 " +  formatTimeString(getTime));
            subtitle.setText(getSubTitle);
            likeNum.setText(getlikeCount);


            Picasso.get()
                    .load(getThumb)
                    .placeholder(R.mipmap.ic_launcher)
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

    private class HeaderViewHolder extends RecyclerView.ViewHolder {


        public HeaderViewHolder(View view) {
            super(view);

        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView moreText;

        public FooterViewHolder(View view) {
            super(view);

            moreText = view.findViewById(R.id.moreCommentBtn);

        }
    }
}
