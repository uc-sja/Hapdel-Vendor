package com.utility.hapdelvendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Activity.OpenCategoryActivity;
import com.utility.hapdelvendor.Activity.OpenProductActivity;
import com.utility.hapdelvendor.LaundaryActivity;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.CircularTextView;
import com.utility.hapdelvendor.Utils.Common;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCatAdapter extends RecyclerView.Adapter<MainCatAdapter.MainCatViewHolder> {
    private Context context;
    private ArrayList<Datum> catList;
    private static final String TAG = "MainCatAdapter";
    private Boolean isMainCategory;
    private Boolean allHaveSubCat;


    public MainCatAdapter(Context context, ArrayList catList, boolean b, boolean allHaveSubCat) {
        this.context = context;
        this.catList = catList;
        this.isMainCategory = b;
        allHaveSubCat = allHaveSubCat;

    }

    @NonNull
    @Override
    public MainCatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_cat_cell, null, false);
        return new MainCatViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MainCatViewHolder mainCatViewHolder, int i) {
        final Datum datum = catList.get(i);
        mainCatViewHolder.cat_name.setText(catList.get(i).getName().toString());
        mainCatViewHolder.cat_name.setSelected(true);

        try {
            Picasso.get().load((catList.get(i).getIcon())).placeholder(R.drawable.ic_restaurant).into(mainCatViewHolder.logo_img);
        } catch (Exception e) {
            Picasso.get().load((R.drawable.ic_restaurant)).into(mainCatViewHolder.logo_img);
        }

        if (this.isMainCategory){
            mainCatViewHolder.cat_disc.setVisibility(View.GONE);
        }

        /*datum getAllHasSubcategories(): This condition checks if the selected category's each and every sub-category
          has subcategory, if yes then will open that category in OpenCategoryActivity
          as we need to show further subcategories, or else we will open that category in
          OpenProductsActivity as atleast one of the subcategories now has products under it
          So we need to show them in tabbed view
         */

        mainCatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(datum.getName().trim().equalsIgnoreCase("laundary")){

                    if(true){
                        Common.showEmptyDialog(context, "Coming Soon", "This feature is coming soon into your app");
                        return;
                    }

                    Intent intent = new Intent(context, LaundaryActivity.class);
                    context.startActivity(intent);
                    return;
                }

                if(datum.getName().trim().equalsIgnoreCase("laundary")){

                    if(true){
                        Common.showEmptyDialog(context, "Coming Soon", "This feature is coming soon into your app");
                        return;
                    }

                }

                if(datum.getName().trim().equalsIgnoreCase("bike"+ File.separator+"taxi")){

                    if(true){
                        Common.showEmptyDialog(context, "Coming Soon", "This feature is coming soon into your app");
                        return;
                    }
                }

                if(datum.getName().trim().equalsIgnoreCase("food")){

                    if(true){
                        Common.showEmptyDialog(context, "Coming Soon", "This feature is coming soon into your app");
                        return;
                    }
                }

                Log.d(TAG, "onClick: has sub categories "+datum.getHasSubcategories());
                if(datum.getAllHasSubcategories().trim().equalsIgnoreCase("y")){
                    Intent intent = new Intent(context, OpenCategoryActivity.class);
                    Log.d(TAG, "onClick: ");
                    intent.putExtra("category", new Gson().toJson(datum));
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, OpenProductActivity.class);
                    Log.d(TAG, "onClick: ");
                    intent.putExtra("category", new Gson().toJson(datum));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    public void updateItems(List<Datum> catList) {
        this.catList.clear();
        notifyDataSetChanged();
        this.catList.addAll(catList);
        Collections.sort(this.catList);
        notifyDataSetChanged();
    }

    public class MainCatViewHolder extends RecyclerView.ViewHolder{
        TextView cat_name;
        ImageView logo_img;
        CircularTextView cat_disc;
        RelativeLayout root_layout;

        public MainCatViewHolder(@NonNull View itemView) {
            super(itemView);
            logo_img = itemView.findViewById(R.id.logo_img);
            cat_name = itemView.findViewById(R.id.cat_name);
            root_layout = itemView.findViewById(R.id.root_layout);
            cat_disc = itemView.findViewById(R.id.cat_disc);
            cat_disc.setSolidColor("#FF3031");

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            int topmargin = context.getResources().getDimensionPixelSize(R.dimen._4sdp);
            int bottom_margin = context.getResources().getDimensionPixelSize(R.dimen._4sdp);
            int right_margin = context.getResources().getDimensionPixelSize(R.dimen._5sdp);
            layoutParams.setMargins(0 , topmargin, right_margin, bottom_margin);
            root_layout.setLayoutParams(layoutParams);

        }
    }
}
