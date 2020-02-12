package com.utility.hapdelvendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.utility.hapdelvendor.Activity.OpenCategoryActivity;
import com.utility.hapdelvendor.Activity.OpenProductActivity;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Subcategory;
import com.utility.hapdelvendor.R;

import java.util.ArrayList;
import java.util.List;

public class TabbedViewAdapter extends RecyclerView.Adapter<TabbedViewAdapter.TabbedViewHolder> {

    Context context;
    ArrayList<Datum> horizontalCatList;
    private static final String TAG = "TabbedViewAdapter";
    private int row_index = -1;
    private Boolean adapter_updated;
    private Subcategory tabCategory;

    public TabbedViewAdapter(Context context, ArrayList<Datum> horizontalCatList) {
        this.context = context;
        this.horizontalCatList = horizontalCatList;
        adapter_updated = false;
    }

    @NonNull
    @Override
    public TabbedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tabbed_cell, null, false);
        return new TabbedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final TabbedViewHolder tabbedViewHolder, final int i) {
        final Datum datum = horizontalCatList.get(i);
        tabbedViewHolder.tabbed_btn.setText(datum.getName());

        tabbedViewHolder.tabbed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = i;
                notifyDataSetChanged();
                Log.d(TAG, "onClick: adapter position " + i);

                //The following link checks if the selected category has further categories, if no, then it calls the fetchProducts function
                //to load the products of that category, but if yes, the it further checks if the selected category's children(subcategory)
                // have atleast one category, if so the it will open that category in the category page or else even if one of those subcategories
                // do not have any further category then the Product page will opened on selection.
                openProductCategory(datum);
            }
        });


        //runs only for first time
        if(adapter_updated){
            adapter_updated = false;
            Log.d(TAG, "onBindViewHolder: adapter updated "+ datum.getHasSubcategories());
            if(tabCategory != null){
                Log.d(TAG, "onBindViewHolder: subcat is not null");
                for(int j = 0; j <  horizontalCatList.size(); j++){
                    Log.d(TAG, "onBindViewHolder: subcat is not null "+horizontalCatList.get(j).getName()+tabCategory.getName());
                    if(horizontalCatList.get(j).getName().equalsIgnoreCase(tabCategory.getName())){
                        row_index = j;
                        openProductCategory(horizontalCatList.get(j));
                        ((OpenProductActivity)context).scrollTo(j);
                        break;
                    }
                }

            } else{
                for(int j = 0; j <  horizontalCatList.size(); j++){
                    if(horizontalCatList.get(j).getHasSubcategories().trim().equalsIgnoreCase("n")){
                        row_index = j;
                        openProductCategory(horizontalCatList.get(j));
                        ((OpenProductActivity)context).scrollTo(j);
                        break;
                    }
                }

            }


        }

        Log.d(TAG, "onBindViewHolder: row index " + row_index + "  i  " + i);

        if (row_index == i) {
            Log.d(TAG, "onBindViewHolder: true");
            tabbedViewHolder.tabbed_btn.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryGreen));
            tabbedViewHolder.tabbed_btn.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            Log.d(TAG, "onBindViewHolder: false");
            tabbedViewHolder.tabbed_btn.setBackgroundColor(context.getResources().getColor(R.color.lighterGray));
            tabbedViewHolder.tabbed_btn.setTextColor(context.getResources().getColor(R.color.darkGray));
        }
    }

    private void openProductCategory(Datum datum) {

        if (datum.getHasSubcategories().trim().equalsIgnoreCase("y")) {
            Log.d(TAG, "onClick: y ");
            {
                if (datum.getAllHasSubcategories().equalsIgnoreCase("y")) {
                    Intent intent = new Intent(context, OpenCategoryActivity.class);
                    intent.putExtra("category", new Gson().toJson(datum));
                    context.startActivity(intent);

                } else {
                    Intent intent = new Intent(context, OpenProductActivity.class);
                    intent.putExtra("category", new Gson().toJson(datum));
                    context.startActivity(intent);
                }
            }
        } else {
            Log.d(TAG, "onClick: no ");
            //Reinitializing total products list on tab click
            ((OpenProductActivity) context).setSelectedDatum(datum);
            ((OpenProductActivity) context).fetchProducts(datum ,"1");
        }
    }


    @Override
    public int getItemCount() {
        return horizontalCatList.size();
    }

    public void updateItems(List<Datum> data, Subcategory tabCategory) {
        this.tabCategory = tabCategory;
        this.horizontalCatList.clear();
        notifyDataSetChanged();
        this.horizontalCatList.addAll(data);
        notifyDataSetChanged();

        this.adapter_updated = true;
    }

    public class TabbedViewHolder extends RecyclerView.ViewHolder{
        public Button tabbed_btn;
        public CardView cardView;

        public TabbedViewHolder(@NonNull View itemView) {
            super(itemView);

            tabbed_btn = itemView.findViewById(R.id.cat_btn);
            cardView = itemView.findViewById(R.id.card_view);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,context.getResources().getDimensionPixelSize(R.dimen._5sdp),context.getResources().getDimensionPixelSize(R.dimen._10sdp),context.getResources().getDimensionPixelSize(R.dimen._5sdp));
            cardView.setLayoutParams(layoutParams);
        }
    }
}
