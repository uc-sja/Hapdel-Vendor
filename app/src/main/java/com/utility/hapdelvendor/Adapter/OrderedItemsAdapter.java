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
import android.widget.ImageView;
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
        holder.product_seller.setText("Sold By: "   );

        if(item.getProductImage()!=null && !isEmpty(item.getProductImage())){
            Picasso.get().load(item.getProductImage()).placeholder(R.drawable.app_icon_png).fit().into(holder.product_img);
        }
//
        holder.product_price.setText(context.getString(R.string.rupee_icon)+item.getPrice());

        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a pop up menu
                PopupMenu popupMenu = new PopupMenu(context, holder.textViewOptions);
                //inflating menu from xml resource

                popupMenu.inflate(R.menu.order_menu);

                //adding  a click listener

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.cancel_order){
                            showAlertDialog(item);
                            return true;
                        }
                        return false;
                    }
                });

                //displaying the popup
                popupMenu.show();
            }
        });


    }

    private void cancelOrder(Item Item) {
            final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyDialogTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Cancelling Order..");
            progressDialog.setCancelable(false);
            if(!((Activity)context).isFinishing()){
                progressDialog.show();
            }

            Call<ResponseModel> loginResponseCall = getApiInstance().cancelOrder(getCurrentUser().getId(), getCurrentUser().getAccessToken(), Item.getId());
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
                            Toast.makeText(context, "Order Cancellation Success", Toast.LENGTH_SHORT).show();
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

    private void showAlertDialog(final Item item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Cancel Item");
        alertDialog.setMessage("Are you sure to cancel this item from your order ?");
//        alertDialog.setIcon(R.drawable.ic_logout);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelOrder(item);
                dialog.dismiss();
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
        ImageView product_img;
        TextView order_status, order_date, product_name, product_quantity, product_seller, product_price, status_color;
        TextView textViewOptions;
        public OrderedItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            order_status = itemView.findViewById(R.id.order_status);
            product_img = itemView.findViewById(R.id.product_img);
            product_name = itemView.findViewById(R.id.product_name);
            product_quantity = itemView.findViewById(R.id.product_quantity);
            product_seller = itemView.findViewById(R.id.product_seller);
            product_price = itemView.findViewById(R.id.product_price);
            status_color = itemView.findViewById(R.id.status_color);

            textViewOptions = itemView.findViewById(R.id.textViewOptions);

            root_layout = itemView.findViewById(R.id.root_layout);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,context.getResources().getDimensionPixelOffset(R.dimen._10sdp),0,context.getResources().getDimensionPixelOffset(R.dimen._10sdp));
            root_layout.setLayoutParams(layoutParams);

        }
    }
}
