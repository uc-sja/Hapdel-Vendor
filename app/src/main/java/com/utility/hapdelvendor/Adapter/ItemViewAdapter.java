package com.utility.hapdelvendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.utility.hapdelvendor.Activity.OrderDetailActivity;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderModel.Item;
import com.utility.hapdelvendor.R;

import java.util.ArrayList;
import java.util.List;

public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.ItemsViewHolder> {
    List<Item> orderedItems;
    Context context;


    public ItemViewAdapter(ArrayList<Item> orderedItems, Context context) {
        this.orderedItems = orderedItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsViewHolder(LayoutInflater.from(parent.getContext()).inflate (R.layout.items_cell, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        Item item = orderedItems.get(position);
        holder.product_name.setText(item.getProductName());
        holder.product_price.setText(context.getResources().getText(R.string.rupee_icon)+" "+ item.getAmount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("order", new Gson().toJson(item));
                context.startActivity(intent);
            }
        });
    }

    public void updateItems(List<Item> items){
        orderedItems.clear();
        notifyDataSetChanged();
        orderedItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return orderedItems.size();
    }

    public class ItemsViewHolder  extends RecyclerView.ViewHolder{
        private LinearLayout root_layout;
        TextView product_name, product_price;
        CardView product_card;
        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_card = itemView.findViewById(R.id.product_card);
            root_layout = itemView.findViewById(R.id.root_layout);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = context.getResources().getDimensionPixelOffset(R.dimen._8sdp);
            root_layout.setLayoutParams(layoutParams);
        }
    }
}
