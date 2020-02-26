package com.utility.hapdelvendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.utility.hapdelvendor.Activity.OrderDetailActivity;
import com.utility.hapdelvendor.Model.RecentOrderModel.Datum;
import com.utility.hapdelvendor.R;

import java.util.ArrayList;
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
        holder.order_id_text.setText("Order Id: #"+datum.getOrderId());

//        Double order_amt = (Double.valueOf(datum.getPrice())-(Double.valueOf(datum.getTotalDiscount())) + Double.valueOf(datum.getCouponDiscount()));

        holder.order_amount.setText(context.getResources().getString(R.string.rupee_icon)+" "+datum.getAmount());
        holder.customer_name.setText("Customer: "+datum.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("order", new Gson().toJson(datum));
                context.startActivity(intent);
            }
        });

        holder.order_status.setText(( datum.getOrderStatus().substring(0,1).toUpperCase() + datum.getOrderStatus().substring(1)));
        setStatusColor(holder.status_color, datum.getOrderStatus());

//        if (datum.getImage() != null && !isEmpty(datum.getImage())) {
//            Log.d(TAG, "onBindViewHolder: " + datum.getImage());
//            if (datum.getImage().contains("|")) {
//                images = datum.getImage().split("\\|");
//                Log.d(TAG, "onBindViewHolder: length " + images[1]);
//                Picasso.get().load(datum.getBaseUrl() + "" + images[0]).placeholder(R.drawable.app_icon_small_png).into(productViewHolder.product_img);
//            } else {
//                Picasso.get().load(datum.getBaseUrl() + "" + datum.getImage()).placeholder(R.drawable.app_icon_small_png).into(productViewHolder.product_img);
//            }
//        } else {
//            productViewHolder.product_img.setImageResource(R.drawable.app_icon_small_png);
//        }

    }

    public void updateItems(List<Datum> data) {
        this.data.clear();
        notifyDataSetChanged();
        this.data.addAll(data);
        notifyDataSetChanged();
    }


    private void setStatusColor(TextView status_color, String orderStatus) {
        switch (orderStatus){
            case "completed": status_color.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                break;
            case "processing": status_color.setBackgroundColor(context.getResources().getColor(R.color.quantum_yellow));
                break;
            case "received": status_color.setBackgroundColor(context.getResources().getColor(R.color.quantum_orange));
                break;
            case "failed": status_color.setBackgroundColor(context.getResources().getColor(R.color.red));
                break;
            case "pickup": status_color.setBackgroundColor(context.getResources().getColor(R.color.com_facebook_blue));
                break;
            case "cancelled": status_color.setBackgroundColor(context.getResources().getColor(R.color.colorDullRed));
                break;
            default:break;
        }
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecentOrderViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout root_layout;
        TextView order_id_text, order_status, order_amount, customer_name, status_color,quantity;
        CircularImageView product_img;

        public RecentOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id_text = itemView.findViewById(R.id.order_id_text);
            order_status = itemView.findViewById(R.id.order_status);
            product_img = itemView.findViewById(R.id.product_img);
            order_amount = itemView.findViewById(R.id.order_amount_text);
            customer_name = itemView.findViewById(R.id.order_date_text);
            status_color = itemView.findViewById(R.id.status_color);
            quantity = itemView.findViewById(R.id.quantity);

            root_layout = itemView.findViewById(R.id.root_layout);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(0,context.getResources().getDimensionPixelOffset(R.dimen._10sdp),0,0);
//            root_layout.setLayoutParams(layoutParams);
        }
    }
}
