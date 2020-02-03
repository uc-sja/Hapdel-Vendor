package com.utility.hapdelvendor.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Activity.OpenCategoryActivity;
import com.utility.hapdelvendor.Activity.OpenProductActivity;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Subcategory;
import com.utility.hapdelvendor.R;

import java.util.Collections;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCatViewHolder> {
    Context context;
    List<Subcategory> subCategoryList;
    private static final String TAG = "SubCategoryAdapter";
    private Datum parentCategory;

    public SubCategoryAdapter(Context context, List<Subcategory> subCategoryList) {
        this.context = context;
        this.subCategoryList = subCategoryList;
    }

    @NonNull
    @Override
    public SubCatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_cat_cell, null, false);
        return new SubCatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCatViewHolder holder, int i) {
        final Subcategory subCategory = subCategoryList.get(i);
         holder.cat_name.setText(subCategoryList.get(i).getName().toString());
        holder.cat_name.setSelected(true);

        try {
            Picasso.get().load((subCategoryList.get(i).getIcon())).placeholder(R.drawable.ic_restaurant).into(holder.logo_img);
        } catch (Exception e) {
            Picasso.get().load((R.drawable.ic_restaurant)).into(holder.logo_img);
        }

        Double dis = ((subCategory.getDiscount()));
        int disc = (int) Math.round(dis);
        if (disc != 0) {
            holder.cat_disc.setVisibility(View.VISIBLE);
            holder.cat_disc.setText(disc+"% off");
        } else {
            holder.cat_disc.setVisibility(View.GONE);

        }

        /*
            subCategory getAllHasSubcategories(): This condition checks if the selected category's each and every sub-category
            has subcategory, if yes then will open that category in OpenCategoryActivity
            as we need to show further subcategories, or else we will open that category in
            OpenProductsActivity as atleast one of the subcategories now has products under it
            So we need to show them in tabbed view
        */

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(subCategory.getAllHasSubcategories().trim().equalsIgnoreCase("y")){
                    Log.d(TAG, "onClick: all has sub categories "+subCategory.getName()+ " parent "+ parentCategory.getName());
                    Intent intent = new Intent(context, OpenCategoryActivity.class);
                    intent.putExtra("category", new Gson().toJson(subCategory));
                    context.startActivity(intent);
                } else {
                    Log.d(TAG, "onClick: all not has sub categories "+subCategory.getName()+ " parent "+ parentCategory.getName());
                    if(subCategory.getHasSubcategories().equalsIgnoreCase("y")){
//                        Toast.makeText(context, "sub has further categories ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, OpenProductActivity.class);
                        intent.putExtra("category", new Gson().toJson(subCategory));
//                        intent.putExtra("selected_cat", new Gson().toJson(subCategory));
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, OpenProductActivity.class);
                        intent.putExtra("category", new Gson().toJson(parentCategory));
                        intent.putExtra("selected_cat", new Gson().toJson(subCategory));
                        context.startActivity(intent);

                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    public void updateItems(List<Subcategory> subCategoryList, Datum parentCategory) {
        this.parentCategory = parentCategory;
        this.subCategoryList.clear();
        notifyDataSetChanged();
        this.subCategoryList.addAll(subCategoryList);
        Collections.sort(this.subCategoryList);
        notifyDataSetChanged();
    }

    public class SubCatViewHolder extends RecyclerView.ViewHolder {
        TextView cat_name;
        ImageView logo_img;
        TextView cat_disc;
        LinearLayout root_layout;
        RelativeLayout category_layout;
        CardView cat_card;

        public SubCatViewHolder(@NonNull View itemView) {
            super(itemView);
            logo_img = itemView.findViewById(R.id.logo_img);
            cat_name = itemView.findViewById(R.id.cat_name);
            root_layout = itemView.findViewById(R.id.root_layout);
            cat_disc = itemView.findViewById(R.id.cat_disc);
            cat_card = itemView.findViewById(R.id.cat_card);
            cat_disc.bringToFront();
            category_layout = itemView.findViewById(R.id.category_layout);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

            int topmargin = context.getResources().getDimensionPixelSize(R.dimen._4sdp);
            int bottom_margin = context.getResources().getDimensionPixelSize(R.dimen._4sdp);
            int right_margin = context.getResources().getDimensionPixelSize(R.dimen._10sdp);
            layoutParams.setMargins(0 , topmargin, right_margin, bottom_margin);
            root_layout.setLayoutParams(layoutParams);
        }
    }
}
