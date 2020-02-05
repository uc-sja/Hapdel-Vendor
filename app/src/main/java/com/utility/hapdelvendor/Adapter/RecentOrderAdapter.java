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
import com.utility.hapdelvendor.Activity.OrderDetailActivity;
import com.utility.hapdelvendor.Model.RecentOrderModel.Datum;
import com.utility.hapdelvendor.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecentOrderAdapter extends RecyclerView.Adapter<RecentOrderAdapter.RecentOrderViewHolder> {
    Context context;
    ArrayList<Datum> data;
    private static final String TAG = "RecentOrderAdapter";

    public RecentOrderAdapter(Context context, ArrayList<Datum> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecentOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_order_cell, null, false);
        return new RecentOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentOrderViewHolder holder, int position) {
        final Datum datum = data.get(position);
        holder.product_name.setText("Order Id: #"+datum.getOrderId());
//        Double order_amt = (Double.valueOf(datum.getPrice())-(Double.valueOf(datum.getTotalDiscount())) + Double.valueOf(datum.getCouponDiscount()));
        holder.order_amount.setText(context.getResources().getString(R.string.rupee_icon)+" "+datum.getAmount());

        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("EE, d MMM yyyy hh:mm a");
        Date date = null;
        try {
            date = format1.parse(datum.getTxnDate());
        } catch (ParseException e) {
            Log.d(TAG, "onBindViewHolder: "+e.toString());
        }
        holder.order_date.setText(format2.format(date));
//        holder.quantity.setText("(x"+datum.getQuantity()+")  ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("order", new Gson().toJson(datum));
                context.startActivity(intent);
            }
        });

    }

    public void updateItems(List<Datum> data) {
        this.data.clear();
        notifyDataSetChanged();
        this.data.addAll(data);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecentOrderViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout root_layout;
        TextView product_name, order_status, order_amount, order_date, status_color,quantity;
        ImageView product_img;

        public RecentOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            order_status = itemView.findViewById(R.id.order_status);
            product_img = itemView.findViewById(R.id.product_img);
            order_amount = itemView.findViewById(R.id.order_amount);
            order_date = itemView.findViewById(R.id.order_date);
            status_color = itemView.findViewById(R.id.status_color);
            quantity = itemView.findViewById(R.id.quantity);

            root_layout = itemView.findViewById(R.id.root_layout);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,context.getResources().getDimensionPixelOffset(R.dimen._10sdp),0,0);
            root_layout.setLayoutParams(layoutParams);
        }
    }
}
