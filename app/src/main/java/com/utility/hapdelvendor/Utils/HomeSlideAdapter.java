package com.utility.hapdelvendor.Utils;

import android.content.Context;
import android.util.Log;

import com.utility.hapdelvendor.Model.BannerModel.Datum;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;


public class HomeSlideAdapter extends SliderAdapter {

    private static final String TAG = "HomeSlideAdapter";
    Context context;
    List<Datum> data;

    public HomeSlideAdapter(Context context, List<Datum> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        Log.d(TAG, "onBindImageSlide: "+position);

        for(int i = 0; i < data.size(); i++){
            if(position == i){
                viewHolder.bindImageSlide(Integer.valueOf(data.get(i).getId()));
                break;
            }
        }
    }

}
