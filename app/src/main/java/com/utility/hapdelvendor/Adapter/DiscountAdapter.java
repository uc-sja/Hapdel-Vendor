package com.utility.hapdelvendor.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utility.hapdelvendor.Dialog.AddDiscount;
import com.utility.hapdelvendor.DiscountList;
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

        holder.discount_value.setText(context.getString(R.string.rupee_icon)+" "+datum.getDiscount());
        holder.min_amt.setText(context.getString(R.string.rupee_icon)+" "+datum.getMinOrder());
        holder.max_amt.setText(context.getString(R.string.rupee_icon)+" "+datum.getMaxDiscount());
        holder.discout_expiry.setText(datum.getExpiryDate());


        boolean expanded = datum.isExpanded();
        // Set the visibility based on state
        holder.items_view_layout.setVisibility(expanded ? View.VISIBLE : View.GONE);
        holder.click_info_layout.setVisibility(expanded? View.GONE : View.VISIBLE);

        holder.view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datum.setExpanded(!expanded);
                notifyDataSetChanged();
            }
        });


        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDiscount addDiscount = new AddDiscount(context, ((DiscountList)context).parentCategory, "edit", datum);
                addDiscount.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                addDiscount.show();
            }
        });
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
        TextView discount_value, min_amt, max_amt, discout_expiry, view_more;
        RelativeLayout root_layout;
        LinearLayout click_info_layout,items_view_layout;
        ImageView edit_btn;

        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            discount_value = itemView.findViewById(R.id.discount_value);
            min_amt = itemView.findViewById(R.id.min_amt);
            max_amt = itemView.findViewById(R.id.max_amt);
            discout_expiry = itemView.findViewById(R.id.discount_expiry);
            click_info_layout = itemView.findViewById(R.id.click_info_layout);
            items_view_layout = itemView.findViewById(R.id.items_view_layout);
            edit_btn = itemView.findViewById(R.id.edit_btn);
            view_more = itemView.findViewById(R.id.view_more);

            root_layout = itemView.findViewById(R.id.root_layout);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,context.getResources().getDimensionPixelOffset(R.dimen._10sdp),0,0);
            root_layout.setLayoutParams(layoutParams);

        }
    }
}
