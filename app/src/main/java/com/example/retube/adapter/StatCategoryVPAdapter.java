package com.example.retube.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.retube.R;

public class StatCategoryVPAdapter extends PagerAdapter {


    private Context mContext = null;

    public StatCategoryVPAdapter() {

    }

    private int[] imageIds;


    public StatCategoryVPAdapter(int[] a, Context context) {
        mContext = context;
        imageIds = a;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;

        if (mContext != null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_item_stat_category_vp, container, false);
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
        return 3;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }
}