package com.utility.hapdelvendor.Utils;

import android.content.Context;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;


public class BannerSliderAdapter extends SliderAdapter {

    private static final String TAG="BannerSliderAdapter";
    private List<Integer> bannerList;
    private Context context;

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        for(int i = 0 ; i < bannerList.size(); i++){
            if(position == i){
                imageSlideViewHolder.bindImageSlide(bannerList.get(i));
            }
        }
    }
}
