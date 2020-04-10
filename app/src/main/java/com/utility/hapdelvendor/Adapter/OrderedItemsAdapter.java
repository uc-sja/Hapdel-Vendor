package com.utility.hapdelvendor.Adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Activity.OrderDetailActivity;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderDetailModel.Item;
import com.utility.hapdelvendor.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;

public class OrderedItemsAdapter extends RecyclerView.Adapter<OrderedItemsAdapter.OrderedItemsViewHolder> {
    Context context;
    List<Item> userOrderDetailList;
    private static final String TAG = "OrderedItemsAdapter";

    public OrderedItemsAdapter(Context context, List<Item> userOrderDetailList) {
        this.context = context;
        this.userOrderDetailList = userOrderDetailList;
    }

    @NonNull
    @Override
    public OrderedItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordered_items_cell, null, false);
        return new OrderedItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderedItemsViewHolder holder, int position) {
        final Item item = userOrderDetailList.get(position);

        setStatusColor(holder.status_color, item.getOrderStatus());
        holder.order_status.setText(( item.getOrderStatus().substring(0,1).toUpperCase() + item.getOrderStatus().substring(1)));


        holder.product_name.setText(item.getProductName());
        holder.product_quantity.setText("Qty:" + item.getQuantity());

        if(item.getProductImage()!=null && !isEmpty(item.getProductImage())){
            Picasso.get().load(item.getProductImage()).placeholder(R.drawable.app_icon_png).fit().into(holder.product_img);
        }
//
        holder.product_price.setText(context.getString(R.string.rupee_icon)+item.getPrice());
            holder.coupon_discount.setText(context.getResources().getString(R.string.rupee_icon)+item.getCouponDiscount());
            holder.admin_discount.setText(context.getResources().getString(R.string.rupee_icon)+item.getDiscountAdmin());
            holder.total_discount.setText(context.getResources().getString(R.string.rupee_icon)+item.getTotalDiscount());
            holder.commission.setText(context.getResources().getString(R.string.rupee_icon)+item.getCommission());
            holder.vendor_discount.setText(context.getResources().getString(R.string.rupee_icon)+item.getDiscountVendor());

            boolean expanded = item.isExpanded();
        Log.d(TAG, "onBindViewHolder: exp" + expanded);
            // Set the visibility based on state
                holder.product_detail_layout.setVisibility(expanded ? View.VISIBLE : View.GONE);

        holder.drop_down_icon.setVisibility(expanded ? View.GONE : View.VISIBLE);
        holder.drop_up_icon.setVisibility(expanded ? View.VISIBLE : View.GONE);


        holder.drop_down_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setExpanded(!expanded);
                notifyDataSetChanged();
            }
        });

        holder.drop_up_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setExpanded(!expanded);
                notifyDataSetChanged();
            }
        });

        holder.product_detail_layout.setVisibility(expanded ? View.VISIBLE : View.GONE);


        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a pop up menu
                PopupMenu popupMenu = new PopupMenu(context, holder.textViewOptions);
                //inflating menu from xml resource

                Log.d(TAG, "onClick: "+item.getService_time());
                //to check if service
                if(item.getType()!=null && item.getType().equalsIgnoreCase("service")){
                    popupMenu.inflate(R.menu.edit_order_menu_service);
                } else {
                    popupMenu.inflate(R.menu.edit_order_menu_product);
                }


                //adding  a click listener

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.cancel_item){
                            showAlertDialog(item, "reject");
                            return true;
                        }
                        if(menuItem.getItemId() == R.id.accept_item){
                            showAlertDialog(item, "accept");
                            return true;
                        }
                        if(menuItem.getItemId() == R.id.complete_item){
                            showAlertDialog(item, "completed");
                            return true;
                        }
                        return false;
                    }
                });

                //displaying the popup
                popupMenu.show();
            }
        });

//        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                acceptOrder(item);
//            }
//        });
//
//        holder.cancel_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cancelOrder(item);
//            }
//        });

//        if(!isEmpty(item.getService_time())){
//
//            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            SimpleDateFormat format2 = new SimpleDateFormat("EE, d MMM yyyy hh:mm a");
//
//            holder.service_time_layout.setVisibility(View.VISIBLE);
//            Date date = null;
//            try {
//                date = format1.parse(item.getService_time());
//                holder.service_time.setText(format2.format(date));
//            } catch (ParseException e) {
//                Log.d(TAG, "onBindViewHolder: " + e.toString());
//            }
//        } else {
//           holder.service_time_layout.setVisibility(View.GONE);
//        }

    }

    private void showAlertDialog(final Item item, String update) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage("Are you sure to "+update+" this item from your order ?");
//        alertDialog.setIcon(R.drawable.ic_logout);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateOrder(item, update);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void updateOrder(Item item, String status) {
            final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyDialogTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Updating Order Status...");
            progressDialog.setCancelable(false);
            if(!((Activity)context).isFinishing()){
                progressDialog.show();
            }

            Call<ResponseModel> loginResponseCall = getApiInstance().updateOrder(getCurrentUser().getId(), getCurrentUser().getAccessToken(), item.getId(), status);
            loginResponseCall.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    progressDialog.dismiss();
                    if(!response.isSuccessful()){
                        Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: fail "+response.code());
                        return;
                    }

                    Log.d(TAG, "onResponse: success"+response.code()+response.body());
                    if(response.body()!=null ){
                        ResponseModel responseModel = null;
                        try {
                            responseModel = response.body();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error in response", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String content="";
                        Log.d(TAG, "onResponse: response msg"+response.body().getResult()+"  msg  ");
                        if (responseModel.getResult().equals("success")){ //very important conditon
                            Log.d(TAG, "onResponse: success");
                            Toast.makeText(context, ""+responseModel.getMsg(), Toast.LENGTH_SHORT).show();
                            ((OrderDetailActivity)context).fetchOrderDetails();

                        }else{
                            content+= responseModel.getMsg();
                            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
                        }

                        Log.d(TAG, "onResponse: login res"+content);
                    } else {
                        Toast.makeText(context, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(context, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }

    @Override
    public int getItemCount() {
        return userOrderDetailList.size();
    }

    public void updateItems(List<Item> userOrderDetailList){
        this.userOrderDetailList.clear();
        notifyDataSetChanged();
        this.userOrderDetailList.addAll(userOrderDetailList);
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

    public class OrderedItemsViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout root_layout;
        ImageView product_img, drop_down_icon, drop_up_icon;
        TextView order_status, order_date, product_name, product_quantity, product_seller, product_price, status_color;
        TextView vendor_discount, coupon_discount, admin_discount, total_discount, commission, textViewOptions, service_time;

        LinearLayout product_detail_layout;

        RelativeLayout service_time_layout;

        Button accept_btn;
        ImageView cancel_btn;

        public OrderedItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            order_status = itemView.findViewById(R.id.order_status);
            product_img = itemView.findViewById(R.id.product_img);
            product_name = itemView.findViewById(R.id.product_name);
            product_quantity = itemView.findViewById(R.id.product_quantity);
            product_seller = itemView.findViewById(R.id.product_seller);
            product_price = itemView.findViewById(R.id.product_price);
            status_color = itemView.findViewById(R.id.status_color);
            drop_down_icon = itemView.findViewById(R.id.dropdown_icon);
            drop_up_icon = itemView.findViewById(R.id.dropup_icon);
            service_time_layout = itemView.findViewById(R.id.service_time_layout);
            service_time = itemView.findViewById(R.id.services_time_text);

            accept_btn = itemView.findViewById(R.id.accept_btn);
            cancel_btn= itemView.findViewById(R.id.cancel_btn);




            product_detail_layout = itemView.findViewById(R.id.product_detail_layout);

            vendor_discount = itemView.findViewById(R.id.vendor_discount);
            coupon_discount = itemView.findViewById(R.id.coupon_discount);
            admin_discount = itemView.findViewById(R.id.admin_discount);
            total_discount = itemView.findViewById(R.id.total_discount);
            commission = itemView.findViewById(R.id.commission);
            cancel_btn = itemView.findViewById(R.id.cancel_btn);
            accept_btn = itemView.findViewById(R.id.accept_btn);
            root_layout = itemView.findViewById(R.id.root_layout);
            textViewOptions = itemView.findViewById(R.id.textViewOptions);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,context.getResources().getDimensionPixelOffset(R.dimen._10sdp),0,0);
            root_layout.setLayoutParams(layoutParams);

        }
    }

}
