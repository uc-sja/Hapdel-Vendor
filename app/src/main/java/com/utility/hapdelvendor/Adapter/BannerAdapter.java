package com.utility.hapdelvendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Activity.OpenCategoryActivity;
import com.utility.hapdelvendor.Model.BannerModel.SimpleBannerModel.Datum;
import com.utility.hapdelvendor.R;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewholder> {
    Context context;
    ArrayList<Datum> bannerItems;
    private static final String TAG = "BannerAdapter";
    public BannerAdapter(Context context, ArrayList<Datum> bannerItems) {
        this.context = context;
        this.bannerItems = bannerItems;
    }

    @NonNull
    @Override
    public BannerViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.banner_cell, null, false);

        return new BannerViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final BannerViewholder bannerViewholder, int i) {
        final Datum datum = bannerItems.get(i);
        bannerViewholder.banner_buttton.setText("Banner btn");

        Log.d(TAG, "onBindViewHolder: title "+datum.getTitle());

//        bannerViewholder.banner_layout.setBackgroundResource(Integer.valueOf(datum.getId()));
        final ImageView img = (ImageView) bannerViewholder.banner_img;

        Picasso.get().load((datum.getBannerImg())).placeholder(R.drawable.loading).into(bannerViewholder.banner_img);

        bannerViewholder.banner_logo_text.setText(datum.getTitle());
        bannerViewholder.banner_subtitle.setText(datum.getTitle());


        bannerViewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OpenCategoryActivity.class);
                com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum categoryDatum = new com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum();
                categoryDatum.setId(datum.getCategoryId());
                intent.putExtra("category", new Gson().toJson(categoryDatum));
                context.startActivity(intent);
            }
        });
    }

    private void init(ImageView img) {
        int a= img.getHeight();
        int b = img.getWidth();


        Log.d(TAG, "init: width "+ a+" height  "+b);
    }

    @Override
    public int getItemCount() {
        return bannerItems.size();
    }

    public void updateItems(List<com.utility.hapdelvendor.Model.BannerModel.SimpleBannerModel.Datum> data) {
        this.bannerItems.clear();
        notifyDataSetChanged();
        this.bannerItems.addAll(data);
        notifyDataSetChanged();
    }

    public class BannerViewholder extends RecyclerView.ViewHolder {
        RelativeLayout banner_layout;
        TextView banner_logo_text, banner_subtitle;
        Button banner_buttton;
        CardView cardView;
        ImageView banner_img;

        public BannerViewholder(@NonNull View itemView) {
            super(itemView);

            banner_buttton = itemView.findViewById(R.id.banner_btn);
            banner_logo_text = itemView.findViewById(R.id.banner_logo_txt);
            banner_buttton = itemView.findViewById(R.id.banner_btn);
            banner_layout = itemView.findViewById(R.id.banner_layout);
            banner_subtitle = itemView.findViewById(R.id.banner_subtitle);
            cardView = itemView.findViewById(R.id.root_layout);
            banner_img = itemView.findViewById(R.id.banner_img);

            int desired_height_params =context.getResources().getDimensionPixelSize(R.dimen._150sdp);
            int desired_width_params = context.getResources().getDimensionPixelSize(R.dimen._150sdp);

            CardView.LayoutParams layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, context.getResources().getDimensionPixelSize(R.dimen._8sdp), 0);
            cardView.setLayoutParams(layoutParams);

        }
    }


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }}
