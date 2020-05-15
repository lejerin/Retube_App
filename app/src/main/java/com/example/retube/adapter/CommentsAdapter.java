package com.example.retube.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retube.Helper.TransferPapago;
import com.example.retube.R;
import com.example.retube.Retrofit.GetDataService;
import com.example.retube.Retrofit.RetrofitInstance;
import com.example.retube.models.comments.Comment;
import com.example.retube.models.comments.Replies;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Comment.Item> commentsList;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    private SparseBooleanArray mSelectedReplies = new SparseBooleanArray(0);

    private SparseBooleanArray mSelectedTransfer = new SparseBooleanArray(0);
    private SparseBooleanArray mSelectedTransferData = new SparseBooleanArray(0);

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    private HashMap<Integer,List<Replies.Item>> repliesHashMap = new HashMap<Integer, List<Replies.Item>>();


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    private String sourceTarget = "ko";
    private TransferPapago transferPapago = new TransferPapago();
    private HashMap<Integer,String> tranferHashMap = new HashMap<Integer,String>();

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    private OnItemClickListener mListener = null ;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public interface OnLanClickListener {
        void onLanClick(View v, int position) ;
    }
    private OnLanClickListener mLanListener = null ;
    public void setOnLanClickListener(OnLanClickListener listener) {
        this.mLanListener = listener ;
    }

    public CommentsAdapter(Context context, List<Comment.Item> videoDetailsList) {
        this.context = context;
        this.commentsList = videoDetailsList;


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_item_comment, parent, false);
            return new YoutubeHolder(view);
        }
        if(viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_header, parent, false);
            return new HeaderViewHolder(view);


        }else if (viewType == TYPE_FOOTER) {
            //Inflating footer view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_footer, parent, false);
            return new FooterViewHolder(itemView);
        } else return null;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {

        if (holder instanceof HeaderViewHolder) {
         //   HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        } else if (holder instanceof FooterViewHolder) {
            //((FooterViewHolder) holder).footerFlag.setText("Footer");
        } else if (holder instanceof YoutubeHolder) {
            position = position -1;
            YoutubeHolder yth = (YoutubeHolder) holder;
            Comment.Item videoYT = commentsList.get(position);
            yth.setData(videoYT);

            if (mSelectedItems.get(position, false) == true) {
                yth.recommentRecyclerView.setVisibility(View.VISIBLE);
                // Create layout manager with initial prefetch item count

                if(repliesHashMap.get(position) != null){

                LinearLayoutManager layoutManager = new LinearLayoutManager(
                        yth.recommentRecyclerView.getContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                );
                layoutManager.setInitialPrefetchItemCount(repliesHashMap.get(position).size());

                // Create sub item view adapter
                SubCommentsAdapter subItemAdapter = new SubCommentsAdapter(context,repliesHashMap.get(position),commentsList.get(position).getSnippet().getTotalReplyCount());

                yth.recommentRecyclerView.setLayoutManager(layoutManager);
                yth.recommentRecyclerView.setAdapter(subItemAdapter);
                yth.recommentRecyclerView.setRecycledViewPool(viewPool);
                }

            } else {
                yth.recommentRecyclerView.setVisibility(View.GONE);
            }


            if(!sourceTarget.equals("ko")){
                yth.transferBtn.setVisibility(View.VISIBLE);
            }else{
                yth.transferBtn.setVisibility(View.GONE);
            }

            if (mSelectedTransfer.get(position, false) == true) {

                // Create layout manager with initial prefetch item count
                    yth.transferText.setText(tranferHashMap.get(position));
                    yth.transferText.setVisibility(View.VISIBLE);


            } else {
                yth.transferText.setVisibility(View.GONE);
            }






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
        TextView title,subtitle,likeNum,messageNum,transferBtn, transferText;
        Button recomment;
        RecyclerView recommentRecyclerView;


        public YoutubeHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.commentImg);
            title = itemView.findViewById(R.id.userName);
            subtitle = itemView.findViewById(R.id.comment);
            likeNum = itemView.findViewById(R.id.commentLikeNum);
            messageNum = itemView.findViewById(R.id.commentrepliesNum);
            recomment = itemView.findViewById(R.id.recommentBtn);
            recommentRecyclerView = itemView.findViewById(R.id.recommentRecyclerView);
            transferBtn = itemView.findViewById(R.id.transferBtn);
            transferText = itemView.findViewById(R.id.transferText);

            if(!sourceTarget.equals("ko")){
                transferBtn.setVisibility(View.VISIBLE);
            }
            transferBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int realPosition = getAdapterPosition() - 1;

                    if (mSelectedTransferData.get(realPosition, false) == true) {
                        if (mSelectedTransfer.get(realPosition, false) == true) {
                            mSelectedTransfer.delete(realPosition);
                            transferText.setVisibility(View.GONE);
                        } else {
                            mSelectedTransfer.put(realPosition, true);
                            transferText.setVisibility(View.VISIBLE);
                        }

                    } else {
                        mSelectedTransferData.put(realPosition, true);
                        if (transferText.getText().equals("")) {
                            Thread thread = new Thread() {
                                public void run() {

                                    String data = transferPapago.startTransfer(subtitle.getText().toString(), sourceTarget, "ko");
                                    transferText.setText(data);
                                    tranferHashMap.put(realPosition,data);
                            }
                            };
                            thread.start();
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                        transferText.setVisibility(View.VISIBLE);
                        mSelectedTransfer.put(realPosition, true);
                    }

                }
            });



            recomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int realPosition = getAdapterPosition() - 1;

                    if (mSelectedReplies.get(realPosition, false) == true) {
                        if (mSelectedItems.get(realPosition, false) == true) {
                            mSelectedItems.delete(realPosition);
                            recommentRecyclerView.setVisibility(View.GONE);
                        } else {
                            mSelectedItems.put(realPosition, true);
                            recommentRecyclerView.setVisibility(View.VISIBLE);
                        }

                    } else {
                        mSelectedReplies.put(realPosition, true);
                        final List<Replies.Item> list = new ArrayList<>();

                        if (commentsList.get(realPosition).getSnippet().getTotalReplyCount() > 0) {
                            GetDataService dataService = RetrofitInstance.getRetrofit().create((GetDataService.class));
                            Call<Replies> repliesRequest = null;
                            repliesRequest = dataService
                                    .getRepliesData("snippet", commentsList.get(realPosition).getId(), 10, "AIzaSyDDy3bLYFNDyZP7E5C4u8TZ_60F_BpL5J0");

                            repliesRequest.enqueue(new retrofit2.Callback<Replies>() {
                                @Override
                                public void onResponse(Call<Replies> call, Response<Replies> response) {

                                    if (response.isSuccessful()) {
                                        if (response.body() != null) {

                                            list.addAll(response.body().getItems());
                                            repliesHashMap.put(realPosition,list);
                                            System.out.println("리스트 개수" + list.size());
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(
                                                    recommentRecyclerView.getContext(),
                                                    LinearLayoutManager.VERTICAL,
                                                    false
                                            );
                                            layoutManager.setInitialPrefetchItemCount(list.size());

                                            // Create sub item view adapter
                                            SubCommentsAdapter subItemAdapter = new SubCommentsAdapter(context,list,commentsList.get(realPosition).getSnippet().getTotalReplyCount());

                                            recommentRecyclerView.setLayoutManager(layoutManager);
                                            recommentRecyclerView.setAdapter(subItemAdapter);
                                            recommentRecyclerView.setRecycledViewPool(viewPool);
                                            subItemAdapter.notifyDataSetChanged();

                                            System.out.println("개수: "+subItemAdapter.getItemCount());
                                            System.out.println("클릭넘버: "+realPosition);

                                            if (mSelectedItems.get(realPosition, false) == true) {
                                                mSelectedItems.delete(realPosition);
                                                recommentRecyclerView.setVisibility(View.GONE);
                                            } else {
                                                mSelectedItems.put(realPosition, true);
                                                recommentRecyclerView.setVisibility(View.VISIBLE);
                                            }


                                        } else {
                                            System.out.println("실패");
                                        }
                                    } else {
                                        System.out.println("실패dd");
                                    }

                                }

                                @Override
                                public void onFailure(Call<Replies> call, Throwable t) {

                                }
                            });
                        }

                    }



                }
            });

        }
        public void setData(Comment.Item data){
            String getTitle = data.getSnippet().getTopLevelComment().getSnippet().getAuthorDisplayName();
            String getSubTitle = data.getSnippet().getTopLevelComment().getSnippet().getTextOriginal();
            String getThumb = data.getSnippet().getTopLevelComment().getSnippet().getAuthorProfileImageUrl();
            String getRecommentCount = data.getSnippet().getTotalReplyCount().toString();
            String getlikeCount = data.getSnippet().getTopLevelComment().getSnippet().getLikeCount().toString();

            title.setText(getTitle);
            subtitle.setText(getSubTitle);
            likeNum.setText(getlikeCount);
            messageNum.setText(getRecommentCount);

            if(getRecommentCount.equals("0")){
                recomment.setVisibility(View.GONE);
                //   recommentRecyclerView.setVisibility(View.GONE);
            }else {
                recomment.setVisibility(View.VISIBLE);
                //   recommentRecyclerView.setVisibility(View.VISIBLE);
                recomment.setText("답글 " + getRecommentCount + "개");
            }

            Picasso.get()
                    .load(getThumb)
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(thumbnail, new Callback() {
                        @Override
                        public void onSuccess() {

                           // Log.d(TAG, "Thumbnail success");

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d(TAG, "Thumbnail error");
                        }
                    });

        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        Spinner spinner;

        public HeaderViewHolder(View view) {
            super(view);

            spinner = view.findViewById(R.id.spinner);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mLanListener != null) {
                        mLanListener.onLanClick(view, position);
                        mSelectedItems.clear();
                        mSelectedReplies.clear();
                        mSelectedTransfer.clear();
                        mSelectedTransferData.clear();
                        tranferHashMap.clear();


                        switch (position){
                            case 0:
                                //한국어
                                sourceTarget = "ko";
                                break;
                            case 1:
                                sourceTarget = "en";
                                break;
                            case 2:
                                sourceTarget = "ka";
                                break;
                            case 3:
                                sourceTarget = "vi";
                                break;

                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView moreCommentBtn;

        public FooterViewHolder(View view) {
            super(view);

            moreCommentBtn = view.findViewById(R.id.moreCommentBtn);
            moreCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos) ;
                        }
                    }
                }
            });

        }
    }
}
