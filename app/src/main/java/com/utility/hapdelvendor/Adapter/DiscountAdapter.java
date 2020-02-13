package com.utility.hapdelvendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utility.hapdelvendor.Model.DiscountModel.Datum;
import com.utility.hapdelvendor.R;

import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {

    List<Datum> discountList;
    Context context;

    public DiscountAdapter(List<Datum> discountList, Context context) {
        this.discountList = discountList;
        this.context = context;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_cell, null, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {

        Datum datum = discountList.get(position);

        holder.discount_value.setText("Discount Value: "+context.getString(R.string.rupee_icon)+" "+datum.getDiscount());
        holder.min_amt.setText("Minimum order amount: " +context.getString(R.string.rupee_icon)+" "+datum.getMinOrder());
        holder.max_amt.setText("Max order amount: "+context.getString(R.string.rupee_icon)+" "+datum.getMaxDiscount());
        holder.discout_expiry.setText("Expiry Date: "+datum.getExpiryDate());


        boolean expanded = datum.isExpanded();
        // Set the visibility based on state
        holder.items_view_layout.setVisibility(expanded ? View.VISIBLE : View.GONE);


        if(expanded){
            holder.click_info_layout.setVisibility(View.GONE);
        } else {
            holder.click_info_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return discountList.size();
    }

    public void updateItems(List<Datum> discountList) {
        this.discountList.clear();
        notifyDataSetChanged();
        this.discountList.addAll(discountList);
        notifyDataSetChanged();
    }

    public class DiscountViewHolder extends RecyclerView.ViewHolder{
        TextView discount_value, min_amt, max_amt, discout_expiry;
        RelativeLayout root_layout;
        LinearLayout click_info_layout,items_view_layout;
        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            discount_value = itemView.findViewById(R.id.discount_value);
            min_amt = itemView.findViewById(R.id.min_amt);
            max_amt = itemView.findViewById(R.id.max_amt);
            discout_expiry = itemView.findViewById(R.id.discount_expiry);
            click_info_layout = itemView.findViewById(R.id.click_info_layout);
            items_view_layout = itemView.findViewById(R.id.items_view_layout);

            root_layout = itemView.findViewById(R.id.root_layout);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,context.getResources().getDimensionPixelOffset(R.dimen._10sdp),0,0);
            root_layout.setLayoutParams(layoutParams);

        }
    }
}
