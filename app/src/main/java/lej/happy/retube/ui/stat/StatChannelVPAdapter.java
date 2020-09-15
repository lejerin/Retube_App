package lej.happy.retube.ui.stat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import lej.happy.retube.R;
import lej.happy.retube.data.models.youtube.Channel;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class StatChannelVPAdapter extends PagerAdapter {

    private HashMap<Integer, Channel.Items> channels = new HashMap<Integer, Channel.Items>();
    private Context mContext = null;

    private int count1 = 0, count2 = 0, count3 = 0;

    public StatChannelVPAdapter(HashMap<Integer, Channel.Items> channels, Context context, int count1, int count2, int count3) {
        this.channels = channels;
        this.mContext = context;
        this.count1 = count1;
        this.count2 = count2;
        this.count3 = count3;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;

        if (mContext != null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_item_stat_channel_vp, container, false);

            TextView channelNameText = (TextView) view.findViewById(R.id.channelNameText);
            channelNameText.setText(channels.get(position).getSnippet().getTitle());

            TextView channelCountText = (TextView) view.findViewById(R.id.channelViewCountText);
            switch (position){
                case 0:
                    channelCountText.setText(count1+"");
                    break;
                case 1:
                    channelCountText.setText(count2+"");
                    break;
                default:
                    channelCountText.setText(count3+"");
                    break;
            }


            ImageView channelImg = (ImageView) view.findViewById(R.id.channelImg);
            String ch = channels.get(position).getSnippet().getThumbnails().getHigh().getUrl();

            if(ch != null){

                Picasso.get()
                        .load(ch)
                        .placeholder(R.drawable.gray)
                        .fit()
                        .centerCrop()
                        .into(channelImg, new Callback() {
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
                channelImg.setImageResource(R.drawable.gray);
            }
            // ImageView imageView = (ImageView) view.findViewById(R.id.img);



           // imageView.setImageResource(imageIds[position]);

        }

        // 뷰페이저에 추가.
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        // 전체 페이지 수는 3개로 고정.

        if(channels.size() <3){
            return channels.size();
        }

        return 3;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }

    @Override
    public float getPageWidth(int position) {
        return (0.95f);
//        return super.getPageWidth(position);
    }
}