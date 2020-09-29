package com.example.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class CustomRadioView extends LinearLayout {

    private ButtonClickListener clickListener;

    private LinearLayout linearLayout;
    private List<ImageButton> btnList = new ArrayList<>();

    public interface ButtonClickListener
    {
        void onClick(int i);
    }

    public CustomRadioView(Context context)
    {
        this(context, null);
    }

    public CustomRadioView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        setup();
    }

    private void setup(){
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_view, this);

        linearLayout = findViewById(R.id.layout);

    }

    public void setBtn(List<Integer> btnImg){

        for(int i=0; i<btnImg.size(); i++){
            ImageButton createdButton = new ImageButton(getContext());
            createdButton.setImageResource(btnImg.get(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 5, 0);
            createdButton.setLayoutParams(params);
            createdButton.setBackgroundResource(R.drawable.custom_button_bg);
            createdButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedBtn(v);
                }
            });
            btnList.add(createdButton);
            linearLayout.addView(btnList.get(i));
        }
    }

    private void selectedBtn(View v){


        for(int i=0; i<btnList.size(); i++){
            if(btnList.get(i) == v){
                btnList.get(i).setSelected(true);
                btnList.get(i).setColorFilter(ContextCompat.getColor(getContext(),
                        R.color.colorAccent));
                clickListener.onClick(i);
            }else{
                btnList.get(i).setSelected(false);
                btnList.get(i).setColorFilter(ContextCompat.getColor(getContext(),
                        R.color.colorPrimaryDark));
            }
        }

    }

    public void setReportListener(ButtonClickListener listener){
        this.clickListener = listener;

    }

}
