package com.utility.hapdelvendor.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utility.hapdelvendor.Model.TransactionModel.Datum;
import com.utility.hapdelvendor.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.JobsViewHolder> {
    Context context;
    List<Datum> txnList;
    private static final String TAG = "TransactionAdapter";

    public TransactionAdapter(Context context, List<Datum> txnList) {
        this.context = context;
        this.txnList = txnList;
    }

    @NonNull
    @Override
    public JobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.txn_cell, null, false);
        return new JobsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobsViewHolder holder, int position) {
        final Datum datum = txnList.get(position);


            holder.order_id.setText("Order ID: #"+datum.getOrderId());

            setStatusColor(holder.approved_img, datum.getPaid());

            holder.txn_amt.setText(context.getResources().getString(R.string.rupee_icon)+ datum.getPrice());

            holder.commission_amt.setText(context.getResources().getString(R.string.rupee_icon)+ datum.getCommissionAmount());

            holder.admin_disc.setText(context.getResources().getString(R.string.rupee_icon)+ datum.getDiscountAdmin());

            holder.vendor_disc.setText(context.getResources().getString(R.string.rupee_icon)+ datum.getDiscountVendor());

            holder.coupon_disc.setText(context.getResources().getString(R.string.rupee_icon)+ datum.getCouponDiscount());

            holder.items_ordered.setText(datum.getQuantity());

    }

    @Override
    public int getItemCount() {
        return txnList.size();
    }

    private void setStatusColor(ImageView approved_img, String orderStatus) {
        switch (orderStatus){
            case "y": approved_img.setVisibility(View.VISIBLE);
                break;
            case "n": approved_img.setVisibility(View.GONE);
                break;
            default:break;
        }
    }


    public void updateItems(List<Datum> userOrderList){
        this.txnList.clear();
        notifyDataSetChanged();
        this.txnList.addAll(userOrderList);
        notifyDataSetChanged();
    }

    public class JobsViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout root_layout;
        TextView order_id, order_status, txn_amt, commission_amt, admin_disc, vendor_disc, coupon_disc, items_ordered;
        ImageView approved_img, status_color;

        public JobsViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.order_id);
            order_status = itemView.findViewById(R.id.order_status);
            txn_amt = itemView.findViewById(R.id.txn_amt);
            commission_amt = itemView.findViewById(R.id.commission_amt);
            admin_disc = itemView.findViewById(R.id.admin_disc);
            vendor_disc = itemView.findViewById(R.id.vendor_disc);
            coupon_disc = itemView.findViewById(R.id.coupon_disc);
            status_color = itemView.findViewById(R.id.status_color);
            items_ordered = itemView.findViewById(R.id.items_ordered);
            approved_img = itemView.findViewById(R.id.approved_img);

            root_layout = itemView.findViewById(R.id.root_layout);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,context.getResources().getDimensionPixelOffset(R.dimen._10sdp),0,0);
            root_layout.setLayoutParams(layoutParams);

        }
    }
}
