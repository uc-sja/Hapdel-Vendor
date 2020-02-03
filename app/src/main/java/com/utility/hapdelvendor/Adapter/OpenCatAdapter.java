package com.utility.hapdelvendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Subcategory;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.CircularTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OpenCatAdapter extends RecyclerView.Adapter<OpenCatAdapter.OpenCatViewHolder> {
    private Context context;
    private ArrayList<Datum> catList;
    private static final String TAG = "OpenCatAdapter";
    private Boolean isMainCategory;
    private Boolean allHaveSubCat;


    public OpenCatAdapter(Context context, ArrayList catList, boolean b, boolean allHaveSubCat) {
        this.context = context;
        this.catList = catList;
        this.isMainCategory = b;
        allHaveSubCat = allHaveSubCat;
    }

    @NonNull
    @Override
    public OpenCatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.open_cat_cell, null, false);
            return new OpenCatViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull OpenCatViewHolder OpenCatViewHolder, int i) {
        final Datum datum = catList.get(i);
        OpenCatViewHolder.cat_name.setText(catList.get(i).getName().toString());
        Picasso.get().load(datum.getIcon()).placeholder(R.drawable.app_icon_png).into(OpenCatViewHolder.cat_icon);
        OpenCatViewHolder.subCategoryAdapter.updateItems(datum.getSubcategories(), datum);
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

    public class OpenCatViewHolder extends RecyclerView.ViewHolder{
        private final SubCategoryAdapter subCategoryAdapter;
        TextView cat_name;
        ImageView logo_img;
        CircularTextView cat_disc;
        RelativeLayout root_layout;
        RecyclerView sub_category_view;
        private CircularImageView cat_icon;

        public OpenCatViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_name = itemView.findViewById(R.id.open_cat_name);
            root_layout = itemView.findViewById(R.id.root_layout);
            sub_category_view = itemView.findViewById(R.id.sub_category_view);
            cat_icon = itemView.findViewById(R.id.cat_icon);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            int topmargin = context.getResources().getDimensionPixelSize(R.dimen._4sdp);
            int bottom_margin = context.getResources().getDimensionPixelSize(R.dimen._10sdp);
            int right_margin = context.getResources().getDimensionPixelSize(R.dimen._5sdp);
            layoutParams.setMargins(0 , topmargin, right_margin, bottom_margin);
            root_layout.setLayoutParams(layoutParams);

            sub_category_view.setLayoutManager(new LinearLayoutManager(context,  LinearLayoutManager.HORIZONTAL, false));
//            sub_category_view.setLayoutManager(new GridLayoutManager(context,  4, GridLayoutManager.VERTICAL, false));
            subCategoryAdapter = new SubCategoryAdapter(context, new ArrayList<Subcategory>());
            sub_category_view.setAdapter(subCategoryAdapter);
        }
    }
}
